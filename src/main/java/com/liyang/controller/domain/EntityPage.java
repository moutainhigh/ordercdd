package com.liyang.controller.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityPage {
	private List<Map<String, Object>> entitys;
	private int size;
	private long totalElements;
	private int totalPages;
	private int number;
	
	public EntityPage() {
		entitys = new ArrayList<>();
	}
	
	public void add(Map<String, Object> entity) {
		if (entity==null) {
			return;
		}
		entitys.add(entity);
	}
	
	public EntityPage clone() {
		EntityPage cloneObj = new EntityPage();
		cloneObj.setNumber(this.number);
		cloneObj.setTotalPages(this.totalPages);
		cloneObj.setTotalElements(this.totalElements);
		cloneObj.setSize(this.size);
		return cloneObj;
	}

	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}

	public List<Map<String, Object>> getEntitys() {
		return entitys;
	}
}
