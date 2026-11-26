package repository;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Adaptador customizado do {@link com.google.gson.Gson} para serializar e desserializar
 * objetos {@link LocalDate} no formato padrão ISO ({@code AAAA-MM-DD}).
 */
public class LocalDateAdapter extends TypeAdapter<LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Serializa um objeto {@link LocalDate} como uma string no formato ISO-8601 no fluxo JSON.
     *
     * @param out   O escritor JSON de destino.
     * @param value A data a ser serializada (escreve {@code null} se o valor for nulo).
     * @throws IOException Se ocorrer erro de escrita no fluxo JSON.
     */
    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.format(FORMATTER));
    }

    /**
     * Lê uma string de data no formato ISO-8601 do fluxo JSON e a converte em {@link LocalDate}.
     *
     * @param in O leitor JSON de origem.
     * @return A instância de {@link LocalDate} correspondente, ou {@code null} se o texto estiver vazio.
     * @throws IOException Se ocorrer erro de leitura no fluxo JSON.
     */
    @Override
    public LocalDate read(JsonReader in) throws IOException {
        String value = in.nextString();
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value, FORMATTER);
    }
}
