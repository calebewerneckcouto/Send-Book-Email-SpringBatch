# Send Book Email SB

Este projeto foi desenvolvido para enviar e-mails de notificação aos usuários que realizaram empréstimos de livros e estão próximos da data de devolução. O envio de e-mails é realizado utilizando a API do [SendGrid](https://sendgrid.com/), com o objetivo de facilitar a devolução dos livros emprestados.

## Funcionalidades

- **Leitura dos usuários com empréstimos próximos da data de devolução**: A aplicação consulta os empréstimos no banco de dados e seleciona os que estão próximos da data de devolução.
- **Processamento de e-mails de notificação**: Para cada usuário com um empréstimo prestes a expirar, um e-mail de notificação é gerado.
- **Envio de e-mails**: E-mails são enviados utilizando a API do SendGrid, informando ao usuário sobre a proximidade da data de devolução do livro.

## Estrutura do Projeto

- **Leitura de Dados**: Utiliza o `JdbcCursorItemReader` para ler as informações do banco de dados relacionadas aos empréstimos.
- **Processamento de Dados**: Um `ItemProcessor` é responsável por gerar um e-mail para cada usuário com um empréstimo próximo da devolução.
- **Envio de E-mails**: O `ItemWriter` envia os e-mails utilizando a API do SendGrid.

## Como Funciona

1. **Banco de Dados**: A aplicação consulta a tabela `tb_user_book_loan` para encontrar empréstimos cujas datas estão dentro de um intervalo de 6 dias a partir da data atual.
2. **Geração do E-mail**: Para cada empréstimo encontrado, um e-mail é gerado com as informações do usuário e do livro.
3. **Envio de E-mail**: O e-mail é enviado através da API do SendGrid.

## Dependências

- **Spring Batch**: Para orquestrar o processo de leitura, processamento e escrita de dados.
- **SendGrid**: Para envio de e-mails.
- **Spring Boot**: Para facilitar a configuração e execução da aplicação.
- **JDBC**: Para conectar-se ao banco de dados e consultar as informações dos empréstimos.

## Requisitos

- JDK 11 ou superior
- Spring Boot
- Dependência do SendGrid configurada corretamente
- Banco de dados com as tabelas `tb_user`, `tb_book`, `tb_user_book_loan` devidamente configuradas

## Configuração

### 1. Configuração do SendGrid

Certifique-se de que a chave de API do SendGrid esteja configurada corretamente. Adicione a chave de API no arquivo `application.properties`:

```properties
sendgrid.api.key=your-sendgrid-api-key

## Banco de Dados
A aplicação espera um banco de dados com as seguintes tabelas:

tb_user: Tabela que contém informações sobre os usuários.
tb_book: Tabela que contém informações sobre os livros.
tb_user_book_loan: Tabela que registra os empréstimos de livros, incluindo o ID do usuário, ID do livro e a data do empréstimo.

## Cronograma
A aplicação é projetada para ser executada em um processo em lote com o uso de Spring Batch. O cronograma pode ser configurado para executar periodicamente, por exemplo, a cada dia, para garantir que os e-mails sejam enviados a tempo.
