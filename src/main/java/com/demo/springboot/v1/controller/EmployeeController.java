package com.demo.springboot.v1.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.demo.springboot.domain.Employee;
import com.demo.springboot.exception.ResourceNotFoundException;
import com.demo.springboot.repository.EmployeeRepository;

@RestController
public class EmployeeController {
	

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> findEmployee(@PathVariable Long id){
		this.validate(id);
		Optional<Employee> employee = employeeRepository.findById(id);
		return new ResponseEntity<Employee>(employee.get(), HttpStatus.OK);
	}
	
	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> findAllEmployee(){
		List<Employee> employees  = employeeRepository.findAll();
		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}
	
	@PostMapping("/employees")
	public ResponseEntity<Void> createEmployee(@Valid @RequestBody Employee employee){
		 employee = employeeRepository.save(employee);
		 HttpHeaders responseHeaders = new HttpHeaders();
		 URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(employee.getId()).toUri();
		 responseHeaders.setLocation(uri);
		 return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
	}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<Void> updateEmployee(@Valid @RequestBody Employee employee){
		validate(employee.getId());
		employeeRepository.save(employee);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@PatchMapping("/employees/{id}")
	public ResponseEntity<Void> updatePartialEmployee(@RequestBody Employee employee){
		validate(employee.getId());
		employeeRepository.save(employee);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable Long id){
		validate(id);
		employeeRepository.deleteById(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	private void validate(Long id){
 	  employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
	}


}