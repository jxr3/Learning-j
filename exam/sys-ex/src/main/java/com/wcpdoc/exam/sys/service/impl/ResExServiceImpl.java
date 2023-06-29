package com.wcpdoc.exam.sys.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wcpdoc.exam.sys.entity.Res;
import com.wcpdoc.exam.sys.service.PostResService;
import com.wcpdoc.exam.sys.service.ResExService;

/**
 * 资源扩展服务层实现
 */
@Service
public class ResExServiceImpl implements ResExService {

	@Resource
	private PostResService postResService;

	@Override
	public void delAndUpdate(Res res) {
		postResService.delByResId(res.getId());
	}
}
