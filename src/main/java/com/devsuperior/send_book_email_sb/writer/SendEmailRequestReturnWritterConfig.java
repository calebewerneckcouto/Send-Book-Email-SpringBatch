package com.devsuperior.send_book_email_sb.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devsuperior.send_book_email_sb.domain.UserBookLoan;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;

@Configuration
public class SendEmailRequestReturnWritterConfig {

    // Logger para registrar informações sobre o processo
    private static Logger LOG = LoggerFactory.getLogger(SendEmailRequestReturnWritterConfig.class);

    @Autowired
    private SendGrid sendGrid; // Instância do SendGrid para envio de e-mails

    /**
     * Configura um ItemWriter para enviar e-mails.
     *
     * @return ItemWriter que processa uma lista de objetos Mail e envia os e-mails.
     */
    @Bean
    public ItemWriter<Mail> sendEmailRequestReturnWritter() {
        // Itera sobre cada e-mail recebido e o envia
        return items -> items.forEach(this::sendEmail);
    }

    /**
     * Envia um e-mail utilizando a API do SendGrid.
     *
     * @param email Objeto Mail que contém as informações do e-mail a ser enviado.
     */
    private void sendEmail(Mail email) {
        LOG.info("Iniciando o envio de e-mail."); // Log para indicar o início do envio

        // Objeto Request para configurar os detalhes da solicitação ao SendGrid
        Request request = new Request();
        try {
            // Define o método da requisição como POST
            request.setMethod(Method.POST);

            // Define o endpoint da API para envio de e-mails
            request.setEndpoint("mail/send");

            // Adiciona o corpo do e-mail à requisição
            request.setBody(email.build());

            // Log com informações sobre o e-mail que será enviado
            LOG.info("[WRITER STEP] Enviando e-mail para: " + email.build());

            // Realiza a chamada à API do SendGrid
            Response response = sendGrid.api(request);

            // Verifica se a resposta indica um erro
            if (response.getStatusCode() >= 400 && response.getStatusCode() <= 500) {
                LOG.error("Erro ao enviar o e-mail: " + response.getBody());
                throw new Exception(response.getBody());
            }

            // Log indicando o status de sucesso do envio
            LOG.info("E-mail enviado com sucesso! Status = " + response.getStatusCode());

        } catch (Exception e) {
            // Log do erro caso ocorra uma exceção
            LOG.error("Erro no envio de e-mail: " + e.getMessage(), e);
        }
    }
}
