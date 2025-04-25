package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import static java.nio.file.StandardOpenOption.*;

public class Data implements Runnable {

    private final BlockingQueue<String> dataQueue;

    String pureValue;   // global string for storing incoming data

    int[][] matrixData = new int[16][16];   //representation of physical matrix pressure sensor

    public Data(BlockingQueue<String> dataQueue) {
        this.dataQueue = dataQueue;
    }
    @Override
    public void run() {
        try{
            receivePureData();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    boolean przetwarzajDane = true; ////////////////////// DAĆ FLAGe NA KIEDY PRZETWARZAĆ A KIEDY NIE
    // processing incoming data
    void receivePureData(){
        try{
            while(przetwarzajDane == true) {    ////////////////////// DAĆ FLAGe NA KIEDY PRZETWARZAĆ A KIEDY NIE
                pureValue = dataQueue.take();
                //System.out.println("Dane do przetworzenia: " + pureValue); //test
                fixData();
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    // allocation of incoming data
    String[] dataPacket = {"", ""};
    void fixData(){
        String data = dataPacket[1] + pureValue;

        dataPacket = data.split("\r\n", 2); // buffer (in Communication) must be minimum 9 bytes long, if it's shorter than this part will properly truncate the value
        String[] dataParts = dataPacket[0].split(";"); // wysylane jest wiecej pakietow danych niz 1 (dana;dana;dana\n\r) przez to ucienicie
        // wycina tylko ostatni znak z praktycznie okolo 200 pakietow a przy zamianie rozdzielonych wartosci
        // pojawia sie dana razem z \n\r to przy zmianie na inteager wywala blad

        int rowIndex = Integer.parseInt(dataParts[0]);
        int columnIndex = Integer.parseInt(dataParts[1]);
        int value = Integer.parseInt(dataParts[2]); // w Communication ustalić odpowiednio buffer zeby wszystkie dane sie wyslaly
        // w tej klasie zrobic substringa, wycinajacego do \n - trzeba wycinac a nie przesylac z odgornie skróconym buforem
        // bo wtedy bufor moze byc za maly przy wartosciach bardzo wysokich

        matrixData[rowIndex][columnIndex] = value;

        System.out.println(matrixData[rowIndex][columnIndex]); //test

        // !!! saveData() is creating multiple files for one measurement
    }

    // saving data for external/later purpose
    int lastSavedValue = 1;
    long startTime = 0;
    void saveData(){
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        Path file = Path.of("./Measurements" +"/"+currentDate.format(formatter)+".dat"); //new file named as current date and saved in Measurements folder

        try{
            Files.createFile(file); //create file
        } catch (FileAlreadyExistsException e){
            System.err.format("File %s already exists.%n", file);   ///////// Tu trzeba zmienić aby nie próbować tworzyć za każdym razem jak próbujemy zapisać dane
        } catch (IOException e){
            System.err.format("Error while creating file %s.%n", file);
        }

        //Check if there's sth new to save
        if(matrixData[15][15] != lastSavedValue){
            long stopTime = System.nanoTime();
            long time = startTime - stopTime;
            startTime = System.nanoTime(); // for saving approximated sampling times

            try (DataOutputStream out = new DataOutputStream(
                    Files.newOutputStream(file, WRITE, APPEND))) {
                out.writeLong(time);    // writing time do file
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        out.writeInt(matrixData[i][j]); // writing measured values to file
                    }
                }
            } catch (IOException e) {
                System.err.println("Error while writing to file");
            }
            lastSavedValue = matrixData[15][15];
        }
    }

    // loading previous measurements
    ArrayList<Long> samplingTimes = new ArrayList<>();
    ArrayList<int[][]> sampledData = new ArrayList<>();
    //public long samplingTime;
    void loadData(String fileName){
        try(DataInputStream in = new DataInputStream(new FileInputStream("./Measurements/"+fileName))){
            while (in.available() > 0) {
                samplingTimes.add(in.readLong());
                int[][] matrix = new int[16][16];
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        matrix[i][j] = in.readInt();
                    }
                }
                sampledData.add(matrix);
//                System.out.println(samplingTimes); //test
//                for(int i = 0; i < samplingTimes.size(); i++){
//                    System.out.println(Arrays.deepToString(sampledData.get(i))); //test
//                }
            }

        } catch (IOException e){
            System.err.format("Error while reading file");
        }
    }

    // for sharing data to visualize it
    public int[][] getMatrixData() {
        return matrixData;
    }
}