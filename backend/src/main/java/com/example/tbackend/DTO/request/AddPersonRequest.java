package com.example.tbackend.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddPersonRequest {
    private String firstname;
    private String lastname;
    private String birthdate;
    private String gender;
}
