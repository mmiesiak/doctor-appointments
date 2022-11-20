package com.example.demo.appointment.repository;

import com.example.demo.appointment.repository.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    /**
     * Find all appointments for a given doctor between two dates.
     *
     * @param doctorId The id of the doctor whose appointments we want to find.
     * @param fromDate The start date of the range.
     * @param toDate The end date of the range.
     * @return List of AppointmentEntity
     */
    List<AppointmentEntity> findAllByDoctorIdAndDateBetween(Long doctorId, LocalDate fromDate, LocalDate toDate);
}
