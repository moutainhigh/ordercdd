package com.liyang.domain.capitalpayment;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.liyang.domain.base.AbstractWorkflowFile;


@Entity
@Table(name="capitalpayment_file")
public class CapitalpaymentFile extends AbstractWorkflowFile<Capitalpayment,CapitalpaymentAct,CapitalpaymentLog>  {
}
