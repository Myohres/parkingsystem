package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    }

    //---------processExitingVehicle-------------

    @Test
    public void processExitingVehicleTest() throws Exception {
        // Given : Un ticket est généré

        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        // When : faire sortir un vehicule
        parkingService.processExitingVehicle();

        // Then : Si appel updateParking 1 fois alors test ok
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void ElseUpdateProcessExitingVehicleTest() throws Exception {
        // Given : Un ticket est généré et updateParking n'est pas mocké

        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        // When : faire sortir un vehicule
        parkingService.processExitingVehicle();

        // Then : Si appel updateParking 0 fois alors test ok
        verify(parkingSpotDAO, Mockito.times(0)).updateParking(any(ParkingSpot.class));
    }

    //---------processIncomingVehicle-------------

    @Test
    public void processIncomingVehicleTest(){
        // Given : On rentrera une voiture sur la place 1
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

        // When : Faire entrer un véhicule
        parkingService.processIncomingVehicle();

        // Then : si appel insertion ticket ds la BD
        verify(ticketDAO,Mockito.times(1)).saveTicket(any(Ticket.class));

    }


    @Test
    public void ErrorProcessIncomingVehicleTes() throws Exception {
        // Given : On rentrera une voiture sur la place 1
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenThrow(new Exception());

        // When : Faire entrer un véhicule
        parkingService.processIncomingVehicle();

        // Then : si 0 appel insertion ticket ds la BD alors test ok
        verify(ticketDAO,Mockito.times(0)).saveTicket(any(Ticket.class));
    }

    //------------GetNextParkingNumberIfAvailableTest----------------

    @Test
    public void GetNextParkingNumberIfAvailableTest(){
        // Given : Un BIKE est choisi pour la prochaine place libre, la 4
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(4);

        // When : Obtenir le parkingSpot de la prochaine place libre
        ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();

        // Then : Si parkingSpot voulu est le numéro 4, pour velo, libre, alors test ok
        assertEquals(4,parkingSpot.getId()) ;
        assertEquals(ParkingType.BIKE,parkingSpot.getParkingType());
        assertTrue(parkingSpot.isAvailable());
    }


    @Test
    public void ElseGetNextParkingNumberIfAvailableTest(){
        // Given : Un BIKE est choisi pour la prochaine place libre, la 0
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(0);

        // When : Obtenir le parkingSpot de la prochaine place libre
        ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();

        // Then : Si new Exception et parkingSpot null alors test ok
        assertNull(parkingSpot);


    }


    @Test
    public void ErrorInputGetNextParkingNumberIfAvailableTest(){
        // Given : Aucun type de vehicule correct n'est choisi
        when(inputReaderUtil.readSelection()).thenReturn(0);

        // When : Obtenir le parkingSpot de la prochaine place libre
        ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();

        // Then : Si logger.error sur le type de vehicule et parkingSpot null alors test ok
        assertNull(parkingSpot);
        verify(parkingSpotDAO,Mockito.times(0)).getNextAvailableSlot(any(ParkingType.class));


    }

    //---------------getVehichleType-------------------

    @Test
    public void getVehichleTypeCarTest(){
        //Given : Une voiture occupe la place 1
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

        // When : obtenir le type de vehicule de la prochaine place libre
        ParkingSpot parkingSpotTest = parkingService.getNextParkingNumberIfAvailable();
        ParkingType parkingTypeTest = parkingSpotTest.getParkingType() ;

        // Then : si le type de vehicule obetnu vaut CAR alors test ok
        assertEquals(ParkingType.CAR ,parkingTypeTest );
    }


    @Test
    public void getVehichleTypeBikeTest(){
        // Given : Un vélo occue la place 4
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(4);

        // When : obtenir le type de vehicule de la prochaine place libre
        ParkingSpot parkingSpotTest = parkingService.getNextParkingNumberIfAvailable();
        ParkingType parkingTypeTest = parkingSpotTest.getParkingType() ;

        // Then : si le type de vehicule obetnu vaut BIKE alors test ok
        assertEquals(ParkingType.BIKE ,parkingTypeTest );
    }


    @Test
    public void getVehichleDefautTest(){
        //Given : Choix du type de vehicule, ni CAR ni BIKE
        when(inputReaderUtil.readSelection()).thenReturn(0);

        // When : Chercher le type de véhicule
        ParkingSpot parkingSpotTest = parkingService.getNextParkingNumberIfAvailable();

        // si choix défaut alors test ok
        assertNull(parkingSpotTest);
    }

}

