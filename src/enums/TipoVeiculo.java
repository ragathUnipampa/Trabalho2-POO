package enums;

/**
 * Define as categorias de veículos disponíveis no catálogo da locadora.
 */
public enum TipoVeiculo {

    /**
     * Representa os carros.
     */
    CARRO("carro"),

    /**
     * Representa as motos.
     */
    MOTO("moto");

    private final String valor;

    TipoVeiculo(String valor) {
        this.valor = valor;
    }

    /**
     * Retorna a representação textual padronizada do tipo de veículo utilizada na persistência.
     *
     * @return O texto correspondente ao tipo (ex: {@code "carro"} ou {@code "moto"}).
     */
    public String getValor() {
        return valor;
    }

    /**
     * Converte uma string na constante {@link TipoVeiculo} correspondente,
     * realizando a normalização de espaços e caixa alta/baixa.
     *
     * @param valor A string representando o tipo do veículo.
     * @return A constante {@link TipoVeiculo} correspondente, ou {@code null} se a entrada for nula ou vazia.
     * @throws IllegalArgumentException Se o texto informado não corresponder a um tipo de veículo cadastrado.
     */
    public static TipoVeiculo fromValue(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        String valorNormalizado = valor.trim().toLowerCase();
        for (TipoVeiculo tipo : values()) {
            if (tipo.valor.equals(valorNormalizado)) {
                return tipo;
            }
        }

        throw new IllegalArgumentException("Tipo de veículo inválido: " + valor);
    }
}
