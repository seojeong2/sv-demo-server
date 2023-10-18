package com.example.demo.dto;

import lombok.Data;

@Data
public class RequestData {

    public String userId;

    public RequestData(String userId) {
        this.userId = userId;
    }

}
