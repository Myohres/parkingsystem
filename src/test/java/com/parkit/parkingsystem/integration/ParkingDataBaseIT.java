package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBaseConfig dataBaseConfig;
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        /*dataBaseConfig = parkingSpotDAO.getDataBaseConfig();
        dataBaseConfig = dataBaseTestConfig ;*/
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        Ticket ticketTestParkingACar = ticketDAO.getTicket("ABCDEF");
        int parkingSpotTest = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertNotNull(ticketTestParkingACar);
        assertEquals(2,parkingSpotTest);
    }

    @Test
    public void testParkingLotExit(){

        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticket.setInTime(new Date(System.currentTimeMillis()-50000000));
        ticket.setOutTime(null);
        ticket.setPrice(0);
        ticket.setVehicleRegNumber("ABCDEF");
        ticketDAO.saveTicket(ticket);
        parkingSpotDAO.updateParking(ticket.getParkingSpot());
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        parkingService.processExitingVehicle();

        ticket = ticketDAO.getTicket("ABCDEF");
        assertEquals(new Date().getTime(), ticket.getOutTime().getTime(), 5000);
        assertNotEquals(0,ticket.getPrice());
        assertEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));

    }

}
