package com.liyang.domain.orderwdxyd;

import com.liyang.domain.base.WorkflowEntityRepository;
import com.liyang.domain.orderwdsjsh.Orderwdsjsh;
import com.liyang.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

//@RepositoryRestResource(excerptProjection = UserProjection.class)
public interface OrderwdxydRepository extends WorkflowEntityRepository<Orderwdxyd> {
    @Query("select m from Orderwdxyd m JOIN m.state s where s.stateCode = :stateCode")
    public Page<Orderwdxyd> getByStateCode(@Param("stateCode") String stateCode, Pageable pageable);

    @Query("select o from Orderwdxyd o  inner join o.state state where o.applyMobile=?1 and state.stateCode=?2")
    public Orderwdxyd getByTelephoneAndStatCode(String telephone, String stateCode);

    //@Query("select o from Orderwdxyd o inner join o.createBy user where user.id=?1")
    @RestResource(exported = false)
    public Page<Orderwdxyd> getByUser(Integer uid, Pageable pageable);

    //查找用户
    //@Query("select u from User u inner join Orderwdxyd o where o.createBy.id=")
    //public User getUserByOrderwdxyd()
    @Query("select o from Orderwdxyd o where o.applyMobile=?1")
    @RestResource(exported = false)
    public Orderwdxyd getByMobile(String mobile);

    //查询没有分配的
    @Query("select o from Orderwdxyd o inner join o.state s where s.stateCode=:stateCode and o.isDistribution=0")
    public Page<Orderwdxyd> findAllByIsDistribution(@Param("stateCode") String stateCode, Pageable pageable);

    @Query("select o from Orderwdxyd o inner join o.state s where s.stateCode=:stateCode")
    public Page<Orderwdxyd> listOwnByStateCode(@Param("stateCode") @Value("ENABLED") String stateCode, Pageable pageable);
    //select m from #{#entityName} m JOIN m.state s where s.stateCode = :stateCode
    //业务员费配到自己下的业务      这个可以改成业务人员登陆的id
//		@Query("select o from Orderwdxyd o inner join o.state s where s.stateCode=:stateCode and o.isDistribution=1 and o.createdBy in  (select u.id from User u where u.serviceUserId=?#{principal.id})")
//		public Page<Orderwdxyd> getOrderwdxydByServiceUserId(@Param("stateCode")String stateCode, Pageable pageable);

    @Query("select o from Orderwdxyd o inner join o.state s where s.stateCode=:stateCode and o.isDistribution=1 and o.serviceUser.id= ?#{principal.id}")
    public Page<Orderwdxyd> getOrderwdxydByServiceUserId(@Param("stateCode") String stateCode, Pageable pageable);

    //		@Query("select o from Orderwdxyd o where o.serviceUserId=:userid and o.state.stateCode=:stateCode")
//		public Page<Orderwdxyd> getOrderwdxydByStateCodeAndUser(@Param("stateCode")String stateCode,@Param("userid")String userid, Pageable pageable);
    @Query("select o from Orderwdxyd o where o.serviceUser.id=:userid and o.state.stateCode=:stateCode")
    public Page<Orderwdxyd> getOrderwdxydByStateCodeAndUser(@Param("stateCode") String stateCode, @Param("userid") Integer userid, Pageable pageable);

    //根据申请人手机号查询
    public Orderwdxyd findByApplyMobile(String telephone);

    //根据申请人身份证号查询
    public Orderwdxyd findByApplyIdentityNo(String identityNo);

    @RestResource(exported = false)
    Orderwdxyd findFirstByCreatedBy(User user);

}