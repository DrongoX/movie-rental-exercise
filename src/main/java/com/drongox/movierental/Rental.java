package com.drongox.movierental;

public class Rental {

    private Tape tape;
    private int daysRented;

    public Rental(Tape tape, int daysRented) {
        this.tape = tape;
        this.daysRented = daysRented;
    }

    public int daysRented() {
        return daysRented;
    }

    public Tape tape() {
        return tape;
    }

    public double computePrice() {
        double price = 0;
        switch (tape().movie().priceCode()) {
            case Constants.Movie.REGULAR:
                price += 2;
                if (daysRented() > 2)
                    price += (daysRented() - 2) * 1.5;
                break;
            case Constants.Movie.NEW_RELEASE:
                price += daysRented() * 3;
                break;
            case Constants.Movie.CHILDRENS:
                price += 1.5;
                if (daysRented() > 3)
                    price += (daysRented() - 3) * 1.5;
                break;
        }
        return price;
    }

    public int computePoints() {
        int point = 1;
        // add bonus for a two day new release rental
        if ((tape().movie().priceCode() == Constants.Movie.NEW_RELEASE) && daysRented() > 1) point++;
        return point;
    }
}
