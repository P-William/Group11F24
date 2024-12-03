package com.example.driveguard.objects;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorReport implements Serializable {
    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;
    private List<Error> errors;

    public ErrorReport(String type, String title, int status, String detail, String instance, List<Error> errors){
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.errors = errors;
    }
}
