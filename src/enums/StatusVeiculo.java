package enums;

/**
 * Representa os estados possíveis de disponibilidade de um veículo na frota da locadora.
 */
public enum StatusVeiculo {

    /**
     * Indica que o veículo está livre no pátio e apto para ser locado.
     */
    DISPONIVEL("disponivel"),

    /**
     * Indica que o veículo está alugado, em manutenção ou bloqueado para novas locações.
     */
    INDISPONIVEL("indisponivel");

    private final String valor;

    StatusVeiculo(String valor) {
        this.valor = valor;
    }

    /**
     * Retorna a representação textual padronizada do status utilizada na persistência.
     *
     * @return O texto correspondente ao status (ex: {@code "disponivel"} ou {@code "indisponivel"}).
     */
    public String getValor() {
        return valor;
    }

    /**
     * Converte uma string (como um valor vindo do banco de dados ou formulário)
     * na constante {@link StatusVeiculo} correspondente, ignorando maiúsculas, minúsculas e espaços extras.
     *
     * @param valor A string representando o status do veículo.
     * @return A constante {@link StatusVeiculo} correspondente, ou {@code null} se a entrada for nula ou vazia.
     * @throws IllegalArgumentException Se o texto informado não corresponder a nenhum status válido.
     */
    public static StatusVeiculo fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        String valorNormalizado = valor.trim().toLowerCase();
        for (StatusVeiculo status : values()) {
            if (status.valor.equals(valorNormalizado)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Status de veículo inválido: " + valor);
    }
}
