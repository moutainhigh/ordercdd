package com.liyang;

import com.liyang.domain.loan.LoanAct;
import com.liyang.domain.loan.LoanActRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @authpr jianger
 * @Date 2018/1/9 下午4:06
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class TestJpaQuery {
    @Autowired
    EntityManager entityManager;
    @Autowired
    LoanActRepository loanActRepository;

    @Test
    public void testQuery() throws ClassNotFoundException {
        Class clazz = Class.forName("com.liyang.domain.loan.LoanLog");
        Map<String, Object> params = new HashMap<>();
//        params.put("label", "上传凭证");
//        params.put("latitude", "30.274085");
//        LoanAct log = loanActRepository.findOne(13);
//        params.put("act", log);
        //这样是错误的，act是关联的对象
        params.put("act",13);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root root = cq.from(clazz);
        cq.select(root);
//        Path<String> entity_id = root.<String>get("label");

        //where 基类的属性
        Predicate p = null;
//        Integer entityId = Integer.parseInt((String) params.get("entity_id"));
        for (Map.Entry<String, Object> map : params.entrySet()) {
            String key = map.getKey();
            Object value = map.getValue();


            Predicate p2 = cb.equal(root.get(key).get("id"), value);
            if (p != null) {
                p = cb.and(p, p2);
            } else {
                p = p2;
            }
        }
        cq.where(p);
//        List results = entityManager.createQuery(cq).getResultList();
        TypedQuery typedQuery = entityManager.createQuery(cq);
        List results = typedQuery.getResultList();

        System.out.println(results.size());
    }

}
