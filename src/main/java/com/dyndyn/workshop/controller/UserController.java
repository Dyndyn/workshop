package com.dyndyn.workshop.controller;

import com.dyndyn.workshop.persist.model.User;
import com.dyndyn.workshop.persist.repo.UserRepository;
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
@RequestMapping("/users")
public class UserController {
    private final static Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> findOne(@PathVariable Integer id) {
        LOG.info("Handling request for retrieving user with id {}", id);
        return userRepository.findById(id)
                .map(employee -> new EntityModel<>(employee,
                        linkTo(methodOn(UserController.class).findOne(employee.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).findAll()).withRel("users")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        LOG.info("Handling request for retrieving all users");

        List<EntityModel<User>> users = userRepository.findAll().stream()
                .map(employee -> new EntityModel<>(employee,
                        linkTo(methodOn(UserController.class).findOne(employee.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).findAll()).withRel("users")))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<EntityModel<User>> create(@RequestBody User user) throws URISyntaxException {
        LOG.info("Handling request for creating user");

        User created = userRepository.create(user);
        EntityModel<User> employeeResource = new EntityModel<>(created,
                linkTo(methodOn(UserController.class).findOne(created.getId())).withSelfRel());
        return ResponseEntity.created(new URI(employeeResource.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                .body(employeeResource);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        LOG.info("Handling request for deleting user with id {}", id);
        userRepository.delete(id);
        return ResponseEntity.noContent().build();
    }


}
