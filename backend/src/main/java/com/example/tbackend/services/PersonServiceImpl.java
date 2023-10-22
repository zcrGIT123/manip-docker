package com.example.tbackend.services;

import com.example.tbackend.DTO.request.AddPersonRequest;
import com.example.tbackend.models.Person;
import com.example.tbackend.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonServiceImpl implements  PersonService{

    @Autowired
    PersonRepository personRepo;
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Person add(AddPersonRequest request) {
        Person person = new Person();
        person.setBirthdate(request.getBirthdate());
        person.setFirstname(request.getFirstname());
        person.setLastname(request.getLastname());
        person.setGender(request.getGender());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        person.setRegistrationdate(LocalDate.now().format(formatter));
        return personRepo.save(person);
    }

    @Override
    public void deleteById(String id) {
        personRepo.deleteById(id);
    }

    @Override
    public Page<Person> getPaginated(String firstname, String lastname, String birthdate, String gender,
                                     String registrationdate, String sortby, String sortdirection, int page,
                                     int size) {

        List<Criteria> andcriterias = new ArrayList<>();
        Criteria expression = new Criteria();
        Query query = new Query();

        if (!firstname.equals("")) {
            andcriterias.add(
                    new Criteria()
                            .and("firstname")
                            .regex("(?i:.*" + escapeMetaCharacters(firstname) + ".*)")
            );
        }

        if (!lastname.equals("")) {
            andcriterias.add(
                    new Criteria()
                            .and("lastname")
                            .regex("(?i:.*" + escapeMetaCharacters(lastname) + ".*)")
            );
        }

        if (!birthdate.equals("")) {
            andcriterias.add(
                    new Criteria()
                            .and("birthdate")
                            .regex("(?i:.*" + escapeMetaCharacters(birthdate) + ".*)")
            );
        }

        if (!gender.equals("")) {
            andcriterias.add(
                    new Criteria()
                            .and("gender")
                            .is(gender)
            );
        }

        if (!registrationdate.equals("")) {
            andcriterias.add(
                    new Criteria()
                            .and("registrationdate")
                            .regex("(?i:.*" + escapeMetaCharacters(registrationdate) + ".*)")
            );
        }

        if(andcriterias.size()!=0){
            expression.andOperator(
                    andcriterias.toArray(new Criteria[andcriterias.size()])
            );
        }

        query.addCriteria(expression);

        Pageable pageable;

        if(!sortby.equals("")) {
            if (sortdirection.equals("ASC")){
                pageable = PageRequest.of(page, size, Sort.by(sortby).ascending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by(sortby).descending());
            }
        } else {
            pageable = PageRequest.of(page, size);
        }

        query.with(pageable);

        List<Person> persons = mongoTemplate.find(query, Person.class);


        return PageableExecutionUtils.getPage(
                persons,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Person.class));
    }

    public String escapeMetaCharacters(String inputString){
        final String[] metaCharacters = {"\\","^","$","{","}","[","]","(",")",".","*","+","?","|","<",">","-","&","%","#"};
        for (int i = 0 ; i < metaCharacters.length ; i++){
            if(inputString.contains(metaCharacters[i])){
                inputString = inputString.replace(metaCharacters[i],"\\"+metaCharacters[i]);
            }
        }
        inputString = transform(inputString);

        return inputString;
    }

    public String transform(String input){
        input = input.replaceAll("a|à|â|ä","[aàâä]");
        input = input.replaceAll("A|À|Â|Ä","[AÀÂÄ]");
        input = input.replaceAll("e|é|è|ê|ë","[eéèêë]");
        input = input.replaceAll("É|È|Ê|Ë|E","[ÉÈÊËE]");
        input = input.replaceAll("ï|î|i","[ïîi]");
        input = input.replaceAll("Î|Ï|I","[ÎÏI]");
        input = input.replaceAll("ô|ö|o","[ôöo]");
        input = input.replaceAll("Ô|Ö|O","[ÔÖO]");
        input = input.replaceAll("û|ù|ü|u","[ûùüu]");
        input = input.replaceAll("Ù|Û|Ü|U","[ÙÛÜU]");
        input = input.replaceAll("ÿ|y","[ÿy]");
        input = input.replaceAll("Ÿ|Y","[ŸY]");
        input = input.replaceAll("ç|c","[çc]");
        input = input.replaceAll("Ç|C","[ÇC]");
        input = input.replaceAll("æ|ae","[æae]");
        input = input.replaceAll("œ|oe","[œoe]");

        return input;
    }
}
