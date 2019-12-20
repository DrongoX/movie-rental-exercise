package com.drongox.movierental;

import java.util.ArrayList;
import java.util.List;

public class Customer {

  private String name;
  private final List<Rental> rentals;

  public Customer(String name) {
    this.name = name;
    this.rentals = new ArrayList();
  }

  public String statement() {
    double totalAmount = 0;
    int frequentRenterPoints = 0;
    StringBuilder sb = new StringBuilder(String.format("Rental Record for %s\n", name()));

    for(Rental rental : this.rentals) {
      double rentalPrice = rental.computePrice();
      totalAmount += rentalPrice;
      frequentRenterPoints += rental.computePoints();

      //show figures for this rental
      sb.append("\t" + rental.tape().movie().name() + "\t" + String.valueOf(rentalPrice) + "\n");

    }
    //add footer lines
    sb.append("Amount owed is " + totalAmount + "\n");
    sb.append("You earned " + frequentRenterPoints + " frequent renter points");
    return sb.toString();

  }

  private String name() {
    return name;
  }

  public void addRental(Rental arg) {
    rentals.add(arg);
  }
}
