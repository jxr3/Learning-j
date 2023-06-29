package com.wcpdoc.exam.home.controller;

import java.math.BigDecimal;
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
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.service.ExamService;
import com.wcpdoc.exam.sys.cache.DictCache;

/**
 * 我的判卷控制层
 */
@Controller
@RequestMapping("/home/myMark")
public class HomeMyMarkController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(HomeMyMarkController.class);
	
	@Resource
	private ExamService examService;
	
	/**
	 * 到达考试列表页面
	 */
	@RequestMapping("/toExamList")
	public String toExamList(Model model) {
		try {
			model.addAttribute("STATE_DICT", DictCache.getIndexDictlistMap().get("STATE"));
			return "home/myMark/examList";
		} catch (Exception e) {
			log.error("到达考试列表页面错误：", e);
			return "home/myMark/examList";
		}
	}
	
	/**
	 * 考试列表
	 */
	@RequestMapping("/examList")
	@ResponseBody
	public PageOut examList(PageIn pageIn) {
		try {
			pageIn.setTen(getCurUser().getId().toString());
			pageIn.setFour("1");
			PageOut pageOut = examService.getListpage(pageIn);
			List<Map<String, Object>> list = pageOut.getRows();
			
			Date curTime = new Date();
			for(Map<String, Object> map : list){
				Date startTime = (Date) map.get("MARK_START_TIME");
				Date endTime = (Date) map.get("MARK_END_TIME");
				if (startTime.getTime() > curTime.getTime()) {
					map.put("EXAM_HAND", "AWAIT");
				} else if (startTime.getTime() <= curTime.getTime() && endTime.getTime() >= curTime.getTime()){
					map.put("EXAM_HAND", "START");
				} else {
					map.put("EXAM_HAND", "END");
				}
			}
			
			return pageOut;
		} catch (Exception e) {
			log.error("考试列表错误：", e);
			return new PageOut();
		}
	}
	
	/**
	 * 到达判卷列表页面
	 */
	@RequestMapping("/toList")
	public String toMarkList(Model model, Integer examId) {
		try {
			model.addAttribute("examId", examId);
			return "home/myMark/markList";
		} catch (Exception e) {
			log.error("到达判卷列表页面错误：", e);
			return "home/myMark/markList";
		}
	}
	
	/**
	 * 判卷列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageOut list(PageIn pageIn) {
		try {
			pageIn.setTen(getCurUser().getId().toString());
			return examService.getMarkListpage(pageIn);
		} catch (Exception e) {
			log.error("判卷列表错误：", e);
			return new PageOut();
		}
	}
	
	/**
	 * 到达判卷页面
	 */
	@RequestMapping("/toMark")
	public String toMark(Model model, Integer examUserId) {
		try {
			examService.toMark(model, getCurUser(), examUserId);
			return "home/myMark/markPaper";
		} catch (Exception e) {
			log.error("到达试卷页面错误：", e);
			model.addAttribute("message", e.getMessage());
			return "home/error";
		}
	}
	
	/**
	 * 更新判卷分数
	 */
	@RequestMapping("/updateScore")
	@ResponseBody
	public PageResult updateScore(Integer examUserQuestionId, BigDecimal score) {
		try {
			examService.updateMarkScore(getCurUser(), examUserQuestionId, score);
			return new PageResult(true, "更新成功");
		} catch (Exception e) {
			log.error("更新判卷分数错误：", e);
			return new PageResult(false, "更新失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成判卷
	 */
	@RequestMapping("/doMark")
	@ResponseBody
	public PageResult doMark(Integer examUserId) {
		try {
			examService.doMark(getCurUser(), examUserId);
			return new PageResult(true, "判卷成功");
		} catch (Exception e) {
			log.error("完成判卷错误：", e);
			return new PageResult(false, "判卷失败：" + e.getMessage());
		}
	}
}
