package com.liyang.domain.loan;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoanoverdueRepository extends JpaRepository<Loanoverdue, Integer> {
	
	@Query("select l from Loanoverdue l where l.loan.id=?1 and l.status=0")
	public Loanoverdue findEnable(Integer loanId);
	
	public Set<Loanoverdue> findByStatus(Integer status);
}
