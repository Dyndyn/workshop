package com.dyndyn.workshop.persist.repo.impl;

import com.dyndyn.workshop.ValidationException;
import com.dyndyn.workshop.persist.model.Appointment;
import com.dyndyn.workshop.persist.repo.AppointmentRepository;
import com.dyndyn.workshop.persist.repo.UserRepository;
import com.dyndyn.workshop.persist.repo.WorkshopRepository;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AppointmentRepositoryImpl implements AppointmentRepository {
    private final Map<Integer, Appointment> appointments = new ConcurrentHashMap<>();
    private final AtomicInteger lastId = new AtomicInteger();

    private final UserRepository userRepository;
    private final WorkshopRepository workshopRepository;

    public AppointmentRepositoryImpl(UserRepository userRepository, WorkshopRepository workshopRepository) {
        this.userRepository = userRepository;
        this.workshopRepository = workshopRepository;
    }

    @Override
    public Optional<Appointment> findById(Integer id) {
        return Optional.ofNullable(appointments.get(id));
    }

    @Override
    public Collection<Appointment> findAll() {
        return appointments.values();
    }

    @Override
    public synchronized Appointment create(Appointment appointment) {
        Objects.requireNonNull(appointment);
        appointment.setUser(userRepository.findByUsername(appointment.getUsername())
                .orElseThrow(() -> new ValidationException("No user with such username exists")));
        appointment.setWorkshop(workshopRepository.findByCompanyName(appointment.getCompanyName())
                .orElseThrow(() -> new ValidationException("No workshop with such name exists")));
        appointment.setId(lastId.incrementAndGet());
        appointments.put(appointment.getId(), appointment);
        return appointment;
    }

    @Override
    public Optional<Appointment> updateDate(Integer appointmentId, ZonedDateTime date) {
        Objects.requireNonNull(date);
        return Optional.ofNullable(appointments.computeIfPresent(appointmentId, (key, value) -> {
            value.setDate(date);
            return value;
        }));
    }

    @Override
    public synchronized Appointment delete(Integer id) {
        return appointments.remove(id);
    }
}
