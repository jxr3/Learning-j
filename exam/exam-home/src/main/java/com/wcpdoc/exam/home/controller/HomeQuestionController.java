package com.wcpdoc.exam.home.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wcpdoc.exam.core.controller.BaseController;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.entity.Question;
import com.wcpdoc.exam.core.entity.QuestionType;
import com.wcpdoc.exam.core.service.QuestionService;
import com.wcpdoc.exam.file.entity.FileEx;
import com.wcpdoc.exam.sys.cache.DictCache;

/**
 * 试题控制层
 */
@Controller
@RequestMapping("/home/question")
public class HomeQuestionController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(HomeQuestionController.class);

	@Resource
	private QuestionService questionService;
	
	/**
	 * 到达试题列表页面
	 */
	@RequestMapping("/toList")
	public String toList(Model model, boolean nav) {
		try {
			model.addAttribute("QUESTION_TYPE_DICT", DictCache.getIndexDictlistMap().get("QUESTION_TYPE"));
			model.addAttribute("QUESTION_DIFFICULTY_DICT", DictCache.getIndexDictlistMap().get("QUESTION_DIFFICULTY"));
			model.addAttribute("STATE_DICT", DictCache.getIndexDictlistMap().get("STATE"));
			model.addAttribute("nav", nav);
			return "home/question/questionList";
		} catch (Exception e) {
			log.error("到达试题列表页面错误：", e);
			return "home/question/questionList";
		}
	}
	
	/**
	 * 获取试题分类数据
	 */
	@RequestMapping("/questionTypeTreeList")
	@ResponseBody
	public List<Map<String, Object>> questionTypeTreeList() {
		try {
			return questionService.getQuestionTypeTreeList(getCurUser().getId());
		} catch (Exception e) {
			log.error("获取试题分类树错误：", e);
			return new ArrayList<Map<String,Object>>();
		}
	}
	
	/**
	 * 完成临时上传附件
	 */
	@RequestMapping("/doTempUpload")
	@ResponseBody
	public Map<String, Object> doTempUpload(@RequestParam("files") MultipartFile[] files) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			String[] allowTypes = { "jpg", "gif", "png", "mp4", "JPG", "GIF", "PNG", "MP4"};
			String fileIds = questionService.doTempUpload(files, allowTypes, getCurUser(), request.getRemoteAddr());
			data.put("error", 0);
			data.put("url", "doDownload?fileId=" + fileIds);
			return data;
		} catch (Exception e) {
			log.error("完成临时上传附件失败：{}", e.getMessage());
			data.put("error", 1);
			data.put("message", e.getMessage());
			return data;
		}
	}
	
	/**
	 * 完成下载附件
	 */
	@RequestMapping(value = "/doDownload")
	public void doDownload(Integer fileId) {
		OutputStream output = null;
		try {
			FileEx fileEx = questionService.getFileEx(fileId);
			String fileName = new String((fileEx.getEntity().getName() + "." + fileEx.getEntity().getExtName()).getBytes("UTF-8"), "ISO-8859-1");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.setContentType("application/force-download");

			output = response.getOutputStream();
			FileUtils.copyFile(fileEx.getFile(), output);
		} catch (Exception e) {
			log.error("完成下载附件失败：", e);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
	
	/**
	 * 试题列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageOut list(PageIn pageIn) {
		try {
			if(getCurUser().getId() != 1){
				pageIn.setEight(getCurUser().getId().toString());
			}
			return questionService.getListpage(pageIn);
		} catch (Exception e) {
			log.error("试题列表错误：", e);
			return new PageOut();
		}
	}
	
	/**
	 * 到达添加试题页面
	 */
	@RequestMapping("/toAdd")
	public String toAdd(Model model, Integer questionTypeId) {
		try {
			model.addAttribute("QUESTION_TYPE_DICT", DictCache.getIndexDictlistMap().get("QUESTION_TYPE"));
			model.addAttribute("QUESTION_DIFFICULTY_DICT", DictCache.getIndexDictlistMap().get("QUESTION_DIFFICULTY"));
			model.addAttribute("STATE_DICT", DictCache.getIndexDictlistMap().get("STATE"));
			model.addAttribute("questionType", questionService.getQuestionType2(questionTypeId));
			return "home/question/questionEdit";
		} catch (Exception e) {
			log.error("到达添加试题页面错误：", e);
			return "home/question/questionEdit";
		}
	}
	
	/**
	 * 完成添加试题
	 */
	@RequestMapping("/doAdd")
	@ResponseBody
	public PageResult doAdd(Question question, String[] answer) {
		try {
			questionService.addAndUpdate(question, answer, getCurUser(), request.getRemoteAddr());
			return new PageResult(true, "添加成功");
		} catch (Exception e) {
			log.error("完成添加试题错误：", e);
			return new PageResult(false, "添加失败：" + e.getMessage());
		}
	}
	
	/**
	 * 到达修改试题页面
	 */
	@RequestMapping("/toEdit")
	public String toEdit(Model model, Integer id) {
		try {
			Question question = questionService.getEntity(id);
			model.addAttribute("question", question);
			
			QuestionType questionType = questionService.getQuestionType(id);
			model.addAttribute("questionType", questionType);
			
			model.addAttribute("QUESTION_TYPE_DICT", DictCache.getIndexDictlistMap().get("QUESTION_TYPE"));
			model.addAttribute("QUESTION_DIFFICULTY_DICT", DictCache.getIndexDictlistMap().get("QUESTION_DIFFICULTY"));
			model.addAttribute("STATE_DICT", DictCache.getIndexDictlistMap().get("STATE"));
			
			return "home/question/questionEdit";
		} catch (Exception e) {
			log.error("到达修改试题页面错误：", e);
			return "home/question/questionEdit";
		}
	}
	
	/**
	 * 完成修改试题
	 */
	@RequestMapping("/doEdit")
	@ResponseBody
	public PageResult doEdit(Question question, String[] answer, boolean newVer) {
		try {
			questionService.updateAndUpdate(question, answer, newVer);
			return new PageResult(true, "修改成功");
		} catch (Exception e) {
			log.error("完成修改试题错误：", e);
			return new PageResult(false, "修改失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成删除试题
	 */
	@RequestMapping("/doDel")
	@ResponseBody
	public PageResult doDel(Integer[] ids) {
		try {
			questionService.delAndUpdate(ids);
			return new PageResult(true, "删除成功");
		} catch (Exception e) {
			log.error("完成删除试题错误：", e);
			return new PageResult(false, "删除失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成word导入试题
	 */
	@RequestMapping("/doWordImp")
	@ResponseBody
	public PageResult doWordImp(@RequestParam("files") MultipartFile file, Integer questionTypeId) {
		try {
			questionService.doWordImp(file, questionTypeId);
			return new PageResult(true, "导入成功");
		} catch (Exception e) {
			log.error("完成导入试题错误：", e);
			return new PageResult(false, "导入失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成word模板导出
	 */
	@RequestMapping(value = "/doWordTemplateExport")
	public void doWordTemplateExport() {
		OutputStream output = null;
		try {
			java.io.File file = new java.io.File(this.getClass().getResource("/").getPath() + "res/试题模板.doc");
			
			String fileName = new String((file.getName()).getBytes("UTF-8"), "ISO-8859-1");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.setContentType("application/force-download");

			output = response.getOutputStream();
			FileUtils.copyFile(file, output);
		} catch (Exception e) {
			log.error("完成下载模板失败：", e);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
}