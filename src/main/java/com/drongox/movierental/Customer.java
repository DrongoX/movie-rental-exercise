package com.drongox.movierental;

import java.util.Vector;
import java.util.stream.Collectors;

public class Customer {

    private String _name;
    private Vector<Rental> _rentals = new Vector<>();

    public Customer(String name) {
        _name = name;
    }

    public String statement() {

        return String.join("" ,
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
                .map(rental -> "\t" + rental.tape().movie().name() + "\t" + rental.computeAmount() + "\n")
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
            int frequentRenterPoints = 0;
            frequentRenterPoints++;
            if ((tape().movie().priceCode() == Movie.NEW_RELEASE) && daysRented() > 1) frequentRenterPoints++;
            return frequentRenterPoints;
        }

        private double computeAmount() {
            double rentalAmount = 0;
            switch (tape().movie().priceCode()) {
                case Movie.REGULAR:
                    rentalAmount += 2;
                    if (daysRented() > 2)
                        rentalAmount += (daysRented() - 2) * 1.5;
                    break;
                case Movie.NEW_RELEASE:
                    rentalAmount += daysRented() * 3;
                    break;
                case Movie.CHILDRENS:
                    rentalAmount += 1.5;
                    if (daysRented() > 3)
                        rentalAmount += (daysRented() - 3) * 1.5;
                    break;

            }
            return rentalAmount;
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

    }
}
