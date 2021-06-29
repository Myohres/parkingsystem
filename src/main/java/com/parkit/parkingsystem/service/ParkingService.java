package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class ParkingService {

    /**
     * initialise Logger "Parking Service".
     */
    private static final Logger LOGGER = LogManager.getLogger("ParkingService");

    /**
     * Create a new FareCalculatorService.
     */
    private static final FareCalculatorService FARE_CALCULATOR_SERVICE =
            new FareCalculatorService();

    /**
     * Call a InputReaderUtil.
     */
    private final InputReaderUtil inputReaderUtil;

    /**
     * Call a ParkingSpotDAO.
     */
    private final ParkingSpotDAO parkingSpotDAO;

    /**
     * Call a TicketDAO.
     */
    private final TicketDAO ticketDAO;

    /**
     * Constructor ParkingService.
     * @param newInputReaderUtil InputReaderUtil
     * @param newParkingSpotDAO ParkingSpotDAO
     * @param newTicketDAO TicketDAO
     */
    public ParkingService(final InputReaderUtil newInputReaderUtil,
                          final ParkingSpotDAO newParkingSpotDAO,
                          final TicketDAO newTicketDAO) {
        this.inputReaderUtil = newInputReaderUtil;
        this.parkingSpotDAO = newParkingSpotDAO;
        this.ticketDAO = newTicketDAO;
    }

    /**
     * Add a new vehicle in DB.
     */
    public void processIncomingVehicle() {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            if (parkingSpot != null && parkingSpot.getId() > 0) {
                String vehicleRegNumber = getVehicleRegNumber();
                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot);

                Date inTime = new Date();
                Ticket ticket = new Ticket();
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0);
                ticket.setInTime(inTime);
                ticket.setOutTime(null);
                ticketDAO.saveTicket(ticket);
                System.out.println("Generated Ticket and saved in DB");
                System.out.println("Please park your vehicle in spot number:"
                        + parkingSpot.getId());
                System.out.println("Recorded in-time for vehicle number:"
                        + vehicleRegNumber + " is:" + inTime);
            }
        } catch (Exception e) {
            LOGGER.error("Unable to process incoming vehicle", e);
        }
    }

    private String getVehicleRegNumber() throws Exception {
        System.out.println(
                "Please type the vehicle "
                        + "registration number and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    /**
     * From DB get next parkingSpot available.
     * @return ParkingSpot parkingSpot
     */
    public ParkingSpot getNextParkingNumberIfAvailable() {
        int parkingNumber;
        ParkingSpot parkingSpot = null;
        try {
            ParkingType parkingType = getVehicleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if (parkingNumber > 0) {
                parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
            } else {
                throw new Exception(
                        "Error fetching parking number from DB. "
                                + "Parking slots might be full");
            }
        } catch (IllegalArgumentException ie) {
            LOGGER.error("Error parsing user input for type of vehicle", ie);
        } catch (Exception e) {
            LOGGER.error("Error fetching next available parking slot", e);
        }
        return parkingSpot;
    }

    private ParkingType getVehicleType() {
        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch (input) {
            case 1:
                return ParkingType.CAR;
            case 2:
                return ParkingType.BIKE;
            default:
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
        }
    }

    /**
     * calculate ticket price.
     * update parking sport availability
     * for exiting vehicle
     */
    public void processExitingVehicle() {
        try {
            String vehicleRegNumber = getVehicleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            Date outTime = new Date();
            ticket.setOutTime(outTime);
            FARE_CALCULATOR_SERVICE.calculateFare(ticket);
            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                System.out.println("Please pay the parking fare:"
                        + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number:"
                        + ticket.getVehicleRegNumber() + " is:" + outTime);
            } else {
                System.out.println("Unable to update ticket information. "
                        + "Error occurred");
            }
        } catch (Exception e) {
            LOGGER.error("Unable to process exiting vehicle", e);
        }
    }
}
