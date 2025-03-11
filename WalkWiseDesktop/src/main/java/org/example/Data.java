package org.example;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

public class Data implements Runnable {

    private final BlockingQueue<String> dataQueue;

    String pureValue;   // global string for storing incoming data

    int[][] matrixData = new int[15][15];   //representation of physical matrix pressure sensor

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
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    // allocation of incoming data
    void fixData(){
        String data = pureValue;

        data = data.substring(0, data.length()-1);
        String[] dataParts = data.split(";");

        int rowIndex = Integer.parseInt(dataParts[0]);
        int columnIndex = Integer.parseInt(dataParts[1]);
        int value = Integer.parseInt(dataParts[2]);

        matrixData[rowIndex][columnIndex] = value;

        //System.out.println(Arrays.deepToString(matrixData)); //test
    }
}