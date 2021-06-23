package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class TicketDAO {

    /**
     * Initialise Logger "TicketDAO".
     */
    private static final Logger LOGGER = LogManager.getLogger("TicketDAO");

    /**
     * Initialise DatabaseConfig.
     */
    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * Save ticket in DB.
     * @param ticket Ticket
     * @return boolean saving ticket
     */
    public boolean saveTicket(final Ticket ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        final int idField = 1;
        final int vehicleRegNumberField = 2;
        final int priceField = 3;
        final int intimeField = 4;
        final int outimeField = 5;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            ps.setInt(idField, ticket.getParkingSpot().getId());
            ps.setString(vehicleRegNumberField, ticket.getVehicleRegNumber());
            ps.setDouble(priceField, ticket.getPrice());
            ps.setTimestamp(intimeField,
                    new Timestamp(ticket.getInTime().getTime()));
            ps.setTimestamp(outimeField,
                    (ticket.getOutTime() == null) ? null
                    : (new Timestamp(ticket.getOutTime().getTime())));
            ps.execute();
            return true;
        } catch (Exception ex) {
            LOGGER.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }

    /**
     * Get ticket from DB.
     * @param vehicleRegNumber String
     * @return ticket
     */
    public Ticket getTicket(final String vehicleRegNumber) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Ticket ticket = null;
        final int idField = 1;
        final int parkingNumberField = 2;
        final int vehicleRegNumberField = 3;
        final int priceField = 4;
        final int inTimeField = 5;
        final int outTimeField = 6;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.GET_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1, vehicleRegNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(idField),
                        ParkingType.valueOf(rs.getString(outTimeField)), false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(parkingNumberField));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(vehicleRegNumberField));
                ticket.setInTime(rs.getTimestamp(priceField));
                ticket.setOutTime(rs.getTimestamp(inTimeField));
            }

        } catch (Exception ex) {
            LOGGER.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
            return ticket;
        }
    }

    /**
     * Update price, outTime and id ticket in DB.
     * @param ticket ticket
     * @return boolean updating ticket
     */
    public boolean updateTicket(final Ticket ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        final int priceField = 1;
        final int outTimeField = 2;
        final int idField = 3;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(priceField, ticket.getPrice());
            ps.setTimestamp(outTimeField, new Timestamp(
                    ticket.getOutTime().getTime()));
            ps.setInt(idField, ticket.getId());
            ps.execute();
            return true;
        } catch (Exception ex) {
            LOGGER.error("Error saving ticket info", ex);
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }

    /**
     * Count visits number vehicle.
     * with enough visits
     * User is Reccurent User
     * @param vehicleRegNumber String
     * @return boolean Recurrent User State
     */
    public Boolean isRecurrentUser(final String vehicleRegNumber) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int visitNumberToBeRecUser = 2;
        int i = 0;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.IS_RECURRENT_USER);
            rs = ps.executeQuery();
            while (i < visitNumberToBeRecUser) {
                rs.next();
                if (rs.getString(1).equals(vehicleRegNumber)) {
                    i++;
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Dont find VehicleRegNumber", ex);
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return i >= 2;
    }
}
