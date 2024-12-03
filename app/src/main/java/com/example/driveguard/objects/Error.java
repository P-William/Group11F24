package com.example.driveguard.objects;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error implements Serializable {
    private String message;
    private String field;
}
