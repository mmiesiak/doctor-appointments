package com.example.demo.acceptance;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
class ListOpeningsAcceptanceTest extends BaseAcceptanceTest {

    @Test
    void listOpenings() throws JSONException {
        // Given: a doctor with working hours from 15:00 to 18:00
        var doctorEntity = createAndSaveDoctor("15:00", "18:00");

        // And: some appointments for the doctor
        createAndSaveAppointment(doctorEntity.getId(), "2022-01-01", "15:00", "15:30");
        createAndSaveAppointment(doctorEntity.getId(), "2022-01-01", "16:00", "17:00");
        createAndSaveAppointment(doctorEntity.getId(), "2022-01-02", "15:00", "17:30");
        createAndSaveAppointment(doctorEntity.getId(), "2022-01-03", "15:00", "18:00");

        // When: calling the API to list the openings for a doctor.
        var response = testRestTemplate.getForEntity("/api/v1/doctors/%s/openings?fromDate=2022-01-01&toDate=2022-01-03".formatted(doctorEntity.getId()), String.class);

        // Then: response status is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // And: response body is not null
        assertNotNull(response.getBody());

        // And: response body contains available opening slots
        JSONAssert.assertEquals("""
                        [
                            {
                                "date": "2022-01-01",
                                "fromTime": "15:30"
                            },
                            {
                                "date": "2022-01-01",
                                "fromTime": "17:00"
                            },
                            {
                                "date": "2022-01-01",
                                "fromTime": "17:30"
                            },
                            {
                                "date": "2022-01-02",
                                "fromTime": "17:30"
                            }
                        ]
                        """, response.getBody(), true);
    }
}
