#include "BluetoothSerial.h"

TaskHandle_t measurePressureTask;
TaskHandle_t sendDataTask;

BluetoothSerial SerialBT;

// pinout
#define ROW_MULTIPLEXER_S0 4  // multiplexer 1 address pins
#define ROW_MULTIPLEXER_S1 0
#define ROW_MULTIPLEXER_S2 2
#define ROW_MULTIPLEXER_S3 15

#define COLUMN_MULTIPLEXER_S0 36  // multiplexer 2 address pins
#define COLUMN_MULTIPLEXER_S1 39
#define COLUMN_MULTIPLEXER_S2 34
#define COLUMN_MULTIPLEXER_S3 35

#define READ_MULTIPLEXER_VOLTAGE 25 // multipexer column value read ADC
#define WRITE_MULTIPLEXER_VOLTAGE 26 // multipexer column value read DAC
// global variables
const unsigned short int multiplexerChannel[16][4] = {  // 4bit binary addresses for all multiplexer channels
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

unsigned int sensorMatrix[16][16] = {	// all measured values
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

void setup() {
  
  // pin setup
  pinMode(ROW_MULTIPLEXER_S0, OUTPUT);
  pinMode(ROW_MULTIPLEXER_S1, OUTPUT);
  pinMode(ROW_MULTIPLEXER_S2, OUTPUT);
  pinMode(ROW_MULTIPLEXER_S3, OUTPUT);

  pinMode(COLUMN_MULTIPLEXER_S0, OUTPUT);
  pinMode(COLUMN_MULTIPLEXER_S1, OUTPUT);
  pinMode(COLUMN_MULTIPLEXER_S2, OUTPUT);
  pinMode(COLUMN_MULTIPLEXER_S3, OUTPUT);

  pinMode(READ_MULTIPLEXER_VOLTAGE, INPUT);

  pinMode(WRITE_MULTIPLEXER_VOLTAGE, OUTPUT);

  // task 1 creation
  xTaskCreatePinnedToCore(
    measurePressure,  //task function to do
    "measurePressureTask",  // task name
    10000,  // stack size
    NULL, // task parameter
    1,  // task priority
    &measurePressureTask, // task handle
    1 // task pinned to core 1
  );

  // task 2 creation
  xTaskCreatePinnedToCore(
    sendData,  //task function to do
    "sendDataTask",  // task name
    10000,  // stack size
    NULL, // task parameter
    1,  // task priority
    &sendDataTask, // task handle
    0 // task pinned to core 0
  );
}

// pressure matrix mat service
void measurePressure(void * pvParameters){
  while(true){
    startMeasuring();
    vTaskDelay(pdMS_TO_TICKS(100));
  }
}

// sharing measured data via bluetooth
void sendData(void * pvParameters){
  Serial.begin(115200);
  SerialBT.begin("WalkWise");

  while(true){
    if (Serial.available()) {
      SerialBT.write(Serial.read());
    }
    if (SerialBT.available()) {
      Serial.write(SerialBT.read());
    }
    vTaskDelay(pdMS_TO_TICKS(20));
  }
}


/***  Pressure sensor operation functions ***/
void writeMux(int channel){	// sending signals to out multiplexer
  analogWrite(WRITE_MULTIPLEXER_VOLTAGE, 255);  // constant 3.3V for common multiplexer pin - setting only on which channel the voltage should be provided

	int multiplexerAddressPin[] = {ROW_MULTIPLEXER_S0, ROW_MULTIPLEXER_S1, ROW_MULTIPLEXER_S2, ROW_MULTIPLEXER_S3};
	for(int i = 0; i<4; i++){
		digitalWrite(multiplexerAddressPin[i], multiplexerChannel[channel][i]); // set multipexer address for channel - (channel = one specific row)
	}
}

int readMux(int channel){ // reading multiplexers input values
	int multiplexerAddressPin[] = {COLUMN_MULTIPLEXER_S0, COLUMN_MULTIPLEXER_S1, COLUMN_MULTIPLEXER_S2, COLUMN_MULTIPLEXER_S3};
	for(int i = 0; i<4; i++){
		digitalWrite(multiplexerAddressPin[i], multiplexerChannel[channel][i]); // set multipexer address for channel - (channel = one specific column)
	}

	int muxValue = analogRead(READ_MULTIPLEXER_VOLTAGE); // Reading common input voltage value
	return muxValue;
}

void startMeasuring(){ // checking which sensor is being pressed and with how much pressure
	for(int i = 0; i<16; i++){  //going through all sensor rows
		writeMux(i);
		for(int j = 0; j<16; j++){  //going through all sensor columns
			int pressureValue = readMux(j);
			sensorMatrix[i][j] = pressureValue;  //save measured data into corresponding matrix index
		}
	}
}


/***  Data transmission functions ***/


/***  Configuration functions ***/
int calibration(){
	/*	TO DO:
	 * Change properties depending on the size of limb and weight of the being
	 * - different voltage ranges
	 */
}

void loop() {
  //
}