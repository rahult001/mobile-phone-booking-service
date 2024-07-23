# Mobile Phone Booking Service

This project is a backend service for managing mobile phone bookings. It provides APIs for booking, returning, and retrieving phones. It uses Java with Spring Boot, MySQL for the database, and Kafka for messaging.

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/Q_6Rm4Tdh5g/0.jpg)](https://www.youtube.com/watch?v=Q_6Rm4Tdh5g)

## Table of Contents

- [Requirements](#requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Testing the Application](#testing-the-application)
- [API Documentation](#api-documentation)

## Requirements

- Java 11 or higher
- Maven 3.6.0 or higher
- MySQL 8.0 or higher
- Kafka 2.8.0 or higher
- Docker (optional, for running MySQL and Kafka in containers)

## Installation

### Clone the Repository

```sh
git clone https://github.com/rahult001/mobile-phone-booking-service.git
cd mobile-phone-booking-service
```
### Set Up MySQL
You can set up MySQL either locally or using Docker.

#### Locally
- Install MySQL
- Create a database named `phonebooking`
- Configure your MySQL user and password
### Set Up Kafka
You can set up Kafka either locally or using Docker.

#### Locally
- Install Kafka
- Create a Topic named `phoneNotifications`
- To see the messages being pushed to the Kafka Topic `phoneNotifications`
  ```sh
  kafka-console-consumer --bootstrap-server localhost:9092 --topic phoneNotifications --from-beginning 
  ```

### Build the Project
```sh
mvn clean install
```
## Configuration
Configure the application by updating the `src/main/resources/application.properties` file with your database and Kafka
settings.
### Database Configuration
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/phone_booking
spring.datasource.username=<Your Username>
spring.datasource.password=<Your Password>
spring.jpa.hibernate.ddl-auto=update
```
### Kafka Configuration
```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=phone-notifications
spring.kafka.consumer.auto-offset-reset=earliest
```
## Running the Application
### Using Maven
```sh
mvn spring-boot:run
```
The application will start and be accessible at `http://localhost:8090`.
## Testing the Application
### Run Unit Tests
```sh
mvn test
```
### API Testing
You can use Swagger UI or tools like Postman/curl to test the APIs.
#### Sample API Requests
- **Book a Phone**
 ```sh
 curl -X POST "http://localhost:8090/phones/book" -H "Content-Type: application/json" -d
'{"phoneId":1,"bookedBy":"Rahul Tiwari"}'
 ```
- **Return a Phone**
 ```sh
 curl -X POST "http://localhost:8090/phones/return?phoneId=1"
 ```
- **Get a Phone**
 ```sh
 curl -X GET "http://localhost:8090/phones/get?phoneId=1"
 ```
## API Documentation
The API documentation is available via Swagger UI at `http://localhost:8090/swagger-ui.html`.
