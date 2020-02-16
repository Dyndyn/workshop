package com.dyndyn.workshop.controller;

import com.dyndyn.workshop.persist.model.Workshop;
import com.dyndyn.workshop.persist.repo.WorkshopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/workshop")
public class WorkshopController {
    private final static Logger LOG = LoggerFactory.getLogger(WorkshopController.class);

    private final WorkshopRepository workshopRepository;

    public WorkshopController(WorkshopRepository workshopRepository) {
        this.workshopRepository = workshopRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Workshop>> findOne(@PathVariable Integer id) {
        LOG.info("Handling request for retrieving workshop with id {}", id);
        return workshopRepository.findById(id)
                .map(employee -> new EntityModel<>(employee,
                        linkTo(methodOn(WorkshopController.class).findOne(employee.getId())).withSelfRel(),
                        linkTo(methodOn(WorkshopController.class).findAll()).withRel("workshops")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<?> findByCity(@PathVariable String city) {
        LOG.info("Handling request for retrieving all workshops");

        List<EntityModel<Workshop>> workshops = workshopRepository.findByCity(city).stream()
                .map(employee -> new EntityModel<>(employee,
                        linkTo(methodOn(WorkshopController.class).findOne(employee.getId())).withSelfRel(),
                        linkTo(methodOn(WorkshopController.class).findAll()).withRel("workshops")))
                .collect(Collectors.toList());
        return ResponseEntity.ok(workshops);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        LOG.info("Handling request for retrieving all workshops");

        List<EntityModel<Workshop>> workshops = workshopRepository.findAll().stream()
                .map(employee -> new EntityModel<>(employee,
                        linkTo(methodOn(WorkshopController.class).findOne(employee.getId())).withSelfRel(),
                        linkTo(methodOn(WorkshopController.class).findAll()).withRel("workshops")))
                .collect(Collectors.toList());
        return ResponseEntity.ok(workshops);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Workshop>> create(@RequestBody Workshop workshop) throws URISyntaxException {
        LOG.info("Handling request for creating workshop");

        Workshop created = workshopRepository.create(workshop);
        EntityModel<Workshop> employeeResource = new EntityModel<>(created,
                linkTo(methodOn(WorkshopController.class).findOne(created.getId())).withSelfRel());
        return ResponseEntity.created(new URI(employeeResource.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                .body(employeeResource);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        LOG.info("Handling request for deleting workshop with id {}", id);
        workshopRepository.delete(id);
        return ResponseEntity.noContent().build();
    }


}
