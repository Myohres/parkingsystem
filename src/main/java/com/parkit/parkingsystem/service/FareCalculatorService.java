package com.parkit.parkingsystem.service;


import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

/** FareCalculatorService.
 *  Calculate Ticket Price
 */

public class FareCalculatorService {
    /**
     * Create a TicketDAO.
     */
    private TicketDAO ticketDAO;

    /**
     * Initialise a TicketDAO.
     */
    public FareCalculatorService() {
        ticketDAO = new TicketDAO();
    }

    /** calculateFare.
     * check duration vehicle in ParkingSpot
     * check vehicle is coming before (Recurrent User)
     * check vehicle type
     * calculate ticketPrice with duration, Rate and reduction
     * send TicketPrice to ticket
     *
     * @param ticket Vehicle Ticket
     */
    public void calculateFare(final Ticket ticket) {
        if ((ticket.getOutTime() == null)
                || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException(
                    "Out time provided is incorrect:" + ticket.getOutTime());
        }
        final int convertTime = 1000 * 60 * 60;
        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();
        double duration = (outHour - inHour) / convertTime;
        double reduction = 1;

        if (duration < Fare.FREE_DURATION) {
            reduction = Fare.VEHICLE_RATE_FOR_LESS_HALF_HOUR;
        } else if (ticketDAO.isRecurrentUser(ticket.getVehicleRegNumber())) {
            reduction = Fare.RECURRENT_USER_RATE;
        }

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR:
                ticket.setPrice(
                        (duration * Fare.CAR_RATE_PER_HOUR) * reduction);
                break;
            case BIKE:
                ticket.setPrice(
                        (duration * Fare.BIKE_RATE_PER_HOUR) * reduction);
                break;
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}
