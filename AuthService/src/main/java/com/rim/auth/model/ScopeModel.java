package com.rim.auth.model;

import java.util.UUID;

import lombok.Data;

@Data
public class ScopeModel {


    private UUID scopeId;
    private String scopeName;
}
