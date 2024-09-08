package org.example;

import javax.bluetooth.*;
import java.io.IOException;
import java.util.Vector;

public class Communication {

    public static Vector<RemoteDevice> availableDevices = new Vector<>(); //Vektor wszystkich dostępnych urządzeń bt

    int[][] receivedData;

    public void receiveData(){

    }

    public static void findDevice() throws IOException, InterruptedException {
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
                    System.out.println("Znaleziono urzadzenie: " + remoteDevice);
                    availableDevices.add(remoteDevice);
                } catch (Exception e){
                    e.printStackTrace();
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
                    System.out.println("Zakończyło się szukanie!!!!!");
                }
            }
        };

        System.out.println("Start szukania:\n");
        discoveryAgent.startInquiry(DiscoveryAgent.GIAC, listener);
        System.out.println(availableDevices);

        //TO BLOKUJE PRORAM a bez tego nie wyszka urzadzeń!!!
        //  Jak wyjsc z wait chyba ze inquiryCompleted ma zsynchronizowac wyjscie z listener.wait?????
        synchronized (inquiryLock){
            try {
                inquiryLock.wait();
            } catch (InterruptedException e){
                System.out.println(availableDevices);
            }
        }

        System.out.println(availableDevices);

       System.out.println(availableDevices.get(1).getFriendlyName(false));

    }

    public static void connectToDevice(){

    }

}
