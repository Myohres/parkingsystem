package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class InputReaderUtil {
    /**
     * initialise Scanner.
     */
    private static final  Scanner SCAN = new Scanner(System.in, "UTF-8");
    /**
     * initialise Logger "inputReaderUtil".
     */
    private static final Logger LOGGER =
            LogManager.getLogger("InputReaderUtil");

    /**
     * Collect user input with asked information by app.
     * @return user input
     */
    public int readSelection() {
        try {
            int input = Integer.parseInt(SCAN.nextLine());
            return input;
        } catch (Exception e) {
            LOGGER.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. "
                    + "Please enter valid number for proceeding further");
            return -1;
        }
    }

    /**
     * Collect user input vehicle number asked  by app.
     * @return vehicleRegNumber
     * @throws Exception
     */
    public String readVehicleRegistrationNumber() throws Exception {
        try {
            String vehicleRegNumber = SCAN.nextLine();
            if (vehicleRegNumber == null
                    || vehicleRegNumber.trim().length() == 0) {
                throw new IllegalArgumentException("Invalid input provided");
            }
            return vehicleRegNumber;
        } catch (Exception e) {
            LOGGER.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. "
                    + "Please enter a valid string "
                    + "for vehicle registration number");
            throw e;
        }
    }


}
