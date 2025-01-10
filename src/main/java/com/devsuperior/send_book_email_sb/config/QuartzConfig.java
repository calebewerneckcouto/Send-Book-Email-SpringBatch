package com.devsuperior.send_book_email_sb.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devsuperior.send_book_email_sb.job.SendBookLoanNotificationScheduleJob;

@Configuration
public class QuartzConfig {

	@Bean
	public JobDetail quartzJobDetail() {
		return JobBuilder.newJob(SendBookLoanNotificationScheduleJob.class).storeDurably().build();
	}

	@Bean
	public Trigger jobTrigger() {
		String exp = "0 38 21 * * ?";  //informar hora que vc quer que seja enviado...
		return TriggerBuilder
				.newTrigger()
				.forJob(quartzJobDetail())
				.startNow()
				.withSchedule(CronScheduleBuilder.cronSchedule(exp))
				.build();
	}
}