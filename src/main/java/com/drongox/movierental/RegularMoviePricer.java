package com.drongox.movierental;

@FunctionalInterface
public interface RegularMoviePricer {

    double computePrice(int rentedDays);

}
