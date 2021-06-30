# Remote AC Controller
## Description
This project's purpose is to remotely control AC units which are not Wifi compatible. The system consists of a Raspberry-Pi-based controller connected to an IR transmitter and a mobile app for sending commands.
The system is capable of controlling power, temperature and fan speed. 
## Technologies & Hardware
* HiveMQ-CE MQTT Server with File RBAC Extension 
* Paho MQTT Python Client Library (for controller)
* Paho MQTT Library for Android (for mobile app)
* AWS EC2 (for hosting the server)
* Raspberry Pi 4 with Raspbian OS (Linux)
* IR Receiver
* IR Transmitter
* BJT
## Project Phases
### Choosing Platforms & Protocols
When you're on your way home in July, waiting for your AC unit to turn on after sending the command from your mobile is completely unacceptable. Therefore, polling for commands was
out of the question. I needed a Real-Time solution, and MQTT protocol provides exactly this - a topic-based publisher-subscriber model with realtively short turnaround times
for this project's purposes. I used HiveMQ Community Edition MQTT Server, and installed it along with the RBAC Extension on an AWS EC2 instance. <br/>
sdfsdf
### Defining MQTT Roles & Credentials
### Implementing MQTT Clients
#### Mobile App
#### Raspberry-Pi-based Controller
### Recording Original IR Signals
### Connecting an IR Transmitter
### Integration
crontab, installation, etc.
## System Workflow
## Demo
