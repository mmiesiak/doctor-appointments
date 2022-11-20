package com.example.demo.acceptance;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

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
class CancelAppointmentValidationAcceptanceTest extends BaseAcceptanceTest {

    @Test
    void cancelAppointment_shouldReturnNotFound_whenAppointmentIsNotFound() {
        // When
        var response = testRestTemplate.exchange("/api/v1/appointments/1", HttpMethod.DELETE, null, Void.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
