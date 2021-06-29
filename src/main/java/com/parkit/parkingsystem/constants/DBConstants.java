package com.parkit.parkingsystem.constants;

public class DBConstants {

    /**
     * default constructor.
     */
    public void dBConstants() { }

    /**
     * SQL command getting Next available parking Spot.
     */
    public static final String GET_NEXT_PARKING_SPOT =
            "select min(PARKING_NUMBER) "
                    + "from parking where AVAILABLE = true and TYPE = ?";
    /**
     * SQL command update availability parking spot.
     */
    public static final String UPDATE_PARKING_SPOT =
            "update parking set available = ? where PARKING_NUMBER = ?";
    /**
     * SQL command save information ticket.
     */
    public static final String SAVE_TICKET =
            "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER,"
                    + " PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
    /**
     * SQL command update price and Out-time ticket.
     */
    public static final String UPDATE_TICKET =
            "update ticket set PRICE=?, OUT_TIME=? where ID=?";
    /**
     * SQL command get a ticket.
     */
    public static final String GET_TICKET =
            "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, "
                    + "p.TYPE from ticket t,parking p where p.parking_number = "
                    + "t.parking_number and t.VEHICLE_REG_NUMBER=? "
                    + "order by t.IN_TIME  limit 1";
    /**
     * SQL command get vehicle number.
     */
    public static final String IS_RECURRENT_USER =
            "SELECT VEHICLE_REG_NUMBER FROM ticket";
}
