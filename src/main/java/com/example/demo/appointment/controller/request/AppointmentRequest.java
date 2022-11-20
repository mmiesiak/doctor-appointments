package com.example.demo.appointment.controller.request;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentRequest {
    /**
     * The date of the appointment
     */
    @NotNull
    private LocalDate date;

    /**
     * The time of when the appointment will begin
     */
    private LocalTime fromTime;

    /**
     * The time of when the appointment will finish
     */
    private LocalTime toTime;

    /**
     * The name of the patient
     */
    @NotBlank
    private String name;

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

    /**
     * If the fromTime and toTime are not null, and the toTime is after the fromTime, then return true.
     *
     * @return A boolean value.
     */
    @AssertTrue(message = "Values are invalid")
    public boolean isValid() {
        return fromTime != null && toTime != null
                && (toTime.isAfter(fromTime) || toTime.equals(fromTime));
    }
}
