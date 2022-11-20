package com.example.demo.appointment.controller;

import com.example.demo.appointment.controller.request.AppointmentRequest;
import com.example.demo.appointment.controller.response.AppointmentResponse;
import com.example.demo.appointment.controller.response.OpeningResponse;
import com.example.demo.appointment.domain.Appointment;
import com.example.demo.appointment.domain.CreateAppointmentCommand;
import com.example.demo.appointment.domain.Opening;

import java.util.List;

public interface AppointmentControllerMapper {
    CreateAppointmentCommand toCreateAppointmentCommand(AppointmentRequest appointmentRequest, Long doctorId);

    AppointmentResponse toAppointmentResponse(Appointment appointment);

    List<OpeningResponse> toOpeningResponseList(List<Opening> openings);
}
