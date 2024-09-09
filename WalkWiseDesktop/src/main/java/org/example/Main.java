package org.example;

import javax.bluetooth.BluetoothStateException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        Communication test = new Communication();
        test.findDevice();

        int AllFoundBtDevices = Communication.availableDevices.size();

        // Finding if on the list of all bt devices is the one called as the value of searchingName
        String btName;    // String for comparing bt devices
        String searchingName = "zejcik"; // bt device name that we want to find and connect to
        for(int i=0; i<AllFoundBtDevices; i++){ // check if there's
            btName = Communication.availableDevices.get(i).getFriendlyName(false);
            if(btName.equals(searchingName)){
                System.out.println("Found device \"zejcik\"!");
                //TO DO: Connect to the device
                //  Communication.connectToDevice
            }
        }

    }

}