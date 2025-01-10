package com.devsuperior.send_book_email_sb.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.devsuperior.send_book_email_sb.domain.UserBookLoan;
import com.sendgrid.helpers.mail.Mail;

@Configuration
public class SendEmailUserStepConfig {

    @Autowired
    @Qualifier("transactionManagerApp") // Injeta o gerenciador de transações específico para a aplicação
    private PlatformTransactionManager transactionManager;

    /**
     * Define o Step do Spring Batch responsável por enviar notificações por e-mail 
     * para usuários com empréstimos próximos ao vencimento.
     *
     * @param readUsersWithLoanCloserToReturnReader ItemReader para ler dados dos empréstimos.
     * @param processLoanNotificationEmailProcessor ItemProcessor para transformar dados do empréstimo em e-mails.
     * @param sendEmailRequestReturnWritter ItemWriter para enviar os e-mails processados.
     * @param jobRepository Repositório de trabalhos do Spring Batch.
     * @return Step configurado para executar o processamento.
     */
    @Bean
    public Step sendEmailUserStep(
            ItemReader<UserBookLoan> readUsersWithLoanCloserToReturnReader, // Leitor que busca os dados de empréstimos
            ItemProcessor<UserBookLoan, Mail> processLoanNotificationEmailProcessor, // Processador que transforma os dados em objetos de e-mail
            ItemWriter<Mail> sendEmailRequestReturnWritter, // Escritor que envia os e-mails processados
            JobRepository jobRepository) { // Repositório do Spring Batch
        return new StepBuilder("sendEmailUserStep", jobRepository) // Cria um StepBuilder com o nome "sendEmailUserStep"
                .<UserBookLoan, Mail>chunk(1, transactionManager) // Define o tamanho do chunk como 1, garantindo transação por item
                .reader(readUsersWithLoanCloserToReturnReader) // Configura o leitor de itens
                .processor(processLoanNotificationEmailProcessor) // Configura o processador de itens
                .writer(sendEmailRequestReturnWritter) // Configura o escritor de itens
                .build(); // Finaliza a configuração do Step
    }
}
