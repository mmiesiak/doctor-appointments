package com.example.demo.acceptance;

import com.example.demo.appointment.repository.AppointmentRepository;
import com.example.demo.appointment.repository.DoctorRepository;
import com.example.demo.appointment.repository.entity.AppointmentEntity;
import com.example.demo.appointment.repository.entity.DoctorEntity;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseAcceptanceTest {
    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @BeforeEach
    void setup() {
        appointmentRepository.deleteAll();
        doctorRepository.deleteAll();
    }

    DoctorEntity createAndSaveDoctor(String startTime, String endTime) {
        var doctorEntity = new DoctorEntity();
        doctorEntity.setName("Doctor Lewis");
        doctorEntity.setStartTime(LocalTime.parse(startTime));
        doctorEntity.setEndTime(LocalTime.parse(endTime));

        return doctorRepository.save(doctorEntity);
    }

    AppointmentEntity createAndSaveAppointment(Long doctorId, String date, String fromTime, String toTime) {
        var appointmentEntity = new AppointmentEntity();
        appointmentEntity.setDoctorId(doctorId);
        appointmentEntity.setDate(LocalDate.parse(date));
        appointmentEntity.setFromTime(LocalTime.parse(fromTime));
        appointmentEntity.setToTime(LocalTime.parse(toTime));
        appointmentEntity.setName("John Doe");

        return appointmentRepository.save(appointmentEntity);
    }
}
