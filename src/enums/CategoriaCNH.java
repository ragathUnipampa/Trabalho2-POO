package enums;

public enum CategoriaCNH {
    A {
        @Override
        public boolean podeAlugar(TipoVeiculo tipo) {
            return TipoVeiculo.MOTO.equals(tipo);
        }
    },
    B {
        @Override
        public boolean podeAlugar(TipoVeiculo tipo) {
            return TipoVeiculo.CARRO.equals(tipo);
        }
    },
    AB {
        @Override
        public boolean podeAlugar(TipoVeiculo tipo) {
            return tipo != null;
        }
    };

    public abstract boolean podeAlugar(TipoVeiculo tipo);
}
