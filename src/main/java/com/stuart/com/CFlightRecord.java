package Air;

import java.io.*;
import java.text.*;
import java.util.*;

import com.google.common.base.CharMatcher;
import com.google.common.base.MoreObjects;

class CFlightRecord implements java.io.Serializable
{
  //
  // mealTypeValues represents an array containing the different kind of meal types
  //
  public static String mealTypeValues[] =  {"Child          ", "Diabetic       ",
      "Halaal         ", "Kosher         ", "Lactose Free   ", "Low Calorie    ",
      "Low Cholesterol", "Low Fat        ", "Low Protein    ", "Low Sodium     ",
      "Sea Food       ", "Vegan          ", "Vegetarian     "};

  //
  // data members
  //

  private String        passengerID;  // ID of passenger (9 digits)
  private String        reservationID;  // reservationID of flight (6 uppercase)
  private String        flightNum;  // flight number (3 digits, right justified)
  private Date        flightDate;  // date of flight
  private String        seatNum;  // seat number (3 digits + char, right justified)
  private char        mealType;  // special meal type
  private short        perceivedQuality;  // perceived meal quality (1 through 5)
  private boolean      checkedIn;  // indicates if passenger has checked in
  private boolean      mealLoaded;  // indicates if a passengers meal was loaded

  //
  // accessor methods
  //

  public String  getPassengerID ()      { return passengerID; }
  public String  getReservationID ()    { return reservationID; }
  public String  getFlightNum ()      { return flightNum; }
  public Date  getFlightDate ()      { return flightDate; }
  public String  getSeatNum ()        { return seatNum; }
  public char  getMealType ()        { return mealType; }
  public short  getPerceivedQuality ()    { return perceivedQuality; }
  public boolean  getCheckedIn ()    { return checkedIn; }
  public boolean  getMealLoaded ()    { return mealLoaded; }

  //
  // mutator methods
  //
  public void  setPassengerID (String s)    { passengerID = s.toUpperCase (); }
  public void  setReservationID (String r)    { reservationID = r.toUpperCase (); }
  public void  setFlightNum (String f)    { flightNum = f.toUpperCase (); }
  public void  setFlightDate (Date d)    { flightDate = d; }
  public void  setSeatNum (String s)    { seatNum = s.toUpperCase (); }
  public void  setMealType (char m)    { mealType = m; }
  public void  setPerceivedQuality (short p)    { perceivedQuality = p; }
  public void  setCheckedIn (boolean c)    { checkedIn = c; }
  public void  setMealLoaded (boolean m)    { mealLoaded = m; }

  //
  // public methods
  //
  public synchronized String toString ()
  //
  // toString will compose a string representation of the flight record object
  //
  {
    return MoreObjects.toStringHelper(this)
    		.add("passengerID", passengerID)
    		.add("reservationID", reservationID)
    		.add("flightNum", flightNum)
    		.add("flightDate", flightDate)
    		.add("seatNum", seatNum)
    		.add("mealType", mealType)
    		.add("perceivedQuality", perceivedQuality)
    		.add("checkedIn", checkedIn)
    		.add("mealLoaded", mealLoaded)
    		.toString();
  }

  public void Copy (CFlightRecord tempFltRec)
  //
  // this will make a copy of tempFltRec into the current object
  //
  {
    this.passengerID = tempFltRec.getPassengerID ();
    this.reservationID = tempFltRec.getReservationID ();
    this.flightNum = tempFltRec.getFlightNum ();
    this.flightDate = tempFltRec.getFlightDate ();
    this.seatNum = tempFltRec.getSeatNum ();
    this.mealType = tempFltRec.getMealType ();
    this.perceivedQuality = tempFltRec.getPerceivedQuality ();
    this.checkedIn = tempFltRec.getCheckedIn ();
    this.mealLoaded = tempFltRec.getMealLoaded ();
  }

  public void  getReservation ()
  //
  // getReservation retrieves flight reservation information
  //
  {
    CPassenger      aPassenger = new CPassenger ();
                      // passenger assigned to this reservation
    boolean        mealOK = false;  // indicates if meal type properly entered
    boolean        dateOK = false;  // indicates if flight date properly entered
    boolean        reservationOK = false;  // indicates if reservationID is valid
    boolean        seatOK = false;  // indicates if seat number is valid
    boolean        flightNumOK = false;  // indicates if flight number ID is valid
    String          flightStrDate;  // used to get a string representing a date
    SimpleDateFormat    flightDateFormat = new SimpleDateFormat ("MMM/dd/yyyy");
                      // used to parse a date

    AirGourmetUtilities.clearScreen ();

    System.out.println ("Please enter the following information about the reservation.\n\n");

    //
    // retrieve and validate a value for reservationID
    //
    while (!reservationOK)
    {
      System.out.print ("Enter the RESERVATION ID: ");
      reservationID = AirGourmetUtilities.readString ();

      if (checkReservationID () )
        if (!alreadyExists () )
          reservationOK = true;
        else
        {
          System.out.println ("\n\tThis RESERVATION ID already exists.\n");
          System.out.println ("\tPlease try again...\n\n");
        }
      else
      {
        System.out.println ("\n\tA RESERVATION ID must be 6 letters.\n");
        System.out.println ("\tPlease try again...\n\n");
      }
    }

    //
    // retrieve and validate a value for flightNum
    //
    while (!flightNumOK)
    {
      System.out.print ("Enter the FLIGHT NUMBER: ");
      flightNum = AirGourmetUtilities.readString ();

      if (checkFlightNum () )
        flightNumOK = true;
      else
      {
        System.out.println ("\n\tA FLIGHT NUMBER must be 3 digits.\n");
        System.out.println ("\tPlease try again...\n\n");
      }
    }

    //
    // retrieve and validate a value for flightDate
    //
    while (!dateOK)
    {
      System.out.print ("Enter the DATE of the flight: ");

      flightStrDate = AirGourmetUtilities.readString ();
      dateOK = true;

      try
      {
        flightDate = flightDateFormat.parse (flightStrDate);
      }
      catch (ParseException pe)
      {
        System.out.println ("\n\tYou entered the date incorrectly.\n");
        System.out.println ("\tPlease use the format mmm/dd/yyyy.\n\n");
        dateOK = false;
      }

    }

    //
    // retrieve and validate a value for seatNum
    //
    while (!seatOK)
    {
      System.out.print ("Enter the SEAT NUMBER assigned to this passenger: ");
      seatNum = AirGourmetUtilities.readString ();

      if (checkSeatNum () )
        if (!seatReserved () )
          seatOK = true;
        else
        {
          System.out.println ("\n\tThis seat is already reserved.\n");
          System.out.println ("\tPlease choose another seat.\n\n");
        }
      else
      {
        System.out.println ("\n\tA SEAT NUMBER must be 3 digits followed by" +
            " an uppercase letter.\n");
        System.out.println ("\tPlease try again...\n\n");
      }
    }

    //
    // retrieve and validate a value for mealType
    //
    while (!mealOK)
    {
      System.out.println ("\n\nList of available special meals:\n\n");
      System.out.println ("  A - Child             B - Diabetic          C - Halaal\n");
      System.out.println ("  D - Kosher            E - Lactose Free      F - Low Cal\n");
      System.out.println ("  G - Low Cholesterol   H - Low Fat           I - Low Protein\n");
      System.out.println ("  J - Low Sodium        K - Sea Food          L - Vegan\n");
      System.out.println ("  M - Vegetarian\n\n");

      System.out.print ("Enter the SPECIAL MEAL for this reservation: ");

      mealType = Character.toUpperCase (AirGourmetUtilities.getChar ());

      if ((mealType >= 'A') && (mealType <= 'M'))
        mealOK = true;
      else
      {
        System.out.println ("Invalid response!\n");
        System.out.println ("Please enter a value from the following list:\n");
      }
    }

    //
    // get passenger information and insert the passenger in the passenger file
    //
    aPassenger.getDescription ();
    aPassenger.insert ();

    //
    // copy the passengerID from the passenger object to the flight record object
    // initialize the values for perceivedQuality, checkedIn, mealLoaded
    //
    passengerID = aPassenger.getPassengerID ();
    perceivedQuality = -1;
    checkedIn = false;
    mealLoaded = false;

    insert ();
    // insert the reservation into the reservation file

  } // getReservation


  public void checkInPassenger ()
  //
  // checkInPassenger sets checkedIn to true for a specific reservation
  //
  {
    boolean        reservationOK = false;  // indicates if reservationID is valid

    AirGourmetUtilities.clearScreen ();
    //
    // retrieve and validate a value for reservationID
    //
    while (!reservationOK)
    {
      System.out.print ("Enter the RESERVATION ID: ");
      reservationID = AirGourmetUtilities.readString ();

      if (checkReservationID () )
        reservationOK = true;
      else
      {
        System.out.println ("\n\tA RESERVATION ID must be 6 letters.\n");
        System.out.println ("\tPlease try again...\n\n");
      }
    }

    //
    // search for the reservation, set checkedIn to true, and then insert the change
    //
    if (alreadyExists () )
    {
      checkedIn = true;
      insert ();

      System.out.println ("\n\n\tThe passenger has been checked in.\n");
      System.out.println ("\tPlease check their identification.\n");

      System.out.println ("\n\nPress <ENTER> to return to main menu...");
      AirGourmetUtilities.pressEnter ();
    }
    else
    {
      System.out.println ("\n\n\tThere is no reservation with this ID...");

      System.out.println ("\n\nPress <ENTER> to return to main menu...");
      AirGourmetUtilities.pressEnter ();
    }

  } // checkInPassenger

  public boolean scanSpecialMeals ()
  //
  // scanSpecialMeals queries the user whether the meal was loaded and then updates the file.
  // It does this for all of the reservations on a specific flight (flight number + flight date)
  //
  {
    boolean        dateOK = false;  // indicates if flight date is properly entered
    boolean        flightNumOK = false;  // indicates if flight number is valid
    boolean        EOF = false;
    char          ch;    // holds user response to Y/N question
    File          fileExists = new File ("fltRec.dat");
                      // used to test if file exists
    CFlightRecord tempFltRec;    // temporary object used for file copying
    String flightStrDate;        // used to get a string representing a date
    SimpleDateFormat flightDateFormat = new SimpleDateFormat ("MMM/dd/yyyy");
                      // used to parse a date

    AirGourmetUtilities.clearScreen ();
    //
    // retrieve and validate a value for flightDate
    //
    while (!dateOK)
    {
      System.out.print ("Enter the DATE of the flight: ");

      flightStrDate = AirGourmetUtilities.readString ();
      dateOK = true;

      try
      {
        flightDate = flightDateFormat.parse (flightStrDate);
      }
      catch (ParseException pe)
      {
        System.out.println ("\n\tYou entered the date incorrectly.\n");
        System.out.println ("\tPlease use the format mmm/dd/yyyy.\n\n");
        dateOK = false;
      }

    }

    //
    // retrieve and validate a value for flightNum
    //
    while (!flightNumOK)
    {
      System.out.print ("Enter the FLIGHT NUMBER: ");
      flightNum = AirGourmetUtilities.readString ();

      if (checkFlightNum () )
        flightNumOK = true;
      else
      {
        System.out.println ("\n\tA FLIGHT NUMBER must be 3 digits.\n");
        System.out.println ("\tPlease try again...\n\n");
      }
    }

    AirGourmetUtilities.clearScreen ();

    if (!fileExists.exists ())
      return false;

    try
    {
      ObjectInputStream in = new ObjectInputStream (new FileInputStream ("fltRec.dat"));
      ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream
          ("tempF.dat"));

      while (!EOF)
      {
        try
        {
          //
          // copy the current flight record file to a temporary file
          //
          tempFltRec = (CFlightRecord)in.readObject ();
          out.writeObject (tempFltRec);
        } // try

        catch (EOFException e)
        {
          EOF = true;
        }

      } // while

      in.close ();
      out.close ();
    } // try

    catch (Exception e)
    {
      e.printStackTrace (System.out);
    }

    EOF = false;

    //
    // copy the temporary file to new flight record file
    // while inserting the passenger object in the proper location after
    // updating mealLoaded
    //

    try
    {
      ObjectInputStream in = new ObjectInputStream (new FileInputStream ("tempF.dat"));
      ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream
          ("fltRec.dat"));

      while (!EOF)
      {
        try
        {
          tempFltRec = (CFlightRecord)in.readObject ();

          if ((flightDate.equals (tempFltRec.getFlightDate ()))
              && (flightNum.toUpperCase ().compareTo (tempFltRec.
                getFlightNum ().toUpperCase () ) == 0))
          {
            System.out.println ("\n\nPASSENGER: " + tempFltRec.getPassengerID ());
            System.out.println ("  SEAT: " + tempFltRec.getSeatNum ());
            System.out.println ("  MEAL TYPE: "
                + mealTypeValues[tempFltRec.getMealType () - 'A']);
            System.out.print ("\n\nWas the meal for this passenger loaded (Y/N) ? ");

            ch = AirGourmetUtilities.getChar ();
            if (Character.toUpperCase (ch) == 'Y')
              tempFltRec.setMealLoaded (true);
            else
              tempFltRec.setMealLoaded (false);
          }
          out.writeObject (tempFltRec);

        } // try

        catch (EOFException e)
        {
          EOF = true;
        }

      } // while

      in.close ();
      out.close ();
    } // try

    catch (Exception e)
    {
      e.printStackTrace (System.out);
    }

    return true;
  } // scanSpecialMeals

  public void scanPostcard ()
  //
  // scanPostcard sets perceivedQuality for a specific reservation to a value entered
  // by the user and inserts the change
  //
  {
    boolean        reservationOK = false;   // indicates if reservationID is valid

    AirGourmetUtilities.clearScreen ();

    //
    // retrieve and validate a value for reservationID
    //
    while (!reservationOK)
    {
      System.out.print ("Enter the RESERVATION ID: ");
      reservationID = AirGourmetUtilities.readString ();

      if (checkReservationID () )
        reservationOK = true;
      else
      {
        System.out.println ("\n\tA RESERVATION ID must be 6 letters.\n");
        System.out.println ("\tPlease try again...\n\n");
      }
    }

    //
    // if the reservation exists, get the perceivedQuality and then
    // write the change back to the file
    //
    if (alreadyExists () )
    {
      String tempString;
      System.out.print ("Enter perceived meal quality (1 thru 5):");

      tempString = AirGourmetUtilities.readString ();
      perceivedQuality = (short)Integer.parseInt (tempString);

      insert ();

      System.out.println ("\n\n\tThe passenger record has been updated.\n");

      System.out.println ("\n\nPress <ENTER> to return to main menu...");
      AirGourmetUtilities.pressEnter ();
    }
    else
    {
      System.out.println ("\n\n\tThere is no reservation with this ID...");

      System.out.println ("\n\nPress <ENTER> to return to main menu...");
      AirGourmetUtilities.pressEnter ();
    }

  } // scanPostcard

  public void insert ()
  //
  // insert inserts a flight record object in the proper place
  //
  {
    boolean        found = false;  // indicates if object insertion point found
    File          fileExists = new File ("fltRec.dat");
                      // used to test if file exists
    CFlightRecord      tempFltRec;  // temporary object used for file copying
    boolean        EOF = false;

    try
    {
      ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream
          ("tempF.dat"));

      if (fileExists.exists ())
      {
        ObjectInputStream in = new ObjectInputStream (new FileInputStream
            ("fltRec.dat"));

        while (!EOF)
        {
          try
          {
            // read/write temporary object from the passenger file
            tempFltRec = (CFlightRecord)in.readObject ();
            out.writeObject (tempFltRec);
          }
          catch (EOFException e)
          {
            EOF = true;
          }

        } // while (!EOF)

        in.close ();
      } // if (fileExists.exists ())
      else
        out.writeObject (this);

      out.close ();
    } // try

    catch (Exception e)
    {
      e.printStackTrace (System.out);
    }

    EOF = false;

    try
    {
      ObjectInputStream in = new ObjectInputStream (new FileInputStream ("tempF.dat"));
      ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream
          ("fltRec.dat"));

      while (!EOF)
      {
        try
        {
          tempFltRec = (CFlightRecord)in.readObject ();

          //
          // copy the temporary file to new flight record file
          // while inserting the flight record object in the proper location
          //
          if (reservationID.toLowerCase ().compareTo (tempFltRec.getReservationID
              ().toLowerCase ()) == 0)
          {
            out.writeObject (this);
            found = true;
          }
          else
            out.writeObject (tempFltRec);

        } // try

        catch (EOFException e)
        {
          if (!found)
            out.writeObject (this);

          EOF = true;
        }

      } // while (!EOF)

      in.close ();
      out.close ();
    } // try

    catch (Exception e)
    {
      e.printStackTrace (System.out);
    }

  } // insert

  //
  // private methods
  //

  private boolean  checkReservationID ()
  //
  // checkReservationID determines if the reservationID is valid
  //
  {
    boolean        valid = true;  // indicates if the reservationID is valid
    short          i;      // used to iterate through chars in the
                      // reservationID
    char          ch;

    reservationID = reservationID.toUpperCase ();

    //
    // make sure that the reservationID is composed of exactly 6 chars
    //
    if (reservationID.length () == 6)
    {
      for (i = 0; i<6; i++)
        {
          ch = reservationID.charAt (i);
          if ((!Character.isLetter (ch)) || (ch == ' '))
            valid = false;
        }
    }
    else
      valid = false;

    return valid;

  } // checkReservationID

  private boolean  checkFlightNum ()
  //
  // checkFlightNum determines if the flight number is valid
  //
  {
    boolean        valid = true;  // indicates if the flight number is valid
    short          i;      // used to iterate thru the chars in flightNum
    StringBuffer      tempFltNum = new StringBuffer ();
                      // used in right justification

    //
    // flightNum must be composed of digits only
    //
    valid = CharMatcher.DIGIT.matchesAllOf(flightNum);
    
    
    // right justify flightNum and zero-fill
    //
    if (valid)
    {
      for (i = 0; i<3; i++)
        if (i < 3 - flightNum.length ())
          tempFltNum.append ('0');
        else
          tempFltNum.append (flightNum.charAt (i + flightNum.length () - 3));

      flightNum = tempFltNum.toString ();

    }
    return valid;

  } // checkFlightNum

  private boolean checkSeatNum ()
  //
  // checkSeatNum determines if the seat number is valid
  //
  {
    boolean        valid = true;  // indicates if the flight number is valid
    short          i;      // used to iterate through chars in seatNum
    StringBuffer  tempSeatNum = new StringBuffer ();
                      // used in right justification

    //
    // seatNum must be composed of digits followed by a single char
    //
    if (Character.isLetter (seatNum.charAt (seatNum.length () - 1)))
    {
      for (i = 0; i< (seatNum.length () - 1); i++)
      {
        if (!Character.isDigit (seatNum.charAt (i)))
          valid = false;
      }
    }
    else
      valid = false;

    // right justify seatNum and zero-fill
    //
    if (valid)
    {
      for (i = 0; i<4; i++)
        if (i < 4 - seatNum.length ())
          tempSeatNum.append ('0');
        else
          tempSeatNum.append (seatNum.charAt (i + seatNum.length () - 4));

      seatNum = tempSeatNum.toString ();
    }

    return valid;

  } // checkSeatNum

  private boolean alreadyExists ()
  //
  // alreadyExists determines if the reservationID of the current object already exists in the file
  //
  {
    boolean        found = false;  // indicates if passenger already exists
    boolean        EOF = false;
    String          searchID;  // the passengerID for which to search
    File          fileExists = new File ("fltRec.dat");
                      // used to test if file exists
    CFlightRecord tempFltRec;    // used to read in object from flight record file

    if (!fileExists.exists ())
      return false;

    searchID = reservationID;

    try
    {
      ObjectInputStream in = new ObjectInputStream (new FileInputStream ("fltRec.dat"));

      while (!EOF)
      {
        try
        {
          //
          // determine if the passenger object already exists
          //
          tempFltRec = (CFlightRecord)in.readObject ();

          //
          // check if there is a match with the searchID
          //
          if (tempFltRec.getReservationID ().toLowerCase ().
              compareTo (searchID.toLowerCase ()) == 0)
          {
            found = true;
            this.Copy (tempFltRec);
            break;
          }
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

    reservationID = searchID;
    return found;

  } // alreadyExists

  private boolean seatReserved ()
  //
  // seatReserved determines if the seat is already reserved
  //
  {
    boolean        found = false;  // indicates if seat reserved
    File          fileExists = new File ("fltRec.dat");
                      // used to test if file exists
    boolean        EOF = false;
    CFlightRecord tempFltRec;    // used to read in object from flight record file

    if (!fileExists.exists ())
      return false;

    try
    {
      ObjectInputStream in = new ObjectInputStream (new FileInputStream ("fltRec.dat"));

      while (!EOF)
      {
        try
        {
          //
          // check if there is a match with the current seat
          //
          tempFltRec = (CFlightRecord)in.readObject ();

          if ((flightNum.toUpperCase ().compareTo (tempFltRec.getFlightNum ()) == 0)
              && (seatNum.toUpperCase ().compareTo (tempFltRec.
                  getSeatNum ()) == 0)
              && (tempFltRec.getFlightDate () == flightDate))
          {
            found = true;
            break;
          }

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

    return found;

  } // seatReserved

} // class CFlightRecord