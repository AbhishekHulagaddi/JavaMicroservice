package com.rim.auth.serviceimpl;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.rim.auth.domain.Resource;
import com.rim.auth.mapper.ResourceMapper;
import com.rim.auth.model.ResourceModel;
import com.rim.auth.repo.ResourceRepo;
import com.rim.auth.service.ResourceService;


@Service
public class ResourceServiceImpl implements ResourceService{


    @Autowired
    private ResourceRepo resourceRepository;
    
    @Autowired
    private ResourceMapper resourceMapper;

    private Map<String, Object> response(boolean status, String message, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("message", message);
        map.put("data", data);
        return map;
    }

    // =====================================================
    // CREATE ROLE
    // =====================================================
    @Override
    public Map<String, Object> createResource(ResourceModel resourceModel) {

        if (resourceModel == null || !StringUtils.hasText(resourceModel.getResourceName())) {
            return response(false, "Resource name is required", null);
        }

        String resourceName = resourceModel.getResourceName().trim();

        if (resourceRepository.existsByResourceNameIgnoreCase(resourceName)) {
            return response(false, "Resource already exists", null);
        }

        Resource resource = resourceMapper.toDomain(resourceModel);
        resource.setCreatedAt(LocalDateTime.now());
        resource.setActive(true);

        resourceRepository.save(resource);

        return response(true, "Resource created successfully", resourceMapper.toModel(resource));
    }

    // =====================================================
    // UPDATE ROLE
    // =====================================================
    @Override
    public Map<String, Object> updateResource(ResourceModel resourceModel) {

        if (resourceModel == null || resourceModel.getResourceId() == null) {
            return response(false, "ResourceId is required", null);
        }

        Optional<Resource> optional = resourceRepository.findById(resourceModel.getResourceId());
        if (optional.isEmpty()) {
            return response(false, "Resource not found", null);
        }

        Resource resource = optional.get();

        if (StringUtils.hasText(resourceModel.getResourceName())) {

            String newName = resourceModel.getResourceName().trim();

            Optional<Resource> existing = resourceRepository.findByResourceNameIgnoreCase(newName);
            if (existing.isPresent() && !existing.get().getResourceId().equals(resource.getResourceId())) {
                return response(false, "Another resource with same name exists", null);
            }

            resource.setResourceName(newName);
        }

        resource.setUpdatedAt(LocalDateTime.now());
        resourceRepository.save(resource);

        return response(true, "Resource updated successfully", resourceMapper.toModel(resource));
    }

    // =====================================================
    // DELETE ROLE (Soft Delete)
    // =====================================================
    @Override
    public Map<String, Object> deleteResource(ResourceModel resourceModel) {

        if (resourceModel == null || resourceModel.getResourceId() == null) {
            return response(false, "ResourceId is required", null);
        }

        Optional<Resource> optional = resourceRepository.findById(resourceModel.getResourceId());
        if (optional.isEmpty()) {
            return response(false, "Resource not found", null);
        }

        Resource resource = optional.get();
        resource.setActive(false);
        resource.setUpdatedAt(LocalDateTime.now());

        resourceRepository.save(resource);

        return response(true, "Resource deleted successfully", null);
    }

    // =====================================================
    // GET BY ID
    // =====================================================
    @Override
    public Map<String, Object> getResourceByResourceId(ResourceModel resourceModel) {

        if (resourceModel == null || resourceModel.getResourceId() == null) {
            return response(false, "ResourceId is required", null);
        }

        Optional<Resource> optional = resourceRepository.findById(resourceModel.getResourceId());
        if (optional.isEmpty()) {
            return response(false, "Resource not found", null);
        }

        return response(true, "Resource fetched", resourceMapper.toModel(optional.get()));
    }

    // =====================================================
    // GET ALL
    // =====================================================
    @Override
    public Map<String, Object> getAllResources() {

        List<Resource> resources = resourceRepository.findAll();

        List<ResourceModel> list = resources.stream()
                .filter(Resource::isActive)
                .map(resourceMapper::toModel)
                .toList();

        return response(true, "All resources fetched", list);
    }
}
