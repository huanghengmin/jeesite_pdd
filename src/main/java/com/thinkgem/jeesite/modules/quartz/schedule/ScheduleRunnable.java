package com.thinkgem.jeesite.modules.quartz.schedule;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.pdd.entity.PddPlatform;
import com.thinkgem.jeesite.modules.sys.entity.User;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 执行定时任务
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月30日 下午12:49:33
 */
public class ScheduleRunnable implements Runnable {
	private Object target;
	private Method method;
	private PddPlatform pddPlatform;
	private User user;
	
	public ScheduleRunnable(String beanName, String methodName, PddPlatform pddPlatform,User user) throws NoSuchMethodException, SecurityException {
		this.target = SpringContextHolder.getBean(beanName);
		this.pddPlatform = pddPlatform;
		this.user = user;
		
		if(pddPlatform!=null&&user!=null){
			this.method = target.getClass().getDeclaredMethod(methodName, PddPlatform.class ,User.class);
		}
	}

	@Override
	public void run() {
		try {
			ReflectionUtils.makeAccessible(method);
			if(pddPlatform!=null&&user!=null){
				method.invoke(target, pddPlatform,user);
			}
		}catch (Exception e) {
			throw new RRException("执行定时任务失败", e);
		}
	}

}
