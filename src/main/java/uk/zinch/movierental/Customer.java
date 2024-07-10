package uk.zinch.movierental;

import java.util.ArrayList;
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
  private Vector<Rental> _rentals = new Vector<>();

  public Customer(String name) {
    _name = name;
  }

  public String statement() {
    double totalAmount = 0;
    int frequentRenterPoints = 0;
    String result = prepareHeader() + "\n";

    var rentals = new ArrayList<>(_rentals);

    for(Rental rental :rentals){
      double thisAmount = 0;

      //determine amounts for rental line
      switch (rental.tape().movie().priceCode()) {
        case Movie.REGULAR:
          thisAmount += 2;
          if (rental.daysRented() > 2)
            thisAmount += (rental.daysRented() - 2) * 1.5;
          break;
        case Movie.NEW_RELEASE:
          thisAmount += rental.daysRented() * 3;
          break;
        case Movie.CHILDRENS:
          thisAmount += 1.5;
          if (rental.daysRented() > 3)
            thisAmount += (rental.daysRented() - 3) * 1.5;
          break;
      }

      totalAmount += thisAmount;

      // add frequent renter points
      frequentRenterPoints++;
      // add bonus for a two day new release rental
      if ((rental.tape().movie().priceCode() == Movie.NEW_RELEASE) && rental.daysRented() > 1) frequentRenterPoints++;

      //show figures for this rental
      result += "\t" + rental.tape().movie().name() + "\t" + thisAmount + "\n";

    }

    result += prepareFooter(totalAmount, frequentRenterPoints);
    return result;
  }

  private String prepareHeader(){
    return "Rental Record for " + name();
  }

  private static String prepareFooter(double totalAmount, int frequentRenterPoints) {
    return "Amount owed is " + totalAmount + "\nYou earned " + frequentRenterPoints + " frequent renter points";
  }


  private String name() {
    return _name;
  }

  public void addRental(Rental rental) {
    _rentals.addElement(rental);
  }
}
