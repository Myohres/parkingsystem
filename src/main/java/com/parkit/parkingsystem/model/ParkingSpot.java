package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

public class ParkingSpot {
    /**
     * number parking spot.
     */
    private int number;
    /**
    * type of vehicle.
     */
    private ParkingType parkingType;
    /**
     * availability of parkingSpot.
     */
    private boolean isAvailable;

    /**
     * ParkingSpot constructor.
     * @param numberInput int
     * @param parkingTypeInput ParkingType
     * @param isAvailableInput boolean
     */
    public ParkingSpot(final int numberInput,
                       final ParkingType parkingTypeInput,
                       final boolean isAvailableInput) {
        this.number = numberInput;
        this.parkingType = parkingTypeInput;
        this.isAvailable = isAvailableInput;
    }

    /**
     * Get ParkingSpot ID.
     * @return number ID
     */
    public int getId() {
        return number;
    }

    /**
     * Set ParkingSpot ID.
     * @param numberInput ID
     */
    public void setId(final int numberInput) {
        this.number = numberInput;
    }

    /**
     * get Parking Type of vehicle.
     * @return parkingType
     */
    public ParkingType getParkingType() {
        return parkingType;
    }

    /**
     * set Parking Type of vehicle.
     * @param parkingTypeInput ParkingType
     */
    public void setParkingType(final ParkingType parkingTypeInput) {
        this.parkingType = parkingTypeInput;
    }

    /**
     * get availability parkingSpot.
     * @return boolean isAvailable
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * set availability parkingSpot.
     * @param available boolean
     */
    public void setAvailable(final boolean available) {
        isAvailable = available;
    }

    /**
     *
     * @param o Object
     * @return number
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    /**
     * @return number
     */
    @Override
    public int hashCode() {
        return number;
    }
}
