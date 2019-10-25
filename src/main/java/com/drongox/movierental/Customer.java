package com.drongox.movierental;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class Customer {

    public static class Tape {

        private String _serialNumber;
        private Movie _movie;

        public Tape(String serialNumber, Movie movie) {
            _serialNumber = serialNumber;
            _movie = movie;
        }

        public Movie movie() {
            return _movie;
        }

        public String serialNumber() {
            return _serialNumber;
        }
    }


    public static class Rental {

        private Tape _tape;
        private int _daysRented;

        public Rental(Tape tape, int daysRented) {
            _tape = tape;
            _daysRented = daysRented;
        }

        public int daysRented() {
            return _daysRented;
        }

        public Tape tape() {
            return _tape;
        }

        public double computeAmount() {
            return tape().movie().getMovieType().computeAmount(daysRented());

        }

        public int computeFrequentRenterPoints() {
            return tape().movie().getMovieType().computeFrequentRenterPoints(daysRented());
        }

    }

    public enum MovieType {

        CHILDREN(2,
                daysRented -> {
                    if (daysRented > 3)
                        return 1.5 + (daysRented - 3) * 1.5;
                    return 1.5;
                },
                daysRented -> 1),

        REGULAR(0,
                daysRented -> {
                    if (daysRented > 2)
                        return 2 + (daysRented - 2) * 1.5;
                    return 2;
                },
                daysRented -> 1),

        NEW_RELEASE(1,
                daysRented -> daysRented * 3,
                daysRented -> daysRented > 1 ? 2 : 1);


        private final int priceCode;
        private final AmountCalculator amountCalculator;
        private final FrequentRenterPointsCalculator frequentRenterPointsCalculator;

        MovieType(int priceCode, AmountCalculator amountCalculator, FrequentRenterPointsCalculator frequentRenterPointsCalculator) {
            this.priceCode = priceCode;
            this.amountCalculator = amountCalculator;
            this.frequentRenterPointsCalculator = frequentRenterPointsCalculator;
        }

        public static MovieType of(int priceCode) {
            return Arrays.stream(values())
                    .filter(type -> type.priceCode == priceCode)
                    .findFirst()
                    .orElseThrow();
        }

        public double computeAmount(int daysRented) {
            return amountCalculator.compute(daysRented);
        }

        public int computeFrequentRenterPoints(int daysRented) {
            return frequentRenterPointsCalculator.compute(daysRented);
        }

        private interface AmountCalculator {
            double compute(int daysRented);
        }

        private interface FrequentRenterPointsCalculator {
            int compute(int daysRented);
        }
    }

    public static class Movie {
        public static final int CHILDRENS = 2;
        public static final int REGULAR = 0;
        public static final int NEW_RELEASE = 1;

        private int _priceCode;
        private String _name;

        public Movie(String name, int priceCode) {
            _name = name;
            _priceCode = priceCode;
        }

        public int priceCode() {
            return _priceCode;
        }

        public String name() {
            return _name;
        }

        public boolean isNewRelease() {
            return priceCode() == NEW_RELEASE;
        }

        public MovieType getMovieType(){
            return MovieType.of(priceCode());
        }
    }


    private String _name;
    private Vector<Rental> _rentals = new Vector<>();
    private List<Rental> rentals = new ArrayList<>();

    public Customer(String name) {
        _name = name;
    }

    public String statement() {
        return generateHeaderLine() +
                generateAmountLines() +
                generateTotalAmountLine() +
                generateTotalFrequentRentersPointsLine();
    }

    public void addRental(Rental rental) {
        rentals.add(rental);
        _rentals.addElement(rental);
    }

    private String generateHeaderLine() {
        return "Rental Record for " + name() + "\n";
    }

    private String generateAmountLines() {
        return rentals.stream()
                .map(this::generateAmountLine)
                .collect(Collectors.joining());
    }

    private String generateAmountLine(Rental rental) {
        return "\t" + rental.tape().movie().name() + "\t" + rental.computeAmount() + "\n";
    }

    private String generateTotalAmountLine() {
        return "Amount owed is " + getTotalAmount() + "\n";
    }

    private String generateTotalFrequentRentersPointsLine() {
        return "You earned " + getTotalFrequentRenterPoints() + " frequent renter points";
    }

    private int getTotalFrequentRenterPoints() {
        return rentals.stream()
                .mapToInt(Rental::computeFrequentRenterPoints)
                .sum();
    }

    private double getTotalAmount() {
        return rentals.stream()
                .mapToDouble(Rental::computeAmount)
                .sum();
    }

    private String name() {
        return _name;
    }
}
