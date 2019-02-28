package com.drongox.movierental;

import java.util.Vector;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Customer {

    private String _name;
    private Vector<Rental> _rentals = new Vector<>();


    public Customer(String name) {
        _name = name;
    }


    public String statement() {

        return String.join("",
                buildHeader(),
                buildRentalDetails(),
                buildFooter()
        );

    }


    private String buildFooter() {
        String result = "Amount owed is " + computeTotalAmount() + "\n";
        result += "You earned " + computeTotalFrequentPoints() + " frequent renter points";
        return result;
    }


    private String buildRentalDetails() {
        return _rentals.stream()
                .map(
                        rental -> "\t" + rental.tape().movie().name() + "\t" + rental.computeAmount()
                                + "\n")
                .collect(Collectors.joining());
    }


    private String buildHeader() {
        return "Rental Record for " + name() + "\n";
    }


    private int computeTotalFrequentPoints() {
        return _rentals.stream()
                .mapToInt(Rental::computeFrequentRenterPoints)
                .sum();
    }


    private double computeTotalAmount() {
        return _rentals.stream()
                .mapToDouble(Rental::computeAmount)
                .sum();

    }


    private String name() {
        return _name;
    }


    public void addRental(Rental arg) {
        _rentals.addElement(arg);
    }


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


        private int computeFrequentRenterPoints() {
            Movie.Type type = Movie.Type.of(tape().movie().priceCode());
            return type.frequentRenterPoints(daysRented());
        }


        private double computeAmount() {
            Movie.Type movieType = Movie.Type.of(tape().movie().priceCode());
            return movieType.rentalAmount(daysRented());
        }


        public int daysRented() {
            return _daysRented;
        }


        public Tape tape() {
            return _tape;
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


        private enum Type {
            CHILDRENS(Movie.CHILDRENS, days ->
            {
                if (days > 3) {
                    return 1.5 + (days - 3) * 1.5;
                }
                return 1.5;
            },
                    days -> 1),
            REGULAR(Movie.REGULAR, days ->
            {
                if (days > 2) {
                    return 2 + (days - 2) * 1.5;
                }
                return 2;
            }, days -> 1),
            NEW_RELEASE(Movie.NEW_RELEASE, days -> days * 3, days -> days > 1 ? 2 : 1);

            private int priceCode;
            private IntToDoubleFunction amountCalculator;
            private IntUnaryOperator frpCalculator;


            Type(int priceCode,
                 IntToDoubleFunction amountCalculator, IntUnaryOperator frpCalculator) {
                this.priceCode = priceCode;
                this.amountCalculator = amountCalculator;
                this.frpCalculator = frpCalculator;
            }


            private static Type of(int priceCode) {
                return Stream.of(values())
                        .filter(type -> type.priceCode == priceCode)
                        .findAny()
                        .orElseThrow(IllegalArgumentException::new);
            }


            public double rentalAmount(int daysRented) {
                return amountCalculator.applyAsDouble(daysRented);
            }


            public int frequentRenterPoints(int daysRented) {
                return frpCalculator.applyAsInt(daysRented);
            }
        }
    }
}
