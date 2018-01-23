package com.liyang.service;

import com.liyang.domain.base.AbstractAuditorAct.ActGroup;
import com.liyang.domain.base.ActRepository;
import com.liyang.domain.base.AuditorEntityRepository;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.capitalproduct.Capitalproduct;
import com.liyang.domain.creditcard.Creditcard;
import com.liyang.domain.vehicle.*;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.orderwdsjsh.Orderwdsjsh;
import com.liyang.domain.orderwdsjsh.OrderwdsjshRepository;
import com.liyang.domain.product.Product;
import com.liyang.domain.productstorefee.ProductstorefeeRepository;
import com.liyang.domain.role.RoleRepository;
import com.liyang.domain.vehicle.*;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObject.Level;
import com.liyang.util.SerialNumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;

import static com.liyang.domain.capitalproduct.Capitalproduct.RepaymentMethodCode.BEFORE_INTEREST_AFTER_PRINCIPAL;

@Service
@Order(38)
public class VehicleService extends AbstractWorkflowService<Vehicle, VehicleWorkflow, VehicleAct, VehicleState, VehicleLog, VehicleFile> {


    @Autowired
    VehicleActRepository vehicleActRepository;

    @Autowired
    VehicleStateRepository vehicleStateRepository;

    @Autowired
    VehicleLogRepository vehicleLogRepository;

    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    VehicleService vehicleService;
    //
    @Autowired
    VehicleWorkflowRepository vehicleWorkflowRepository;

    @Override
    @PostConstruct
    public void sqlInit() {
        long count = vehicleActRepository.count();
        if (count == 0) {

            VehicleAct save1 = vehicleActRepository.save(new VehicleAct("创建", "create", 10, ActGroup.UPDATE));
            VehicleAct save2 = vehicleActRepository.save(new VehicleAct("读取", "read", 20, ActGroup.READ));
            VehicleAct save3 = vehicleActRepository.save(new VehicleAct("更新", "update", 30, ActGroup.UPDATE));
            VehicleAct save4 = vehicleActRepository.save(new VehicleAct("删除", "delete", 40, ActGroup.UPDATE));
            VehicleAct save5 = vehicleActRepository.save(new VehicleAct("自己的列表", "listOwn", 50, ActGroup.READ));
            VehicleAct save6 = vehicleActRepository.save(new VehicleAct("部门的列表", "listOwnDepartment", 60, ActGroup.READ));
            VehicleAct save7 = vehicleActRepository
                    .save(new VehicleAct("部门和下属部门的列表", "listOwnDepartmentAndChildren", 70, ActGroup.READ));
            VehicleAct save8 = vehicleActRepository.save(new VehicleAct("通知其他人", "noticeOther", 80, ActGroup.NOTICE));
            VehicleAct save9 = vehicleActRepository.save(new VehicleAct("通知操作者", "noticeActUser", 90, ActGroup.NOTICE));
            VehicleAct save10 = vehicleActRepository.save(new VehicleAct("通知展示人", "noticeShowUser", 100, ActGroup.NOTICE));

            VehicleState newState = new VehicleState("已创建", 0, "CREATED");
            newState = vehicleStateRepository.save(newState);

            VehicleState enableState = new VehicleState("有效", 100, "ENABLED");
            enableState.setActs(new HashSet<>(Arrays.asList(save1, save2, save3, save4, save5, save6, save7)));
            vehicleStateRepository.save(enableState);
            vehicleStateRepository.save(new VehicleState("无效", 200, "DISABLED"));
            vehicleStateRepository.save(new VehicleState("已删除", 300, "DELETED"));

        }

    }

    public Vehicle saveByOrdercdd(Ordercdd ordercdd) {
        Vehicle vehicle = vehicleRepository.findVehicleByPlateNumber(ordercdd.getApplyPlateNumber());
        if (vehicle == null) {
            vehicle = new Vehicle();
        }
        vehicle.setRegisterRegionName(ordercdd.getApplyRegisterReigionName());
        vehicle.setPlateNumber(ordercdd.getApplyPlateNumber());
        vehicle.setVehicleModel(ordercdd.getApplyVehicleModel());
        vehicle.setGPSCode(ordercdd.getGPSCode());
        vehicle.setAnnualSurveyMaturity(ordercdd.getApplyAnnualSurveyMaturity());
        vehicle.setVehicleDate(ordercdd.getVehicleDate());
        vehicle.setVehicleKm(ordercdd.getVehicleKm());
        vehicle.setVehicleProvince(ordercdd.getVehicleProvince());
        vehicle.setVehicleCity(ordercdd.getVehicleCity());
        vehicle.setUser(ordercdd.getUser());
        vehicle.setPerson(ordercdd.getPerson());
        vehicle.setAnnualPolicyMaturity(ordercdd.getApplyAnnualPolicyMaturity());

        vehicle.setState(vehicleStateRepository.findByStateCode("ENABLED"));
        return vehicleRepository.save(vehicle);
    }

    @Override
    public LogRepository<VehicleLog> getLogRepository() {
        // TODO Auto-generated method stub
        return vehicleLogRepository;
    }

    @Override
    public AuditorEntityRepository<Vehicle> getAuditorEntityRepository() {

        return vehicleRepository;
    }

    @Override
    @PostConstruct
    public void injectLogRepository() {
        new Vehicle().setLogRepository(vehicleLogRepository);

    }

    @Override
    public VehicleLog getLogInstance() {
        // TODO Auto-generated method stub
        return new VehicleLog();
    }

    @Override
    public ActRepository<VehicleAct> getActRepository() {
        // TODO Auto-generated method stub
        return vehicleActRepository;
    }

    @Override
    @PostConstruct
    public void injectEntityService() {
        new Vehicle().setService(this);

    }

    @Override
    public VehicleFile getFileLogInstance() {
        // TODO Auto-generated method stub
        return new VehicleFile();
    }
}
