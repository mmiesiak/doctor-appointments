package com.example.demo.appointment.repository.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity(name = "appointment")
public class AppointmentEntity {
    /**
     * The id of the appointment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The doctor's id for whom the appointment is created
     */
    private Long doctorId;

    /**
     * The date of the appointment
     */
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
    private String name;

    /**
     * The date when the appointment was created
     */
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
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

    public void setFromTime(LocalTime from) {
        this.fromTime = from;
    }

    public LocalTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalTime to) {
        this.toTime = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Transient
    public LocalDateTime getFromDateTime() {
        return LocalDateTime.from(date.atTime(fromTime));
    }

    @Transient
    public LocalDateTime getToDateTime() {
        return LocalDateTime.from(date.atTime(toTime));
    }
}
