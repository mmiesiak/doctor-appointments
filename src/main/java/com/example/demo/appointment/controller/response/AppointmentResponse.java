package com.example.demo.appointment.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentResponse {
    /**
     * The id of the appointment
     */
    private Long id;

    /**
     * The date of the appointment
     */
    private LocalDate date;

    /**
     * The time of when the appointment will begin
     */
    @JsonFormat(pattern = "HH:mm")
    private LocalTime fromTime;

    /**
     * The time of when the appointment will finish
     */
    @JsonFormat(pattern = "HH:mm")
    private LocalTime toTime;

    /**
     * The name of the patient
     */
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalTime fromTime) {
        this.fromTime = fromTime;
    }

    public LocalTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalTime toTime) {
        this.toTime = toTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
