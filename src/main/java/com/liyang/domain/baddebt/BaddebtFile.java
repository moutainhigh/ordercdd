package com.liyang.domain.baddebt;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.liyang.domain.base.AbstractWorkflowFile;


@Entity
@Table(name="baddebt_file")
public class BaddebtFile extends AbstractWorkflowFile<Baddebt,BaddebtAct,BaddebtLog>  {
	
}
