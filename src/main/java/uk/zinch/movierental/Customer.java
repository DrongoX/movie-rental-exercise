package uk.zinch.movierental;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Customer {

  public static class Tape {

    private String _serialNumber;
    private Movie _movie;

    public Tape(String serialNumber, Movie movie) {
      _serialNumber = serialNumber;
      _movie = movie;
    }

    public Movie movie() {
      return _movie;
    }

    public String serialNumber() {
      return _serialNumber;
    }
  }


  public static class Rental {

    private Tape _tape;
    private int _daysRented;

    public Rental(Tape tape, int daysRented) {
      _tape = tape;
      _daysRented = daysRented;
    }

    public int daysRented() {
      return _daysRented;
    }

    public Tape tape() {
      return _tape;
    }
    
    public double computeAmount() {
    	double currentAmount = 0;
    	
    	//determine amounts for each line
          switch (tape().movie().priceCode()) {
            case Movie.REGULAR:
              currentAmount += 2;
              if (daysRented() > 2)
                currentAmount += (daysRented() - 2) * 1.5;
              break;
            case Movie.NEW_RELEASE:
              currentAmount += daysRented() * 3;
              break;
            case Movie.CHILDRENS:
              currentAmount += 1.5;
              if (daysRented() > 3)
                currentAmount += (daysRented() - 3) * 1.5;

          }
    	return currentAmount;
    }

	public int computeFrequentRenterPoints() {
	      int frequentRenterPoints = 1;
	      
	      // add bonus for a two day new release rental
	      if ((tape().movie().priceCode() == Movie.NEW_RELEASE) && daysRented() > 1) {
	    	  frequentRenterPoints++;
	      }
	      
		return frequentRenterPoints;
	  }
  }


  public static class Movie {
    public static final int  CHILDRENS = 2;
    public static final int  REGULAR = 0;
    public static final int  NEW_RELEASE = 1;

    private int _priceCode;
    private String _name;

    public Movie(String name, int priceCode) {
      _name = name;
      _priceCode = priceCode;
    }

    public int priceCode() {
      return _priceCode;
    }

    public String name() {
      return _name;
    }
  }



  private String _name;
  private Vector _rentals = new Vector();
  private List<Rental> rentals = new ArrayList<>();

  public Customer(String name) {
    _name = name;
  }

  public String statement() {
    double totalAmount = 0;
    int frequentRenterPoints = 0;
    String result = "Rental Record for " + name() + "\n";
    
    for (Rental rental : rentals) {
      totalAmount += rental.computeAmount();
      
      frequentRenterPoints += rental.computeFrequentRenterPoints();

      //show figures for this rental
      result += "\t" + rental.tape().movie().name() + "\t" + rental.computeAmount() + "\n";
    }
    
    //add footer lines
    result += "Amount owed is " + totalAmount + "\n";
    result += "You earned " + frequentRenterPoints + " frequent renter points";

    return result;
  }

  private String name() {
    return _name;
  }

  public void addRental(Rental arg) {
    _rentals.addElement(arg);
    rentals.add(arg);
  }
}
