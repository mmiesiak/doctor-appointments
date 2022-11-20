package com.example.demo.appointment.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Opening {
    private LocalDate date;
    private LocalTime fromTime;

    public Opening(LocalDate date, LocalTime fromTime) {
        this.date = date;
        this.fromTime = fromTime;
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
}
