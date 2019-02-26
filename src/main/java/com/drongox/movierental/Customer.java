package com.drongox.movierental;

import java.util.Vector;
import java.util.function.DoubleFunction;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Customer
{

  public static class Tape
  {

    private String _serialNumber;
    private Movie _movie;


    public Tape(String serialNumber, Movie movie)
    {
      _serialNumber = serialNumber;
      _movie = movie;
    }


    public Movie movie()
    {
      return _movie;
    }


    public String serialNumber()
    {
      return _serialNumber;
    }
  }


  public static class Rental
  {

    private Tape _tape;
    private int _daysRented;


    public Rental(Tape tape, int daysRented)
    {
      _tape = tape;
      _daysRented = daysRented;
    }


    public int daysRented()
    {
      return _daysRented;
    }


    public Tape tape()
    {
      return _tape;
    }


    public double getAmount()
    {
      return tape().movie().movieType.getAmount(daysRented());
    }

    public int getFrequentRenterPoints()
    {
      return tape().movie().movieType.getFrequentRenterPoints(daysRented());
    }
  }


  public static class Movie
  {
    public static final int CHILDRENS = 2;
    public static final int REGULAR = 0;
    public static final int NEW_RELEASE = 1;

    private int _priceCode;
    private String _name;
    private MovieType movieType;


    public Movie(String name, int priceCode)
    {
      _name = name;
      _priceCode = priceCode;
      movieType = MovieType.of(priceCode);
    }


    public int priceCode()
    {
      return _priceCode;
    }


    public String name()
    {
      return _name;
    }
  }


  private enum MovieType
  {
    CHILDRENS(Movie.CHILDRENS,

              days ->
              {
                if (days > 3) {
                  return 1.5 + (days - 3) * 1.5;
                }
                else {
                  return 1.5;
                }
              },

              days -> 1),

    REGULAR(Movie.REGULAR,

            days ->
            {
              if (days > 2) {
                return 2 + (days - 2) * 1.5;
              }
              else
                return 2.0;
            },

            days -> 1),

    NEW_RELEASE(Movie.NEW_RELEASE, days -> days * 3.0, days -> days > 1 ? 2 : 1);


    private int priceCode;
    private DoubleFunction<Double> amountCalculator;
    private IntFunction<Integer> frpCalculator;


    MovieType(int priceCode,
              DoubleFunction<Double> amountCalculator,
              IntFunction<Integer> frpCalculator)
    {
      this.priceCode = priceCode;
      this.amountCalculator = amountCalculator;
      this.frpCalculator = frpCalculator;
    }


    public static MovieType of(int priceCode)
    {
      return Stream.of(values())
                   .filter(e -> e.priceCode == priceCode)
                   .findAny()
                   .orElseThrow(IllegalArgumentException::new);
    }


    public double getAmount(int daysRented)
    {
      return amountCalculator.apply(daysRented);
    }


    public int getFrequentRenterPoints(int daysRented)
    {
      return frpCalculator.apply(daysRented);
    }
  }


  private String _name;
  private Vector<Rental> _rentals = new Vector<>();


  public Customer(String name)
  {
    _name = name;
  }


  public String statement()
  {
    return String.join("",
                       getHeader(),
                       getStatementBody(),
                       getTotalAmountStatement(),
                       getTotalFrpStatement());
  }


  private String getTotalFrpStatement()
  {
    return "You earned " + getTotalFrequentRenterPoints() + " frequent renter points";
  }


  private String getTotalAmountStatement()
  {
    return "Amount owed is " + getTotalAmount() + "\n";
  }


  private String getHeader()
  {
    return "Rental Record for " + name() + "\n";
  }


  private String getStatementBody()
  {
    return _rentals
        .stream()
        .map(rental -> "\t" + rental.tape().movie().name() + "\t" + rental.getAmount() + "\n")
        .collect(Collectors.joining());
  }


  private int getTotalFrequentRenterPoints()
  {
    return _rentals
        .stream()
        .mapToInt(Rental::getFrequentRenterPoints)
        .sum();
  }


  private double getTotalAmount()
  {
    return _rentals
        .stream()
        .mapToDouble(Rental::getAmount)
        .sum();
  }


  private String name()
  {
    return _name;
  }


  public void addRental(Rental arg)
  {
    _rentals.addElement(arg);
  }
}
