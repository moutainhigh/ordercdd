package com.liyang.listener;

import org.springframework.stereotype.Component;

import com.liyang.domain.storebonus.StoreBonus;
import com.liyang.domain.storebonus.StoreBonusAct;
import com.liyang.domain.storebonus.StoreBonusFile;
import com.liyang.domain.storebonus.StoreBonusLog;
import com.liyang.domain.storebonus.StoreBonusState;
import com.liyang.domain.storebonus.StoreBonusWorkflow;

@Component
public class StoreBonusRestEventListener  extends WorkflowRestEventListener<StoreBonus,StoreBonusWorkflow,StoreBonusAct,StoreBonusState,StoreBonusLog,StoreBonusFile> {

}
