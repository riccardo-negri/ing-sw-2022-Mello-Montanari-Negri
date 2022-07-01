# ing-sw-2022-Mello-Montanari-Negri
Software Engineering Project - Politecnico di Milano - Academic Year 2021/2022

## Group Members
Group: PSP1
- Pietro Mello Rella ([@mellopietro](https://github.com/mellopietro) - matriculation number: 937516)
- Montanari Tommaso ([@Tommimon](https://github.com/Tommimon) - matriculation number: 932673)
- Negri Riccardo ([@riccardo-negri](https://github.com/riccardo-negri) - matriculation number: 936820)

## Description
The project consists in the development of a software version of the Eriantys board game.

Full game specs and game rules [here](https://github.com/riccardo-negri/ing-sw-2022-Mello-Montanari-Negri/tree/main/.github/assets/specs).

## Implemented Functionalities

| Functionality (FA = "Additional Functionality") | Status |
|:------------------------------------------------|:------:|
| Basic rules                                     |   ✅    |
| Complete rules                                  |   ✅    |
| Socket                                          |   ✅    |
| CLI                                             |   ✅    |
| GUI                                             |   ✅    |
| FA - All character cards                        |   ✅    |
| FA - 4 players                                  |   ✅    |
| FA - Multiple games                             |   ✅    |
| FA - Persistence                                |   ✅    |
| FA - Resilience to disconnections*              |   ✅*   |


 \* While a player is not logged in, the game continues until the disconnected player's turn.

## Requirements
Minimun JRE version: 17.

## Compile
To run the tests and compile the software execute
```bash
mvn package
```
in the root of the project.

On Windows and MacOS sometimes the tests of the server might fail. To avoid to run the tests of the server execute 
```bash
mvn -Dtest=!ConnectTest,!EndGameTest,!LobbiesTest,!MovesTest,!PortTest,!SavesManagerTest package
```
## Execute

### Client
To execute the client download the EriantysClient.jar from [here](https://github.com/riccardo-negri/ing-sw-2022-Mello-Montanari-Negri/tree/main/deliveries/jar)
and open a terminal in the same folder.
#### GUI
To execute the client with a graphical interface just run 
```bash
java -jar EriantysClient.jar
```
To enable the logger add the option `-d` or `--debug` to the command.

#### CLI
To execute the client with the terminal interface run
```bash
java -jar EriantysClient.jar --cli
```
To enable the logger add the option `-d` or `--debug` to the command.

To display ANSI codes correctly in your terminal do the following based on your OS:
- Linux: works by default
- MacOS: use `iTerm2` terminal
- Windows: run in the terminal `chcp 65001` and then run the jar with also the following option `-Dfile.encoding=UTF-8`


### Server
To execute the client download the EriantysServer.jar from [here](https://github.com/riccardo-negri/ing-sw-2022-Mello-Montanari-Negri/tree/main/deliveries/jar),
open a terminal in the same folder and run
```bash
java -jar EriantysServer.jar
```
To enable the logger add the option `-d` or `--debug` to the command.

To change matchmaking server port use the option `-p` or `--port` followed by the port number.  

## Screenshots
### GUI
![GUI](.github/assets/images/GUI.png?raw=true)
### CLI
![CLI](.github/assets/images/CLI.png?raw=true)
