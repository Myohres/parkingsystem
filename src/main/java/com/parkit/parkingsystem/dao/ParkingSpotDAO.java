package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ParkingSpotDAO {
    /**
     * Initialise Logger "ParkingSpotDAO".
     */
    private static final Logger LOGGER = LogManager.getLogger("ParkingSpotDAO");

    /**
     * Create new DataBaseConfig.
     */
    private DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * set DataBaseConfig.
     * @param newDataBaseConfig DataBaseConfig
     */
    public void setDataBaseConfig(final DataBaseConfig newDataBaseConfig) {
        dataBaseConfig =  newDataBaseConfig;
    }

    /**
     * get DataBaseConfig.
     * @return dataBaseConfig
     */
    public DataBaseConfig getDataBaseConfig() {
        return this.dataBaseConfig;
    }

    /**
     * Get next parking spot available.
     * @param parkingType ParkingType
     * @return parkingSpot
     */
    public int getNextAvailableSlot(final ParkingType parkingType) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        int result = -1;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            ps.setString(1, parkingType.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (Exception ex) {
            LOGGER.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return result;
    }

    /**
     * update parking spot information.
     * @param parkingSpot ParkingSpot
     * @return boolean updating
     */
    public boolean updateParking(final ParkingSpot parkingSpot) {
        //update the availability fo that parking slot
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());
            int updateRowCount = ps.executeUpdate();
            return (updateRowCount == 1);
        } catch (Exception ex) {
            LOGGER.error("Error updating parking info", ex);
            return false;
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
    }

}
