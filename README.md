<div align="center">
  <img src="https://github.com/IgorZeitz/WalkWise/blob/main/photo/WalkWise_LogoNazwaREDME.png" alt="Logo" width="400" height="300">
  <br />

  </div>
  <br>
  WalkWise is an engineering project aimed at develope a modular, scalable, and wireless pressure mapping system for gait analysis. The system collects and transmits real-time pressure data from sensor matrices to a computer application for visualization and analysis. The repository is organized into 3 branches:
  
- [WalkWiseRTOS](https://github.com/IgorZeitz/WalkWise/tree/WalkWiseRTOS) - source files for the microcontroller firmware (FreeRTOS based),
- [Desktop](https://github.com/IgorZeitz/WalkWise/tree/desktop) - source files for the main desktop application,
- [Filtration & Classification](https://github.com/IgorZeitz/WalkWise/tree/Filtration-%26-Classification) - files responsible for Gaussian filtration and automatic foot pressure region classification.
  
 ## Technical details:
- Custom-designed pressure sensor matrix
- Custom-designed PCB with ESP32
- Bluetooth connection
- Software for data acquisition and visualization
- Firmware for MCU

<details open="open">
<summary>Table of Contents</summary>

- [About](#about)
- [Usage](#Usage)
- [Central Unit](#Central-Unit)
- [Measurement Unit](#Measurement-Unit)
    - [MCU](#MCU)
    - [Pressure Mat](#Pressure-Mat)

</details>

## About
The project consists of three parts, described specificly in each section. which are developement of:
- pressure sensor
- device hardware
- software (MCU & PC)

The aim of this device is to provide a modular, scalable and adaptable in many environments sytem for pressure mapping. Such a solution should be accurate, rellable and budget-friendly at once allowing giat measurements anywhere.

The concept of the solution is presented in the block diagram:
<div align="center">
  <img src="https://github.com/IgorZeitz/WalkWise/blob/main/photo/schemat_blokowy.png" alt="Logo" width="600" height="300">
</div>

## Usage
The System of giat anallysis and pressure mapping might be useful in medicine, veterinary, sports or physiotherapy. The observation of limbs pressure distribution may help detect some anomallies or irregularities in movement which can contribute to recognition of potential illnesses. Precise identification of health or motor function issues may prevent injuries or detect conditions like pronation, supination or flat feet.

Measurements carried out for a rat with paralysis of the hind legs:
<h1 align="center"> 
  <img src="https://github.com/IgorZeitz/WalkWise/blob/main/photo/IMG_2757.jpg" alt="Logo" width="800" height="600">
</h1>

### Central Unit
The software for the central unit was crated to give users controll of the device and abillity to read measurements. The code inicialises wireless bluetooth connection with paired WalkWise device. Beside connection and data transmission program also allows users to visulize measurements in real time. Each started measurement is at once saved in computer memory for later additional prezentation if necessary.

Class diagram:

<img src="https://github.com/IgorZeitz/WalkWise/blob/main/photo/UML%20class.jpg" alt="Logo" width="800" height="600">

All available user operations possible to execute form GUI are:
- Starting a measurement
- Loading previously saved measurement
- Exporting data to .csv for additional processing

| Use case diagram | GUI |
|:------------------:|:-----:|
| <img src="https://github.com/IgorZeitz/WalkWise/blob/main/photo/Use_case_uml.png" alt="Logo" width="407" height="338"> | <img src="https://github.com/IgorZeitz/WalkWise/blob/main/photo/GUI%20(1).png" alt="Logo"> |

Measurements are represented in a form of heat map which represents each responsive point on a sensor
<h1 align="center"> 
  <img src="https://github.com/IgorZeitz/WalkWise/blob/main/photo/mapacieplna.png" alt="Logo" width="500" height="400">
</h1>

### Measurement Unit

Measurement unit consists of mcu, multiplexers, usb-c port for powering up a device and additional ftdi to program device. All used components are soldierd to custom made PCB board:

<img src="https://github.com/IgorZeitz/WalkWise/blob/main/photo/brd.png" alt="Logo">

To properly secure design, all is embedded in 3D printed PLA case with openings for necessary wires:

  <img src="https://github.com/IgorZeitz/WalkWise/blob/main/photo/TopCasePCB.png" alt="Logo">

#### MCU
With use of FreeRTOS 2 task were created. One specified for carrying out measurements and one for sending data via bluetooth. Each task was then assigned to one core of ESP32.

<div align="center">
<img src="https://github.com/IgorZeitz/WalkWise/blob/main/photo/Sequence%20diagram.jpg" width="700" height="440">
</div>

#### Pressure Mat
Final prototype presents as shown:
| Sensor with measurement unit | Internal structure of sensor |
|:----------------------------:|:----------------------------:|
| <img src="https://github.com/IgorZeitz/WalkWise/blob/main/photo/IMG_3175.jpg" alt="Logo" width="600" height="800"> | <img src="https://github.com/IgorZeitz/WalkWise/blob/main/photo/IMG_2738.jpg" alt="Logo" width="600" height="800"> |

For the static frame, after device calibration, cross talk reduction and gaussian filtering the example of right foot pressure print representation as heat map gives likely-looking results:

<div align="center">
<img src="https://github.com/IgorZeitz/WalkWise/blob/main/photo/gauss.png" alt="Logo" width="450" height="350">
</div>


To unlock the full potential of the device, a basic machine-learning classifier has been implemented. Its purpose is to automatically identify which region of the foot is experiencing the highest pressure, helping to detect conditions such as supination or pronation. However, to achieve the full functionality and reliability of this AI assistant, it is necessary to provide additional labeled data for supervised learning.

<div align="center">
<img src="https://github.com/IgorZeitz/WalkWise/blob/main/photo/Zrzut%20ekranu%202025-07-21%20164658.png" alt="Logo" width="450" height="350">
</div>

[filtr]: <https://github.com/IgorZeitz/WalkWise/tree/Filtration-%26-Classification>
[rtos]: <https://github.com/IgorZeitz/WalkWise/tree/WalkWiseRTOS>
[desktop]: <https://github.com/IgorZeitz/WalkWise/tree/desktop>
