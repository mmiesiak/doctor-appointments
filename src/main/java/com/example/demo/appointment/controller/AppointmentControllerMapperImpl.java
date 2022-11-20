package com.example.demo.appointment.controller;

import com.example.demo.appointment.controller.request.AppointmentRequest;
import com.example.demo.appointment.controller.response.AppointmentResponse;
import com.example.demo.appointment.controller.response.OpeningResponse;
import com.example.demo.appointment.domain.Appointment;
import com.example.demo.appointment.domain.CreateAppointmentCommand;
import com.example.demo.appointment.domain.Opening;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppointmentControllerMapperImpl implements AppointmentControllerMapper {

    private final ModelMapper modelMapper;

    public AppointmentControllerMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CreateAppointmentCommand toCreateAppointmentCommand(AppointmentRequest appointmentRequest, Long doctorId) {
        CreateAppointmentCommand createAppointmentCommand = modelMapper.map(appointmentRequest, CreateAppointmentCommand.class);
        createAppointmentCommand.setDoctorId(doctorId);
        return createAppointmentCommand;
    }

    @Override
    public AppointmentResponse toAppointmentResponse(Appointment appointment) {
        return modelMapper.map(appointment, AppointmentResponse.class);
    }

    @Override
    public List<OpeningResponse> toOpeningResponseList(List<Opening> openings) {
        return modelMapper.map(openings, new TypeToken<List<OpeningResponse>>() {}.getType());
    }
}
