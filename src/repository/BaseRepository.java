package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Classe base abstrata que centraliza operações genéricas de persistência, consulta
 * e sanitização de dados via API REST do Supabase.
 *
 * @param <T> O tipo da entidade de domínio gerenciada pelo repositório concreto.
 */
public abstract class BaseRepository<T> {
    protected final Gson gson;
    protected final String baseUrl;

    /**
     * Inicializa o repositório base configurando a URL do recurso e a instância
     * do {@link Gson} com suporte à serialização de {@link LocalDate}.
     *
     * @param baseUrl A URL base do endpoint da tabela no Supabase.
     */
    protected BaseRepository(String baseUrl) {
        this.baseUrl = baseUrl;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    /**
     * Anexa um parâmetro de filtro de igualdade ({@code eq}) à string de consulta HTTP,
     * validando se o campo está na lista branca de campos permitidos.
     *
     * @param query         O {@link StringBuilder} acumulando os parâmetros da URL.
     * @param allowedFields O conjunto de nomes de colunas autorizados para filtragem.
     * @param field         O nome da coluna no banco de dados.
     * @param value         O valor a ser filtrado (ignorado se for {@code null} ou string em branco).
     * @throws IllegalArgumentException Se o campo informado não estiver contido em {@code allowedFields}.
     */
    protected void appendFilter(
            StringBuilder query,
            Set<String> allowedFields,
            String field,
            Object value
    ) {
        if (value == null) {
            return;
        }

        if (value instanceof String valorString && valorString.isBlank()) {
            return;
        }

        if (!allowedFields.contains(field)) {
            throw new IllegalArgumentException("Campo de consulta nao permitido: " + field);
        }

        query.append("&")
                .append(encodeQueryComponent(field))
                .append("=eq.")
                .append(encodeQueryComponent(String.valueOf(value)));
    }

    /**
     * Valida e higieniza um mapa de dados antes de enviá-lo em uma requisição de atualização parcial ({@code PATCH}).
     *
     * @param payload       O mapa contendo os pares coluna/valor a serem atualizados.
     * @param allowedFields O conjunto de colunas permitidas para alteração.
     * @return Um novo {@link Map} contendo apenas os campos validados e não nulos.
     * @throws IllegalArgumentException Se o payload estiver vazio, contiver colunas não permitidas ou valores nulos.
     */
    protected Map<String, Object> sanitizePatchPayload(
            Map<String, Object> payload,
            Set<String> allowedFields
    ) {
        if (payload == null || payload.isEmpty()) {
            throw new IllegalArgumentException("Nenhum dado foi informado para atualizacao.");
        }

        Map<String, Object> sanitized = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            if (!allowedFields.contains(entry.getKey())) {
                throw new IllegalArgumentException("Campo de atualizacao nao permitido: " + entry.getKey());
            }
            if (entry.getValue() == null) {
                throw new IllegalArgumentException("O campo '" + entry.getKey() + "' nao pode ser nulo.");
            }
            sanitized.put(entry.getKey(), entry.getValue());
        }
        return sanitized;
    }

    /**
     * Executa uma consulta {@code GET} na tabela configurada e converte a resposta JSON
     * em uma lista tipada de entidades.
     *
     * @param query Os parâmetros de filtro já formatados e codificados para anexar à URL.
     * @param tipo  A classe da entidade de destino para desserialização.
     * @return Uma {@link List} contendo os registros encontrados.
     * @throws RuntimeException Se ocorrer falha de comunicação ou erro na conversão do JSON.
     */
    protected List<T> buscarLista(String query, Class<T> tipo) {
        try {
            String url = baseUrl + "?select=*";
            if (query != null && !query.isBlank()) {
                url += query;
            }
            String responseBody = SupabaseClient.send(url, "GET", null, null).body();
            Type tipoLista = TypeToken.getParameterized(List.class, tipo).getType();
            return gson.fromJson(responseBody, tipoLista);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar registros: " + e.getMessage(), e);
        }
    }

    /**
     * Valida os atributos do objeto e insere um novo registro no banco de dados via requisição {@code POST}.
     *
     * @param url           A URL de destino da requisição.
     * @param payload       O objeto contendo os dados da nova entidade a ser persistida.
     * @param allowedFields O conjunto de campos autorizados para gravação.
     * @param tipo          A classe da entidade para desserializar o registro retornado pelo banco.
     * @return A entidade persistida com os dados gerados pelo banco (como ID e timestamps), ou {@code null}.
     * @throws IllegalArgumentException Se o payload não for um objeto JSON válido ou contiver campos não autorizados.
     * @throws RuntimeException         Se ocorrer falha na requisição HTTP ou no processamento da resposta.
     */
    protected T salvar(
            String url,
            Object payload,
            Set<String> allowedFields,
            Class<T> tipo
    ) {
        try {
            JsonElement payloadElement = JsonParser.parseString(gson.toJson(payload));
            if (!payloadElement.isJsonObject()) {
                throw new IllegalArgumentException("O corpo da requisicao deve ser um objeto JSON.");
            }

            JsonObject payloadObject = payloadElement.getAsJsonObject();
            for (String field : payloadObject.keySet()) {
                if (!allowedFields.contains(field)) {
                    throw new IllegalArgumentException("Campo de cadastro nao permitido: " + field);
                }
            }

            String responseBody = SupabaseClient.send(
                    url,
                    "POST",
                    gson.toJson(payloadObject),
                    "return=representation"
            ).body();
            JsonElement resposta = JsonParser.parseString(responseBody);
            if (!resposta.isJsonArray() || resposta.getAsJsonArray().isEmpty()) {
                return null;
            }
            return gson.fromJson(resposta.getAsJsonArray().get(0), tipo);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao persistir registro: " + e.getMessage(), e);
        }
    }

    /**
     * Atualiza parcialmente um ou mais registros no banco de dados via requisição {@code PATCH}.
     *
     * @param url           A URL contendo o filtro identificador do registro a ser alterado.
     * @param payload       O mapa com os campos e novos valores a serem aplicados.
     * @param allowedFields O conjunto de colunas permitidas para modificação.
     * @param tipo          A classe da entidade para desserializar o registro atualizado.
     * @return A entidade atualizada retornada pelo banco, ou {@code null} se nenhum registro foi afetado.
     * @throws IllegalArgumentException Se os dados informados violarem as regras de sanitização.
     * @throws RuntimeException         Se ocorrer erro na comunicação com o servidor.
     */
    protected T atualizar(
            String url,
            Map<String, Object> payload,
            Set<String> allowedFields,
            Class<T> tipo
    ) {
        try {
            Map<String, Object> sanitizedPayload = sanitizePatchPayload(payload, allowedFields);
            String json = gson.toJson(sanitizedPayload);
            String responseBody = SupabaseClient.send(url, "PATCH", json, "return=representation").body();
            JsonElement resposta = JsonParser.parseString(responseBody);
            if (!resposta.isJsonArray() || resposta.getAsJsonArray().isEmpty()) {
                return null;
            }
            return gson.fromJson(resposta.getAsJsonArray().get(0), tipo);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar registro: " + e.getMessage(), e);
        }
    }

    /**
     * Codifica um componente de valor de consulta para uso seguro em URLs (UTF-8),
     * substituindo sinais de {@code +} por {@code %20}.
     *
     * @param valor O texto bruto a ser codificado.
     * @return A string devidamente codificada para URL, ou string vazia se {@code valor} for {@code null}.
     * @throws IllegalArgumentException Se ocorrer falha na codificação de caracteres.
     */
    protected String encodeQueryComponent(String valor) {
        if (valor == null) {
            return "";
        }

        StringBuilder encoded = new StringBuilder();
        try {
            String urLEncoded = java.net.URLEncoder.encode(valor, StandardCharsets.UTF_8);
            encoded.append(urLEncoded.replace("+", "%20"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Nao foi possivel codificar o parametro.", e);
        }
        return encoded.toString();
    }
}
