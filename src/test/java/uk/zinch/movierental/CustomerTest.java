package uk.zinch.movierental;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomerTest {

  @Test
  @DisplayName("Should return the owed amount 0 and frequent renter points 0")
  void Should_return_zero_owed_amount_and_frp() {
    Customer customer = new Customer("Ilya");
    String invoice = customer.statement();
    Assertions.assertThat(invoice).isEqualTo("Rental Record for Ilya\n" +
                                                 "Amount owed is 0.0\n" +
                                                 "You earned 0 frequent renter points");
  }

  @Test
  @DisplayName("Should return the owed amount and frequent renter points of rental for children movie")
  void Should_return_owed_amount_and_frp_of_rental_for_children_movie() {
    Customer customer = new Customer("Ilya");
    Customer.Movie movie = new Customer.Movie("Joker", Customer.Movie.CHILDRENS);
    Customer.Tape tape = new Customer.Tape("serialNumber", movie);
    Customer.Rental rental = new Customer.Rental(tape, 5);
    customer.addRental(rental);
    String invoice = customer.statement();
    Assertions.assertThat(invoice).isEqualTo("Rental Record for Ilya\n" +
                                                 "\tJoker\t4.5\n" +
                                                 "Amount owed is 4.5\n" +
                                                 "You earned 1 frequent renter points");
  }

  @Test
  @DisplayName("Should return the owed amount and frequent renter points of rental for regular movie")
  void Should_return_owed_amount_and_frp_of_rental_for_regular_movie() {
    Customer customer = new Customer("Ilya");
    Customer.Movie movie = new Customer.Movie("Joker", Customer.Movie.REGULAR);
    Customer.Tape tape = new Customer.Tape("serialNumber", movie);
    Customer.Rental rental = new Customer.Rental(tape, 5);
    customer.addRental(rental);
    String invoice = customer.statement();
    Assertions.assertThat(invoice).isEqualTo("Rental Record for Ilya\n" +
                                                 "\tJoker\t6.5\n" +
                                                 "Amount owed is 6.5\n" +
                                                 "You earned 1 frequent renter points");
  }

  @Test
  @DisplayName("Should return the owed amount and frequent renter points of rental for new release movie")
  void Should_return_owed_amount_and_frp_of_rental_for_new_release_movie() {
    Customer customer = new Customer("Ilya");
    Customer.Movie movie = new Customer.Movie("Joker", Customer.Movie.NEW_RELEASE);
    Customer.Tape tape = new Customer.Tape("serialNumber", movie);
    Customer.Rental rental = new Customer.Rental(tape, 5);
    customer.addRental(rental);
    String invoice = customer.statement();
    Assertions.assertThat(invoice).isEqualTo("Rental Record for Ilya\n" +
                                                 "\tJoker\t15.0\n" +
                                                 "Amount owed is 15.0\n" +
                                                 "You earned 2 frequent renter points");
  }

  @Test
  void addRental() {
  }
}
