package uk.zinch.movierental;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.function.IntToDoubleFunction;

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

    private double calculateAmount(){
      return Movie.Type.of(tape().movie().priceCode()).calculateAmount(daysRented());
    }
  }


  public static class Movie {
    public static final int  CHILDRENS = 2;
    public static final int  REGULAR = 0;
    public static final int  NEW_RELEASE = 1;

    private enum Type {
      REGULAR(Movie.REGULAR,  (days) -> days <= 2 ? 2 : 2 + (days - 2) * 1.5),
      CHILDREN(Movie.CHILDRENS, (days) -> days <= 3 ? 1.5 : 1.5 + (days - 3) * 1.5),
      NEW_RELEASE(Movie.NEW_RELEASE, (days) -> days * 3);

      private final int legacyCode;
      private final IntToDoubleFunction amountCalculator;

      Type(int legacyCode, IntToDoubleFunction amountCalculator) {
        this.legacyCode = legacyCode;
        this.amountCalculator = amountCalculator;
      }

      public static Type of(int legacyCode) {
        return Arrays.stream(values())
                .filter(type -> type.legacyCode == legacyCode)
                .findFirst().orElseThrow();
      }

      public double calculateAmount(int daysRented) {
        return this.amountCalculator.applyAsDouble(daysRented);
      }
    }

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
      double rentalAmount = rental.calculateAmount();
      totalAmount += rentalAmount;

      // add frequent renter points
      frequentRenterPoints++;

      // add bonus for a two day new release rental
      if ((rental.tape().movie().priceCode() == Movie.NEW_RELEASE) && rental.daysRented() > 1) frequentRenterPoints++;

      //show figures for this rental
      result += "\t" + rental.tape().movie().name() + "\t" + rentalAmount + "\n";

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
