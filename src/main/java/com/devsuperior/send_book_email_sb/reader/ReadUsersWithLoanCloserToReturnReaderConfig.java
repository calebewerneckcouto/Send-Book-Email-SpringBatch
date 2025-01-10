package com.devsuperior.send_book_email_sb.reader;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import com.devsuperior.send_book_email_sb.domain.Book;
import com.devsuperior.send_book_email_sb.domain.User;
import com.devsuperior.send_book_email_sb.domain.UserBookLoan;

@Configuration
public class ReadUsersWithLoanCloserToReturnReaderConfig {

    /**
     * Método que define o leitor de itens do Spring Batch para ler registros
     * de usuários com empréstimos próximos ao vencimento.
     * 
     * @param dataSource Fonte de dados configurada no projeto.
     * @return Um ItemReader configurado para buscar os registros desejados.
     */
    @Bean
    public ItemReader<UserBookLoan> readUsersWithLoanCloserToReturnReader(@Qualifier("appDS") DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<UserBookLoan>()
                .name("readUsersWithLoanCloserToReturnReader") // Nome do leitor
                .dataSource(dataSource) // Fonte de dados utilizada
                .sql("""
                    SELECT u.id AS user_id,
                        u.name AS user_name,
                        u.email AS user_email,
                        b.id AS book_id,
                        b.name AS book_name,
                        l.loan_date
                    FROM tb_user_book_loan l
                    INNER JOIN tb_user u ON l.user_id = u.id
                    INNER JOIN tb_book b ON l.book_id = b.id
                    WHERE l.loan_date + INTERVAL '6 day' = CURRENT_DATE
                 """) // Consulta SQL que busca os dados do empréstimo próximo ao vencimento
                .rowMapper(rowMapper()) // Mapeador para converter os resultados do SQL em objetos
                .build();
    }

    /**
     * Define um RowMapper para mapear os resultados do SQL em objetos UserBookLoan.
     * 
     * @return Um RowMapper configurado.
     */
    private RowMapper<UserBookLoan> rowMapper() {
        return new RowMapper<UserBookLoan>() {
            @Override
            public UserBookLoan mapRow(ResultSet rs, int rowNum) throws SQLException {
                // Criação do objeto User com base nos campos retornados pelo SQL
                User user = new User(rs.getInt("user_id"), rs.getString("user_email"), rs.getString("user_name"));
                
                // Criação do objeto Book e configuração de seus atributos
                Book book = new Book();
                book.setId(rs.getInt("book_id"));
                book.setName(rs.getString("book_name"));

                // Obtenção da data de empréstimo do ResultSet
                Date loanDate = rs.getDate("loan_date");

                // Criação do objeto UserBookLoan que une User, Book e a data de empréstimo
                UserBookLoan userBookLoan = new UserBookLoan(user, book, loanDate);

                // Exibe no console o empréstimo formatado (para fins de depuração)
                System.out.println(formatUserBookLoan(userBookLoan));

                // Retorna o objeto UserBookLoan
                return userBookLoan;
            }
        };
    }

    /**
     * Método para formatar os detalhes de um UserBookLoan para exibição no console.
     * 
     * @param userBookLoan Objeto que representa o empréstimo de um livro por um usuário.
     * @return Uma string formatada com as informações do empréstimo.
     */
    private String formatUserBookLoan(UserBookLoan userBookLoan) {
        // Configuração do formato da data para dd/MM/yyyy
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        
        // Formata as informações do usuário (ID, nome e e-mail)
        sb.append(String.format("Usuário ID: %d, Nome: %s, E-mail: %s\n", 
                               userBookLoan.getUser().getId(), 
                               userBookLoan.getUser().getName(), 
                               userBookLoan.getUser().getEmail()));
        
        // Formata as informações do livro (ID e título)
        sb.append(String.format("Livro ID: %d, Título: %s\n", 
                               userBookLoan.getBook().getId(), 
                               userBookLoan.getBook().getName()));
        
        // Formata a data de empréstimo
        sb.append(String.format("Data do Empréstimo: %s", 
                formatter.format(userBookLoan.getLoan_date())));
        
        // Retorna a string formatada
        return sb.toString();
    }
}
