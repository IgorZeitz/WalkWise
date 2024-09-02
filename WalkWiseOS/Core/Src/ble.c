/*
 * ble.c
 *
 *  Created on: Aug 24, 2024
 *      Author: igorz
 */
#include "ble.h"

  uint8_t matrix_data[16][16] = {	// przykladowa tablica danych - w rzeczywistosci bedzie aktualizowana z sensor.c
		    {0, 101, 200, 213, 222, 50, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {0, 0, 0, 200, 10, 10, 110, 10, 244, 0, 0, 0, 0, 0, 0, 0},
		    {0, 0, 0, 0, 200, 10, 10, 10, 244, 0, 0, 0, 0, 0, 0, 0},
		    {0, 0, 0, 0, 0, 200, 200, 222, 0, 0, 0, 0, 0, 0, 0, 0},
		    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		    {12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
		};

  uint16_t uart_data[16];	// bufor do wysylania przez uart

int is_connected(){
	//return 1; // connection failed
	return 0; // connected to desktop
}

int send_data(){
	//return 1; // data failed to send

	// PROTOTYP FUNKCJI - testy w main.c innego projektu
	  for(int i = 0; i<16; i++){
		  for(int j = 0; j<16; j++){
			  sprintf(uart_data, "%d\t", matrix_data[i][j]);
				  //tx_buffer[] = uart_data[j];
				  HAL_Delay(1000);
				  // TO DO: ustalic w jaki sposob szybciej wysylac te dane (przy krotszym czasie delay'a dane nie zostaja poprawnie wysylane -
				  //	czesc wartosci wysylana jest razem jako jedna a czasami czesc danych jest pomijana)
				  HAL_UART_Transmit(&huart1, uart_data, 16, 10);	// Sending 1 row column sensors value separated with tabs
		  }
		  HAL_UART_Transmit(&huart1, "\n\r", 2, 10);	// Switching to next row
		  if(i==15){	//after last iteration
			  HAL_UART_Transmit(&huart1, ";\n\r", 3, 10); // ; = end of all values

			  // TO DO: Ilość bitów w transmisji uart musi byc uzalezniona od jakiejś kalibracji czujnikow (wielkosc mierzonego stworzonka i jego ciezar)
			  //	bo przy wysylaniu mniejszej liczby niz wyznaczono tabulator sie wykrzacza (przynajmniej w konsoli - nie wiem jaki to bedzie mialo wplyw do
			  //	wysylania jako plik tekstowy)

		  }
	  }
	//

	return 0; // data sent correctly
}
