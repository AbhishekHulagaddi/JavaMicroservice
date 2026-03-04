package com.rim.auth.service;

import java.util.Map;

import com.rim.auth.model.ResourceModel;

public interface ResourceService {

	public Map<String , Object> createResource (ResourceModel resourceModel);
	
	public Map<String , Object> updateResource (ResourceModel resourceModel);
	
	public Map<String , Object> deleteResource (ResourceModel resourceModel);
	
	public Map<String , Object> getResourceByResourceId (ResourceModel resourceModel);
	
	public Map<String , Object> getAllResources ();
}
