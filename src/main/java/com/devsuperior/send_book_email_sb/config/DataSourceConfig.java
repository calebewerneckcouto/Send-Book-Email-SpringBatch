package com.devsuperior.send_book_email_sb.config;

// Importa as classes necessárias para gerenciar DataSources e transações no Spring
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

// Anotação que define esta classe como uma classe de configuração do Spring
@Configuration
public class DataSourceConfig {

    // Define um bean para o DataSource principal (o que será usado por padrão pelo Spring)
    @Primary // Indica que este é o DataSource principal quando houver múltiplos DataSources configurados
    @Bean // Define um método como um bean gerenciado pelo Spring
    @ConfigurationProperties(prefix = "spring.datasource") 
    // Lê as configurações do arquivo de propriedades usando o prefixo "spring.datasource"
    public DataSource springDS() {
        return DataSourceBuilder.create().build(); 
        // Cria e retorna um DataSource com base nas configurações definidas
    }

    // Define um segundo DataSource com configurações diferentes
    @Bean // Cria um novo bean gerenciado pelo Spring
    @ConfigurationProperties(prefix = "app.datasource") 
    // Lê as configurações do arquivo de propriedades usando o prefixo "app.datasource"
    public DataSource appDS() {
        return DataSourceBuilder.create().build(); 
        // Cria e retorna o DataSource específico para "app.datasource"
    }

    // Define um gerenciador de transações para o DataSource "appDS"
    @Bean // Declara o gerenciador de transações como um bean gerenciado pelo Spring
    public PlatformTransactionManager transactionManagerApp(@Qualifier("appDS") DataSource dataSource) {
        // Usa a anotação @Qualifier para associar o DataSource correto ("appDS")
        return new DataSourceTransactionManager(dataSource); 
        // Retorna uma instância de DataSourceTransactionManager associada ao DataSource especificado
    }
}
