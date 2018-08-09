# Uber Taxi
This project designed a **Simulated Taxi Call System** (like Uber), which can simulate taxis responsing calls from passengers and the process of picking up passengers. Testers can use the interface to complete the whole operation process.

To achieve different functions, the system can be divided into three phases as shown below. 

Basic time unit of the system is 100ms.

### Simple Taxi

#### City Map

- Using the grid map to simlate a city map.  All roads are either horizontal or vertical. If there is a road between two points, there must be a connection between the two points.
- The file format is .txt. The map is represented by **80-line strings with 80 characters**. Each character is an **integer between 0 and 3**, which indicates an **80×80 contiguous matrix**.
- For example:
  - `Ai,j= 0` represents there is no connection between `(i,j)`, `(i,j+1)` or `(i+1,j)`.
  - `Ai,j= 1` represents there is a connection between `(i,j)` and  `(i,j+1)`, but no connection with `(i+1,j)`.
  - `Ai,j= 2` represents there is a connection between `(i+1,j)` and  `(i,j+1)`, but no connection with `(i,j+1)`.
  - `Ai,j= 3` represents  `(i,j)`, `(i,j+1)` and `(i+1,j)` are all connected.

#### Taxi

- The maximum numbers of taxis is 100. The starting position of the taxi is randomly distributed by the tester.
- It takes 100ms for a taxi to travel a unit-side.
- There are 4 states for each taxi:
  - **Being Serviced:** the taxi is runnig and the passengers are in the car.
  - **Will Service:** there is no passenger in the car, but the passenger has been assigned to the car.
  - **Waiting for Service:** there is no passenger in the car and the system doesn't assign any passengers to the car.
  - **Out of Service:** the taxi is not running.
  - Notify: the initial status of a taxi is *Waiting for Service*.

- There're 4 transformations of state for a taxi:
  - When the taxi is *Waiting for Service*, the system assigns passengers to the car. Then the taxi enters the *Will Service* state.
  - When the taxi is *Waiting for Service*, it needs to stop for 1s after continuously running for 20s. After rest, the taxi will run again.
  - After the taxi arrives at the position where passenger waits for (*Will Service*), it needs to stop for 1s to pick up the passenger.
  - After the taxi arrives at the position where passenger heads for (*Being Serviced*), it needs the stop for 1s to drop the passenger.
- There are 2 ways for a taxi to run:
  - When *Waiting for Service*, if a taxi encounters a branch, it can randomly select a branch to run.
  - When *Being Serviced* or *Will Service*, the taxi is required to run the shortest path
- The taxi has credit accumulation. 
  - The initial credit of all cars is 0. 
  - Each time when the taxi grab a order, credit += 1.
  - Each time successfully serve a passenger, credit += 3.

#### Class & Thread Design

- **Taxi Thread:** ` public class Taxi extends TimerTask`

  Transforme the states of taxi, juding the randomly running direction.

- **Map Class:** `public class Map`

  Reading and converting map.txt;

  BFS algorithm searching for the shortest path;

  Determining whether the two points are connected;

  Looking for passengers;

  Adding requests;

  Deleting requests.

- **Point Class:** `public class Point`

  Used to store coordinate information and connectivity of points on the map.

- **PassSendReq Thread:** `public class PassSendReq implements Runnable`

  Adding passsengers; 

  Tester can add codes in `run()` thread to run the system.

- **Passenger Thread:** `public class Passenger implements Runnable`

  Adding passengers;

  Choosing passengers;

  Assigning passengers to the taxi.

-  **Schedule Thread: **`public class Scheduler extends TimerTask`

  Scheduling thread.

- **Main Class：**`public class Main`

  Starting all threads.

#### SOLID Principles

Note: This design does not include subclasses or interfaces, and there is no need to extend the functionality (uniformly allowed to extend), so such parts are skipped.

- SRP

  Each class or method has only one clear responsibility. 

  See the comments in the code for details. 

  The method name is also used to illustrate the responsibilities.

- OCP

  Close: No modification allowed; Open: Allow extension.

  Variable declarations are all private. To meet requirements, some variables are also static. 

  For example, in Map Class, `private static Point[][] matrix;`and`private static Vector<Passenger>[][] map_matrix` are all static.

- DIP

  High-level classes should not rely on low-level classes.

  The Point class has no low-level classes;

  The Map class, the PassSendReq thread, the Passenger thread, the PassengerQueue class, the Schedule thread, and the Taxi thread have no dependencies.

### Senior Taxi (FlowControl & RoadObstacle)

Compared to the Simple Taxi, the Senior Taxi adds some advanced functions:

#### City Map with Obstacle

- Supporting to **dynamically close or open** some connected edges on the map during the running process. The total number of affected edges must be no more than 5.
- For each open connection edge on the map, defining the numbers of taxis passing from that side in the unit time window as the traffic on that side. The length of the time window is recommended to be 50ms, but not more than 100ms.

#### Taxi with Traffic Flow

The ways for a taxi to run has been updated as below due to the new traffic flow rules:

- When *Waiting for Service*:
  - If a taxi encounters a branch, it should **choose the side with smallest traffic flow**;
  - If there are mutiple sides with the smallest flow, the taxi can randomly select a branch to go.
- When *Will Service* & *Being Serviced*:
  - The taxi is required to **choose the shortest path**.
  - If the shortest path corresponds to multiple walking edges, **select ing the side with the smallest traffic flow**. 
  - If there are still multiple edges with the smallest flow, the taxi can randomly select either edge to go.

### Final Taxi (CrossRoad & TrafficLight)

Compared to the Simple Taxi, the Senior Taxi adds some advanced functions:

#### Cross Road

- Adding 'crossroad' attribute to the map, which means each intersection node in previous projects can be extended to **either plane intersection or stereo intersection**.
- In order to preserve the input method of the original map, the program should add a file input separately to define the road intersection. The file content is an 80-line string with 80 characters per line, and each character is 0 or 1. **0 means stereo intersection and 1 means plane intersection**. In theory, the number of the two should be half.

#### Traffic Lights

- Adding 'traffic light' attribute to the map. Traffic lights exist at the T cross or 十 cross on each plane intersction. 
