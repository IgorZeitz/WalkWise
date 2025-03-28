package org.example;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

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
    }

    // saving data for external purpose
    void saveData(){
        // TO DO
    }

    // loading previous measurements
    void readData(){
        // TO DO
    }

    // for sharing data to visualize it
    public int[][] getMatrixData() {
        return matrixData;
    }
}