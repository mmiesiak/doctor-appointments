package com.example.demo.appointment.service;

import com.example.demo.appointment.domain.Appointment;
import com.example.demo.appointment.domain.CreateAppointmentCommand;
import com.example.demo.appointment.domain.Opening;
import com.example.demo.appointment.repository.entity.AppointmentEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AppointmentServiceMapperImpl implements AppointmentServiceMapper {

    private final ModelMapper modelMapper;

    public AppointmentServiceMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AppointmentEntity toAppointmentEntity(CreateAppointmentCommand createAppointmentCommand) {
        return modelMapper.map(createAppointmentCommand, AppointmentEntity.class);
    }

    @Override
    public Appointment toAppointment(AppointmentEntity appointmentEntity) {
        return modelMapper.map(appointmentEntity, Appointment.class);
    }

    @Override
    public List<Opening> toOpeningList(List<LocalDateTime> slots) {
        return slots.stream()
                .map(localDateTime -> new Opening(localDateTime.toLocalDate(), localDateTime.toLocalTime()))
                .toList();
    }
}
