package com.parkit.parkingsystem.model;

import java.util.Date;

public class Ticket {
    /**
     * ID unique number ticket.
     */
    private int id;
    /**
     /* Number ParkingSpot, type vehicle and availability.
     */
    private ParkingSpot parkingSpot;
    /**
     * Number Matriculation Vehicle.
     */
    private String vehicleRegNumber;
    /**
     * ticket price.
     */
    private double price;
    /**
     * Arrival date vehicle.
     */
    private Date inTime;
    /**
     * Get out date vehicle.
     */
    private Date outTime;
    /**
     * Get Id ticket.
     * @return id
     */
    public int getId() {
        return id;
    }
    /**
     * set id ticket.
     * @param newId int
     */
    public void setId(final int newId) {
        this.id = newId;
    }
    /**
     * get ParkingSpot.
     * @return parkingSpot
     */
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }
    /**
     * set ParkingSpot.
     * @param newParkingSpot ParkingSpot
     */
    public void setParkingSpot(final ParkingSpot newParkingSpot) {
        this.parkingSpot = newParkingSpot;
    }
    /**
     * get Vehicle Matriculation number.
     * @return vehicleRegNumber
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }
    /**
     * set Vehicle Matriculation number.
     * @param newVehicleRegNumber String
     */
    public void setVehicleRegNumber(final String newVehicleRegNumber) {
        this.vehicleRegNumber = newVehicleRegNumber;
    }
    /**
     * get ticket price.
     * @return price
     */
    public double getPrice() {
        return price;
    }
    /**
     * set ticket newPrice.
     * @param newPrice double
     */
    public void setPrice(final double newPrice) {
        this.price = newPrice;
    }
    /**
     * Get arrival date vehicle.
     * @return intime
     */
    public Date getInTime() {
        if (this.inTime == null) {
            return null;
        }
        return new Date(this.inTime.getTime());
    }
    /**
     * set arrival date vehicle.
     * @param newinTime Date
     */
    public void setInTime(final Date newinTime) {
        if (newinTime == null) {
            this.inTime = null;
        } else {
            this.inTime = new Date(newinTime.getTime());
        }
    }
    /**
     * Get get out date vehicle.
     * @return outTime
     */
    public Date getOutTime() {
        if (this.outTime == null) {
            return null;
        }
        return new Date(this.outTime.getTime());
    }
    /**
     * Set get out date vehicle.
     * @param newOutTime Date
     */
    public void setOutTime(final Date newOutTime) {
        if (newOutTime == null) {
            this.outTime = null;
        } else {
            this.outTime = new Date(newOutTime.getTime());
        }
    }
}
