package com.example.tbackend.controllers;

import com.example.tbackend.DTO.request.AddPersonRequest;
import com.example.tbackend.models.Person;
import com.example.tbackend.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
@CrossOrigin(origins = "*")
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping("/get-paging")
    public ResponseEntity<?> getPersonPaging(
            @RequestParam("lastname") String lastname,
            @RequestParam("firstname") String firstname,
            @RequestParam("birthdate") String birthdate,
            @RequestParam("gender") String gender,
            @RequestParam("registrationdate") String registrationdate,
            @RequestParam("sortby") String sortby,
            @RequestParam("sortdirection") String sortdirection,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        Page<Person> persons = personService.getPaginated(firstname, lastname, birthdate, gender,
                registrationdate, sortby, sortdirection, page, size);
        return ResponseEntity.ok(persons);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPerson(@RequestBody AddPersonRequest request) {
        Person person = personService.add(request);
        return ResponseEntity.ok(person);
    }

}
