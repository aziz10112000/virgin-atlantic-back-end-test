package com.virginholidays.backend.test.repository;

import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.configuration.DataSourceConfiguration;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ResourceLoader;

@ExtendWith(MockitoExtension.class)
public class FlightInfoRepositoryImplTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private DataSourceConfiguration dataSourceConfiguration;

    private FlightInfoRepositoryImpl repository;

    @BeforeEach
    public void beforeEach() {

        when(dataSourceConfiguration.getCsvLocation())
                .thenReturn("flights.csv");

        ClassLoader classLoader = mock(ClassLoader.class);
        URL resource = getClass().getResource("/flights.csv");

        when(resourceLoader.getClassLoader())
                .thenReturn(classLoader);

        when(classLoader.getResource(anyString()))
                .thenReturn(resource);

        repository = new FlightInfoRepositoryImpl(
                resourceLoader,
                dataSourceConfiguration);
    }

    @Test
    public void testFindAll() throws ExecutionException, InterruptedException {

        Optional<List<Flight>> maybeFlights = repository
                .findAll()
                .toCompletableFuture()
                .get();

        assertThat(maybeFlights.isPresent(), equalTo(true));
        assertThat(maybeFlights.get().size(), equalTo(27));

        assertThat(
                maybeFlights.get().get(0).departureTime(),
                equalTo(LocalTime.of(9, 0)));

        assertThat(
                maybeFlights.get().get(0).destination(),
                equalTo("Antigua"));

        assertThat(
                maybeFlights.get().get(0).iata(),
                equalTo("ANU"));

        assertThat(
                maybeFlights.get().get(0).flightNo(),
                equalTo("VS033"));

        assertThat(
                maybeFlights.get().get(0).days().size(),
                equalTo(1));

        assertThat(
                maybeFlights.get().get(0).days().get(0),
                equalTo(DayOfWeek.TUESDAY));

        assertThat(
                maybeFlights.get().get(11).departureTime(),
                equalTo(LocalTime.of(15, 35)));

        assertThat(
                maybeFlights.get().get(11).destination(),
                equalTo("Las Vegas"));

        assertThat(
                maybeFlights.get().get(11).iata(),
                equalTo("LAS"));

        assertThat(
                maybeFlights.get().get(11).flightNo(),
                equalTo("VS044"));

        assertThat(
                maybeFlights.get().get(11).days().size(),
                equalTo(7));

        assertThat(
                maybeFlights.get().get(11).days().get(0),
                equalTo(DayOfWeek.SUNDAY));

        assertThat(
                maybeFlights.get().get(11).days().get(1),
                equalTo(DayOfWeek.MONDAY));

        assertThat(
                maybeFlights.get().get(11).days().get(2),
                equalTo(DayOfWeek.TUESDAY));

        assertThat(
                maybeFlights.get().get(11).days().get(3),
                equalTo(DayOfWeek.WEDNESDAY));

        assertThat(
                maybeFlights.get().get(11).days().get(4),
                equalTo(DayOfWeek.THURSDAY));

        assertThat(
                maybeFlights.get().get(11).days().get(5),
                equalTo(DayOfWeek.FRIDAY));

        assertThat(
                maybeFlights.get().get(11).days().get(6),
                equalTo(DayOfWeek.SATURDAY));
    }

    @Test
    public void testFindAllReusesLoadedFlightData()
            throws ExecutionException, InterruptedException {

        Optional<List<Flight>> firstResult = repository
                .findAll()
                .toCompletableFuture()
                .get();

        Optional<List<Flight>> secondResult = repository
                .findAll()
                .toCompletableFuture()
                .get();

        assertThat(firstResult.isPresent(), equalTo(true));
        assertThat(secondResult.isPresent(), equalTo(true));

        assertThat(
                firstResult.get(),
                equalTo(secondResult.get()));
    }
}