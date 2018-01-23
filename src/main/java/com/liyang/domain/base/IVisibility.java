package com.liyang.domain.base;

import java.util.Set;

import com.liyang.domain.role.Role;

public interface IVisibility {

	public Set<Role> getVisibleRoles();
	public void setVisibleRoles(Set<Role> visibleRoles);
}
