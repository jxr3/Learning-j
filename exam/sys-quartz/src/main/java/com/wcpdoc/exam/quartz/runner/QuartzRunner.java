package com.wcpdoc.exam.quartz.runner;

import java.util.List;

import javax.annotation.Resource;

import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.wcpdoc.exam.quartz.entity.Cron;
import com.wcpdoc.exam.quartz.service.CronService;
import com.wcpdoc.exam.quartz.util.QuartzUtil;

/**
 * 定时任务启动
 * 
 * v1.0 zhanghc 2019年12月16日下午11:32:55
 */
@Component
public class QuartzRunner implements ApplicationRunner {
	private static final Logger log = LoggerFactory.getLogger(QuartzRunner.class);
	
	@Resource
	private CronService cronService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<Cron> cronList = cronService.getList();
		for (Cron cron : cronList) {
			if (cron.getState() != 1) {
				continue;
			}
			
			try {
				log.info("启动定时任务【{}】开始：", cron.getName());
				@SuppressWarnings("unchecked")
				Class<Job> jobClass = (Class<Job>) Class.forName(cron.getJobClass());
				QuartzUtil.addJob(jobClass, cron.getId(), cron.getCron());
				log.info("启动定时任务【{}】完成：", cron.getName());
			} catch (Exception e) {
				log.error("启动任务失败：", e);
			}
		}
	}
}
