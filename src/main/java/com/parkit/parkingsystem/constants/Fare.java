package com.parkit.parkingsystem.constants;

public class Fare {
    /**
     * default constructor.
     */
    public void constructor() { }
    /**
     * Constant Rate for bike per hour.
     */
    public static final double BIKE_RATE_PER_HOUR = 1.0;
    /**
     * Constant Rate for car per hour.
     */
    public static final double CAR_RATE_PER_HOUR = 1.5;
    /**
     * Constant Rate for vehicle stay less 30 minutes.
     */
    public static final double VEHICLE_RATE_FOR_LESS_HALF_HOUR = 0;
    /**
     * Constant Rate for vehicle came before.
     */
    public static final double RECURRENT_USER_RATE = 0.95;
    /**
     * Constant for time free price.
     */
    public static final double FREE_DURATION = 0.5;
}
