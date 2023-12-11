# Loyalty Reward Points

This is a simple Spring Boot application that allows calculation of loyalty reward points for a customer based on amount
spent. The application uses an in-memory H2 database to store customer and transaction data. The application is 
making a few assumptions:
- The transaction amounts are stored in cents.
- There is a batch job that runs every 24 hours to remove points earned on the day 3 months+1 day from today.
It is assumed that the batch job will run at 12:00 AM every day.
- If the reward points need to be calculated for existing clients and transactions, the batch job will need to be run. 
There is an endpoint that performs this functionality in TransactionController.
- The endpoints are for internal use only and there is no authentication or authorization implemented.

# Running a Loyalty Reward Points app

## Prerequisites

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) installed.
- [Gradle](https://gradle.org/install/) installed.

## Clone the Repository

Clone the repository containing your Java 17 Spring Boot project.

```bash
git clone https://github.com/your-username/your-spring-boot-project.git
cd your-spring-boot-project
```

## Build the Project
Navigate to the root directory of your project and build it using Gradle.

```bash
./gradlew build
```

## Run the Application
Run the Spring Boot application using the Gradle bootRun task.

```bash
./gradlew bootRun
```
## Additional Configuration

Update the application.properties or application.yml file in the src/main/resources directory to configure the h2 
database. It is recommended to change password to an environment variable.

## Testing

Execute tests using the following Gradle command:

```bash
./gradlew test
```

Please note that in order to run the API tests, the application needs to be running.