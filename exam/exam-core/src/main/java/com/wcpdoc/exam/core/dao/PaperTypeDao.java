package com.wcpdoc.exam.core.dao;

import java.util.List;
import java.util.Map;

import com.wcpdoc.exam.core.dao.BaseDao;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.entity.PaperType;

/**
 * 试卷分类数据访问层接口
 * 
 * v1.0 zhanghc 2017-05-25 16:34:59
 */
public interface PaperTypeDao extends BaseDao<PaperType>{
	/**
	 * 获取试卷分类树
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * @return List<Map<String,Object>>
	 */
	List<Map<String, Object>> getTreeList();

	/**
	 * 移动试卷分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * @param sourceId
	 * @param targetId
	 * void
	 */
	void doMove(Integer sourceId, Integer targetId);

	/**
	 * 获取所有子试卷分类列表，包括自己
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * @param id 
	 * void
	 */
	List<PaperType> getAllSubPaperTypeList(Integer id);
	
	/**
	 * 获取试卷分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * @param name
	 * @return PaperType
	 */
	PaperType getPaperTypeByName(String name);

	/**
	 * 获取权限用户
	 * 
	 * v1.0 zhanghc 2018年5月29日下午11:26:22
	 * @param pageIn
	 * @return PageOut
	 */
	PageOut getAuthUserListpage(PageIn pageIn);

	/**
	 * 获取权限用户
	 * 
	 * v1.0 zhanghc 2018年5月29日下午11:26:22
	 * @param pageIn
	 * @return PageOut
	 */
	PageOut getAuthUserAddList(PageIn pageIn);

	/**
	 * 获取试卷分类列表
	 * 
	 * v1.0 zhanghc 2018年6月3日上午11:20:49
	 * @return List<PaperType>
	 */
	List<PaperType> getList();

	/**
	 * 获取试卷分类列表
	 * 
	 * v1.0 zhanghc 2018年6月6日下午10:03:33
	 * @param parentId
	 * @return List<PaperType>
	 */
	List<PaperType> getList(Integer parentId);
}
