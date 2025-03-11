#include "BluetoothSerial.h"

TaskHandle_t measurePressureTask;
TaskHandle_t sendDataTask;

BluetoothSerial SerialBT;

void setup() {
  
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
  int cnt=0;
  while(true){
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

void loop() {
  //
}
