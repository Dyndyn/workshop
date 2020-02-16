package com.dyndyn.workshop.persist.repo.impl;

import com.dyndyn.workshop.exception.ValidationException;
import com.dyndyn.workshop.persist.model.Workshop;
import com.dyndyn.workshop.persist.repo.WorkshopRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class WorkshopRepositoryImpl implements WorkshopRepository {
    private final Map<Integer, Workshop> workshops = new ConcurrentHashMap<>();
    private final Set<String> companyNames = new HashSet<>();
    private final AtomicInteger lastId = new AtomicInteger();

    @Override
    public Optional<Workshop> findById(Integer id) {
        return Optional.ofNullable(workshops.get(id));
    }

    @Override
    public List<Workshop> findByCity(String city) {
        return workshops.values().stream().filter(workshop -> city.equals(workshop.getCity()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Workshop> findByCompanyName(String companyName) {
        return workshops.values().stream().filter(workshop -> companyName.equals(workshop.getCompanyName())).findAny();
    }

    @Override
    public Collection<Workshop> findAll() {
        return workshops.values();
    }

    @Override
    public synchronized Workshop create(Workshop workshop) {
        Objects.requireNonNull(workshop);
        if (companyNames.contains(workshop.getCompanyName())) {
            throw new ValidationException("Such company names already exists");
        }
        workshop.setId(lastId.incrementAndGet());
        workshops.put(workshop.getId(), workshop);
        companyNames.add(workshop.getCompanyName());
        return workshop;
    }

    @Override
    public synchronized Workshop delete(Integer id) {
        Workshop workshop = workshops.remove(id);
        if (workshop != null) {
            companyNames.remove(workshop.getCompanyName());
        }
        return workshop;
    }
}
