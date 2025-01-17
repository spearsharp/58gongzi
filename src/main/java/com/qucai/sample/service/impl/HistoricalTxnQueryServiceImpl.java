package com.qucai.sample.service.impl;

import java.util.List;
import java.util.Map;

import com.qucai.sample.vo.PersonalTxnStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qucai.sample.common.PageParam;
import com.qucai.sample.dao.HistoricalTxnQueryDao;
import com.qucai.sample.entity.HistoricalTxnQuery;
import com.qucai.sample.service.HistoricalTxnQueryService;

@Service("HistoricalTxnQueryService")
@Transactional	
public class HistoricalTxnQueryServiceImpl implements HistoricalTxnQueryService {

    @Autowired
    private HistoricalTxnQueryDao historicalTxnQueryDao;

/*
    @Autowired
    private TrRoleResourceDao trRoleResourceDao;
*/
    @Override
    public HistoricalTxnQuery selectByPrimaryKey(String t_Txn_ID_his) {
        return historicalTxnQueryDao.selectByPrimaryKey(t_Txn_ID_his);
    }
    
    @Override
    public int ClearHisTxnPay(String[] HisTxnSelectedIDs) {
        return historicalTxnQueryDao.ClearHisTxnPay(HisTxnSelectedIDs);
    }
    
    @Override
    public int DueHisTxnPay(String[] HisTxnSelectedIDs) {
        return historicalTxnQueryDao.DueHisTxnPay(HisTxnSelectedIDs);
    }
    
    @Override
    public HistoricalTxnQuery selectByOrderCode(String OrderCode) {
        return historicalTxnQueryDao.selectByOrderCode(OrderCode);
    }

    @Override
    public List<HistoricalTxnQuery> findAllList(Map<String, Object> paramMap) {
        return historicalTxnQueryDao.findAllList(paramMap);
    }
    
    @Override
    public List<HistoricalTxnQuery> findSearchList(Map<String, Object> paramSearchMap) {
        return historicalTxnQueryDao.findSearchList(paramSearchMap);
    }

    @Override
    public List<PersonalTxnStatic> SearchPersonalTxnStatic(Map<String, Object> paramMap) {
        return historicalTxnQueryDao.SearchPersonalTxnStatic(paramMap);
    }
    
    @Override
    public PageInfo<HistoricalTxnQuery> findAllList(Map<String, Object> paramMap, PageParam pp) {
        PageHelper.startPage(pp.getPageNum(), pp.getPageSize(), true, true);
        List<HistoricalTxnQuery> list = historicalTxnQueryDao.findAllList(paramMap);
        return new PageInfo<HistoricalTxnQuery>(list);
    }
    
    @Override
    public PageInfo<HistoricalTxnQuery> findSearchList(PageParam pp, Map<String, Object>paramSearchMap) {
        PageHelper.startPage(pp.getPageNum(), pp.getPageSize(), true, true);
        List<HistoricalTxnQuery> list = historicalTxnQueryDao.findSearchList(paramSearchMap);
        return new PageInfo<HistoricalTxnQuery>(list);
    }
    
    @Override
    public int  MarkFailedPayment(String OrderCode){
        return  historicalTxnQueryDao.MarkFailedPayment(OrderCode);
    }
    

    /*
    public List<HistoricalTxnQuery> findTreetableList(Map<String, Object> paramMap) {
        List<HistoricalTxnQuery> rList = historicalTxnQueryDao.findAllList(paramMap);
        if(CollectionUtils.isEmpty(rList)) {
        	return null;
        }else {
        	return assembleTreeList(initGroupMap(rList), "");
        }
    }
    
    @Override
    public List<HistoricalTxnQueryGrant> findGrantTreetableList(String roleId, Integer platform) {
        List<HistoricalTxnQueryGrant> rList = historicalTxnQueryDao.findManagerHistoricalTxnQueryGrantAllList(roleId, platform);
        if(CollectionUtils.isEmpty(rList)) {
        	return null;
        }else {
        	return assembleTreeList2(initGroupMap(rList), "");
        }
    }
    
    @Override
    public List<Resource> findAuthResourceListByManagerId(String managerId) {
        List<Resource> rList = resourceDao.findAuthResourceListByManagerId(managerId);
        if(CollectionUtils.isEmpty(rList)) {
        	return null;
        }else {
        	return assembleTreeList(initGroupMap(rList), "");
        }
    }
    
    private <T extends Resource> Map<String, List<T>> initGroupMap(List<T> rList){
        Map<String, List<T>> map = new HashMap<String, List<T>>();
        for (T r : rList) {
            if (StringUtils.isBlank(r.getParentId())) {
                if (map.containsKey("")) {
                    map.get("").add(r);
                } else {
                    List<T> t = new ArrayList<T>();
                    t.add(r);
                    map.put("", t);
                }
                continue;
            } else {
                if (map.containsKey(r.getParentId())) {
                    map.get(r.getParentId()).add(r);
                } else {
                    List<T> t = new ArrayList<T>();
                    t.add(r);
                    map.put(r.getParentId(), t);
                }
            }
        }
        return map;
    }

    private <T extends Resource> List<T> assembleTreeList(Map<String, List<T>> map, String key){
        List<T> rs = new ArrayList<T>();
        for(T r : map.get(key)){
            rs.add(r);
            if(map.containsKey(r.getId())){
                rs.addAll(assembleTreeList(map, r.getId()));
            }
        }
        return rs;
    }
    
    private List<ResourceGrant> assembleTreeList2(Map<String, List<ResourceGrant>> map, String key){
        List<ResourceGrant> rs = new ArrayList<ResourceGrant>();
        for(ResourceGrant r : map.get(key)){
            rs.add(r);
            if(map.containsKey(r.getId())){
                rs.addAll(assembleTreeList2(map, r.getId()));
            } else {
            	r.setIsLeaf(true);
            }
        }
        return rs;
    }
*/
    
}
