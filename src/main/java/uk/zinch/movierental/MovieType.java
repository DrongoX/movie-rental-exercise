package uk.zinch.movierental;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;

public enum MovieType {
  REGULAR(Customer.Movie.REGULAR, daysRented -> 1, daysRented -> daysRented > 2 ? 2 + (daysRented - 2) * 1.5 : 2),
  CHILDREN(Customer.Movie.CHILDRENS, daysRented -> 1, daysRented -> daysRented > 3 ? (daysRented - 2) * 1.5 : 1.5),
  NEW_RELEASE(Customer.Movie.NEW_RELEASE, daysRented -> daysRented > 1 ? 2 : 1, daysRented -> daysRented * 3),
  UNKNOWN(Integer.MAX_VALUE, daysRented -> 1, daysRented -> 0);

  private final int priceCode;
  private final IntFunction<Integer> frpCalculator;
  private final IntToDoubleFunction amountCalculator;

  MovieType(int priceCode, IntFunction<Integer> calculator, IntToDoubleFunction amountCalculator) {
    this.priceCode = priceCode;
    this.frpCalculator = calculator;
    this.amountCalculator = amountCalculator;

  }

  public static MovieType from(int priceCode) {
    return Arrays.stream(values())
                 .filter(mv -> mv.getPriceCode() == priceCode)
                 .findFirst().orElse(UNKNOWN);
  }

  private int getPriceCode() {
    return priceCode;
  }

  public int calculateFrequentRenterPoints(int daysRented) {
    return this.frpCalculator.apply(daysRented);
  }

  public double calculateAmount(int daysRented) {
    return this.amountCalculator.applyAsDouble(daysRented);
  }

  private interface Calculator {

    int calculate(int daysRented);
  }

  private interface AmountCalculator {

    double calculate(int daysRented);
  }
}
