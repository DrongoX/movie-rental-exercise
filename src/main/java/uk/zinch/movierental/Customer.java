package uk.zinch.movierental;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

interface MovieRentalCalculator {
    Double calculateAmount();

    Integer calculateFrequentRenterPoints();
}


class ChildrenMovieRentalCalculator implements MovieRentalCalculator {
    private final Customer.Rental rental;

    public ChildrenMovieRentalCalculator(Customer.Rental rental) {
        this.rental = rental;
    }

    @Override
    public Double calculateAmount() {
        return 1.5 + Math.max((rental.daysRented() - 3.0) * 1.5, 0);
    }

    @Override
    public Integer calculateFrequentRenterPoints() {
        return 1;
    }
}

class RegularMovieRentalCalculator implements MovieRentalCalculator {
    private final Customer.Rental rental;

    public RegularMovieRentalCalculator(Customer.Rental rental) {
        this.rental = rental;
    }

    @Override
    public Double calculateAmount() {
        return 2.0 + Math.max((rental.daysRented() - 2.0) * 1.5, 0);
    }

    @Override
    public Integer calculateFrequentRenterPoints() {
        return 1;
    }
}

class NewReleaseMovieRentalCalculator implements MovieRentalCalculator {
    private final Customer.Rental rental;

    public NewReleaseMovieRentalCalculator(Customer.Rental rental) {
        this.rental = rental;
    }

    @Override
    public Double calculateAmount() {
        return rental.daysRented() * 3.0;
    }

    @Override
    public Integer calculateFrequentRenterPoints() {
        var result = 1;
        if (rental.daysRented() > 1) result++;
        return result;
    }
}


public class Customer {

    public record Tape(String serialNumber, Movie movie) {
    }

    public record Rental(Tape tape, Integer daysRented) {
        int priceCode() {
            return tape.movie.priceCode;
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
                        .append(createRentalCalculator(rental).calculateAmount())
                        .append("\n"))
                .reduce(new StringBuilder(), StringBuilder::append)
                .toString();
    }

    private int getFrequentRenterPoints(List<Rental> rentals) {
        return rentals
                .stream()
                .map(rental -> createRentalCalculator(rental).calculateFrequentRenterPoints())
                .reduce(0, Integer::sum);
    }

    private double getTotalAmount(List<Rental> rentals) {
        return rentals
                .stream()
                .map(rental -> createRentalCalculator(rental).calculateAmount())
                .reduce(0.0, Double::sum);
    }

    private String writeHeader() {
        return "Rental Record for " + name();
    }

    private static String writeFooter(double totalAmount, int frequentRenterPoints) {
        return "Amount owed is " + totalAmount + "\nYou earned " + frequentRenterPoints + " frequent renter points";
    }

    private static MovieRentalCalculator createRentalCalculator(Rental rental) {
        return switch (Movie.Type.of(rental.priceCode())) {
            case Movie.Type.CHILDREN -> new ChildrenMovieRentalCalculator(rental);
            case Movie.Type.NEW_RELEASE -> new NewReleaseMovieRentalCalculator(rental);
            case Movie.Type.REGULAR -> new RegularMovieRentalCalculator(rental);
        };
    }


    private String name() {
        return _name;
    }

    public void addRental(Rental rental) {
        _rentals.addElement(rental);
    }
}