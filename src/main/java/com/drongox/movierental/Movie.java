package com.drongox.movierental;

public class Movie {


    private int priceCode;
    private String name;

    public Movie(String name, int priceCode) {
      this.name = name;
      this.priceCode = priceCode;
    }

    public int priceCode() {
      return priceCode;
    }

    public String name() {
      return name;
    }
  }