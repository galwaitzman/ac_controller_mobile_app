# Remote AC Controller
## Description
This project's purpose is to remotely control AC units which are not intrinsically WiFi-compatible. The system consists of a Raspberry-Pi-based controller connected to an IR transmitting circuit, and a mobile app for sending commands.
The system is capable of controlling power, temperature and fan speed. 
## Technologies
### Server
* HiveMQ-CE MQTT Server
* File RBAC Extension for HiveMQ-CE Server
* Hosted on AWS EC2
### Mobile App
* Android SDK
* Paho MQTT Android Client Library
### Controller
* Paho MQTT Python Client Library
* Lirc Virtual Device for Linux
* ir-ctl tool for Linux
* Cron Utility
## Hardware
* Raspberry Pi 4 with Raspbian OS (Linux)
* IR Receiver
* IR LED Transmitter
* BJT
## Project Components
### 1. Platforms & Protocols
When you send your AC unit a Turn-On command before you get home, you expect it to start cooling the room immediately, not at some random time in the future. Therefore, having the controller periodically poll for commands was
out of the question. A Real Time solution was preferred and MQTT protocol was suitable for the task: a topic-based publisher-subscriber model which has good round trip times
for this project's purposes and is also lightweight and easy to use. The project relies on HiveMQ Community Edition MQTT Server, installed along with the RBAC Extension on an AWS EC2 instance. <br/>
As for the controller unit, Raspberry Pi was chosen due to its functional flexibility, small size and low pricing. 
### 2. MQTT Roles & Credentials
In the server configuration files, two roles are defined: Command Maker and Controller. <br/>
A Command Maker can publish to Command topics only, and subscribe to Status topics.<br/>
A Controller can subscribe to Command topics that match its ClientId and publish to Status topics that match its ClientId.<br/>
Each user has at least one role, and has to enter its username and password In order to connect to the server.
### 3. Implementation
#### 3.1 Mobile App
The app provides the user with a simple remote-control display and allows them to control the power status (ON/OFF), temperature, and fan speed of the AC unit. The app's functinality is event-driven. An event can be either the user's interactions with the display widgets, or an incoming status message from the controller unit. Naturally, the app's MQTT client is given the Command Maker role. Upon startup, after establishing the MQTT connection, the app sends a status request to the controller and then displays the current status of the AC unit to the user (based on the last values saved in the controller unit). To make it easier for the user to manipulate the AC attributes, as well as to avoid sending unnecessary commands, each outgoing command message is delayed by 1sec after the last user interaction. For each modification made, the MQTT client receives a confirmation from the controller that the associated command has been transmitted to the AC unit. Once this confirmation is received,  the user is informed of the new status.
#### 3.2 Raspberry-Pi-based Controller
The controller's MQTT client is constantly waiting for incmoing commands. Once a command arrives, it is executed using the IR-transmitting circuit and sends back a confirmation message on the relevant status topic (power / temperature / fan).
The controller's ClientId contains its mac address, so it can only receive messages specifically addressed to it.
Implementing the execution of the received commands includes two phases:
1. Preliminary phase: Building an IR receiver circuit and recording signals of the original remote-control of the AC unit, using Lirc virtual device and ir-ctl tool.
2. Operational phase: Building an IR transmitter circuit to be used for transmitting the relevant IR signals.
Ultimately, Cron utility is used in order to make the program run at OS startup, and the controller is installed within clear sight of the AC unit built-in IR receiver.<br/>
<img src = "https://github.com/galwaitzman/ac_controller_mobile_app/blob/master/%E2%80%8F%E2%80%8Fcontroller.PNG" width = "50%" height="50%"/>

## System Workflow
Commands are sent from mobile app users to the MQTT server, which passes them on to the controller. The controller executes the commands by sending IR signals to the AC unit, and sends confirmation messages back to mobile app users.<br/><br/>
<img src = "https://github.com/galwaitzman/ac_controller_mobile_app/blob/master/%E2%80%8F%E2%80%8Fworkflow.PNG" width = "55%" height="55%"/>

## Demo

<img src = "https://github.com/galwaitzman/ac_controller_mobile_app/blob/master/demo.gif" width = "20%" height="20%"/>
