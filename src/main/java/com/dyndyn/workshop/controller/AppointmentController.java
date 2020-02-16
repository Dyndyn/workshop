package com.dyndyn.workshop.controller;

import com.dyndyn.workshop.controller.dto.DateDTO;
import com.dyndyn.workshop.persist.model.Appointment;
import com.dyndyn.workshop.persist.repo.AppointmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    private final static Logger LOG = LoggerFactory.getLogger(AppointmentController.class);

    private final AppointmentRepository appointmentRepository;

    public AppointmentController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Appointment>> findOne(@PathVariable Integer id) {
        LOG.info("Handling request for retrieving appointment with id {}", id);
        return appointmentRepository.findById(id)
                .map(employee -> new EntityModel<>(employee,
                        linkTo(methodOn(AppointmentController.class).findOne(employee.getId())).withSelfRel(),
                        linkTo(methodOn(AppointmentController.class).findAll()).withRel("appointments")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        LOG.info("Handling request for retrieving all appointments");

        List<EntityModel<Appointment>> appointments = appointmentRepository.findAll().stream()
                .map(employee -> new EntityModel<>(employee,
                        linkTo(methodOn(AppointmentController.class).findOne(employee.getId())).withSelfRel(),
                        linkTo(methodOn(AppointmentController.class).findAll()).withRel("appointments")))
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Appointment>> create(@PathVariable Integer id, @RequestBody DateDTO newDate) {
        LOG.info("Handling request for updating appointment");

        return appointmentRepository.updateDate(id, newDate.getDate())
                .map(employee -> new EntityModel<>(employee,
                linkTo(methodOn(AppointmentController.class).findOne(employee.getId())).withSelfRel(),
                linkTo(methodOn(AppointmentController.class).findAll()).withRel("appointments")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<EntityModel<Appointment>> updateDate(@RequestBody Appointment appointment)
            throws URISyntaxException {
        LOG.info("Handling request for creating appointment");

        Appointment created = appointmentRepository.create(appointment);
        EntityModel<Appointment> employeeResource = new EntityModel<>(created,
                linkTo(methodOn(AppointmentController.class).findOne(created.getId())).withSelfRel());
        return ResponseEntity.created(new URI(employeeResource.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                .body(employeeResource);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        LOG.info("Handling request for deleting appointment with id {}", id);
        appointmentRepository.delete(id);
        return ResponseEntity.noContent().build();
    }


}
