package pagamento;

/**
 * Implementação de {@link FormaPagamento} referente a transferências instantâneas via PIX.
 * Aplica automaticamente um desconto promocional de 10% sobre o valor total da locação.
 */
public class PagamentoPix implements FormaPagamento {

    /**
     * {@inheritDoc}
     * <p>Concede um desconto de 10% sobre o valor bruto da locação como incentivo
     * para pagamentos à vista via PIX.</p>
     *
     * @param valor {@inheritDoc}
     * @return O valor final da locação já com o desconto de 10% subtraído.
     */
    @Override
    public double realizarPagamento(double valor) {
        double desconto = valor * 0.10;
        return valor - desconto;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code "PIX"}
     */
    @Override
    public String getNome () {
        return "PIX";
    }
}
