package com.wcpdoc.exam.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wcpdoc.exam.core.entity.Exam;
import com.wcpdoc.exam.core.entity.ExamType;
import com.wcpdoc.exam.core.service.ExamService;
import com.wcpdoc.exam.core.service.ExamTypeExService;

/**
 * 考试分类扩展服务层实现
 */
@Service
public class ExamTypeExServiceImpl implements ExamTypeExService {

	@Resource
	private ExamService examService;

	@Override
	public void delAndUpdate(ExamType examType) {
		List<Exam> examList = examService.getList(examType.getId());
		for (Exam exam : examList) {
			exam.setExamTypeId(1);
		}
	}
}
