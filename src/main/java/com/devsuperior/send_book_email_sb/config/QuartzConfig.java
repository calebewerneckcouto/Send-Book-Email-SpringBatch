package com.devsuperior.send_book_email_sb.config;

// Importa as classes necessárias para configurar o Quartz Scheduler
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Importa a classe do job que será agendado
import com.devsuperior.send_book_email_sb.job.SendBookLoanNotificationScheduleJob;

// Define que esta classe é uma configuração Spring
@Configuration
public class QuartzConfig {

    // Define um bean para o JobDetail, que representa a tarefa (Job) que será executada
    @Bean
    public JobDetail quartzJobDetail() {
        return JobBuilder.newJob(SendBookLoanNotificationScheduleJob.class) // Associa o Job à classe que contém a lógica de execução
                .storeDurably() // Indica que este Job será armazenado permanentemente, mesmo que não tenha gatilhos associados
                .build(); // Constrói o JobDetail
    }

    // Define um bean para o Trigger, que especifica quando o Job será executado
    @Bean
    public Trigger jobTrigger() {
        String exp = "0 38 21 * * ?"; 
        // Expressão Cron que define a frequência de execução do Job
        // Neste exemplo, o Job será executado às 21:38 todos os dias

        return TriggerBuilder
                .newTrigger() // Cria um novo Trigger
                .forJob(quartzJobDetail()) // Associa o Trigger ao Job configurado
                .startNow() // Inicia o Trigger imediatamente após a configuração
                .withSchedule(CronScheduleBuilder.cronSchedule(exp)) // Define a frequência usando a expressão Cron
                .build(); // Constrói o Trigger
    }
}
