<h2>Uber Ride - A Microservices Backend Architecture</h2>

<h3>Overview</h3>

This is the backend architecture for an Uber-like ride-hailing application, built using microservices for scalability, real-time updates, and asynchronous communication.

<h3>Microservices</h3>

Here is a list of all the microservices used in this project:

1. **Auth Service - [Repository Link](https://github.com/shubham1957/UberProject-AuthService)**  
   - Handles JWT-based authentication and authorization using Spring Security implementing DaoAuthenticationProvider.

2. **Entity Service - [Repository Link](https://github.com/shubham1957/UberProject-EntityService)**  
   - Manages database models and migrations using Flyway, and serves as a central repository for shared data entities.

3. **Booking Service - [Repository Link](https://github.com/shubham1957/UberProject-BookingService)**  
   - Handles ride booking logic, including interactions with the Location and Review services.

4. **Socket Server - [Repository Link](https://github.com/shubham1957/UberProject-SocketServer)**  
   - Manages real-time WebSocket communication with drivers and passengers using the STOMP protocol.

5. **Location Service - [Repository Link](https://github.com/shubham1957/UberProject-LocationService)**  
   - Uses Redis to track and fetch nearby drivers for new ride requests.

6. **Review Service - [Repository Link](https://github.com/shubham1957/UberProject-ReviewService)**  
   - Manages feedback from both drivers and passengers after ride completion.

7. **Eureka Server - [Repository Link](https://github.com/shubham1957/UberProject-ServiceDiscovery-EurekaServer)**  
   - Provides service discovery for all microservices, ensuring seamless inter-service communication.
  
8. **Socket Web Client - [Repository Link](https://github.com/shubham1957/Uber-SocketWebClient)**  
   - A simple WebSocket client used by the drivers to receive ride requests and send responses (e.g., accepting rides and sending their Id).
