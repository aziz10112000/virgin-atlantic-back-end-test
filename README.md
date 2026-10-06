Virgin Atlantic - Flight Information Display

This is the completed Virgin Atlantic backend coding assessment.

The application reads flight information from src/main/resources/flights.csv and returns the flights operating on a requested date, ordered by departure time.

Requirements

Java 16
Maven 3.x

Running the application

Run the tests:

mvn clean test

Start the application:

mvn spring-boot:run

The application starts on:

http://localhost:8080

API

Get flights for a date

GET /back-end-test/{date}/results

The date should be provided in yyyy-MM-dd format.

For example:

GET http://localhost:8080/back-end-test/2026-10-05/results

The application uses the date supplied in the request to determine the day of the week, filters the flights from flights.csv, and returns them in departure-time order.

Example

For 2026-10-05, the response starts with:

[
  {
    "departureTime": "09:00",
    "destination": "Grenada",
    "iata": "GND",
    "flightNo": "VS089",
    "days": ["MONDAY"]
  },
  {
    "departureTime": "09:00",
    "destination": "St Lucia",
    "iata": "UVF",
    "flightNo": "VS089",
    "days": ["MONDAY"]
  },
  {
    "departureTime": "10:15",
    "destination": "Havana",
    "iata": "HAV",
    "flightNo": "VS063",
    "days": ["MONDAY", "THURSDAY"]
  }
]

The remaining flights are returned in the same chronological order.

Changes made

Completed all FIXME sections in the application.
Changed the endpoint to use the date supplied in the URL rather than the current date.
Added filtering based on the requested day's schedule.
Added chronological sorting by departure time.
Kept the supplied flights.csv unchanged.
Improved repository loading so flight data is loaded once and reused.
Added structured error handling for invalid dates.
Added unit tests for repository, service, and resource behaviour.
Added integration tests for the HTTP endpoint.
No new dependencies were added.

Tests

The project currently has 10 tests covering:

CSV flight loading
Repository flight-data reuse
Filtering flights by day
Sorting by departure time
Empty service results
Valid request dates
Invalid dates
Structured invalid-date error responses
HTTP endpoint integration
REST endpoint behaviour

Run:

mvn clean test

Expected result:

Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS

Data

Flight data is loaded from:

src/main/resources/flights.csv

The supplied flight data has not been changed.