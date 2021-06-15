package com.parkit.parkingsystem.service;


import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;


public class FareCalculatorService {

    private TicketDAO ticketDAO;

    public FareCalculatorService(){
        ticketDAO = new TicketDAO();
    }

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime());
        }

        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();
        double duration = (outHour - inHour) / 1000 / 60 / 60;
        double reduction = 1;

        if (duration < 0.5) {
            reduction = Fare.VEHICLE_RATE_FOR_LESS_HALF_HOUR;
        }
        if (ticketDAO.IsRecurrentUser(ticket.getVehicleRegNumber())){
            reduction = Fare.RECURRENT_USER_RATE;
        }

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                ticket.setPrice((duration * Fare.CAR_RATE_PER_HOUR) * reduction );
                break;
            }
            case BIKE: {
                ticket.setPrice((duration * Fare.BIKE_RATE_PER_HOUR) * reduction );
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}
