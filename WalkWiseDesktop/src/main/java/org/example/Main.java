package org.example;

import javax.sound.midi.Receiver;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
/*
        BlockingQueue<String> testQueue = new LinkedBlockingQueue<>();

        Communication test = new Communication(testQueue);
        Data testPrzesylDaty = new Data(testQueue);

        Thread receiverThread = new Thread(test);

        Thread dataThread = new Thread(testPrzesylDaty);

        Communication.findAllDevice();

        Communication.connectToDevice(Communication.findSpecificDevice("WalkWise"));

        testPrzesylDaty.saveData();

        receiverThread.start();
        dataThread.start();

        System.out.println("To już po:");
*/

        //TEST

        //TEST END

        //TO DO: Test if bluetooth is on!!!

//        test.findAllDevice();
//
//        test.findSpecificDevice("WalkWise");
//        // TO DO: connect to this device
//        test.connectToDevice(Communication.findSpecificDevice("WalkWise"));

        //test.connectToDevice(Communication.findSpecificDevice("WalkWise"));

       // while(true){
         //   System.out.println("tu");
        //} // zostaje połączony tel  - fajnie
        //To DO: wysyłanie/odbieranie daych

        //TO DO: Przycisk do włączenia i wyłączenia bluetooth
            // odpalenie funkcji z klasy Communication od razu wyrzuci błąd przy wyłączonym bluetoot
                // dlatego dodanie opcji sprawdzenia przed wykonaniem funkcji czy bluetooth on
                    // jak nie to komunikat o włączeniu lub jakieś automatyczne włączenie

//        Visualization visualization = new Visualization();
//
//        visualization.displayRTHeatmap(testPrzesylDaty);

    }
}