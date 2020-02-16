package com.dyndyn.workshop.persist.repo;

import com.dyndyn.workshop.persist.model.Appointment;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Optional;

public interface AppointmentRepository {

    Optional<Appointment> findById(Integer id);
    Collection<Appointment> findAll();
    Appointment create(Appointment appointment);
    Optional<Appointment> updateDate(Integer appointmentId, ZonedDateTime date);
    Appointment delete(Integer id);

}
