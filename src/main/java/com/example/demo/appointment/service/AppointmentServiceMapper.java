package com.example.demo.appointment.service;

import com.example.demo.appointment.domain.Appointment;
import com.example.demo.appointment.domain.CreateAppointmentCommand;
import com.example.demo.appointment.domain.Opening;
import com.example.demo.appointment.repository.entity.AppointmentEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentServiceMapper {
    AppointmentEntity toAppointmentEntity(CreateAppointmentCommand createAppointmentCommand);

    Appointment toAppointment(AppointmentEntity appointmentEntity);

    List<Opening> toOpeningList(List<LocalDateTime> slots);
}
