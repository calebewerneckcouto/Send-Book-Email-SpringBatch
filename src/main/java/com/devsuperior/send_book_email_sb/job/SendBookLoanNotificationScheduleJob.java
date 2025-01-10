package com.devsuperior.send_book_email_sb.job;

// Importa as classes necessárias para integrar o Quartz com o Spring Batch
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

// Define esta classe como uma configuração Spring
@Configuration
public class SendBookLoanNotificationScheduleJob extends QuartzJobBean {
    
    // Injeta o bean do Spring Batch que representa o Job a ser executado
    @Autowired
    private Job job;
    
    // Injeta o JobExplorer, que é usado para explorar e consultar os jobs no Spring Batch
    @Autowired
    private JobExplorer jobExplorer;
    
    // Injeta o JobLauncher, responsável por iniciar a execução dos jobs no Spring Batch
    @Autowired
    private JobLauncher jobLaucher;

    // Método que será executado pelo Quartz quando o Job for acionado
    @Override
    protected void executeInternal(JobExecutionContext context) {
        // Cria parâmetros para o Job usando o JobExplorer
        JobParameters jobParameters = new JobParametersBuilder(this.jobExplorer)
                .getNextJobParameters(this.job) // Obtém os próximos parâmetros válidos para o Job
                .toJobParameters(); // Converte para o formato de JobParameters
        
        try {
            // Executa o Job com os parâmetros criados
            this.jobLaucher.run(this.job, jobParameters);
        } catch (Exception e) {
            // Trata possíveis exceções que ocorram durante a execução do Job
            e.printStackTrace();
        }
    }
}
