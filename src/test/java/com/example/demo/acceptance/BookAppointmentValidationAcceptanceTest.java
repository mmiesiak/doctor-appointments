package com.example.demo.acceptance;

import com.example.demo.appointment.repository.entity.DoctorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
Problem Definition

A busy hospital has a list of dates that a doctor is available to see patients.
Their process is manual and error-prone leading to overbooking.
They also have a hard time visualizing all of the available time for a doctor for specific dates.

Interview Task

Create a REST API that enables a simple scheduling system that manages doctor availabilities and allows patients to book appointments.

Data Model

Define a set of data models that include:

a way track assign booked appointments
a way to track patients
a way to track doctors
a way to track a doctor's working hours and days

REST API

Implement the following two (2) functionalities, plus one (1) optional (please do not implement anything more or less):

Find a doctor's working hours
Book a doctor opening
Optionally, create and update the list of doctor's working hours

I made some minor corrections with red color in order to better communicate the API to be implemented.

Implement the following three (3) operations (please do not implement anything more or less):

List appointment availability (openings) based on a date range and a doctor. So, this will return a list of appointments that can be booked.
The input will be a date range (i.e., 2022-11-10 to 2022-11-15) and a certain doctor.
Book an appointment for a certain doctor. Of course, if appointment is already booked you will return a proper error response.
Cancel an appointment.

I have to say that the slot design is not part of the requirements.
I decided to go this way in order to make implementation easier as well as because this is how most of the doctors book appointments.

Deliverables

Upload code to a GitHub public project and send the link
Optionally, add unit tests for API operations

 */
class BookAppointmentValidationAcceptanceTest extends BaseAcceptanceTest {

    @ParameterizedTest
    @CsvSource(value = {
            "'',15:00,16:00,John Doe",
            "2022-01-01,'',16:00,John Doe",
            "2022-01-01,15:00,'',John Doe",
            "2022-01-01,15:00,16:00,''",
            "2022-01-01,15:00,16:00,' '",
            "01-01-2022,15:00,16:00,John Doe",
            "2022-01-01,5:00,16:00,John Doe",
            "2022-01-01,15:00,6:00,John Doe",
            "2022-01-01,16:00,15:00,John Doe"
            // todo: datetime is before now
    })
    void bookAppointment_shouldReturnBadRequest_whenBodyIsInvalid(String date, String fromTime, String toTime, String name) {
        // Given
        var request = """
                {
                    "date": "%s",
                    "fromTime": "%s",
                    "toTime": "%s",
                    "name": "%s"
                }
                """.formatted(date, fromTime, toTime, name);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);

        // When
        var response = testRestTemplate.postForEntity("/api/v1/doctors/1/appointments", httpEntity, String.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void bookAppointment_shouldReturnNotFound_whenDoctorIsNotFound() {
        // Given
        var request = """
                {
                    "date": "2022-01-01",
                    "fromTime": "15:00",
                    "toTime": "16:00",
                    "name": "John Doe"
                }
                """;

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);

        // When
        var response = testRestTemplate.postForEntity("/api/v1/doctors/1/appointments", httpEntity, String.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "14:00,19:00",
            "14:00,15:00",
            "14:30,15:30",
            "17:30,18:30",
            "18:00,19:00"
            // todo: from, to is not multiply of 30 minutes
    })
    void bookAppointment_shouldReturnNotFound_whenOpeningIsNotFound(String fromTime, String toTime) {
        // Given
        DoctorEntity doctorEntity = createAndSaveDoctor("15:00", "18:00");

        var request = """
                {
                    "date": "2022-01-01",
                    "fromTime": "%s",
                    "toTime": "%s",
                    "name": "John Doe"
                }
                """.formatted(fromTime, toTime);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);

        // When
        var response = testRestTemplate.postForEntity("/api/v1/doctors/%s/appointments".formatted(doctorEntity.getId()), httpEntity, String.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "15:00,18:00",
            "14:30,17:30",
            "15:30,18:30",
            "14:30,18:30",
            "15:30,17:30"
    })
    void bookAppointment_shouldReturnConflict_whenOpeningIsAlreadyBooked(String fromTime, String toTime) {
        // Given
        DoctorEntity doctorEntity = createAndSaveDoctor("14:00", "19:00");
        createAndSaveAppointment(doctorEntity.getId(), "2022-01-01", "15:00", "18:00");

        var request = """
                {
                    "date": "2022-01-01",
                    "fromTime": "%s",
                    "toTime": "%s",
                    "name": "John Doe"
                }
                """.formatted(fromTime, toTime);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);

        // When
        var response = testRestTemplate.postForEntity("/api/v1/doctors/%s/appointments".formatted(doctorEntity.getId()), httpEntity, String.class);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    // todo: sends concurrently 50 requests for the same opening and verify that only one appointment is booked
    @Test
    void bookAppointment_shouldReturnConflict_whenMultiplePatientsTryToBookTheSameOpening() {
//        List<CompletableFuture> requests = new ArrayList<>();
//        IntStream.range(1, 50).forEach(ignore -> requests.add(CompletableFuture.supplyAsync(() -> null)));
//
//        CompletableFuture.allOf(requests.toArray(new CompletableFuture[requests.size()]));
    }
}
