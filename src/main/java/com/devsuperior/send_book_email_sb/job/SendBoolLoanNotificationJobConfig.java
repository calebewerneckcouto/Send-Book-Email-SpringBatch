package com.devsuperior.send_book_email_sb.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendBoolLoanNotificationJobConfig {
	
	@Bean
	public Job sendBoolLoanNotificationJob(Step sendEmailUserStep,JobRepository jobRepository) {
		return new JobBuilder("sendBoolLoanNotificationJob", jobRepository)
				.start(sendEmailUserStep)
				.incrementer(new RunIdIncrementer())
				.build();
	}

}
