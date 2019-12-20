package com.drongox.movierental;

public class Tape {

    private String serialNumber;
    private Movie movie;

    public Tape(String serialNumber, Movie movie) {
        this.serialNumber = serialNumber;
        this.movie = movie;
    }

    public Movie movie() {
        return movie;
    }

    public String serialNumber() {
        return serialNumber;
    }
}