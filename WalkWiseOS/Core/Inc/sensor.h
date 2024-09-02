/*
 * sensor.h
 *
 *  Created on: Aug 24, 2024
 *      Author: igorz
 */

#ifndef SRC_SENSOR_H_
#define SRC_SENSOR_H_

#include "stm32g0xx_hal.h"

void setup();
int calibration();

int readMux();
void writeMux();

void activated_sensor();

//void read_pressure(int value, int row, int column);

#endif /* SRC_SENSOR_H_ */
