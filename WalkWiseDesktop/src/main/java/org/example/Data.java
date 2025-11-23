package org.example;

import java.io.*;
import java.nio.file.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import static java.nio.file.StandardOpenOption.*;

public class Data implements Runnable {

    private final BlockingQueue<String> dataQueue;

    int[][] calibrationMatrix = new int[16][16];

    //Power law regression parameters for each column to calculate weight
    double[] c = {0.0956, 0.0881, 0.0786, 0.0857, 0.108, 0.1, 0.0882, 0.108, 0.111, 0.0964, 0.118, 0.108, 0.115, 0.0907, 0.0964, 0.0603};
    double[] K = {0.0706, 0.106, 0.152, 0.137, 0.107, 0.139, 0.21, 0.139, 0.12, 0.165, 0.126, 0.153, 0.137, 0.13, 0.111, 0.437};
    double c1 = 3.05;
    double K1 = 0.0177;

    String pureValue;   // global string for storing incoming data

    int[][] matrixData = new int[16][16];   //representation of physical matrix pressure sensor
    int[][] weightData = new int[16][16];

    public Data(BlockingQueue<String> dataQueue) {
        this.dataQueue = dataQueue;
    }
    @Override
    public void run() {
        try{
            getCalibrationMatrix();
            receivePureData(true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //boolean processData = true; ////////////////////// DAĆ FLAGe NA KIEDY PRZETWARZAĆ A KIEDY NIE
    // processing incoming data
    void receivePureData(boolean processData){
        try{
            while(processData == true) {
                pureValue = dataQueue.take();
                //System.out.println("Dane do przetworzenia: " + pureValue); //test
                fixData();
                saveData(GUI.currentTime);
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    void receivePureData(){
        try{
                pureValue = dataQueue.take();
                fixData();
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

        if(matrixData[rowIndex][columnIndex] != value){
            System.out.println(rowIndex + " " + columnIndex + " " + value);
        }

        matrixData[rowIndex][columnIndex] = value;// - calibrationMatrix[rowIndex][columnIndex];

        //System.out.println(matrixData[rowIndex][columnIndex]); //test
        //System.out.println(rowIndex + " " + columnIndex + " " + value);

        //calculateWeight(matrixData, c, K);
        calculateWeight(matrixData, c1, K1);
    }

    //Calculate weight values form ADC 32param
    void calculateWeight(int[][] adcValues, double[] c, double[] K){
        for(int i = 0; i < 16; i++){    //column - for each column different calibrated parameters
            for(int j = 0; j < 16; j++){    //row
                double V = adcValues[j][i]*3.3/Math.pow(2,12);
                weightData[j][i] = (int) Math.round(Math.pow(V/c[i], 1.0/K[i]));
            }
        }
    }

    //Calculate weight values form ADC 2param
    void calculateWeight(int[][] adcValues, double c, double K){
        for(int i = 0; i < 16; i++){    //column - for each column different calibrated parameters
            for(int j = 0; j < 16; j++){    //row
                double V = adcValues[j][i]*3.3/Math.pow(2,12);
                weightData[j][i] = (int) Math.round(Math.pow(V/c, 1.0/K));
            }
        }
    }

    // saving data for external/later purpose
    long startTime = 0;
    void saveData(LocalDateTime currentTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        Path file = Path.of("./Measurements" +"/"+currentTime.format(formatter)+".dat"); //new file named as current date and saved in Measurements folder

        if(Files.exists(file)){ //Check if the file exist
            long stopTime = System.nanoTime();
            long time = startTime - stopTime;   // approximated sampling time
            startTime = System.nanoTime();

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
        } else {
            try{
                Files.createFile(file); //create file
            } catch (FileAlreadyExistsException e){
                System.err.format("File %s already exists.%n", file);
            } catch (IOException e){
                System.err.format("Error while creating file %s.%n", file);
            }
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

    // change .dat file to .csv
    void exportCSV(String fileName) throws FileNotFoundException {
        loadData(fileName);
        try (PrintWriter csvWriter = new PrintWriter("./Measurements/"+ fileName.replace(".dat", ".csv"))) {
            for(int i = 0; i < samplingTimes.size(); i++){
                csvWriter.print(samplingTimes.get(i));  // printing time values
                csvWriter.print(Arrays.deepToString(sampledData.get(i))); // printing all matrix values
                csvWriter.println();    // data separator
            }
            csvWriter.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // calibration
    int max = 0;
    public void getCalibrationMatrix(){
        int divider = 1;
        LocalTime start = LocalTime.now();
        while(Duration.between(start, LocalDateTime.now()).toMinutes() < 1){
            for(int counter = 0; counter < 256; counter++){
                receivePureData();
            }

            for(int i = 0; i < 16; i++){
                for(int j = 0; j < 16; j++){
                    if(matrixData[i][j] > max){
                        max = matrixData[i][j];
                    }
                    if(matrixData[i][j] != 0){
                        calibrationMatrix[i][j] = (matrixData[i][j]+calibrationMatrix[i][j])/divider;
                    }
                }
            }

            divider++;

//            System.out.println(divider);
//            System.out.println(Arrays.deepToString(calibrationMatrix));
//            System.out.println(Arrays.deepToString(matrixData));
        }
        System.out.println("KONIEC KALIBRACJI");
    }

    // for sharing data to visualize it
    public int[][] getMatrixData() {
        return matrixData;
    }
}