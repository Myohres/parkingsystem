package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        float inHour =  (ticket.getInTime().getHours() * 60) + (ticket.getInTime().getMinutes()) ;
        float outHour = (ticket.getOutTime().getHours() * 60) + (ticket.getOutTime().getMinutes());

        //TODO: change deprecated
        float durationDay= ( (ticket.getOutTime().getDay()) - (ticket.getInTime().getDay()) )*24;
        float duration = durationDay + (outHour - inHour)/60;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}