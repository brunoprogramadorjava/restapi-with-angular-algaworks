package com.algaworks.algamoneyapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algamoneyapi.event.ResourceCreatedEvent;
import com.algaworks.algamoneyapi.model.People;
import com.algaworks.algamoneyapi.repository.PeopleRepository;
import com.algaworks.algamoneyapi.service.PeopleService;

@RestController
@RequestMapping("/people")
public class PeopleController {

	@Autowired
	private PeopleRepository peopleRepository;

	@Autowired
	private PeopleService peopleService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<People> getAll() {
		return peopleRepository.findAll();
	}

	@GetMapping("/{id}")
	public People getById(@PathVariable Long id) {
		return peopleRepository.findOne(id);
	}

	@PostMapping
	public ResponseEntity<People> InsertPeople(@Valid @RequestBody People people, HttpServletResponse response) {
		People savedPeople = peopleRepository.save(people);
		publisher.publishEvent(new ResourceCreatedEvent(this, response, savedPeople.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(savedPeople);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remove(@PathVariable Long id) {
		peopleRepository.delete(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<People> update(@PathVariable Long id, @Valid @RequestBody People people) {
		People savedPeople = peopleService.update(id, people);
		return ResponseEntity.ok(savedPeople);
	}
	
	@PutMapping("/{id}/active")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void changeActiveProperty(@PathVariable Long id, @RequestBody Boolean active) {
		peopleService.changeActiveProperty(id, active);
	}
}
