package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        Communication test = new Communication();

        //TO DO: Test if bluetooth is on!!!

        test.findAllDevice();

        test.findSpecificDevice("zejcik");
        // TO DO: connect to this device
        test.connectToDevice(Communication.findSpecificDevice("zejcik"));

        //test.connectToDevice(Communication.findSpecificDevice("WalkWise"));

       // while(true){
         //   System.out.println("tu");
        //} // zostaje połączony tel  - fajnie
        //To DO: wysyłanie/odbieranie daych

        //TO DO: Przycisk do włączenia i wyłączenia bluetooth
            // odpalenie funkcji z klasy Communication od razu wyrzuci błąd przy wyłączonym bluetoot
                // dlatego dodanie opcji sprawdzenia przed wykonaniem funkcji czy bluetooth on
                    // jak nie to komunikat o włączeniu lub jakieś automatyczne włączenie

    }
}