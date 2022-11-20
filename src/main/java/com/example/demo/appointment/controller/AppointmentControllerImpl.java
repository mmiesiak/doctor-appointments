package com.example.demo.appointment.controller;

import com.example.demo.appointment.controller.request.AppointmentRequest;
import com.example.demo.appointment.controller.request.ListOpeningQueryParams;
import com.example.demo.appointment.controller.response.AppointmentResponse;
import com.example.demo.appointment.controller.response.OpeningResponse;
import com.example.demo.appointment.domain.Appointment;
import com.example.demo.appointment.domain.Opening;
import com.example.demo.appointment.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
class AppointmentControllerImpl implements AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentControllerMapper mapper;

    AppointmentControllerImpl(AppointmentService appointmentService, AppointmentControllerMapper mapper) {
        this.appointmentService = appointmentService;
        this.mapper = mapper;
    }

    /**
     * > It takes an appointment request and a doctor id, calls the bookAppointment function in the
     * AppointmentService class, and returns an appointment response
     *
     * @param appointmentRequest This is the request object that is sent from the client.
     * @param doctorId The id of the doctor that the appointment is being booked with.
     * @return AppointmentResponse
     */
    @Override
    public ResponseEntity<AppointmentResponse> bookAppointment(AppointmentRequest appointmentRequest, Long doctorId) {
        Appointment appointment = appointmentService.bookAppointment(mapper.toCreateAppointmentCommand(appointmentRequest, doctorId));

        URI location = URI.create(AppointmentController.GET_APPOINTMENT_ENDPOINT_PATH.formatted(appointment.getId()));
        return ResponseEntity.created(location).body(mapper.toAppointmentResponse(appointment));
    }

    /**
     * The cancelAppointment function in the AppointmentController class calls the cancelAppointment function in the
     * AppointmentService class.
     *
     * @param appointmentId The id of the appointment to cancel.
     */
    @Override
    public void cancelAppointment(Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
    }

    /**
     * > It returns a list of openings for a doctor between two dates
     *
     * @param doctorId The id of the doctor for which the openings are to be listed.
     * @param listOpeningQueryParams This is a custom object that contains the parameters that are passed in the request.
     * @return List of OpeningResponse objects
     */
    @Override
    public List<OpeningResponse> listOpenings(Long doctorId, ListOpeningQueryParams listOpeningQueryParams) {
        List<Opening> openings = appointmentService.listOpenings(doctorId, listOpeningQueryParams.getFromDate(), listOpeningQueryParams.getToDate());
        return mapper.toOpeningResponseList(openings);
    }
}
