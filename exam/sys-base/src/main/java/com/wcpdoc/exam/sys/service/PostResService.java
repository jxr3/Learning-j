package com.wcpdoc.exam.sys.service;

import com.wcpdoc.exam.core.service.BaseService;
import com.wcpdoc.exam.sys.entity.PostRes;

/**
 * 岗位资源服务层接口
 */
public interface PostResService extends BaseService<PostRes> {

	/**
	 * 根据岗位ID删除实体
	 */
	void delByPostId(Integer postId);

	/**
	 * 根据资源ID删除实体
	 */
	void delByResId(Integer resId);

}
