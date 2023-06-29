package com.wcpdoc.exam.home.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.wcpdoc.exam.core.entity.Paper;
import com.wcpdoc.exam.core.entity.PaperQuestion;
import com.wcpdoc.exam.core.entity.PaperQuestionEx;
import com.wcpdoc.exam.core.entity.PaperType;
import com.wcpdoc.exam.core.service.PaperService;
import com.wcpdoc.exam.sys.cache.DictCache;

/**
 * 试卷控制层
 */
@Controller
@RequestMapping("/home/paper")
public class HomePaperController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(HomePaperController.class);
	
	@Resource
	private PaperService paperService;
	
	/**
	 * 到达试卷列表页面
	 */
	@RequestMapping("/toList")
	public String toList(Model model, boolean nav) {
		try {
			model.addAttribute("STATE_DICT", DictCache.getIndexDictlistMap().get("STATE"));
			model.addAttribute("nav", nav);
			return "home/paper/paperList";
		} catch (Exception e) {
			log.error("到达试卷列表页面错误：", e);
			return "home/paper/paperList";
		}
	}
	
	/**
	 * 获取试卷分类树形列表
	 */
	@RequestMapping("/paperTypeTreeList")
	@ResponseBody
	public List<Map<String, Object>> getPaperTypeTreeList() {
		try {
			return paperService.getPaperTypeTreeList(getCurUser().getId());
		} catch (Exception e) {
			log.error("获取试卷分类树错误：", e);
			return new ArrayList<Map<String,Object>>();
		}
	}
	
	/**
	 * 试卷列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageOut list(PageIn pageIn) {
		try {
			if(getCurUser().getId() != 1){
				pageIn.setEight(getCurUser().getId().toString());
			}
			return paperService.getListpage(pageIn);
		} catch (Exception e) {
			log.error("试卷列表错误：", e);
			return new PageOut();
		}
	}
	
	/**
	 * 到达添加试卷页面
	 */
	@RequestMapping("/toAdd")
	public String toAdd(Model model, Integer paperTypeId) {
		try {
			model.addAttribute("STATE_DICT", DictCache.getIndexDictlistMap().get("STATE"));
			model.addAttribute("paperType", paperService.getPaperType2(paperTypeId));
			return "home/paper/paperEdit";
		} catch (Exception e) {
			log.error("到达添加试卷页面错误：", e);
			return "home/paper/paperEdit";
		}
	}
	
	/**
	 * 完成添加试卷
	 */
	@RequestMapping("/doAdd")
	@ResponseBody
	public PageResult doAdd(Paper paper) {
		try {
			paper.setUpdateTime(new Date());
			paper.setUpdateUserId(getCurUser().getId());
			paperService.add(paper);
			return new PageResult(true, "添加成功");
		} catch (Exception e) {
			log.error("完成添加试卷错误：", e);
			return new PageResult(false, "添加失败：" + e.getMessage());
		}
	}
	
	/**
	 * 到达修改试卷页面
	 */
	@RequestMapping("/toEdit")
	public String toEdit(Model model, Integer id) {
		try {
			Paper paper = paperService.getEntity(id);
			model.addAttribute("paper", paper);
			model.addAttribute("STATE_DICT", DictCache.getIndexDictlistMap().get("STATE"));
			
			PaperType paperType = paperService.getPaperType(id);
			model.addAttribute("paperType", paperType);
			return "home/paper/paperEdit";
		} catch (Exception e) {
			log.error("到达修改试卷页面错误：", e);
			return "home/paper/paperEdit";
		}
	}
	
	/**
	 * 完成修改试卷
	 */
	@RequestMapping("/doEdit")
	@ResponseBody
	public PageResult doEdit(Paper paper) {
		try {
			Paper entity = paperService.getEntity(paper.getId());
			entity.setUpdateTime(new Date());
			entity.setUpdateUserId(getCurUser().getId());
			entity.setName(paper.getName());
			entity.setDescription(paper.getDescription());
			entity.setState(paper.getState());
			entity.setPaperTypeId(paper.getPaperTypeId());
			paperService.update(entity);
			return new PageResult(true, "修改成功");
		} catch (Exception e) {
			log.error("完成修改试卷错误：", e);
			return new PageResult(false, "修改失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成删除试卷
	 */
	@RequestMapping("/doDel")
	@ResponseBody
	public PageResult doDel(Integer[] ids) {
		try {
			paperService.delAndUpdate(ids);
			return new PageResult(true, "删除成功");
		} catch (Exception e) {
			log.error("完成删除试卷错误：", e);
			return new PageResult(false, "删除失败：" + e.getMessage());
		}
	}
	
	/**
	 * 到达配置试卷页面
	 */
	@RequestMapping("/toCfg")
	public String toCfg(Model model, Integer id) {
		try {
			Paper paper = paperService.getEntity(id);
			model.addAttribute("paper", paper);
			List<PaperQuestionEx> paperQuestionExList = paperService.getPaperList(id);
			model.addAttribute("paperQuestionExList", paperQuestionExList);
			model.addAttribute("id", id);
			return "home/paper/paperCfg";
		} catch (Exception e) {
			log.error("到达配置试卷页面错误：", e);
			return "home/paper/paperCfg";
		}
	}
	
	/**
	 * 到达添加章节页面
	 */
	@RequestMapping("/toChapterAdd")
	public String toChapterAdd(Model model, Integer id) {
		try {
			PaperQuestion chapter = new PaperQuestion();
			chapter.setPaperId(id);
			model.addAttribute("chapter", chapter);
			return "home/paper/chapterEdit";
		} catch (Exception e) {
			log.error("到达添加章节页面错误：", e);
			return "home/paper/chapterEdit";
		}
	}
	
	/**
	 * 完成添加章节
	 */
	@RequestMapping("/doChapterAdd")
	@ResponseBody
	public PageResult doChapterAdd(PaperQuestion chapter) {
		try {
			paperService.doChapterAdd(chapter, getCurUser());
			return new PageResult(true, "添加成功");
		} catch (Exception e) {
			log.error("完成添加章节错误：", e);
			return new PageResult(false, "添加失败：" + e.getMessage());
		}
	}
	
	/**
	 * 到达修改章节页面
	 */
	@RequestMapping("/toChapterEdit")
	public String toChapterEdit(Model model, Integer chapterId) {
		try {
			PaperQuestion chapter = paperService.getPaperQuestion(chapterId);
			model.addAttribute("chapter", chapter);
			return "home/paper/chapterEdit";
		} catch (Exception e) {
			log.error("到达修改章节页面错误：", e);
			return "home/paper/chapterEdit";
		}
	}
	
	/**
	 * 完成修改章节
	 */
	@RequestMapping("/doChapterEdit")
	@ResponseBody
	public PageResult doChapterEdit(PaperQuestion chapter) {
		try {
			paperService.doChapterEdit(chapter, getCurUser());
			return new PageResult(true, "修改成功");
		} catch (Exception e) {
			log.error("完成修改章节错误：", e);
			return new PageResult(false, "修改失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成删除章节
	 */
	@RequestMapping("/doChapterDel")
	@ResponseBody
	public PageResult doChapterDel(Model model, Integer chapterId) {
		try {
			paperService.doChapterDel(chapterId);
			return new PageResult(true, "删除成功");
		} catch (Exception e) {
			log.error("完成删除章节错误：", e);
			return new PageResult(false, "删除失败：" + e.getMessage());
		}
	}
	
	/**
	 * 到达添加试题页面
	 */
	@RequestMapping("/toQuestionAdd")
	public String toQuestionAdd(Model model, Integer id) {
		try {
			model.addAttribute("id", id);
			model.addAttribute("QUESTION_TYPE_DICT", DictCache.getIndexDictlistMap().get("QUESTION_TYPE"));
			model.addAttribute("QUESTION_DIFFICULTY_DICT", DictCache.getIndexDictlistMap().get("QUESTION_DIFFICULTY"));
			model.addAttribute("STATE_DICT", DictCache.getIndexDictlistMap().get("STATE"));
			return "home/paper/questionAdd";
		} catch (Exception e) {
			log.error("到达添加试题页面错误：", e);
			return "home/paper/questionAdd";
		}
	}
	
	/**
	 * 获取试题分类数据
	 */
	@RequestMapping("/questionTypeTreeList")
	@ResponseBody
	public List<Map<String, Object>> questionTypeTreeList() {
		try {
			return paperService.getQuestionTypeTreeList(getCurUser().getId());
		} catch (Exception e) {
			log.error("获取试题分类树错误：", e);
			return new ArrayList<Map<String,Object>>();
		}
	}
	
	/**
	 * 试题列表
	 */
	@RequestMapping("/questionList")
	@ResponseBody
	public PageOut questionList(PageIn pageIn) {
		try {
			if(getCurUser().getId() != 1){
				pageIn.setEight(getCurUser().getId().toString());
			}
			return paperService.getQuestionListpage(pageIn);
		} catch (Exception e) {
			log.error("试题列表错误：", e);
			return new PageOut();
		}
	}
	
	/**
	 * 完成添加试题
	 */
	@RequestMapping("/doQuestionAdd")
	@ResponseBody
	public PageResult doQuestionAdd(Integer chapterId, Integer[] questionIds) {
		try {
			paperService.doQuestionAdd(chapterId, questionIds, getCurUser());
			return new PageResult(true, "添加成功");
		} catch (Exception e) {
			log.error("完成添加试题错误：", e);
			return new PageResult(false, "添加失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成清空试题
	 */
	@RequestMapping("/doQuestionClear")
	@ResponseBody
	public PageResult doQuestionClear(Integer chapterId) {
		try {
			paperService.doQuestionClear(chapterId, getCurUser());
			return new PageResult(true, "添加成功");
		} catch (Exception e) {
			log.error("完成添加试题错误：", e);
			return new PageResult(false, "添加失败：" + e.getMessage());
		}
	}
	
	/**
	 * 到达设置分数页面
	 */
	@RequestMapping("/toScoresUpdate")
	public String toScoresUpdate(Model model, Integer chapterId) {
		try {
			model.addAttribute("chapterId", chapterId);
			return "home/paper/scoresUpdate";
		} catch (Exception e) {
			log.error("到达设置分数页面错误：", e);
			return "home/paper/scoresUpdate";
		}
	}
	
	/**
	 * 完成设置分数
	 */
	@RequestMapping("/doScoresUpdate")
	@ResponseBody
	public PageResult doScoresUpdate(Integer chapterId, BigDecimal score, String options) {
		try {
			paperService.doScoresUpdate(chapterId, score, options);
			return new PageResult(true, "设置成功");
		} catch (Exception e) {
			log.error("完成设置分数错误：", e);
			return new PageResult(false, "设置失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成章节上移
	 */
	@RequestMapping("/doChapterUp")
	@ResponseBody
	public PageResult doChapterUp(Integer chapterId) {
		try {
			paperService.doChapterUp(chapterId);
			return new PageResult(true, "移动成功");
		} catch (Exception e) {
			log.error("完成章节上移错误：", e);
			return new PageResult(false, "移动失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成章节下移
	 */
	@RequestMapping("/doChapterDown")
	@ResponseBody
	public PageResult doChapterDown(Integer chapterId) {
		try {
			paperService.doChapterDown(chapterId);
			return new PageResult(true, "移动成功");
		} catch (Exception e) {
			log.error("完成章节下移错误：", e);
			return new PageResult(false, "移动失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成设置分数
	 */
	@RequestMapping("/doScoreUpdate")
	@ResponseBody
	public PageResult doScoreUpdate(Integer paperQuestionId, BigDecimal score, String options) {
		try {
			paperService.doScoreUpdate(paperQuestionId, score, options);
			return new PageResult(true, "设置成功");
		} catch (Exception e) {
			log.error("完成设置分数错误：", e);
			return new PageResult(false, "设置失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成试题上移
	 */
	@RequestMapping("/doQuestionUp")
	@ResponseBody
	public PageResult doQuestionUp(Integer paperQuestionId) {
		try {
			paperService.doQuestionUp(paperQuestionId);
			return new PageResult(true, "移动成功");
		} catch (Exception e) {
			log.error("完成试题上移错误：", e);
			return new PageResult(false, "移动失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成试题下移
	 */
	@RequestMapping("/doQuestionDown")
	@ResponseBody
	public PageResult doQuestionDown(Integer paperQuestionId) {
		try {
			paperService.doQuestionDown(paperQuestionId);
			return new PageResult(true, "移动成功");
		} catch (Exception e) {
			log.error("完成试题下移错误：", e);
			return new PageResult(false, "移动失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成试题删除
	 */
	@RequestMapping("/doQuestionDel")
	@ResponseBody
	public PageResult doQuestionDel(Integer paperQuestionId) {
		try {
			paperService.doQuestionDel(paperQuestionId);
			return new PageResult(true, "删除成功");
		} catch (Exception e) {
			log.error("完成试题删除错误：", e);
			return new PageResult(false, "删除失败：" + e.getMessage());
		}
	}
}
