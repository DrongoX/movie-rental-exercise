package com.drongox.movierental;

import static org.junit.Assert.*;

import com.drongox.movierental.Customer.Movie;
import com.drongox.movierental.Customer.Rental;
import com.drongox.movierental.Customer.Tape;
import org.junit.Test;

public class CustomerTest
{
  @Test
  public void should_print_2_0_and_1_fpp_when_regular_1_day()
  {
    Customer customer = new Customer("Ilya");
    Movie titanic = new Movie("Titanic", Movie.REGULAR);
    Tape tape = new Tape("1232BC", titanic);
    Rental rental = new Rental(tape, 1);
    customer.addRental(rental);

    String statement = customer.statement();

    assertEquals("Rental Record for Ilya\n"
                 + "\tTitanic\t2.0\n"
                 + "Amount owed is 2.0\n"
                 + "You earned 1 frequent renter points", statement);
  }


  @Test
  public void should_print_9_0_and_2_fpp_when_new_release_3_day()
  {
    Customer customer = new Customer("Ilya");
    Movie movie = new Movie("Avengers 5", Movie.NEW_RELEASE);
    Tape tape = new Tape("MARV12312", movie);
    Rental rental = new Rental(tape, 3);
    customer.addRental(rental);

    String statement = customer.statement();

    assertEquals("Rental Record for Ilya\n"
                 + "\tAvengers 5\t9.0\n"
                 + "Amount owed is 9.0\n"
                 + "You earned 2 frequent renter points", statement);
  }


  @Test
  public void should_print_8_0_and_3_fpp_when_several_movies_2_day()
  {
    Customer customer = new Customer("Ilya");

    Movie avengers = new Movie("Avengers 5", Movie.NEW_RELEASE);
    Tape marv12312 = new Tape("MARV12312", avengers);
    Rental avengersRental = new Rental(marv12312, 2);
    customer.addRental(avengersRental);

    Movie titanic = new Movie("Titanic", Movie.REGULAR);
    Tape titanicTape = new Tape("1232BC", titanic);
    Rental titanicRental = new Rental(titanicTape, 2);
    customer.addRental(titanicRental);

    String statement = customer.statement();

    assertEquals("Rental Record for Ilya\n"
                 + "\tAvengers 5\t6.0\n"
                 + "\tTitanic\t2.0\n"
                 + "Amount owed is 8.0\n"
                 + "You earned 3 frequent renter points", statement);
  }
}