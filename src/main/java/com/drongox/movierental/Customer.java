package com.drongox.movierental;

import java.util.Enumeration;
import java.util.Vector;

public class Customer {

    private String _name;
    private Vector<Rental> _rentals = new Vector<>();

    public Customer(String name) {
        _name = name;
    }

    public String statement() {
        double totalAmount = 0;
        int totalFrequentPoints = 0;
        String result = "Rental Record for " + name() + "\n";
        for (Rental rental : _rentals) {
            totalAmount += getRentalAmount(rental);

            totalFrequentPoints += computeFrequentRenterPoints(rental);

            result += "\t" + rental.tape().movie().name() + "\t" + getRentalAmount(rental) + "\n";

        }

        result += "Amount owed is " + totalAmount + "\n";
        result += "You earned " + totalFrequentPoints + " frequent renter points";
        return result;

    }

    private int computeFrequentRenterPoints(Rental rental) {
        int frequentRenterPoints = 0;
        frequentRenterPoints++;
        if ((rental.tape().movie().priceCode() == Movie.NEW_RELEASE) && rental.daysRented() > 1) frequentRenterPoints++;
        return frequentRenterPoints;
    }

    private double getRentalAmount(Rental rental) {
        double rentalAmount = 0;
        switch (rental.tape().movie().priceCode()) {
            case Movie.REGULAR:
                rentalAmount += 2;
                if (rental.daysRented() > 2)
                    rentalAmount += (rental.daysRented() - 2) * 1.5;
                break;
            case Movie.NEW_RELEASE:
                rentalAmount += rental.daysRented() * 3;
                break;
            case Movie.CHILDRENS:
                rentalAmount += 1.5;
                if (rental.daysRented() > 3)
                    rentalAmount += (rental.daysRented() - 3) * 1.5;
                break;

        }
        return rentalAmount;
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
