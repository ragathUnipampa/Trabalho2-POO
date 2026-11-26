package pagamento;

/**
 * Implementação de {@link FormaPagamento} referente a transações efetuadas via cartão.
 * Mantém o valor integral da locação sem aplicação de descontos ou taxas adicionais.
 */
public class PagamentoCartao implements FormaPagamento {

    /**
     * {@inheritDoc}
     * <p>Para pagamentos via cartão, o valor cobrado corresponde exatamente ao valor bruto informado.</p>
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
     * @return {@code "CARTAO"}
     */
    @Override
    public String getNome() {
        return "CARTAO";
    }
}
