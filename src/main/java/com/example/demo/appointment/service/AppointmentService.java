package com.example.demo.appointment.service;

import com.example.demo.appointment.domain.Appointment;
import com.example.demo.appointment.domain.CreateAppointmentCommand;
import com.example.demo.appointment.domain.Opening;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    Appointment bookAppointment(CreateAppointmentCommand createAppointmentCommand);

    void cancelAppointment(Long appointmentId);

    List<Opening> listOpenings(Long doctorId, LocalDate fromDate, LocalDate toDate);
}
