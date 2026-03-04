package com.rim.auth.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.rim.auth.domain.Resource;
import com.rim.auth.model.ResourceModel;

@Component
public class ResourceMapper {
	

    public ResourceModel toModel(Resource resource) {
        if (resource == null) return null;
        ResourceModel model = new ResourceModel();
        model.setResourceId(resource.getResourceId());
        model.setResourceName(resource.getResourceName());
        return model;
    }


    public Resource toDomain(ResourceModel model) {
        if (model == null) return null;
        Resource resource = new Resource();
        resource.setResourceId(model.getResourceId()); 
        resource.setResourceName(model.getResourceName());
        return resource;
    }

    public static void updateDomain(ResourceModel model, Resource resource) {
        if (model == null || resource == null) return;

        resource.setResourceName(model.getResourceName());
        resource.setUpdatedAt(LocalDateTime.now());
    }
}
