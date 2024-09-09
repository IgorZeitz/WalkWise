package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        Communication test = new Communication();
        test.findAllDevice();

        test.findSpecificDevice("zejcik");

    }
}