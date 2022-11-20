package com.example.demo.appointment.controller;

import com.example.demo.appointment.controller.request.AppointmentRequest;
import com.example.demo.appointment.controller.request.ListOpeningQueryParams;
import com.example.demo.appointment.controller.response.AppointmentResponse;
import com.example.demo.appointment.controller.response.OpeningResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/v1")
public interface AppointmentController {
    String GET_APPOINTMENT_ENDPOINT_PATH = "/api/v1/appointments/%d";

    @PostMapping("/doctors/{id}/appointments")
    ResponseEntity<AppointmentResponse> bookAppointment(@RequestBody @Valid AppointmentRequest appointmentRequest, @PathVariable("id") Long doctorId);

    @DeleteMapping("/appointments/{id}")
    void cancelAppointment(@PathVariable("id") Long appointmentId);

    @GetMapping("/doctors/{id}/openings")
    List<OpeningResponse> listOpenings(@PathVariable("id") Long doctorId, @Valid ListOpeningQueryParams listOpeningQueryParams);
}
