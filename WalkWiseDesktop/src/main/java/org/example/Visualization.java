package org.example;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.*;

import org.knowm.xchart.*;

public class Visualization {

    String dataName;

    int[][] data1;//= matrixData;
    int[][] data2;

    int[] xData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    int[] yData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    int heatMapMaxScale = 1500 ;

    //public Visualization(BlockingQueue<String> dataQueue) {
    //    super(dataQueue);
    //}

    BlockingQueue<String> testQueue = new LinkedBlockingQueue<>();

    void displayRTHeatmap(Data data) throws InterruptedException {
        JFrame frame = new JFrame("Measurement");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // Display the window.
        // frame.setVisible(true);
        frame.setSize(500, 500);
        //

        //Data matrixData = new Data(testQueue);

        HeatMapChart mainChart = new HeatMapChartBuilder().width(800).height(600).title("HeatMap").build();
        mainChart.setTitle("DATA");
        data1 = data.matrixData;//matrixData.getMatrixData();

        data2 = data.weightData;

        //mainChart.addSeries("Pomiar", xData, yData, data1);

        HeatMapSeries heatmapScale = mainChart.addSeries("Pomiar", xData, yData, data1);
        heatmapScale.setMin(0.0);
        //heatmapScale.setMax(heatMapMaxScale);


        XChartPanel<HeatMapChart> heatmapPanel = new XChartPanel<>(mainChart);

        //heatmapPanel.setVisible(true);
        heatmapPanel.setSize(400, 400);
        //
        //frame.add(mainChart);
        SwingWrapper<HeatMapChart> sw = new SwingWrapper<HeatMapChart>(mainChart);
        sw.displayChart();
        frame.add(heatmapPanel);

        //int cnt = 0;
        while (true) {

            Thread.sleep(100);  // TU CZESC PROBLEMU PREDKOSCI WYSWIETLANIA?????

            data1 = data.matrixData;//matrixData.getMatrixData();
            mainChart.updateSeries("Pomiar", xData, yData, data1);
            mainChart.getSeriesMap().get("Pomiar").setMin(0.0);
            //mainChart.getSeriesMap().get("Pomiar").setMax(heatMapMaxScale);
            sw.repaintChart();

            //array = array+i;

        }
    }

    //Displaying heatmap from external .dat files
    void displayHeatmapExternalDat(Data data, String fileName) throws InterruptedException {
        JFrame frame = new JFrame("Saved Measurement");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Display the window.
        // frame.setVisible(true);
        frame.setSize(500, 500);
        //

        //Data matrixData = new Data(testQueue);

        HeatMapChart mainChart = new HeatMapChartBuilder().width(800).height(600).title("HeatMap").build();
        mainChart.setTitle("DATA");
        data1 = data.matrixData;//matrixData.getMatrixData();
        //mainChart.addSeries("Pomiar", xData, yData, data1);

        XChartPanel<HeatMapChart> heatmapPanel = new XChartPanel<>(mainChart);

        HeatMapSeries heatmapScale = mainChart.addSeries("Pomiar", xData, yData, data2);
        heatmapScale.setMin(0);
        //heatmapScale.setMax(heatMapMaxScale);

        //heatmapPanel.setVisible(true);
        heatmapPanel.setSize(400, 400);
        //
        //frame.add(mainChart);
        SwingWrapper<HeatMapChart> sw = new SwingWrapper<HeatMapChart>(mainChart);
        sw.displayChart();
        frame.add(heatmapPanel);

        //int cnt = 0;
        data.loadData(fileName);
        //while (true) {  // DO DODANIA FLAGA na on/off

                int i;
                for (i = 0; i < data.samplingTimes.size(); i++){
                    //wait(Math.abs(data.samplingTime));
                    if(i > 0){
                        Thread.sleep(Math.abs(data.samplingTimes.get(i))/1000000);
                    }
                    data1 = data.sampledData.get(i);
                    mainChart.updateSeries("Pomiar", xData, yData, data1);
                    mainChart.getSeriesMap().get("Pomiar").setMin(0.0);
                    //mainChart.getSeriesMap().get("Pomiar").setMax(heatMapMaxScale);
                    sw.repaintChart();
                }
        //};
    }

}