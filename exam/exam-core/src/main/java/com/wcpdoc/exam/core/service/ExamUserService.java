package com.wcpdoc.exam.core.service;

import com.wcpdoc.exam.core.entity.ExamUser;
import com.wcpdoc.exam.core.service.BaseService;
/**
 * 考试用户服务层接口
 * 
 * v1.0 zhanghc 2017-06-19 16:28:29
 */
public interface ExamUserService extends BaseService<ExamUser>{

	/**
	 * 删除实体
	 * 
	 * v1.0 zhanghc 2017年6月26日下午2:09:55
	 * @param examId
	 * @param id
	 * void
	 */
	void del(Integer examId, Integer id);

	/**
	 * 获取考试用户信息
	 * 
	 * v1.0 zhanghc 2017年7月3日上午9:41:59
	 * @param examId
	 * @param userId
	 * @return ExamUser
	 */
	ExamUser getEntity(Integer examId, Integer userId);

}
