package com.drongox.movierental;

import java.util.ArrayList;
import java.util.Enumeration;
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
  private List<Rental> rentals = new ArrayList<>();

  public Customer(String name) {
    _name = name;
  }

  public String statement() {
    double totalAmount = 0;
    int frequentRenterPoints = 0;
    Enumeration<Rental> rentals = _rentals.elements();
    StringBuilder result = new StringBuilder("Rental Record for " + name() + "\n");
    while (rentals.hasMoreElements()) {
      Rental rental = rentals.nextElement();

      double thisAmount = getAmountFor(rental);

      totalAmount += thisAmount;

      frequentRenterPoints += getFrequentRenterPoints(rental);

      //show figures for this rental
      result.append(getRentalAmountLine(thisAmount, rental));

    }
    //add footer lines
    result.append("Amount owed is ").append(String.valueOf(totalAmount)).append("\n");
    result.append("You earned ").append(String.valueOf(frequentRenterPoints)).append(" frequent renter points");
    return result.toString();

  }

  private int getFrequentRenterPoints(Rental rental) {
    // add frequent renter points
    int frequentRenterPoints = 1;
    // add bonus for a two day new release rental
    if ((rental.tape().movie().priceCode() == Movie.NEW_RELEASE) && rental.daysRented() > 1) frequentRenterPoints++;
    return frequentRenterPoints;
  }

  private double getAmountFor(Rental each) {
    double thisAmount = 0;

    //determine amounts for each line
    switch (each.tape().movie().priceCode()) {
      case Movie.REGULAR:
        thisAmount += 2;
        if (each.daysRented() > 2)
          thisAmount += (each.daysRented() - 2) * 1.5;
        break;
      case Movie.NEW_RELEASE:
        thisAmount += each.daysRented() * 3;
        break;
      case Movie.CHILDRENS:
        thisAmount += 1.5;
        if (each.daysRented() > 3)
          thisAmount += (each.daysRented() - 3) * 1.5;
        break;

    }
    return thisAmount;
  }

  private String getRentalAmountLine(double thisAmount, Rental each) {
    return "\t" + each.tape().movie().name() + "\t" + String.valueOf(thisAmount) + "\n";
  }

  private String name() {
    return _name;
  }

  public void addRental(Rental rental) {
    rentals.add(rental);
    _rentals.addElement(rental);
  }
}
