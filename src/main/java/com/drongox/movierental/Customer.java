package com.drongox.movierental;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

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

    return generateHeaderLine() +
            generateAmountLines() +
            generateTotalAmountLine() +
            generateTotalFrequentRentersPointsLine();

  }

  private String generateTotalFrequentRentersPointsLine() {
    return "You earned " + getTotalFrequentRenterPoints() + " frequent renter points";
  }

  private String generateTotalAmountLine() {
    return "Amount owed is " + getTotalAmount() + "\n";
  }

  private String generateAmountLines() {
    return rentals.stream()
            .map(this::generateAmountLine)
            .collect(Collectors.joining());
  }

  private String generateAmountLine(Rental rental) {
    return getRentalAmountLine(getAmountFor(rental), rental);
  }

  private String generateHeaderLine() {
    return "Rental Record for " + name() + "\n";
  }

  private int getTotalFrequentRenterPoints() {
    return rentals.stream()
            .mapToInt(this::getFrequentRenterPoints)
            .sum();
  }

  private double getTotalAmount() {
    return rentals.stream()
            .mapToDouble(this::getAmountFor)
            .sum();
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
