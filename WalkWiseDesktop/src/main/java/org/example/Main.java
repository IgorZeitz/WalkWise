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

    public volatile boolean startTransmission = false;
    public volatile boolean startVisualization = false;

    public static void main(String[] args) throws IOException, InterruptedException {

//        BlockingQueue<String> testQueue = new LinkedBlockingQueue<>();
////
//        Communication test = new Communication(testQueue);
//        Data testPrzesylDaty = new Data(testQueue);
//
//        Thread receiverThread = new Thread(test);
//
//        Thread dataThread = new Thread(testPrzesylDaty);
//
//        Communication.findAllDevice();
//
//        Communication.connectToDevice(Communication.findSpecificDevice("WalkWise"));
//
//        testPrzesylDaty.saveData();
//
//        receiverThread.start();
//        dataThread.start();
//
//        System.out.println("To już po:");

        //  MAIN CODE


//        while(true){
//            if(startTransmission);
//        }
        //

        //GUI TEST
        GUI okno = new GUI();
        GUI.launch(GUI.class, args);
        //

        //TEST
//        testPrzesylDaty.saveData();
//        testQueue.put("0;0;100\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testQueue.put("15;15;1\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testPrzesylDaty.saveData();
//        testQueue.put("0;1;100\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testQueue.put("15;15;2\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testPrzesylDaty.saveData();
//        testQueue.put("0;2;100\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testQueue.put("15;15;3\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testPrzesylDaty.saveData();
//        testQueue.put("0;3;100\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testQueue.put("15;15;4\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testPrzesylDaty.saveData();
//        testQueue.put("0;4;100\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testQueue.put("15;15;5\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testPrzesylDaty.saveData();
//        testQueue.put("0;5;100\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testQueue.put("15;15;6\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testPrzesylDaty.saveData();
//        testQueue.put("0;6;100\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testQueue.put("15;15;7\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testPrzesylDaty.saveData();
//        testQueue.put("0;7;100\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testQueue.put("15;15;8\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testPrzesylDaty.saveData();
//        testQueue.put("0;8;100\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testQueue.put("15;15;9\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testPrzesylDaty.saveData();
//        testQueue.put("0;9;100\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testQueue.put("15;15;10\r\n");
//        testPrzesylDaty.receivePureData();
//        testPrzesylDaty.przetwarzajDane = true;
//        testPrzesylDaty.saveData();
        //TEST END

        //TO DO: Test if bluetooth is on!!!

//        test.findAllDevice();
//
//        test.findSpecificDevice("WalkWise");
//        // TO DO: connect to this device
//        test.connectToDevice(Communication.findSpecificDevice("WalkWise"));
//
//        test.connectToDevice(Communication.findSpecificDevice("WalkWise"));

       // while(true){
         //   System.out.println("tu");
        //} // zostaje połączony tel  - fajnie
        //To DO: wysyłanie/odbieranie daych

        //TO DO: Przycisk do włączenia i wyłączenia bluetooth
            // odpalenie funkcji z klasy Communication od razu wyrzuci błąd przy wyłączonym bluetoot
                // dlatego dodanie opcji sprawdzenia przed wykonaniem funkcji czy bluetooth on
                    // jak nie to komunikat o włączeniu lub jakieś automatyczne włączenie

        //Visualization visualization = new Visualization();

        //System.out.println(testPrzesylDaty.sampledData);

        //visualization.displayHeatmapExternalDat(testPrzesylDaty, "12-04-2025_12-22-51.dat");



    }
}