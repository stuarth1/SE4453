package Air;

import java.io.*;
import java.text.*;
import java.util.*;

class CReport
{
  //
  // data members
  //

  protected Date      fromDate;  // fromDate and toDate represent the
  protected Date      toDate;  // range of dates used in the report
  protected short      recsPerScreen;  // # of records in a report per screen
  protected boolean    printHeader;  // indicates if theHeader is always to be
                      // printed
  protected String      theHeader;  // title of the report

  //
  // accessor methods
  //

  public Date    getFromDate ()    { return fromDate; }
  public Date    getToDate  ()    { return toDate; }

  //
  // mutator methods
  //
  public void    setFromDate (Date f)  { fromDate = f; }
  public void    setToDate (Date t)  { toDate = t; }

  //
  // overridden methods
  //
  // Description:
  //    -getQualifications:    This method retrieves from the user any values
  //                that are needed to determine if a record
  //                qualifies for inclusion in the report
  //    -qualifiesForReport:  This method applies the qualifications to a
  //                flight record to see if it should be in the report
  //    -printRecord:      This method prints a flight record to the screen
  //                based on the type of report
  //

  protected void getQualifications ()
  //
  // getQualifications is the default method for getting the qualifications.
  // It obtains the start and end dates that define the range of the report
  //
  {
    boolean        dateOK = false;  // indicates if a date was properly entered
    boolean        rangeOK = false;  // indicates if report range properly entered
    String          fromStrDate, toStrDate;
    SimpleDateFormat flightDateFormat = new SimpleDateFormat ("MMM/dd/yyyy");
    Calendar toCalendar = new GregorianCalendar ();
                      // used to convert to date
    Calendar fromCalendar = new GregorianCalendar ();
                      // used to convert from date

    AirGourmetUtilities.clearScreen ();

    while (!rangeOK)
    {
      //
      // retrieve and validate a value for the start date of the report
      //
      while (!dateOK)
      {
        System.out.print ("Enter the start date for the report: ");

        fromStrDate = AirGourmetUtilities.readString ();
        dateOK = true;

        try
        {
          fromDate = flightDateFormat.parse (fromStrDate);
          fromCalendar.setTime (fromDate);
        }
        catch (ParseException pe)
        {
          System.out.println ("\n\tYou entered the date incorrectly.\n");
          System.out.println ("\tPlease use the format mmm/dd/yyyy.\n\n");
          dateOK = false;
        }

      }

      //
      // retrieve and validate a value for the end date of the report
      //
      dateOK = false;
      while (!dateOK)
      {
        System.out.print ("Enter the end date for the report: ");

        toStrDate = AirGourmetUtilities.readString ();
        dateOK = true;

        try
        {
          toDate = flightDateFormat.parse (toStrDate);
          toCalendar.setTime (toDate);
        }
        catch (ParseException pe)
        {
          System.out.println ("\n\tYou entered the date incorrectly.\n");
          System.out.println ("\tPlease use the format mmm/dd/yyyy.\n\n");
          dateOK = false;
        }

      }

      //
      // validate the report date range
      //
      if ((toCalendar.after (fromCalendar)) || (toCalendar.equals (fromCalendar)))
        rangeOK = true;
      else
      {
        System.out.println ("\n\tThe 'end' date is less than the 'start' date.\n");
        System.out.println ("\tPlease enter a valid date range.\n\n");
      }
    } // while (!rangeOK)

  } // getQualifications

  protected boolean qualifiesForReport (CFlightRecord aFlightRecord) {return true;}

  protected void printRecord (CFlightRecord aFlightRecord) {}

  //
  // public method
  //
  public void print ()
  //
  // print is the default method for printing a report.  It iterates through all of the records in the
  // flight record file.  For each record that qualifies for the report, it invokes function printRecord
  //
  {
    int          numRecs = 0;  // count of total flight records
    boolean        EOF = false;
    File          fileExists = new File ("fltRec.dat");
                      // used to test if file exists
    CFlightRecord tempFltRec;    // used to read in object from flight record file

    if (fileExists.exists ())
    {
      getQualifications ();
      AirGourmetUtilities.clearScreen ();

      try
      {
        ObjectInputStream in = new ObjectInputStream (new FileInputStream
            ("fltRec.dat"));

        while (!EOF)
        {
          try
          {
            //
            // determine if the passenger object already exists
            //
            tempFltRec = (CFlightRecord)in.readObject ();

            //
            // check if the flight date is within the given range
            // and that this the record qualifies for the report
            //
            if (qualifiesForReport (tempFltRec))
            {
              if (printHeader)
              {
                //
                // pause the screen after every recsPerScreen flight records
                //
                if (( (numRecs % recsPerScreen) == 0) && (numRecs != 0))
                {
                  System.out.println ("\n\n Press <ENTER> to view" 
                        + " the next screen...");
                  AirGourmetUtilities.pressEnter ();
                }

                //
                // display a header message when needed
                //
                if ((numRecs % recsPerScreen) == 0)
                {
                  AirGourmetUtilities.clearScreen ();
                  System.out.println ("\n\n                         Air Gourmet\n");
                  System.out.println ("      " + theHeader);
                }

              } // if (printHeader)

              //
              // call the method to handle this record
              //
              printRecord (tempFltRec);

              numRecs++;

            } // if (qualifiesForReport (tempFltRec))

          } // try

          catch (EOFException e)
          {
          EOF = true;
          }

        } // while

      in.close ();
      } // try

      catch (Exception e)
      {
      e.printStackTrace (System.out);
      }

      //
      // print the report trailer
      //
      if (printHeader)
      {
        if (numRecs == 0)
          System.out.println ("\n\n\tThere were no records to print...\n\n");

        System.out.println ("\n\nPress <ENTER> to return to main menu...");
        AirGourmetUtilities.pressEnter ();
      }

    } // if (fileExists.exists ())

  } // print

} // class CReport