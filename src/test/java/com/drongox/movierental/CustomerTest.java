package com.drongox.movierental;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CustomerTest {

    @Test
    public void statement_withClientWithoutPurchase_shouldReturnZeroDollarAndZeroPoint() {
        // Given
        final String name = "Jeremy";
        Customer customer = new Customer(name);

        // When
        final String statement = customer.statement();

        // Then
        final String expectedResult = "Rental Record for Jeremy\n" +
                "Amount owed is 0.0\n" +
                "You earned 0 frequent renter points";
        Assertions.assertThat(statement).isEqualTo(expectedResult);
    }

    @Test
    public void statement_withOneDayRegularRental_shouldReturnTwoDollarsAndOnePoint() {
        // Given
        final String name = "Jeremy";
        Customer customer = new Customer(name);
        final String serialNumber = "serialNumber";
        final Customer.Tape tape = new Customer.Tape(serialNumber, new Customer.Movie("Alien", Customer.Movie.REGULAR));
        final Customer.Rental rental = new Customer.Rental(tape, 1);
        customer.addRental(rental);

        // When
        final String statement = customer.statement();

        // Then
        final String expectedResult = "Rental Record for Jeremy\n" +
                "\tAlien\t2.0\n" +
                "Amount owed is 2.0\n" +
                "You earned 1 frequent renter points";
        Assertions.assertThat(statement).isEqualTo(expectedResult);
    }

    @Test
    public void statement_withTwoMoviesOneDayRegular_shouldReturnTwoDollarsAndOnePoint() {
        // Given
        final String name = "Jeremy";
        Customer customer = new Customer(name);

        final Customer.Tape alien = new Customer.Tape("serialNumber", new Customer.Movie("Alien", Customer.Movie.REGULAR));
        final Customer.Rental alientRental = new Customer.Rental(alien, 1);
        customer.addRental(alientRental);

        final Customer.Tape lordOfTheRings = new Customer.Tape("serialNumber", new Customer.Movie("Lord of the rings", Customer.Movie.REGULAR));
        final Customer.Rental lotrRental = new Customer.Rental(lordOfTheRings, 1);
        customer.addRental(lotrRental);

        // When
        final String statement = customer.statement();

        // Then
        final String expectedResult = "Rental Record for Jeremy\n" +
                "\tAlien\t2.0\n" +
                "\tLord of the rings\t2.0\n" +
                "Amount owed is 4.0\n" +
                "You earned 2 frequent renter points";
        Assertions.assertThat(statement).isEqualTo(expectedResult);
    }

    @Test
    public void statement_withOneDayChildrenRental_shouldReturnOneAndHalfDollarsAndOnePoint() {
        // Given
        final String name = "Didier";
        Customer customer = new Customer(name);
        final String serialNumber = "serialNumber";
        final int type = Customer.Movie.CHILDRENS;
        final Customer.Tape tape = new Customer.Tape(serialNumber, new Customer.Movie("Tom and Jerry", type));
        final Customer.Rental rental = new Customer.Rental(tape, 1);
        customer.addRental(rental);

        // When
        final String statement = customer.statement();

        // Then
        final String expectedResult = "Rental Record for Didier\n" +
                "\tTom and Jerry\t1.5\n" +
                "Amount owed is 1.5\n" +
                "You earned 1 frequent renter points";
        Assertions.assertThat(statement).isEqualTo(expectedResult);
    }

    @Test
    public void statement_withFourDaysChildrenRental_shouldReturnOneAndHalfDollarsAndOnePoint() {
        // Given
        final String name = "Didier";
        Customer customer = new Customer(name);
        final String serialNumber = "serialNumber";
        final int type = Customer.Movie.CHILDRENS;
        final Customer.Tape tape = new Customer.Tape(serialNumber, new Customer.Movie("Tom and Jerry", type));
        final Customer.Rental rental = new Customer.Rental(tape, 4);
        customer.addRental(rental);

        // When
        final String statement = customer.statement();

        // Then
        final String expectedResult = "Rental Record for Didier\n" +
                "\tTom and Jerry\t3.0\n" +
                "Amount owed is 3.0\n" +
                "You earned 1 frequent renter points";
        Assertions.assertThat(statement).isEqualTo(expectedResult);
    }

    @Test
    public void statement_withFourDaysNewReleaseRental_shouldReturnOneAndHalfDollarsAndOnePoint() {
        // Given
        final String name = "Didier";
        Customer customer = new Customer(name);
        final String serialNumber = "serialNumber";
        final int type = Customer.Movie.NEW_RELEASE;
        final Customer.Tape tape = new Customer.Tape(serialNumber, new Customer.Movie("Tom and Jerry", type));
        final Customer.Rental rental = new Customer.Rental(tape, 4);
        customer.addRental(rental);

        // When
        final String statement = customer.statement();

        // Then
        final String expectedResult = "Rental Record for Didier\n" +
                "\tTom and Jerry\t12.0\n" +
                "Amount owed is 12.0\n" +
                "You earned 2 frequent renter points";
        Assertions.assertThat(statement).isEqualTo(expectedResult);
    }
}