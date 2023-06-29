package com.wcpdoc.exam.file.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wcpdoc.exam.core.controller.BaseController;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.entity.PageResultEx;
import com.wcpdoc.exam.file.entity.FileEx;
import com.wcpdoc.exam.file.service.FileService;

/**
 * 附件控制层
 */
@Controller
@RequestMapping("/file")
public class FileController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(FileController.class);

	@Resource
	private FileService fileService;

	/**
	 * 到达附件列表页面
	 */
	@RequestMapping("/toList")
	public String toList() {
		try {
			return "file/file/fileList";
		} catch (Exception e) {
			log.error("到达附件列表页面错误：", e);
			return "file/file/fileList";
		}
	}

	/**
	 * 附件列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageOut list(PageIn pageIn) {
		try {
			return fileService.getListpage(pageIn);
		} catch (Exception e) {
			log.error("附件列表错误：", e);
			return new PageOut();
		}
	}

	/**
	 * 到达上传附件页面
	 */
	@RequestMapping("/toUpload")
	public String toUpload() {
		try {
			return "file/file/fileUpload";
		} catch (Exception e) {
			log.error("到达上传附件页面错误：", e);
			return "file/file/fileUpload";
		}
	}

	/**
	 * 完成临时上传附件
	 */
	@RequestMapping("/doTempUpload")
	@ResponseBody
	public PageResult doTempUpload(@RequestParam("files") MultipartFile[] files) {
		try {
			String[] allowTypes = { "jpg", "gif", "png", "zip", "rar", "doc", "xls", "docx", "xlsx" };
			String fileIds = fileService.doTempUpload(files, allowTypes, getCurUser(), request.getRemoteAddr());
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("fileIds", fileIds);
			return new PageResultEx(true, "完成临时上传附件成功", data);
		} catch (Exception e) {
			log.error("完成临时上传附件失败：", e);
			return new PageResult(false, "完成临时上传附件失败：" + e.getMessage());
		}
	}

	/**
	 * 完成上传附件
	 */
	@RequestMapping("/doUpload")
	@ResponseBody
	public PageResult doUpload(Integer[] ids) {
		StringBuilder message = new StringBuilder();
		try {
			for (Integer id : ids) {
				try {
					if (id == null) {
						continue;
					}
					fileService.doUpload(id, getCurUser(), request.getRemoteAddr());
				} catch (Exception e) {
					log.error("完成上传附件失败：", e);
					message.append(e.getMessage()).append("；");
				}
			}

			if (message.length() > 0) {
				throw new RuntimeException(message.toString());
			}

			return new PageResult(true, "完成上传附件成功");
		} catch (Exception e) {
			log.error("完成上传附件失败：", e);
			return new PageResult(false, "完成上传附件失败：" + message);
		}
	}

	/**
	 * 完成下载附件
	 */
	@RequestMapping(value = "/doDownload")
	public void doDownload(Integer id) {
		OutputStream output = null;
		try {
			FileEx fileEx = fileService.getEntityEx(id);
			String fileName = new String((fileEx.getEntity().getName() + "." 
					+ fileEx.getEntity().getExtName()).getBytes("UTF-8"), "ISO-8859-1");
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
	 * 完成删除附件
	 */
	@RequestMapping("/doDel")
	@ResponseBody
	public PageResult doDel(Integer[] ids) {
		try {
			fileService.delAndUpdate(ids);
			return new PageResult(true, "删除成功");
		} catch (Exception e) {
			log.error("完成删除附件错误：", e);
			return new PageResult(false, "删除失败：" + e.getMessage());
		}
	}
}
