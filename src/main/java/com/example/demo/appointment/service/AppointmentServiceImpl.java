package com.example.demo.appointment.service;

import com.example.demo.appointment.domain.Appointment;
import com.example.demo.appointment.domain.CreateAppointmentCommand;
import com.example.demo.appointment.domain.Opening;
import com.example.demo.appointment.repository.AppointmentRepository;
import com.example.demo.appointment.repository.DoctorRepository;
import com.example.demo.appointment.repository.entity.AppointmentEntity;
import com.example.demo.appointment.repository.entity.DoctorEntity;
import com.example.demo.exception.ApplicationConflictException;
import com.example.demo.exception.ApplicationNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * > This class is a service that provides methods for creating, deleting appointments and listing openings
 */
@Service
class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentServiceMapper mapper;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, AppointmentServiceMapper mapper) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.mapper = mapper;
    }

    /**
     * > It validates the working hours of the doctor and the availability of the appointment slot, and then saves the
     * appointment
     *
     * @param createAppointmentCommand This is the command object that contains the data that is required to create an
     * appointment.
     * @return Appointment
     */
    @Override
    @Transactional
    public Appointment bookAppointment(CreateAppointmentCommand createAppointmentCommand) {
        DoctorEntity doctorEntity = doctorRepository.findAndLockById(createAppointmentCommand.getDoctorId())
                .orElseThrow(ApplicationNotFoundException::new);

        validateWorkingHours(doctorEntity, createAppointmentCommand);
        validateOpenings(createAppointmentCommand);

        AppointmentEntity appointmentEntity = appointmentRepository.save(mapper.toAppointmentEntity(createAppointmentCommand));
        return mapper.toAppointment(appointmentEntity);
    }

    /**
     * > If the appointment start time is before the doctor's start time or after the doctor's end time, or if the
     * appointment end time is after the doctor's end time, then throw an exception
     *
     * @param doctorEntity The doctor entity that we fetched from the database.
     * @param createAppointmentCommand This is the command object that contains the appointment data
     */
    private void validateWorkingHours(DoctorEntity doctorEntity, CreateAppointmentCommand createAppointmentCommand) {
        if (createAppointmentCommand.getFromTime().isBefore(doctorEntity.getStartTime())
                || createAppointmentCommand.getFromTime().isAfter(doctorEntity.getEndTime())
                || createAppointmentCommand.getToTime().isAfter(doctorEntity.getEndTime())) {
            throw new ApplicationNotFoundException();
        }
    }

    /**
     * If the slot is booked, throw an exception.
     *
     * @param createAppointmentCommand This is the command object that contains the appointment data
     */
    private void validateOpenings(CreateAppointmentCommand createAppointmentCommand) {
        if (isSlotBooked(createAppointmentCommand)) {
            throw new ApplicationConflictException();
        }
    }

    /**
     * If the appointment slot is already booked, then return true
     *
     * @param createAppointmentCommand The command object that contains the details of the appointment to be created.
     * @return A boolean value.
     */
    private boolean isSlotBooked(CreateAppointmentCommand createAppointmentCommand) {
        LocalTime fromTime = createAppointmentCommand.getFromTime();
        LocalTime toTime = createAppointmentCommand.getToTime();

        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAllByDoctorIdAndDateBetween(
                createAppointmentCommand.getDoctorId(),
                createAppointmentCommand.getDate(),
                createAppointmentCommand.getDate());

        return appointmentEntityList.stream().anyMatch(appointment ->
                (fromTime.equals(appointment.getFromTime()) || (fromTime.isAfter(appointment.getFromTime()) && fromTime.isBefore(appointment.getToTime())))
                        || (toTime.equals(appointment.getToTime()) || (toTime.isAfter(appointment.getFromTime()) && toTime.isBefore(appointment.getToTime())))
                        || (fromTime.isBefore(appointment.getFromTime()) && toTime.isAfter(appointment.getToTime()))
        );
    }

    /**
     * > This function deletes an appointment from the database
     *
     * @param appointmentId The id of the appointment to be cancelled.
     */
    @Override
    public void cancelAppointment(Long appointmentId) {
        AppointmentEntity appointmentEntity = appointmentRepository.findById(appointmentId)
                .orElseThrow(ApplicationNotFoundException::new);

        appointmentRepository.delete(appointmentEntity);
    }

    /**
     * > It returns a list of openings between two dates
     *
     * @param doctorId The id of the doctor
     * @param fromDate The date from which you want to start listing openings.
     * @param toDate The last date to search for openings.
     * @return A list of Opening objects.
     */
    @Override
    public List<Opening> listOpenings(Long doctorId, LocalDate fromDate, LocalDate toDate) {
        List<LocalDateTime> openings = getOpeningsBetweenDates(doctorId, fromDate, toDate);
        return mapper.toOpeningList(openings);
    }

    /**
     * > Get all the slots between two dates that are not booked
     *
     * @param doctorId The id of the doctor
     * @param fromDate The date from which the slots are to be calculated.
     * @param toDate The date until which you want to get the openings.
     * @return A list of LocalDateTime objects.
     */
    private List<LocalDateTime> getOpeningsBetweenDates(Long doctorId, LocalDate fromDate, LocalDate toDate) {
        DoctorEntity doctorEntity = doctorRepository.findById(doctorId)
                .orElseThrow(ApplicationNotFoundException::new);

        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAllByDoctorIdAndDateBetween(doctorId, fromDate, toDate);

        LocalDateTime start = LocalDateTime.from(fromDate.atTime(doctorEntity.getStartTime()));
        LocalDateTime stop = LocalDateTime.from(toDate.atTime(doctorEntity.getEndTime()));

        List<LocalDateTime> slots = new ArrayList<>();
        LocalDateTime slotStart = start;
        while (slotStart.isBefore(stop)) {
            if (isSlotBooked(slotStart, appointmentEntityList)) {
                slots.add(slotStart);
            }

            slotStart = calculateNextSlotStart(doctorEntity, slotStart);
        }
        return slots;
    }

    /**
     * > It calculates the next slot start time by adding 30 minutes to the current slot start time. If the current slot
     * start time is equal to the doctor's end time, then it adds one day to the current slot start time and sets the time
     * to the doctor's start time
     *
     * @param doctorEntity The doctor for whom the slots are being generated.
     * @param slotStart The start time of the slot.
     * @return A list of slots
     */
    private static LocalDateTime calculateNextSlotStart(DoctorEntity doctorEntity, LocalDateTime slotStart) {
        slotStart = slotStart.plusMinutes(30);

        if (slotStart.toLocalTime().equals(doctorEntity.getEndTime())) {
            LocalDate slotStartLocalDate = slotStart.toLocalDate();
            slotStart = slotStartLocalDate.plusDays(1).atTime(doctorEntity.getStartTime());
        }
        return slotStart;
    }

    /**
     * If there are no appointments in the list that have a start time before the slot start time and an end time after the
     * slot start time, then the slot is not booked.
     *
     * @param slotStart The start time of the slot we're checking
     * @param appointmentEntityList List of appointments for a particular doctor
     * @return A boolean value.
     */
    private boolean isSlotBooked(LocalDateTime slotStart, List<AppointmentEntity> appointmentEntityList) {
        return appointmentEntityList.stream().noneMatch(appointmentEntity -> isBetween(slotStart, appointmentEntity));
    }

    /**
     * It checks if the slotStartLocalTime is equal to the appointmentEntity's fromDateTime or if it is between the
     * appointmentEntity's fromDateTime and toDateTime
     *
     * @param slotStartLocalTime The start time of the slot that we are trying to book.
     * @param appointmentEntity The appointment entity that we are checking for conflicts.
     * @return A list of available slots for a given date.
     */
    private static boolean isBetween(LocalDateTime slotStartLocalTime, AppointmentEntity appointmentEntity) {
        return slotStartLocalTime.isEqual(appointmentEntity.getFromDateTime())
                || (slotStartLocalTime.isAfter(appointmentEntity.getFromDateTime()) && slotStartLocalTime.isBefore(appointmentEntity.getToDateTime()));
    }
}
