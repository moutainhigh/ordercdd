package com.liyang.domain.baddebt;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.liyang.domain.base.LogRepository;
import com.liyang.message.EnumOperationMessageType;

public interface BaddebtLogRepository extends LogRepository<BaddebtLog> {
	
	@Query("select b from BaddebtLog b join b.entity e where e.id = ?1 and b.messageType=?2")
	public List<BaddebtLog> findByEntityIdAndMessageType(Integer id, EnumOperationMessageType messageType);
}