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

        /*
          (duration*taux) *reduction
        */


        if (duration < 0.5) {
            ticket.setPrice(Fare.VEHICLE_RATE_FOR_LESS_HALF_HOUR);
        } else if (ticketDAO.IsRecurrentUser(ticket.getVehicleRegNumber())) {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice((duration * Fare.CAR_RATE_PER_HOUR) * Fare.RECURRENT_USER_RATE);
                    break;
                }
                case BIKE: {
                    ticket.setPrice((duration * Fare.BIKE_RATE_PER_HOUR) * Fare.RECURRENT_USER_RATE);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        } else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        }
    }


}
