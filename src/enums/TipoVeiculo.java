package enums;

public enum TipoVeiculo {
    CARRO("carro"),
    MOTO("moto");

    private final String valor;

    TipoVeiculo(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

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
