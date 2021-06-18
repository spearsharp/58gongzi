package com.qucai.sample.dao;

import com.qucai.sample.entity.Ewallet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface EwalletDao {
    int deleteByPrimaryKey(String t_personalewallet_ID);

    int insertSelective(Ewallet record);

    Ewallet selectByPrimaryKey(String t_personalewallet_ID);

//    FinanceProduct selectBySearch(String t_FProd_Name, Date create_time);

    int updateByPrimaryKeySelective(Ewallet record);

	int updateCompanyStaffsCreditOn(String t_TreasuryDB_OrgName);

	int updateCompanyStaffsCreditOff(String t_TreasuryDB_OrgName);

    List<Ewallet> findAllList(Map<String, Object> paramMap);

    List<Ewallet> findSearchList(Map<String, Object> paramSearchMap);


//    PageInfo<FinanceProduct> findAllList(Map<String, Object> paramMap, PageParam pp);

//  List<FinanceProductGrant> findManagerFinanceProductGrantAllList(@Param("roleId") String roleId, @Param("platform") Integer platform);

//  String findResourceChildId(String id);

//    int deleteByPrimaryKeyStr(String t_FProd_ID);

//    List<FinanceProduct> findAuthFinanceProductListByManagerId(String managerId);

    int existEwalletName(@Param("t_personalewallet_ID") String t_personalewallet_ID, @Param("t_personalewallet_ApplierPID") String t_personalewallet_ApplierPID);


    //企业端的资源
//  List<FinanceProductGrant> findEntFinanceProductGrantAllList(String roleId);
}