package uk.zinch.movierental;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

interface MovieRentalCalculator {
    Double amount(Customer.Rental rental);

    Integer frequentRenterPoints(Customer.Rental rental);
}

enum MovieRentalCalculatorSingleton {
    CHILDREN(new ChildrenMovieRentalCalculator(), Customer.Movie.Type.CHILDREN),
    NEW_RELEASE(new NewReleaseMovieRentalCalculator(), Customer.Movie.Type.NEW_RELEASE),
    REGULAR(new RegularMovieRentalCalculator(), Customer.Movie.Type.REGULAR);

    final MovieRentalCalculator calculator;
    final Customer.Movie.Type movieType;

    MovieRentalCalculatorSingleton(MovieRentalCalculator calculator, Customer.Movie.Type movieType) {
        this.calculator = calculator;
        this.movieType = movieType;
    }
}


class ChildrenMovieRentalCalculator implements MovieRentalCalculator {

    @Override
    public Double amount(Customer.Rental rental) {
        return 1.5 + Math.max((rental.daysRented() - 3.0) * 1.5, 0);
    }

    @Override
    public Integer frequentRenterPoints(Customer.Rental rental) {
        return 1;
    }
}

class RegularMovieRentalCalculator implements MovieRentalCalculator {
    @Override
    public Double amount(Customer.Rental rental) {
        return 2.0 + Math.max((rental.daysRented() - 2.0) * 1.5, 0);
    }

    @Override
    public Integer frequentRenterPoints(Customer.Rental rental) {
        return 1;
    }
}

class NewReleaseMovieRentalCalculator implements MovieRentalCalculator {
    @Override
    public Double amount(Customer.Rental rental) {
        return rental.daysRented() * 3.0;
    }

    @Override
    public Integer frequentRenterPoints(Customer.Rental rental) {
        var result = 1;
        if (rental.daysRented() > 1) result++;
        return result;
    }
}


public class Customer {

    public record Tape(String serialNumber, Movie movie) {
    }

    public record Rental(Tape tape, Integer daysRented) {

        private MovieRentalCalculator calculator() {
            return Arrays
                    .stream(MovieRentalCalculatorSingleton.values())
                    .filter(calculator -> Movie.Type.of(this.priceCode()).equals(calculator.movieType))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("calculator not found"))
                    .calculator;
        }

        private int priceCode() {
            return tape.movie.priceCode;
        }

        double amount() {
            return calculator().amount(this);
        }

        int frequentRenterPoints() {
            return calculator().frequentRenterPoints(this);
        }
    }

    public record Movie(String name, Integer priceCode) {
        public static final int CHILDRENS = 2;
        public static final int REGULAR = 0;
        public static final int NEW_RELEASE = 1;

        enum Type {
            REGULAR(Movie.REGULAR),
            CHILDREN(Movie.CHILDRENS),
            NEW_RELEASE(Movie.NEW_RELEASE);

            private final int legacyCode;


            Type(Integer legacyCode) {
                this.legacyCode = legacyCode;
            }

            public static Type of(int legacyCode) {
                return Arrays.stream(values()).filter(type -> type.legacyCode == legacyCode).findFirst().orElseThrow();
            }
        }
    }

    private final String _name;
    private final Vector<Rental> _rentals = new Vector<>();

    public Customer(String name) {
        _name = name;
    }

    public String statement() {
        var rentals = new ArrayList<>(_rentals);

        String result = writeHeader() + "\n";
        result += writeStatementBody(rentals);
        result += writeFooter(getTotalAmount(rentals), getFrequentRenterPoints(rentals));
        return result;
    }

    private String writeStatementBody(List<Rental> rentals) {
        return rentals
                .stream()
                .map(rental -> new StringBuilder()
                        .append("\t")
                        .append(rental.tape().movie().name())
                        .append("\t")
                        .append(rental.amount())
                        .append("\n"))
                .reduce(new StringBuilder(), StringBuilder::append)
                .toString();
    }

    private int getFrequentRenterPoints(List<Rental> rentals) {
        return rentals
                .stream()
                .map(Rental::frequentRenterPoints)
                .reduce(0, Integer::sum);
    }

    private double getTotalAmount(List<Rental> rentals) {
        return rentals
                .stream()
                .map(Rental::amount)
                .reduce(0.0, Double::sum);
    }

    private String writeHeader() {
        return "Rental Record for " + name();
    }

    private static String writeFooter(double totalAmount, int frequentRenterPoints) {
        return "Amount owed is " + totalAmount + "\nYou earned " + frequentRenterPoints + " frequent renter points";
    }


    private String name() {
        return _name;
    }

    public void addRental(Rental rental) {
        _rentals.addElement(rental);
    }
}