package org.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.*;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.HeatMapChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;

public class Visualization {

    String dataName;

    int[][] array = {
            {0, 0, 0, 0, 0, 0, 193, 77, 45, 167, 184, 26, 134, 113, 192, 78},
            {0, 0, 0, 0, 0, 0, 163, 97, 33, 150, 124, 60, 144, 19, 108, 172},
            {0, 0, 0, 0, 0, 0, 111, 71, 99, 122, 38, 198, 135, 191, 117, 81},
            {0, 0, 0, 0, 0, 0, 140, 56, 161, 200, 75, 29, 148, 192, 107, 62},
            {50, 187, 177, 144, 171, 125, 93, 198, 20, 85, 156, 110, 68, 123, 102, 130},
            {72, 189, 191, 114, 90, 151, 60, 176, 182, 49, 200, 103, 27, 164, 195, 141},
            {58, 97, 143, 187, 109, 31, 138, 73, 193, 81, 54, 124, 197, 130, 41, 175},
            {116, 151, 178, 39, 104, 165, 66, 121, 169, 59, 146, 20, 179, 94, 136, 86},
            {137, 200, 99, 113, 180, 120, 195, 87, 55, 129, 44, 170, 25, 190, 142, 157},
            {162, 53, 98, 192, 176, 153, 134, 60, 178, 73, 145, 199, 58, 108, 126, 91},
            {119, 32, 153, 164, 49, 131, 173, 112, 77, 192, 159, 105, 143, 188, 69, 147},
            {195, 187, 29, 121, 167, 138, 75, 53, 160, 89, 135, 50, 144, 98, 140, 110},
            {81, 123, 57, 182, 94, 200, 33, 191, 141, 96, 71, 166, 128, 154, 184, 56},
            {105, 178, 88, 116, 42, 92, 175, 190, 61, 158, 45, 132, 153, 198, 63, 79},
            {185, 107, 146, 99, 193, 20, 83, 151, 120, 127, 195, 48, 177, 74, 160, 101},
            {189, 65, 52, 85, 110, 157, 136, 199, 32, 180, 117, 150, 171, 56, 103, 166}
    };

    int[][] data1;//= matrixData;
    int[] xData = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
    int[] yData = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};

    //public Visualization(BlockingQueue<String> dataQueue) {
    //    super(dataQueue);
    //}

    BlockingQueue<String> testQueue = new LinkedBlockingQueue<>();

    void displayRTHeatmap(Data data) throws InterruptedException {
        JFrame frame = new JFrame("Measurement");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Display the window.
        // frame.setVisible(true);
        frame.setSize(500,500);
        //

        //Data matrixData = new Data(testQueue);

        HeatMapChart mainChart =  new HeatMapChartBuilder().width(800).height(600).title("HeatMap").build();
        mainChart.setTitle("DATA");
        data1 = data.matrixData;//matrixData.getMatrixData();
        mainChart.addSeries("Pomiar", xData, yData,data1);

        XChartPanel<HeatMapChart> heatmapPanel = new XChartPanel<>(mainChart);

        //heatmapPanel.setVisible(true);
        heatmapPanel.setSize(400,400);
        //
        //frame.add(mainChart);
        SwingWrapper<HeatMapChart> sw = new SwingWrapper<HeatMapChart>(mainChart);
        sw.displayChart();
        frame.add(heatmapPanel);

        //int cnt = 0;
        while(true){

            Thread.sleep(100);

            data1 = data.matrixData;//matrixData.getMatrixData();
            mainChart.updateSeries("Pomiar", xData, yData,data1);
            sw.repaintChart();

            //array = array+i;

        }
    }
}
