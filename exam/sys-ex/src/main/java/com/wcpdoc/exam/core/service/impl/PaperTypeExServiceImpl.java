package com.wcpdoc.exam.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wcpdoc.exam.core.entity.Paper;
import com.wcpdoc.exam.core.entity.PaperType;
import com.wcpdoc.exam.core.service.PaperService;
import com.wcpdoc.exam.core.service.PaperTypeExService;

/**
 * 试卷分类扩展服务层实现
 */
@Service
public class PaperTypeExServiceImpl implements PaperTypeExService {

	@Resource
	private PaperService paperService;

	@Override
	public void delAndUpdate(PaperType paperType) {
		List<Paper> paperList = paperService.getList(paperType.getId());
		for (Paper paper : paperList) {
			paper.setPaperTypeId(1);
		}
	}
}
