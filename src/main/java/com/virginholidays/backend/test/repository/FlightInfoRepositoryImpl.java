package com.virginholidays.backend.test.repository;

import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.configuration.DataSourceConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import static java.time.LocalTime.parse;
import static java.util.Objects.requireNonNull;

@Repository
public class FlightInfoRepositoryImpl implements FlightInfoRepository {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(FlightInfoRepositoryImpl.class);

    private final List<Flight> flights;

    public FlightInfoRepositoryImpl(
            ResourceLoader resourceLoader,
            DataSourceConfiguration dataSourceConfiguration) {

        this.flights = List.copyOf(
                loadFlights(resourceLoader, dataSourceConfiguration));
    }

    @Override
    public CompletionStage<Optional<List<Flight>>> findAll() {
        return CompletableFuture.completedFuture(Optional.of(flights));
    }

    private List<Flight> loadFlights(
            ResourceLoader resourceLoader,
            DataSourceConfiguration dataSourceConfiguration) {

        LOGGER.info("Loading flight information from CSV");

        URL resource = requireNonNull(
                resourceLoader.getClassLoader()
                        .getResource(dataSourceConfiguration.getCsvLocation()));

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(resource.openStream()))) {

            return reader.lines()
                    .skip(1L)
                    .map(this::flight)
                    .toList();

        } catch (IOException e) {
            LOGGER.error("Could not retrieve flight data", e);
            throw new UncheckedIOException(e);
        }
    }

    private Flight flight(String line) {

        String[] split = line.split(",");

        List<String> elements = new ArrayList<>();
        List<DayOfWeek> days = new ArrayList<>();

        int index = 0;

        for (String token : split) {

            if (token != null && !token.isBlank()) {
                switch (index) {
                    case 4 -> days.add(DayOfWeek.SUNDAY);
                    case 5 -> days.add(DayOfWeek.MONDAY);
                    case 6 -> days.add(DayOfWeek.TUESDAY);
                    case 7 -> days.add(DayOfWeek.WEDNESDAY);
                    case 8 -> days.add(DayOfWeek.THURSDAY);
                    case 9 -> days.add(DayOfWeek.FRIDAY);
                    case 10 -> days.add(DayOfWeek.SATURDAY);
                    default -> elements.add(token);
                }
            }

            index++;
        }

        return new Flight(
                parse(elements.get(0)),
                elements.get(1),
                elements.get(2),
                elements.get(3),
                days);
    }
}