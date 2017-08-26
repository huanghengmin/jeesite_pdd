/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.pdd.service;

import java.util.List;

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
		return super.get(id);
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
	}
	
	@Transactional(readOnly = false)
	public void delete(PddOrder pddOrder) {
		super.delete(pddOrder);
	}

    public Page<PddOrder> findPageByUser(Page<PddOrder> pddOrderPage, PddOrder pddOrder) {
		pddOrder.setPage(pddOrderPage);
		pddOrderPage.setList(pddOrderDao.findListByUser(pddOrder));
		return pddOrderPage;
    }
}
