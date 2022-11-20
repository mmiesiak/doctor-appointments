package com.example.demo.appointment.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class OpeningResponse {
    /**
     * The date of the available opening
     */
    private LocalDate date;

    /**
     * The time of the available opening
     */
    @JsonFormat(pattern = "HH:mm")
    private LocalTime fromTime;

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
}
