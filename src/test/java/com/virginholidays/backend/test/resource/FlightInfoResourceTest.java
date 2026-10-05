package com.virginholidays.backend.test.resource;

import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.service.FlightInfoService;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FlightInfoResourceTest {

    @Test
    void shouldReturnFlightsForRequestedDate() {

        Flight flight = new Flight(
                LocalTime.of(9, 0),
                "Grenada",
                "GND",
                "VS089",
                List.of(DayOfWeek.MONDAY)
        );

        FlightInfoService service = outboundDate ->
                CompletableFuture.completedFuture(Optional.of(List.of(flight)));

        FlightInfoResource resource = new FlightInfoResource(service);

        var response = resource.getResults("2026-10-05")
                .toCompletableFuture()
                .join();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(List.of(flight), response.getBody());
    }

    @Test
    void shouldReturnNoContentWhenThereAreNoFlights() {

        FlightInfoService service = outboundDate ->
        CompletableFuture.completedFuture(Optional.empty());

        FlightInfoResource resource = new FlightInfoResource(service);

        var response = resource.getResults("2026-10-06")
                .toCompletableFuture()
                .join();

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void shouldRejectInvalidDate() {

        FlightInfoService service = outboundDate ->
                CompletableFuture.completedFuture(Optional.empty());

        FlightInfoResource resource = new FlightInfoResource(service);

        assertThrows(
                java.time.format.DateTimeParseException.class,
                () -> resource.getResults("2026-02-30")
        );
    }
}