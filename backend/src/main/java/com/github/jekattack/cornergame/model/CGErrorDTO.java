package com.github.jekattack.cornergame.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Data
public class CGErrorDTO {
    private String message;
    private List<String> subMessages = new ArrayList<>();

    public CGErrorDTO(String message, IllegalStateException e){
        this.message = message;
        subMessages = List.of(e.getMessage());
    }

    public CGErrorDTO(String message, NoSuchElementException e){
        this.message = message;
        subMessages = List.of(e.getMessage());
    }

    public CGErrorDTO(Exception e){
        this.message = "Unknown Error occured";
        subMessages = List.of(e.getMessage());
    }

    public CGErrorDTO(String message, String ...subMessages){
        this.message = message;
        Collections.addAll(this.subMessages, subMessages);
    }
}