package com.dyndyn.workshop.persist.repo;

import com.dyndyn.workshop.persist.model.Workshop;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface WorkshopRepository {

    Optional<Workshop> findById(Integer id);
    List<Workshop> findByCity(String city);
    Collection<Workshop> findAll();
    Workshop create(Workshop workshop);
    Workshop delete(Integer id);

}
