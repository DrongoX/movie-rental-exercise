package com.drongox.movierental;

import static org.junit.Assert.*;

import com.drongox.movierental.Customer.Movie;
import com.drongox.movierental.Customer.Rental;
import com.drongox.movierental.Customer.Tape;
import org.junit.Assert;
import org.junit.Test;

public class CustomerTest
{

  @Test
  public void should_return_zero_point_and_zero_amount_when_no_rental_record()
  {
    Customer customer = new Customer(null);

    String statement = customer.statement();

    assertEquals("Rental Record for null\n"
                        + "Amount owed is 0.0\n"
                        + "You earned 0 frequent renter points", statement);

  }


  @Test
  public void should_return_1_frp_amount_3_when_new_release_1_day()
  {
    Customer customer = new Customer("Omar");

    Movie movie = new Movie("Dune", Movie.NEW_RELEASE);
    Tape tape = new Tape(null, movie);

    Rental rental = new Rental(tape, 1);
    customer.addRental(rental);

    String statement = customer.statement();

    assertEquals("Rental Record for Omar\n"
                 + "\tDune\t3.0\n"
                 + "Amount owed is 3.0\n"
                 + "You earned 1 frequent renter points", statement);

  }

  @Test
  public void should_return_2_frp_amount_10_5_when_two_tapes()
  {
    Customer customer = new Customer("Omar");

    Movie movie = new Movie("Dune", Movie.NEW_RELEASE);
    Tape tape = new Tape(null, movie);
    Rental rental = new Rental(tape, 1);
    customer.addRental(rental);

    Movie movie2 = new Movie("Twilight", Movie.CHILDRENS);
    Tape tape2 = new Tape(null, movie2);
    Rental rental2 = new Rental(tape2, 7);
    customer.addRental(rental2);

    String statement = customer.statement();

    assertEquals("Rental Record for Omar\n"
                 + "\tDune\t3.0\n"
                 + "\tTwilight\t7.5\n"
                 + "Amount owed is 10.5\n"
                 + "You earned 2 frequent renter points", statement);

  }
}