# Elevator
This project designed a **Multi-elevator Scheduling and Operating System**.

To achieve different functions, the system can be divided into three phases.

### Simple Elevator

The first phase is a Simple Elevator. The detailed descriptions are shown below:

- The elevator has 10 floors. Each floor has ten requested buttons inside the elevator car, and two direction buttons outside the car;
- Set the elevator to one floor when the program starts or resets;
- The Simple Elevator conducts Fool Operating Strategy, which means it  continuously scans the queue, and fetches the requests according to FAFS  (First Arrived First Served，FAFS) policy. Only when the request is executed, the next request will be scheduled.
- The elevator spends 0.5s to run one floor. After reaching the floor, the series of actions such as stopping and opening the door comsumes 1s.

Input Specifications:

- Floor request: `(FR, m, UP/DOWN, T)`. `FR` is a fixed signal, `m` is the requested floor number, `UP` and `DOWN` are direction requests, and `T` is the  time when the request appears.
- Inside car request: `(ER, n, T)` .`ER` is a fixed signal, `n` is the requested floor number, and `T` is the  time when the request appears.

Output Specifications:

- `(n, UP/DOWN, t)` . `n` is floor number, `UP` and `DOWN` are direction that elevator heads for, and `t` is the operation time from the first request appears.

Class Contains:

- Control.java
- Elevator.java
- Floor.java
- Main.java
- Request.java
- RequestQueue.java

### Smart Elevator

The Smart Elevator is designed on the basis of Simple Elevator, which introducing `extends` mechanism to implement more functions.

Compared to the Simple Elevator, the Smart Elevator adds some basic descriptions:

- The elevator system consists of three identical elevators.
-  Three elevators run between 1 (include) and 20 (include) floors.
- The system is arranged by a scheduler, which determines how the three elevators respond to the floor request (FR request). The ER request can only be processed by the elevator.
- In order to improve the observability of the system state during the test, it is necessary to set the elevator to run on one floor for 3s and the opening&closing door process consumes 6s.
- The Smart Elevator can respond requests passingly.

### Multi-elevator Scheduling and Operating System

The Multi-elevator System contains three elevators by implementing the Multithreading mechanism.

Class Contains:

- Control.java
- Elevator.java
- Floor.java
- Main.java
- Request.java
- RequestList.java
- RequestMonitor

