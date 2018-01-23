package com.liyang.domain.feebackup;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.liyang.domain.feebackup.Feebackup.BACKUPSOURCE;

public interface FeebackupRepository extends JpaRepository<Feebackup, Integer> {
	
	@Modifying
	@Transactional
	@Query("delete from Feebackup f where f.ordercdd.id = ?1 and f.backupsource = ?2")
	public void deleteByOrdercdd(Integer ordercddId, BACKUPSOURCE backupsource);
}
