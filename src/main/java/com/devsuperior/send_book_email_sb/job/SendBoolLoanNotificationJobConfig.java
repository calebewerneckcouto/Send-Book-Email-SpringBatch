package com.devsuperior.send_book_email_sb.job;

// Importa as classes necessárias para configurar o Job no Spring Batch
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Marca a classe como uma configuração Spring
@Configuration
public class SendBoolLoanNotificationJobConfig {

    // Define o bean do Job que será gerenciado pelo Spring Batch
    @Bean
    public Job sendBoolLoanNotificationJob(Step sendEmailUserStep, JobRepository jobRepository) {
        // Cria um novo Job com o nome "sendBoolLoanNotificationJob" e associa ao repositório de jobs
        return new JobBuilder("sendBoolLoanNotificationJob", jobRepository)
                .start(sendEmailUserStep) // Define o Step inicial do Job
                .incrementer(new RunIdIncrementer()) // Incrementa automaticamente o ID do Job em cada execução
                .build(); // Constrói e retorna o Job
    }
}
