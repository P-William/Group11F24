package com.example.driveguard.objects;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Driver implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private int overallScore;
    private String accountCreationDate;
    public Driver(){
        this.username = "";
        this.firstName = "Guest";
        this.lastName = "";
        this.id = -1;
        this.overallScore = 0;
        this.accountCreationDate = "";
    }
}
