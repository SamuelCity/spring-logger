# Go City - Spring Logger

Go City - Spring Logger is collection of utilities to improve logging in your Spring Boot Web services. This repository manages two packages: `com.gocity.spring-logger` and `com.gocity.spring-logger-test`

These libraries feature:

- ECS Logging for environments outside of profiles `local` and `test`
- Standard log formatting for `local` and `test` profiles
- `WARN` logs with request and response information when any `4xx` or `5xx` HTTP status codes are returned to the client
- An `@Obscure` annotation which will mark properties for exclusion from appearing in logs when combined with the included `obscure` method used to override the `toString` method on objects  
- An in-memory logger for testing, so you can assert on log messages

## TODO
- [ ] Add a more comprehensive test suite for features
- [ ] Add an annotation processor to error compilation when `toString` hasn't been overridden on objects annotated with `@Obscure`
- [ ] Add more test functionality to `spring-logger-test`, including assertion helpers
- [ ] Pipeline (GitHub Actions) and publish process

## Setup
It's easy to get started with Go City - Spring Logger. Simply add the following to your `build.gradle` under dependencies:

```
implementation 'com.gocity:spring-logger:0.0.1-SNAPSHOT'

// If you would like the test utilities
testImplementation 'com.gocity:spring-logger-test:0.0.1-SNAPSHOT'
```

### Exception Logging

Once you have the dependency you can enable auto-configuration for the exception logger by annotating your `@SpringBootApplication` with `@EnableExceptionLogger`.

```
import com.gocity.logger.EnableExceptionLogger;

@SpringBootApplication
@EnableExceptionLogger
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### ECS Logging
ECS logging will be enabled by including this library. There is no need for any configuration.

### Memory Logger
If you wish to assert on log lines in your `@SpringBootTest`s you can do so with `spring-logger-test`. See below for example usage.

```
import com.gocity.logger.test.MemoryLogger;

@Test
@DisplayName("should log the exception in the logs when an exception is thrown")
public void shouldLogException() {
    final var logger = MemoryLogger.create("com.gocity");

    // Call method
    
    final var messages = logger.getFormattedMessages();
    final var expected = "My expected log line";

    assertThat(messages, hasItem(containsString(expected)));
}
```
