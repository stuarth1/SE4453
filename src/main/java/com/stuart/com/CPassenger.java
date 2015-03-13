package Air;

import java.io.*;

import com.google.common.base.MoreObjects;

class CPassenger implements java.io.Serializable
{
  //
  // data members
  //
  private String        passengerID;  // ID of passenger (9 digits)
  private String        firstName;  // first name of passenger (15 chars)
  private char        middleInit;  // middle initial of passenger (1 char)
  private String        lastName;  // last name of passenger (15 chars)
  private String        suffix;    // suffix of passenger (5 chars)
  private String        address1;  // first line of passenger address (25 chars)
  private String        address2;  // second line of pass. address (25 chars)
  private String        city;    // city in which passenger lives (14 chars)
  private String        state;    // state in which passenger lives (14 chars)
  private String        postalCode;  // postal code of passenger (10 chars)
  private String        country;  // country in which pass. lives (20 chars)

  //
  // accessor methods
  //
  public String  getPassengerID ()      { return passengerID; }
  public String  getFirstName ()      { return firstName; }
  public char  getMiddleInit ()        { return middleInit; }
  public String  getLastName ()      { return lastName; }
  public String  getSuffix ()          { return suffix; }
  public String  getAddress1 ()        { return address1; }
  public String  getAddress2 ()        { return address2; }
  public String  getCity ()          { return city; }
  public String  getState ()          { return state; }
  public String  getPostalCode ()      { return postalCode; }
  public String  getCountry ()        { return country; }

  //
  // mutator methods
  //
  public void  setPassengerID (String s)    { passengerID = s.toUpperCase (); }
  public void  setFirstName (String f)    { firstName = f.toUpperCase (); }
  public void  setMiddleInit (char m)    { middleInit = m; }
  public void  setLastName (String l)    { lastName = l.toUpperCase (); }
  public void  setSuffix (String s)      { suffix = s.toUpperCase (); }
  public void  setAddress1 (String a1)    { address1 = a1.toUpperCase (); }
  public void  setAddress2 (String a2)    { address2 = a2.toUpperCase (); }
  public void  setCity (String c)      { city = c.toUpperCase (); }
  public void  setState (String s)      { state = s.toUpperCase (); }
  public void  setPostalCode (String p)    { postalCode = p.toUpperCase (); }
  public void  setCountry (String c)    { country = c.toUpperCase (); }

  //
  // public methods
  //

  public synchronized String toString ()
  //
  // toString composes a string representation of the passenger object
  //
  {
	return MoreObjects.toStringHelper(this)
			.add("passengerID", passengerID)
			.add("firstName", firstName)
			.add("middleInit", middleInit)
			.add("lastName", lastName)
			.add("suffix", suffix)
			.add("address1", address1)
			.add("address2", address2)
			.add("city", city)
			.add("state", state)
			.add("postalCode", postalCode)
			.toString();
  }

  public void Copy (CPassenger tempPassenger)
  //
  // Copy  makes a copy of tempPassenger into the current object
  //
  {
    this.passengerID = tempPassenger.getPassengerID ();
    this.firstName = tempPassenger.getFirstName ();
    this.middleInit = tempPassenger.getMiddleInit ();
    this.lastName = tempPassenger.getLastName ();
    this.suffix = tempPassenger.getSuffix ();
    this.address1 = tempPassenger.getAddress1 ();
    this.address2 = tempPassenger.getAddress2 ();
    this.city = tempPassenger.getCity ();
    this.state = tempPassenger.getState ();
    this.postalCode = tempPassenger.getPostalCode ();
    this.country = tempPassenger.getCountry ();
  }

  public boolean getPassenger (String searchID)
  //
  // getPassenger loads the passenger from the file that has passengerID equal to searchID
  // Returns true if the passenger was found and loaded
  //
  {
    boolean        found = false;  // indicates if passenger already exists
    File          fileExists = new File ("passenger.dat");
                      // used to test if file exists
    CPassenger      tempPassenger;  // temporary object used to determine if
                      // object already exists
    boolean        EOF = false;

    if (!fileExists.exists ())
      return false;

    try
    {
      ObjectInputStream in = new ObjectInputStream (new FileInputStream 
          ("passenger.dat"));

      while (!EOF)
      {
        try
        {
          //
          // determine if the passenger object already exists
          //
          tempPassenger = (CPassenger)in.readObject ();

          //
      // check if there is a match with searchID
          //
          if (tempPassenger.getPassengerID ().toLowerCase ().compareTo
              (searchID.toLowerCase ()) == 0)
            {
              found = true;
              this.Copy (tempPassenger);
              break;
            }
        } // try
        catch (EOFException e)
        {
          EOF = true;
        }

      } // while (!EOF)

      in.close ();
    } // try
    catch (Exception e)
    {
      e.printStackTrace (System.out);
    }

    return found;

  } // getPassenger


  public void getDescription ()
  //
  // getDescription retrieves passenger information
  //
  {
    AirGourmetUtilities.clearScreen ();

    System.out.println ("Please enter the following information about the passenger.\n\n");

    System.out.println ("Enter the PASSENGER ID assigned to this passenger");
    System.out.print (" (9 numbers only - no spaces or dashes): ");
    passengerID = AirGourmetUtilities.readString ();

    if (!alreadyExists () )
    {
      System.out.print ("Enter the FIRST name of the passenger: ");
      firstName = AirGourmetUtilities.readString ();

      System.out.print ("Enter the MIDDLE INITIAL of the passenger: ");
      middleInit = AirGourmetUtilities.getChar ();

      System.out.print ("Enter the LAST name of the passenger: ");
      lastName = AirGourmetUtilities.readString ();

      System.out.print ("Enter the SUFFIX used by the passenger: ");
      suffix = AirGourmetUtilities.readString ();

      System.out.print ("Enter the ADDRESS (first line) of the passenger: ");
      address1 = AirGourmetUtilities.readString ();

      System.out.print ("Enter the ADDRESS (second line) of the passenger: ");
      address2 = AirGourmetUtilities.readString ();

      System.out.print ("Enter the CITY where the passenger lives: ");
      city = AirGourmetUtilities.readString ();

      System.out.print ("Enter the STATE where the passenger lives: ");
      state = AirGourmetUtilities.readString ();

      System.out.print ("Enter the POSTAL CODE where the passenger lives: ");
      postalCode = AirGourmetUtilities.readString ();

      System.out.print ("Enter the COUNTRY where the passenger lives: ");
      country = AirGourmetUtilities.readString ();
    }
  } // getDescription


  public void insert ()
  //
  // insert inserts a passenger object in the proper place
  //
  {
    boolean        found = false;  // indicates if object insertion point found
    File          fileExists = new File ("passenger.dat");
                      // used to test if file exists
    CPassenger      tempPassenger;  // temporary object used for file copying
    boolean        EOF = false;

    try
    {
      ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream
          ("tempP.dat"));

      if (fileExists.exists ())
      {
        ObjectInputStream in = new ObjectInputStream (new FileInputStream
            ("passenger.dat"));

        while (!EOF)
        {
          try
          {
            // read/write temporary object from the passenger file
            tempPassenger = (CPassenger)in.readObject ();
            out.writeObject (tempPassenger);
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
      ObjectInputStream in = new ObjectInputStream (new FileInputStream ("tempP.dat"));
      ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream
          ("passenger.dat"));

      while (!EOF)
      {
        try
        {
          tempPassenger = (CPassenger)in.readObject ();

          //
          // check if there is already a passenger with the current ID.
          // If one exists, it means a change of address so write the new
          // passenger object into the file
          //
          if (passengerID.compareTo (tempPassenger.getPassengerID ().
              toLowerCase ()) == 0)
          {
            out.writeObject (this);
            found = true;
          }
          else
            out.writeObject (tempPassenger);

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
  // private method
  //

  private boolean alreadyExists ()
  //
  // alreadyExists determines if the passengerID of the current object already exists in the file
  // if the ID exists, then the user is asked if the values stored in the file
  // are to be used
  //
  {
    char          ch;    // holds user response to Y/N question
    boolean        found = false;  // indicates if passenger already exists
    String          searchID;  // the passengerID for which to search
    File          fileExists = new File ("passenger.dat");
                      // used to test if file exists
    CPassenger      tempPassenger;  // temporary object used to determine if
                      // object already exists
    boolean        EOF = false;

    if (!fileExists.exists ())
      return false;

    searchID = passengerID;

    try
    {
      ObjectInputStream in = new ObjectInputStream (new FileInputStream
          ("passenger.dat"));

      while (!EOF)
      {
        try
        {
          //
          // determine if the passenger object already exists
          //
          tempPassenger = (CPassenger)in.readObject ();

          if (tempPassenger.getPassengerID ().toLowerCase ().compareTo
              (searchID.toLowerCase ()) == 0)
          {
            found = true;
            this.Copy (tempPassenger);
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

    //
    // A record was found that has the same passengerID.
    // Ask the users if they want to use this record in the current
    // reservation (if the user answers N, then a new name/address may be
    // given to this passengerID)
    //
    if (found)
    {
      System.out.println ("\n\n");
      System.out.println ("The following passenger exists: \n\n");
      System.out.println (this.toString () + "\n");

      System.out.println ("Do you want to use this name and address to make a ");
      System.out.print ("reservation for this passenger (Y/N)? ");

      ch = AirGourmetUtilities.getChar ();
      System.out.println ("\n");

      found = false;

      if (Character.toUpperCase (ch) == 'Y')
        found = true;
    }
    passengerID = searchID;
    return found;

  } // alreadyExists

} // class CPassenger