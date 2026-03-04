package com.rim.auth.model;

import java.util.UUID;

import lombok.Data;


@Data
public class ResourceModel {

    private UUID resourceId;

    private String resourceName;
    
}
