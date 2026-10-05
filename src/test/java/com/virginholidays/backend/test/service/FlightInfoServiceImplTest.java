package com.virginholidays.backend.test.service;

import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.repository.FlightInfoRepository;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlightInfoServiceImplTest {

    @Test
    void shouldFilterFlightsByDayAndSortByDepartureTime() {

        Flight lateMondayFlight = new Flight(
                LocalTime.of(10, 15),
                "Havana",
                "HAV",
                "VS063",
                List.of(DayOfWeek.MONDAY)
        );

        Flight earlyMondayFlight = new Flight(
                LocalTime.of(9, 0),
                "Grenada",
                "GND",
                "VS089",
                List.of(DayOfWeek.MONDAY)
        );

        Flight tuesdayFlight = new Flight(
                LocalTime.of(11, 5),
                "Barbados",
                "BGI",
                "VS029",
                List.of(DayOfWeek.TUESDAY)
        );

        FlightInfoRepository repository = () -> CompletableFuture.completedFuture(
                Optional.of(List.of(lateMondayFlight, tuesdayFlight, earlyMondayFlight))
        );

        FlightInfoService service = new FlightInfoServiceImpl(repository);

        List<Flight> result = service
                .findFlightByDate(LocalDate.of(2026, 10, 5))
                .toCompletableFuture()
                .join()
                .orElseThrow();

        assertEquals(2, result.size());
        assertEquals("VS089", result.get(0).flightNo());
        assertEquals("VS063", result.get(1).flightNo());
        assertEquals(LocalTime.of(9, 0), result.get(0).departureTime());
        assertEquals(LocalTime.of(10, 15), result.get(1).departureTime());
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoFlightsForTheDay() {

        Flight mondayFlight = new Flight(
                LocalTime.of(10, 15),
                "Havana",
                "HAV",
                "VS063",
                List.of(DayOfWeek.MONDAY)
        );

        FlightInfoRepository repository = () -> CompletableFuture.completedFuture(
                Optional.of(List.of(mondayFlight))
        );

        FlightInfoService service = new FlightInfoServiceImpl(repository);

        List<Flight> result = service
                .findFlightByDate(LocalDate.of(2026, 10, 6))
                .toCompletableFuture()
                .join()
                .orElseThrow();

        assertTrue(result.isEmpty());
    }
}