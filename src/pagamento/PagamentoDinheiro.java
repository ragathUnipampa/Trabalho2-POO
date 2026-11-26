package pagamento;

/**
 * Implementação de {@link FormaPagamento} referente a pagamentos realizados em espécie (dinheiro).
 * Mantém o valor integral da locação sem aplicação de descontos ou taxas adicionais.
 */
public class PagamentoDinheiro implements FormaPagamento {

    /**
     * {@inheritDoc}
     * <p>Para pagamentos em dinheiro, o valor cobrado corresponde exatamente ao valor bruto informado.</p>
     *
     * @param valor {@inheritDoc}
     * @return O mesmo valor original recebido, sem alterações.
     */
    @Override
    public double realizarPagamento(double valor) {
        return valor;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code "DINHEIRO"}
     */
    @Override
    public String getNome() {
        return "DINHEIRO";
    }
}
