/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.pdd.service;

import java.util.List;

import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.pdd.entity.PddOrder;
import com.thinkgem.jeesite.modules.pdd.dao.PddOrderDao;


/**
 * 订单管理Service
 * @author hhm
 * @version 2017-08-22
 */
@Service
@Transactional(readOnly = true)
public class PddOrderService extends CrudService<PddOrderDao, PddOrder> {

	@Autowired
	private PddOrderDao pddOrderDao;


	public PddOrder get(String id) {
		return UserUtils.getPddOrder(id);
	}
	
	public List<PddOrder> findList(PddOrder pddOrder) {
		return super.findList(pddOrder);
	}
	
	public Page<PddOrder> findPage(Page<PddOrder> page, PddOrder pddOrder) {
		return super.findPage(page, pddOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(PddOrder pddOrder) {
		super.save(pddOrder);
		UserUtils.clearPlatformCache(pddOrder.getPddPlatform());
		UserUtils.clearOrderCache(pddOrder);
	}
	
	@Transactional(readOnly = false)
	public void delete(PddOrder pddOrder) {
		super.delete(pddOrder);
		UserUtils.clearPlatformCache(pddOrder.getPddPlatform());
		UserUtils.clearOrderCache(pddOrder);
	}

    public Page<PddOrder> findPageByUser(Page<PddOrder> pddOrderPage, PddOrder pddOrder) {
		pddOrder.setPage(pddOrderPage);
		pddOrderPage.setList(pddOrderDao.findListByUser(pddOrder));
		return pddOrderPage;
    }
	/**
	 * 更新数据
	 * @param pddOrder
	 * @return
	 */
	@Transactional(readOnly = false)
	public int updateByOrderSn(PddOrder pddOrder){
		UserUtils.clearPlatformCache(pddOrder.getPddPlatform());
		UserUtils.clearOrderCache(pddOrder);
		return pddOrderDao.updateByOrderSn(pddOrder);

	};

	/**
	 * 获取单条数据
	 * @param order_sn
	 * @return
	 */
	public PddOrder getByOrderSn(String order_sn){
		return pddOrderDao.getByOrderSn(order_sn);
	}


	public PddOrder getByLogisticCode(String logisticCode){
		return pddOrderDao.getByLogisticCode(logisticCode);
	}

	@Transactional(readOnly = false)
	public PddOrder updateByLogisticCode(PddOrder pddOrder){
		UserUtils.clearPlatformCache(pddOrder.getPddPlatform());
		UserUtils.clearOrderCache(pddOrder);
		return pddOrderDao.updateByLogisticCode(pddOrder);
	}

	public long findCountByUser(PddOrder entity){
		return pddOrderDao.findCountByUser(entity);
	}

	public long findCountByPlatform(PddOrder entity){
		return pddOrderDao.findCountByPlatform(entity);
	}

	public List<PddOrder> findListByNotSignInStatus(PddOrder pddOrder){
		return pddOrderDao.findListByNotSignInStatus(pddOrder);
	}
}
