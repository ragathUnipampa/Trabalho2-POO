package enums;

public enum StatusVeiculo {
    DISPONIVEL("disponivel"),
    INDISPONIVEL("indisponivel");

    private final String valor;

    StatusVeiculo(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

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
