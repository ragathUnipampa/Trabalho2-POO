package enums;

/**
 * Representa as categorias da Carteira Nacional de Habilitação (CNH) suportadas
 * pelo sistema e define a regra de compatibilidade para locação de cada tipo de veículo.
 */
public enum CategoriaCNH {

    /**
     * Habilitação exclusiva para condução de veículos motorizados de duas ou três rodas (motos).
     */
    A {
        @Override
        public boolean podeAlugar(TipoVeiculo tipo) {
            return TipoVeiculo.MOTO.equals(tipo);
        }
    },

    /**
     * Habilitação exclusiva para condução de veículos automotores de quatro rodas (carros).
     */
    B {
        @Override
        public boolean podeAlugar(TipoVeiculo tipo) {
            return TipoVeiculo.CARRO.equals(tipo);
        }
    },

    /**
     * Habilitação combinada que permite a condução tanto de motos quanto de carros.
     */
    AB {
        @Override
        public boolean podeAlugar(TipoVeiculo tipo) {
            return tipo != null;
        }
    };

    /**
     * Verifica se o titular desta categoria de CNH está legalmente habilitado
     * a alugar o tipo de veículo especificado.
     *
     * @param tipo O {@link TipoVeiculo} que se deseja alugar.
     * @return {@code true} se a categoria for compatível com o veículo; {@code false} caso contrário.
     */
    public abstract boolean podeAlugar(TipoVeiculo tipo);
}
