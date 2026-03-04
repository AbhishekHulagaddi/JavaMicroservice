package com.rim.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rim.auth.model.ResourceModel;
import com.rim.auth.service.ResourceService;
import com.rim.auth.utils.WebConstantUrl;


@RestController
@RequestMapping(WebConstantUrl.RESOURCE)
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    // ============================================
    // CREATE ROLE
    // ============================================
    @PostMapping(WebConstantUrl.CREATE)
    public Map<String, Object> createResource(@RequestBody ResourceModel resourceModel) {
        return resourceService.createResource(resourceModel);
    }

    // ============================================
    // UPDATE ROLE
    // ============================================
    @PostMapping(WebConstantUrl.UPDATE)
    public Map<String, Object> updateResource(@RequestBody ResourceModel resourceModel) {
        return resourceService.updateResource(resourceModel);
    }

    // ============================================
    // DELETE ROLE
    // ============================================
    @PostMapping(WebConstantUrl.DELETE)
    public Map<String, Object> deleteResource(@RequestBody ResourceModel resourceModel) {
        return resourceService.deleteResource(resourceModel);
    }

    // ============================================
    // GET ROLE BY ID
    // ============================================
    @PostMapping(WebConstantUrl.GET_BY_ID)
    public Map<String, Object> getResourceById(@RequestBody ResourceModel resourceModel) {
        return resourceService.getResourceByResourceId(resourceModel);
    }

    // ============================================
    // GET ALL ROLES
    // ============================================
    @PostMapping(WebConstantUrl.GET_ALL)
    public Map<String, Object> getAllResources() {
        return resourceService.getAllResources();
    }
}
