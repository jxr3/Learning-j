package com.wcpdoc.exam.home.controller;

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
import com.wcpdoc.exam.core.entity.ExamType;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.service.ExamTypeService;
import com.wcpdoc.exam.core.util.ValidateUtil;
import com.wcpdoc.exam.sys.entity.User;

/**
 * 考试分类控制层
 */
@Controller
@RequestMapping("/home/examType")
public class HomeExamTypeController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(HomeExamTypeController.class);
	
	@Resource
	private ExamTypeService examTypeService;
	
	/**
	 * 到达考试分类列表页面
	 */
	@RequestMapping("/toList")
	public String toList() {
		try {
			return "home/examType/examTypeList";
		} catch (Exception e) {
			log.error("到达考试分类列表页面错误：", e);
			return "home/examType/examTypeList";
		}
	}
	
	/**
	 * 获取考试分类树
	 */
	@RequestMapping("/treeList")
	@ResponseBody
	public List<Map<String, Object>> treeList() {
		try {
			return examTypeService.getTreeList();
		} catch (Exception e) {
			log.error("获取考试分类树错误：", e);
			return new ArrayList<Map<String,Object>>();
		}
	}
	
	/**
	 * 考试分类列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageOut list(PageIn pageIn) {
		try {
			return examTypeService.getListpage(pageIn);
		} catch (Exception e) {
			log.error("考试分类列表错误：", e);
			return new PageOut();
		}
	}
	
	/**
	 * 到达添加考试分类页面
	 */
	@RequestMapping("/toAdd")
	public String toAdd(Model model, Integer parentId) {
		try {
			model.addAttribute("parent", examTypeService.getEntity(parentId));
			return "home/examType/examTypeEdit";
		} catch (Exception e) {
			log.error("到达添加考试分类页面错误", e);
			return "home/examType/examTypeEdit";
		}
	}
	
	/**
	 * 完成添加考试分类
	 */
	@RequestMapping("/doAdd")
	@ResponseBody
	public PageResult doAdd(ExamType examType) {
		try {
			examType.setUpdateUserId(getCurUser().getId());
			examType.setUpdateTime(new Date());
			examType.setState(1);
			examTypeService.addAndUpdate(examType);
			return new PageResult(true, "添加成功");
		} catch (Exception e) {
			log.error("完成添加考试分类错误：", e);
			return new PageResult(false, "添加失败：" + e.getMessage());
		}
	}
	
	/**
	 * 到达修改考试分类页面
	 */
	@RequestMapping("/toEdit")
	public String toEdit(Model model, Integer id) {
		try {
			ExamType examType = examTypeService.getEntity(id);
			model.addAttribute("examType", examType);
			ExamType parent = examTypeService.getEntity(examType.getParentId());
			if(parent != null){
				model.addAttribute("parent", examTypeService.getEntity(examType.getParentId()));
			}
			return "home/examType/examTypeEdit";
		} catch (Exception e) {
			log.error("到达修改考试分类页面错误", e);
			return "home/examType/examTypeEdit";
		}
	}
	
	/**
	 * 完成修改考试分类
	 */
	@RequestMapping("/doEdit")
	@ResponseBody
	public PageResult doEdit(ExamType examType) {
		try {
			ExamType entity = examTypeService.getEntity(examType.getId());
			entity.setName(examType.getName());
			entity.setUpdateTime(new Date());
			entity.setUpdateUserId(((User)getCurUser()).getId());
			entity.setNo(examType.getNo());
			examTypeService.editAndUpdate(entity);
			return new PageResult(true, "修改成功");
		} catch (Exception e) {
			log.error("完成修改考试分类错误：", e);
			return new PageResult(false, "修改失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成删除考试分类
	 */
	@RequestMapping("/doDel")
	@ResponseBody
	public PageResult doDel(Integer[] ids) {
		try {
			examTypeService.delAndUpdate(ids);
			return new PageResult(true, "删除成功");
		} catch (Exception e) {
			log.error("完成删除考试分类错误：", e);
			return new PageResult(false, "删除失败：" + e.getMessage());
		}
	}
	
	/**
	 * 到达权限列表页面
	 */
	@RequestMapping("/toAuth")
	public String toAuth(Model model, Integer id) {
		try {
			model.addAttribute("id", id);
			return "home/examType/examTypeAuthList";
		} catch (Exception e) {
			log.error("到达权限列表页面错误：", e);
			return "home/examType/examTypeAuthList";
		}
	}
	
	/**
	 * 获取组织机构树
	 */
	@RequestMapping("/authUserOrgTreeList")
	@ResponseBody
	public List<Map<String, Object>> authUserOrgTreeList() {
		try {
			return examTypeService.getOrgTreeList();
		} catch (Exception e) {
			log.error("获取组织机构树错误：", e);
			return new ArrayList<Map<String,Object>>();
		}
	}
	
	/**
	 * 权限用户列表
	 */
	@RequestMapping("/authUserList")
	@ResponseBody
	public PageOut authUserList(PageIn pageIn) {
		try {
			return examTypeService.getAuthUserListpage(pageIn);
		} catch (Exception e) {
			log.error("权限用户列表错误：", e);
			return new PageOut();
		}
	}
	
	/**
	 * 到达添加权限用户列表页面
	 */
	@RequestMapping("/toAuthUserAddList")
	public String toAuthUserAddList(Model model, Integer id) {
		try {
			model.addAttribute("id", id);
			return "home/examType/examTypeAuthUserAddList";
		} catch (Exception e) {
			log.error("到达添加权限用户列表页面错误：", e);
			return "home/examType/examTypeAuthUserAddList";
		}
	}
	
	/**
	 * 权限用户添加列表
	 */
	@RequestMapping("/authUserAddList")
	@ResponseBody
	public PageOut authUserAddList(PageIn pageIn) {
		try {
			return examTypeService.getAuthUserAddList(pageIn);
		} catch (Exception e) {
			log.error("权限用户添加列表错误：", e);
			return new PageOut();
		}
	}
	
	/**
	 * 完成添加权限用户
	 */
	@RequestMapping("/doAuthUserAdd")
	@ResponseBody
	public PageResult doAuthUserAdd(Integer id, Integer[] userIds, boolean syn2Sub) {
		try {
			examTypeService.doAuthUserAdd(id, userIds, syn2Sub, getCurUser());
			return new PageResult(true, "添加成功");
		} catch (Exception e) {
			log.error("完成添加权限用户错误：", e);
			return new PageResult(false, "添加失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成删除权限用户
	 */
	@RequestMapping("/doAuthUserDel")
	@ResponseBody
	public PageResult doAuthUserDel(Integer id, Integer[] userIds, boolean syn2Sub) {
		try {
			examTypeService.doAuthUserDel(id, userIds, syn2Sub, getCurUser());
			return new PageResult(true, "删除成功");
		} catch (Exception e) {
			log.error("完成删除权限用户错误：", e);
			return new PageResult(false, "删除失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成添加权限机构
	 */
	@RequestMapping("/doAuthOrgUpdate")
	@ResponseBody
	public PageResult doAuthOrgUpdate(Integer id, Integer[] orgIds, boolean syn2Sub) {
		try {
			examTypeService.doAuthOrgUpdate(id, orgIds, syn2Sub, getCurUser());
			return new PageResult(true, "添加成功");
		} catch (Exception e) {
			log.error("完成添加权限机构错误：", e);
			return new PageResult(false, "添加成功：" + e.getMessage());
		}
	}
	
	/**
	 * 获取组织机构树
	 */
	@RequestMapping("/authOrgOrgTreeList")
	@ResponseBody
	public List<Map<String, Object>> authOrgOrgTreeList(Integer id) {
		try {
			List<Map<String, Object>> orgTreeList = examTypeService.getOrgTreeList();
			ExamType examType = examTypeService.getEntity(id);
			if(examType == null){
				return orgTreeList;
			}
			
			String orgIds = examType.getOrgIds();
			if(!ValidateUtil.isValid(orgIds)){
				return orgTreeList;
			}
			
			for(Map<String, Object> map : orgTreeList){
				String orgId = map.get("ID").toString();
				if(orgIds.contains("," + orgId + ",")){
					map.put("CHECKED", true);
				}
			}
			return orgTreeList;
		} catch (Exception e) {
			log.error("获取组织机构树错误：", e);
			return new ArrayList<Map<String,Object>>();
		}
	}
	
	/**
	 * 获取机构岗位树
	 */
	@RequestMapping("/authPostOrgTreeList")
	@ResponseBody
	public List<Map<String, Object>> authPostOrgTreeList(Integer id) {
		try {
			List<Map<String, Object>> orgPostTree = examTypeService.getOrgPostTreeList();
			ExamType examType = examTypeService.getEntity(id);
			if(examType == null){
				return orgPostTree;
			}
			
			String postIds = examType.getPostIds();
			if(!ValidateUtil.isValid(postIds)){
				return orgPostTree;
			}
			
			for(Map<String, Object> map : orgPostTree){
				String type = map.get("TYPE").toString();
				if(!"POST".equals(type)){
					continue;
				}
				
				String postId = map.get("ID").toString();
				if(postIds.contains("," + postId + ",")){
					map.put("CHECKED", true);
				}
			}
			
			return orgPostTree;
		} catch (Exception e) {
			log.error("获取机构岗位树错误：", e);
			return new ArrayList<Map<String,Object>>();
		}
	}
	
	/**
	 * 完成添加权限岗位
	 */
	@RequestMapping("/doAuthPostUpdate")
	@ResponseBody
	public PageResult doAuthPostUpdate(Integer id, Integer[] postIds, boolean syn2Sub) {
		try {
			examTypeService.doAuthPostUpdate(id, postIds, syn2Sub, getCurUser());
			return new PageResult(true, "添加成功");
		} catch (Exception e) {
			log.error("完成添加权限岗位错误：", e);
			return new PageResult(false, "添加成功：" + e.getMessage());
		}
	}
}
