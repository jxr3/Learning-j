package com.wcpdoc.exam.sys.service;

import java.util.List;
import java.util.Map;

import com.wcpdoc.exam.core.service.BaseService;
import com.wcpdoc.exam.sys.entity.Org;

/**
 * 组织机构服务层接口
 */
public interface OrgService extends BaseService<Org> {

	/**
	 * 添加组织机构
	 */
	void addAndUpdate(Org org);

	/**
	 * 修改组织机构 
	 * 
	 * v1.0 zhanghc 2016年7月9日下午11:36:59
	 * 
	 * @param org
	 *            void
	 */
	void editAndUpdate(Org org);

	/**
	 * 删除组织机构 
	 * 
	 * v1.0 zhanghc 2016-5-8上午11:00:00
	 * 
	 * @param ids
	 * void
	 */
	void delAndUpdate(Integer[] ids);

	/**
	 * 获取组织机构树 
	 * 
	 * v1.0 zhanghc 2016-5-8上午11:00:00
	 * 
	 * @return List<Map<String,Object>>
	 */
	List<Map<String, Object>> getTreeList();

	/**
	 * 移动组织机构 
	 * 
	 * v1.0 zhanghc 2016-5-8上午11:00:00
	 * 
	 * @param sourceId
	 * @param targetId
	 * void
	 */
	void doMove(Integer sourceId, Integer targetId);
}
