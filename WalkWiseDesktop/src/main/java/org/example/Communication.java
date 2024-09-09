package org.example;

import javax.bluetooth.*;
import java.io.IOException;
import java.util.Vector;

public class Communication {

    public static Vector<RemoteDevice> availableDevices = new Vector<>(); // All found BT devices storage

    int[][] receivedData;

    public void receiveData(){

    }

    // Finding all available bt devices
    public static void findAllDevice() throws IOException, InterruptedException {
        //Host device info
        LocalDevice myDevice = LocalDevice.getLocalDevice();
        String myDeviceAddress = myDevice.getBluetoothAddress();
        String myDeviceName = myDevice.getFriendlyName();
        //boolean myDeviceVisibility = myDevice

        // Działa
        //System.out.println("Komp adres: " + myDevice.getBluetoothAddress());
        //System.out.println("Komp nazwa: " + myDevice.getFriendlyName());
        //System.out.println("Komp wykrywalne? " + myDevice.getDiscoverable());

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
        discoveryAgent.startInquiry(DiscoveryAgent.GIAC, listener);
        System.out.println(availableDevices);

        //New object inquiryLock helped with synchronizing .wait(), Don't know if this is a good solution
        synchronized (inquiryLock){
            try {
                inquiryLock.wait();
            } catch (InterruptedException e){
                //System.out.println(availableDevices);
                System.out.println("Tu cos nie dziala");
            }
        }

       // System.out.println(availableDevices);

      // System.out.println(availableDevices.get(1).getFriendlyName(false));

    }

    // Finding if the device that we want to connect to, is available
    public static void findSpecificDevice(String searchingDeviceName) throws IOException {

        String btName;    // String for comparing bt devices
        int AllFoundBtDevices = Communication.availableDevices.size();

        for(int i=0; i<AllFoundBtDevices; i++){ // check if there's
            btName = Communication.availableDevices.get(i).getFriendlyName(false);
            if(btName.equals(searchingDeviceName)){
                System.out.println("Found device: " + btName);
                //TO DO: Connect to the device
                //  Communication.connectToDevice
            }
        }
    }

/*    public static void connectToDevice(RemoteDevice remoteDevice){
        try {
            // UUID dla usługi Serial Port Profile (SPP)
            UUID uuid = new UUID(0x1101);  // Standard RFCOMM
            String connectionURL = "btspp://" + remoteDevice.getBluetoothAddress() + ":" + uuid.toString() + ";authenticate=false;encrypt=false;";

            // Nawiązanie połączenia
            StreamConnection streamConnection = (StreamConnection) Connector.open(connectionURL);

            // Pobierz strumienie wejścia/wyjścia
            InputStream inputStream = streamConnection.openInputStream();
            OutputStream outputStream = streamConnection.openOutputStream();

            System.out.println("Połączono z urządzeniem: " + remoteDevice.getFriendlyName(false));

            // Możesz teraz komunikować się z urządzeniem za pomocą strumieni
            // Przykład wysyłania danych
            outputStream.write("Hello from Java Bluetooth!".getBytes());

            // Zamknij połączenie po zakończeniu
            inputStream.close();
            outputStream.close();
            streamConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 */
}
