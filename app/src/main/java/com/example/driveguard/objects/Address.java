package com.example.driveguard.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
    private ServerLocation location;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
