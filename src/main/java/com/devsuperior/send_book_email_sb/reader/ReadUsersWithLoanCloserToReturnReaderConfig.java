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

   

    @Bean
    public ItemReader<UserBookLoan> readUsersWithLoanCloserToReturnReader(@Qualifier("appDS") DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<UserBookLoan>().name("readUsersWithLoanCloserToReturnReader")
                .dataSource(dataSource)
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
                 """)
                .rowMapper(rowMapper()).build();
    }

    private RowMapper<UserBookLoan> rowMapper() {
        return new RowMapper<UserBookLoan>() {
            @Override
            public UserBookLoan mapRow(ResultSet rs, int rowNum) throws SQLException {
                // Atribuindo o e-mail ao campo email e o nome ao campo name corretamente
                User user = new User(rs.getInt("user_id"), rs.getString("user_email"), rs.getString("user_name"));
                Book book = new Book();
                book.setId(rs.getInt("book_id"));
                book.setName(rs.getString("book_name"));

                Date loanDate = rs.getDate("loan_date");

                UserBookLoan userBookLoan = new UserBookLoan(user, book, loanDate);

                System.out.println(formatUserBookLoan(userBookLoan));

                return userBookLoan;
            }
        };
    }


    private String formatUserBookLoan(UserBookLoan userBookLoan) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        
        // Corrigir a formatação para separar o nome e o e-mail
        sb.append(String.format("Usuário ID: %d, Nome: %s, E-mail: %s\n", 
                               userBookLoan.getUser().getId(), 
                               userBookLoan.getUser().getName(), 
                               userBookLoan.getUser().getEmail()));
        
        sb.append(String.format("Livro ID: %d, Título: %s\n", 
                               userBookLoan.getBook().getId(), 
                               userBookLoan.getBook().getName()));
        
        sb.append(String.format("Data do Empréstimo: %s", 
                formatter.format(userBookLoan.getLoan_date())));
        
        return sb.toString();
    }
}