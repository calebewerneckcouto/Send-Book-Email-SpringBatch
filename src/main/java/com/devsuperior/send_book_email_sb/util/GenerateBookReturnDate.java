package com.devsuperior.send_book_email_sb.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class GenerateBookReturnDate {

    // Número de dias padrão para o retorno do livro
    public static int numDaysToReturnBook = 7;

    // Formato da data para exibição (dia/mês/ano)
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Obtém a data de devolução do livro formatada.
     * Adiciona `numDaysToReturnBook` dias à data do empréstimo fornecida.
     *
     * @param loan_date Data do empréstimo do livro.
     * @return Data de devolução formatada como String.
     */
    public static String getDate(Date loan_date) {
        // Converte a data de empréstimo para um objeto Calendar
        Calendar calendar = dateToCalendar(loan_date);

        // Adiciona o número de dias para devolução ao Calendar
        calendar.add(Calendar.DATE, numDaysToReturnBook);

        // Converte de volta para Date e formata como String
        String result = dateFormat.format(calendarToDate(calendar));

        return result;
    }

    /**
     * Converte um objeto Calendar em um objeto Date.
     *
     * @param calendar Objeto Calendar a ser convertido.
     * @return Objeto Date correspondente.
     */
    private static Date calendarToDate(Calendar calendar) {
        return calendar.getTime(); // Obtém a representação Date do Calendar
    }

    /**
     * Converte um objeto Date em um objeto Calendar.
     *
     * @param loan_date Objeto Date a ser convertido.
     * @return Objeto Calendar correspondente.
     */
    private static Calendar dateToCalendar(Date loan_date) {
        Calendar calendar = Calendar.getInstance(); // Cria uma instância de Calendar
        calendar.setTime(loan_date); // Define a data do Calendar com a data fornecida
        return calendar;
    }
}
