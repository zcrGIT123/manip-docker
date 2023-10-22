package com.example.tbackend.services;

import com.example.tbackend.DTO.request.AddPersonRequest;
import com.example.tbackend.models.Person;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PersonService {
    Person add(AddPersonRequest request);
    void deleteById(String id);
    Page<Person> getPaginated(String firstname, String lastname, String birthdate, String gender, String registrationdate,
                              String sortby, String sortdirection, int page, int size);
}
