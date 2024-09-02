/*
 * sensor.c
 *
 *  Created on: Aug 24, 2024
 *      Author: igorz
 */

#include "sensor.h"

//Values for multiplexer output signals
unsigned short int wS0 = 0;	//PA1, PA4-PA6 (PA2, PA3 for UART)
unsigned short int wS1 = 0;
unsigned short int wS2 = 0;
unsigned short int wS3 = 0;

//Values for multiplexer input signals
unsigned short int rS0 = 0; //PA7-PA10
unsigned short int rS1 = 0;
unsigned short int rS2 = 0;
unsigned short int rS3 = 0;

unsigned short int rSIG_pin = 0;	// PB4 pin for reading Voltage from velostat

const unsigned short int mux_channel[16][4] = {  // 4bit binary addresses for all multiplexer channels
    {0, 0, 0, 0}, // channel 0
    {0, 0, 0, 1}, // channel 1
    {0, 0, 1, 0}, // channel 2
    {0, 0, 1, 1}, // channel 3
    {0, 1, 0, 0}, // channel 4
    {0, 1, 0, 1}, // channel 5
    {0, 1, 1, 0}, // channel 6
    {0, 1, 1, 1}, // channel 7
    {1, 0, 0, 0}, // channel 8
    {1, 0, 0, 1}, // channel 9
    {1, 0, 1, 0}, // channel 10
    {1, 0, 1, 1}, // channel 11
    {1, 1, 0, 0}, // channel 12
    {1, 1, 0, 1}, // channel 13
    {1, 1, 1, 0}, // channel 14
    {1, 1, 1, 1}  // channel 15
};

int sensor_matrix[16][16] = {	// All "sensors" values
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
};


int calibration(){
	/*	TO DO:
	 * Change properties depending on the size of limb and weight of the being
	 * - different voltage ranges
	 * */
}

void writeMux(int channel){	// sending signals to out multiplexer
	int address_mux_out_pin[] = {GPIO_PIN_1, GPIO_PIN_4, GPIO_PIN_5, GPIO_PIN_6};
	for(int i = 0; i<4; i++){
		HAL_GPIO_WritePin(GPIOA, address_mux_out_pin[i], mux_channel[channel][i]);
	}
}

int readMux(int channel){ // reading multiplexers input values
	int address_mux_in_pin[] = {GPIO_PIN_7, GPIO_PIN_8, GPIO_PIN_11, GPIO_PIN_12};
	for(int i = 0; i<4; i++){
		HAL_GPIO_WritePin(GPIOA, address_mux_in_pin[i], mux_channel[channel][i]); // Write to choose multiplexer channel to read
	}

	int muxValue = HAL_GPIO_ReadPin(GPIOB, GPIO_PIN_4); // Reading common input voltage value
	return muxValue;
}

//NIEPOTRZEBNE po co funkcja jak może być jeden zapis
//void read_pressure(int value, int row, int column){
//	// TO DO: figure out voltage values for pressure
//	sensor_matrix[row][column] = value;
//	// TO DO: function for displaying pressed sensor
//}

void activated_sensor(){ // figuring out which sensor is being pressed
	for(int i = 0; i<16; i++){
		//podanie napięcia pojedynczo na mux out
		writeMux(i);

		for(int j = 0; j<16; j++){
			//po podaniu napięcia na 1 mux out
			//sprawdzenie 16 kanałów mux in
			int pressureValue = readMux(j); //sensor [i][j] value(voltage but analogically pressure) at this moment
			//read_pressure(pressureValue, i, j);
			sensor_matrix[i][j] = pressureValue;
		}

	}
}

//void display_readings(){} Displaying from desktop not from uC
