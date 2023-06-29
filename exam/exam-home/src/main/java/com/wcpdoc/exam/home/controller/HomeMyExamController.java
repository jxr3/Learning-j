package com.wcpdoc.exam.home.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcpdoc.exam.core.controller.BaseController;
import com.wcpdoc.exam.core.entity.ExamUser;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.service.ExamService;
import com.wcpdoc.exam.core.util.HibernateUtil;
import com.wcpdoc.exam.sys.cache.DictCache;

/**
 * 我的考试控制层
 */
@Controller
@RequestMapping("/home/myExam")
public class HomeMyExamController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(HomeMyExamController.class);
	
	@Resource
	private ExamService examService;
	
	/**
	 * 到达我的考试列表页面
	 */
	@RequestMapping("/toList")
	public String toList(Model model) {
		try {
			return "home/myExam/myExamList";
		} catch (Exception e) {
			log.error("到达我的考试列表页面错误：", e);
			return "home/myExam/myExamList";
		}
	}
	
	/**
	 * 我的考试列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageOut list(PageIn pageIn) {
		try {
			pageIn.setNine(getCurUser().getId() + "");
			PageOut listpage = examService.getListpage(pageIn);
			List<Map<String, Object>> list = listpage.getRows();
			
			Date curTime = new Date();
			for(Map<String, Object> map : list){
				ExamUser examUser = examService.getExamUser((int)map.get("ID"), getCurUser().getId());
				map.put("TOTAL_SCORE", examUser.getTotalScore());
				map.put("EXAM_USER_STATE", examUser.getState());
				
				Date startTime = (Date) map.get("START_TIME");
				Date endTime = (Date) map.get("END_TIME");
				if (startTime.getTime() > curTime.getTime()) {
					map.put("EXAM_HAND", "AWAIT");
				} else if (startTime.getTime() <= curTime.getTime() && endTime.getTime() >= curTime.getTime()){
					map.put("EXAM_HAND", "START");
				} else {
					map.put("EXAM_HAND", "END");
				}
			}
			HibernateUtil.formatDict(list, DictCache.getIndexkeyValueMap(), "EXAM_USER_STATE", "EXAM_USER_STATE");
			return listpage;
		} catch (Exception e) {
			log.error("我的考试列表错误：", e);
			return new PageOut();
		}
	}
	
	/**
	 * 到达试卷页面
	 */
	@RequestMapping("/toPaper")
	public String toPaper(Model model, Integer examId) {
		try {
			model.addAttribute("examId", examId);
			examService.toPaper(model, getCurUser(), examId);
			return "home/myExam/myExamPaper";
		} catch (Exception e) {
			log.error("到达试卷页面错误：", e);
			model.addAttribute("message", e.getMessage());
			return "home/error";
		}
	}
	
	/**
	 * 更新答案
	 */
	@RequestMapping("/updateAnswer")
	@ResponseBody
	public PageResult updateAnswer(Integer examUserQuestionId, String answer) {
		try {
			examService.updateAnswer(getCurUser(), examUserQuestionId, answer);
			return new PageResult(true, "更新成功");
		} catch (Exception e) {
			log.error("更新答案错误：", e);
			return new PageResult(false, "更新失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成试卷
	 */
	@RequestMapping("/doPaper")
	@ResponseBody
	public PageResult doPaper(Integer examUserId) {
		try {
			examService.doPaper(getCurUser(), examUserId);
			return new PageResult(true, "完成成功");
		} catch (Exception e) {
			log.error("完成试卷错误：", e);
			return new PageResult(false, "完成失败：" + e.getMessage());
		}
	}
}
