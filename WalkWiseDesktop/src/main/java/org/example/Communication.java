package org.example;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

public class Communication implements Runnable {

    public static Vector<RemoteDevice> availableDevices = new Vector<>(); // All found BT devices storage

    int[][] receivedData;

    public static StreamConnection streamConnection;

    private final BlockingQueue<String> dataQueue;

    public Communication(BlockingQueue<String> dataQueue) {
        this.dataQueue = dataQueue;
    }

//    public void receiveData(){}

    @Override
    public void run() {
        try{
            receiveData();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Finding all available bt devices
    public static void findAllDevice() throws IOException, InterruptedException {   // BUG!!! It searches only paired devices not those that are really available at the moment!!!
        //Host device info
        LocalDevice myDevice = LocalDevice.getLocalDevice();
        String myDeviceAddress = myDevice.getBluetoothAddress();
        String myDeviceName = myDevice.getFriendlyName();
        //boolean myDeviceVisibility = myDevice

        DiscoveryAgent discoveryAgent = myDevice.getDiscoveryAgent();

        Object inquiryLock = new Object();

        DiscoveryListener listener = new DiscoveryListener() {
            @Override
            public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
                try {
                    System.out.println("Found device: " + remoteDevice);
                    availableDevices.add(remoteDevice);
                } catch (Exception e){
                    System.out.println("No more available devices");
                }

            }

            @Override
            public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {

            }

            @Override
            public void serviceSearchCompleted(int i, int i1) {

            }

            @Override
            public void inquiryCompleted(int i) {
                synchronized (inquiryLock){
                    inquiryLock.notify();
                    System.out.println("Searching ended!");
                }
            }
        };

        System.out.println("Searching started:\n");
        synchronized (inquiryLock) {
            boolean inquiryStarted = discoveryAgent.startInquiry(DiscoveryAgent.GIAC, listener);  // Uruchomienie skanowania
            if (inquiryStarted) {
                inquiryLock.wait();  // Czekaj, aż wyszukiwanie się zakończy
            } else {
                System.out.println("Failed to start device inquiry.");
            }
        }
        System.out.println(availableDevices);

        //New object inquiryLock helped with synchronizing .wait(), Don't know if this is a good solution
  /*      synchronized (inquiryLock){
            try {
                inquiryLock.wait();
            } catch (InterruptedException e){
                //System.out.println(availableDevices);
                System.out.println("Tu cos nie dziala");
            }
      } */

       // System.out.println(availableDevices);

      // System.out.println(availableDevices.get(1).getFriendlyName(false));

    }

    // Finding if the device that we want to connect to, is available
    public static RemoteDevice findSpecificDevice(String searchingDeviceName) throws IOException {

        String btName;    // String for comparing found bt devices names with one specific name
        RemoteDevice foundDevice = null;

        int allFoundBtDevices = Communication.availableDevices.size();

        for(int i=0; i<allFoundBtDevices; i++){ // loop that seeks specific name through all available devices
            btName = availableDevices.get(i).getFriendlyName(false);
            if(btName.equals(searchingDeviceName)){
                System.out.println("Found device: " + btName);
                foundDevice = availableDevices.get(i);
                return foundDevice;
            }
        }
        return foundDevice;
    }

    //  Connecting to the device
    public static void connectToDevice(RemoteDevice deviceToConnect){

        try {
            // UUID for bluetooth SPP (Serial Port Profile)
            UUID uuid = new UUID(0x1101);  // Standard RFCOMM value
            String connectionURL = "btspp://" + deviceToConnect.getBluetoothAddress() + ":1"  + ";authenticate=false;encrypt=false;master=false;"; // Full connectionURL of the device that we want to connect to

            streamConnection = (StreamConnection) Connector.open(connectionURL); // establish connection
            /*
            Tu pojawia się błąd! Wchodzi po tym do catch'a ale catch wywoływany chyba dlatego że uzyskano połączenie bo urzadzenie się łączy (testowane na słuchawkach)
            śmiesznie bo z telefonem się łączy normalnie bez błędu więc chyba kwiestia że słuchawki nie mogą po SSP - błędy takie same gdy słuchawki schowane w etui więc xd
            EDIT 11.03: chyba aktualnie z ESP nie ma tu żadnych problemów
             */

            //OutputStream outputStream = streamConnection.openOutputStream();
            //outputStream.write("Hello from Java Bluetooth!".getBytes()); // No need for writing to a device
            //outputStream.close();

            //System.out.println("Połączono z urządzeniem: " + deviceToConnect.getFriendlyName(false)); //test

            // Zamknij połączenie po zakończeniu
            //inputStream.close();
            //streamConnection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean odbierajDane = true;    ////////////////////// DAĆ FLAGe NA KIEDY ODBIERAĆ A KIEDY NIE
    // receiving data from the connected device
    public void receiveData(){
        try (InputStream inputStream = streamConnection.openInputStream()) {

            StringBuilder receivedData = new StringBuilder();
            byte[] buffer = new byte[1023]; // buffer for input data
            int bytesRead;

            while(odbierajDane == true) {   ////////////////////// DAĆ FLAGe NA KIEDY ODBIERAĆ A KIEDY NIE
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    String receivedPart = new String(buffer, 0, bytesRead);

                    receivedData.append(receivedPart); // append one whole data portion

                    if (receivedData.toString().contains("\n")) {   // one portion of data ends with "\n" - exiting append if "\n" encountered
                        break;
                    }
                }

                //System.out.println(receivedData); //test
                dataQueue.put(receivedData.toString()); //Queuing data for mutex
                receivedData.delete(0, receivedData.length()); // Clearing received data for next data
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}