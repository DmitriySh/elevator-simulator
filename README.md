elevator-simulator
=======

My pet project.  
This is a prototype of small and not very smart simulator of elevator in multithreaded execution. 
The `elevator simulator` has a set of commands and users can interact with it from the shell of terminal.  
Powered by: `Java SE` and `Guice`. 
Project uses blocking queues in case of threading model: incoming commands help to connect 
different threads.
  
  
  
## Rules:  

  * Run program (use `./scripts/start_elevator.sh`).
```bash
$ java -jar elevator-simulator-all-*.jar -n 15 -h 4 -v 2 -d 5
```   
```text
-n = max number of floors [5..20]
-h = height of floor [1..10]
-v = Velocity of elevator [1..10] m/sec
-d = Duration of time between the opening and closing elevator door [2..20] sec
```   

  * Choose command: (`/h - help`).
```bash  
/h
	h - help
	You see current message

	b - button [1..20]
	Press the button to select the floor

	e - elevator
	Invoke the elevator on the 1st floor (main lobby)

	q - quit
	End session and quit

Start your command with slash symbol '/'
Author: Dmitriy Shishmakov  
```     
   
  * Use commands `/b <number>` or `/e` to communicate with elevator.
```bash
/e
	1 floor, elevator open doors
	1 floor, elevator close doors
	
/b 7
	2 floor
	3 floor
	4 floor
	5 floor
	6 floor
	7 floor
	7 floor, elevator open doors
	7 floor, elevator close doors

/e
	6 floor
	5 floor
	4 floor
	3 floor
	2 floor
	1 floor
	1 floor, elevator open doors
	1 floor, elevator close doors

/b seven
Your floor number is not valid. Please try again...
```  

  * `Elevator simulator` has a set of states and each of them has own rules to process the commands.
```text
       ----------------------------------------------
       |                                            ↓
    ---------        -----------------         -------------
    IdleState   -->  MoveUpOrDownState   -->   StopOpenState
    ---------        -----------------         -------------
       ↑                    ↑                       ↓
       ↑                    ↑                       ↓ 
       ↑                    ↑                  --------------  
       -------------------  ---------------    StopCloseState
                                               --------------
```    
  
  * Use command `/q` or `^C` (Ctrl + C) to end the session
  
  * `Elevator simulator` writes a separate log file `client.log`.
```bash
elevator-simulator/scripts$ tree --charset unicode
.
|-- logs
|   `-- client.log
`-- start_elevator.sh

1 directory, 2 files
```  


  
## Requirements:

  * Java SE Development Kit 8 (or newer)  
  * Gradle 2.x (or you could use Gradle wrapper)   
  * Git 1.7.x (or newer)  



## Run

  *  Build project. Go to the root path `/elevator-simulator/` of the project and run:  
```sh
elevator-simulator$ ./gradlew clean build

> Configure project :
Version 0.1


BUILD SUCCESSFUL in 3s
6 actionable tasks: 6 executed

```  

  *  Run server: 
```sh
elevator-simulator/scripts$ ./start_elevator.sh
----- // -----    ServiceController START 2017-08-07T05:55:37.724    ----- // -----
ServiceController starting...
ConsoleService starting...
ConsoleService started
ConsoleService get ready, choose command: (/h - help)
ElevatorService starting...
ElevatorService started
ServiceController started, state: RUN
```  

## Stop

  * `Elevator simulator` is terminated by command `\q` or response to a user interrupt, such as typing `^C` (Ctrl + C), or a system-wide event of shutdown.  
```sh
/q
ServiceController stopping...
ConsoleService stopping...
ConsoleService waiting for shutdown...
ConsoleService stopped
ElevatorService stopping...
ElevatorService waiting for shutdown...
ElevatorService stopped
Executor service stopping...
Executor service stopped
ServiceController stopped, state: IDLE
----- // -----    ServiceController STOP 2017-08-07T05:56:58.906    ----- // -----
Buy!
Thread[service-main-hook-thread,5,main] was interrupted by hook
ServiceController stopping...
Warning! ServiceController already stopped, state: IDLE
```
