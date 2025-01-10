package com.devsuperior.send_book_email_sb.processor;

// Importa as classes necessárias para processar e-mail e gerenciar beans
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Importa as classes do domínio e utilitários usados
import com.devsuperior.send_book_email_sb.domain.UserBookLoan;
import com.devsuperior.send_book_email_sb.util.GenerateBookReturnDate;

// Importa as classes da biblioteca SendGrid para criação e envio de e-mails
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

// Marca a classe como uma configuração Spring
@Configuration
public class ProcessLoanNotificationEmailProcessorConfig {
	
	// Define o ItemProcessor como um Bean gerenciado pelo Spring
	@Bean
	public ItemProcessor<UserBookLoan, Mail> processLoanNotificationEmailProcessor() {
		// Retorna um ItemProcessor implementado como uma classe anônima
		return new ItemProcessor<UserBookLoan, Mail>() {
			
			// Implementa o método process que transforma um UserBookLoan em um e-mail (Mail)
			@Override
			public Mail process(UserBookLoan loan) throws Exception {
				// Cria o remetente do e-mail com endereço e nome
				Email from = new Email("calebewerneck@gmail.com", "Biblioteca Municipal");
				
				// Define o destinatário do e-mail usando o e-mail do usuário
				Email to = new Email(loan.getUser().getEmail());
				
				// Cria o conteúdo do e-mail com o texto gerado dinamicamente
				Content content = new Content("text/plain", generateEmailText(loan));
				
				// Monta o objeto Mail contendo remetente, assunto, destinatário e conteúdo
				Mail mail = new Mail(from, "Notificação devolução livro", to, content);
				
				// Simula um atraso de 1 segundo para o envio do e-mail
				Thread.sleep(1000);
				
				// Exibe no console o e-mail do destinatário (para monitoramento/log)
				System.out.println("Enviando e-mail para: " + loan.getUser().getEmail());

				// Retorna o objeto Mail pronto para ser enviado
				return mail;
			}
			
			// Método auxiliar para gerar o texto do e-mail com informações do empréstimo
			private String generateEmailText(UserBookLoan loan) {		
			    StringBuilder writer = new StringBuilder();
			    
			    // Adiciona o cabeçalho do e-mail com nome e matrícula do usuário
			    writer.append(String.format("Prezado(a), %s, matrícula %d\n", 
			    	loan.getUser().getName(), loan.getUser().getId()));
			    
			    // Adiciona informações sobre o livro e a data de devolução
			    writer.append(String.format("Informamos que o prazo de devolução do livro '%s' é amanhã (%s).\n", 
			    	loan.getBook().getName(), GenerateBookReturnDate.getDate(loan.getLoan_date())));
			    
			    // Solicitação de renovação ou devolução
			    writer.append("Solicitamos que você renove o livro ou devolva, assim que possível.\n");
			    
			    // Informações sobre o horário de funcionamento da biblioteca
			    writer.append("A Biblioteca Municipal está funcionando de segunda a sexta, das 9h às 17h.\n\n");
			    
			    // Adiciona a assinatura do e-mail
			    writer.append("Atenciosamente,\n");
			    writer.append("Setor de empréstimo e devolução\n");
			    writer.append("BIBLIOTECA MUNICIPAL");
			    
			    // Retorna o texto completo como uma String
			    return writer.toString();
			}
		};
	}
}
