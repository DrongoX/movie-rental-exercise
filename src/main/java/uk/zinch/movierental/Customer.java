package uk.zinch.movierental;

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

    public double computeAmount() {
      	return  MovieType.from(tape().movie().priceCode()).calculateAmount(daysRented());
    }

	public int computeFrequentRenterPoints() {
	      return MovieType.from(tape().movie().priceCode()).calculateFrequentRenterPoints(daysRented());
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
    return buildCustomerInformations() + buildAllRentalDetails() + buildTotalInformations();
  }

  private String buildTotalInformations() {
     return "Amount owed is " + computeTotalAmount() + "\n" +
     "You earned " + computeTotalFrequentRenterPoints() + " frequent renter points";
  }

  private String buildCustomerInformations() {
    return "Rental Record for " + name() + "\n";
  }

  private String buildAllRentalDetails() {
    return rentals.stream().map(rental-> "\t" + rental.tape().movie().name() + "\t" + rental.computeAmount() + "\n")
            .collect(Collectors.joining());
  }

  private int computeTotalFrequentRenterPoints() {
    return rentals.stream().mapToInt(Rental::computeFrequentRenterPoints).sum();
  }

  private double computeTotalAmount() {
    return rentals.stream().mapToDouble(Rental::computeAmount).sum();
  }

  private String name() {
    return _name;
  }

  public void addRental(Rental arg) {
    _rentals.addElement(arg);
    rentals.add(arg);
  }
}
