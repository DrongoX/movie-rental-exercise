package com.drongox.movierental;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CustomerTest {

    private Customer customer;

    @Before
    public void setUp() {
        this.customer = new Customer("Guy 1");
    }


    @Test
    public void shouldCalculatePrice(){
        Movie movieOne = new Movie("Film 1", Constants.Movie.CHILDRENS);
        Movie movieTwo = new Movie("Film 2", Constants.Movie.NEW_RELEASE);

        Tape tapeOne = new Tape("123", movieOne);
        Tape tapeTwo = new Tape("456", movieTwo);

        Rental rentalOne = new Rental(tapeOne, 5);
        Rental rentalTwo = new Rental(tapeTwo, 10);

        customer.addRental(rentalOne);
        assertThat(customer.statement()).isEqualTo("Rental Record for Guy 1\n" +
                "\tFilm 1\t4.5\n" +
                "Amount owed is 4.5\n" +
                "You earned 1 frequent renter points");

        customer.addRental(rentalTwo);
        assertThat(customer.statement()).isEqualTo("Rental Record for Guy 1\n" +
                "\tFilm 1\t4.5\n" +
                "\tFilm 2\t30.0\n" +
                "Amount owed is 34.5\n" +
                "You earned 3 frequent renter points");

    }

}