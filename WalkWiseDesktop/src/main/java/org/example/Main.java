package org.example;

import javax.bluetooth.BluetoothStateException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        Communication test = new Communication();
        test.findDevice();
    }

}