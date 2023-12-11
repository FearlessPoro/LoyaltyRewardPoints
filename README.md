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