package pagamento;

/**
 * Contrato base para as modalidades de pagamento suportadas pelo sistema de locação.
 * Define as operações essenciais para o cálculo do valor final a pagar e a identificação
 * da forma de pagamento utilizada, permitindo a extensão de novos métodos sem alterar
 * o fluxo principal de locação (Princípio Aberto/Fechado - OCP).
 */
public interface FormaPagamento {

    /**
     * Processa o cálculo do valor final da locação de acordo com as regras específicas
     * da modalidade de pagamento (como aplicação de descontos ou acréscimos).
     *
     * @param valor O valor bruto original da locação antes de ajustes financeiros.
     * @return O valor líquido final a ser cobrado do cliente.
     */
    double realizarPagamento(double valor);

    /**
     * Retorna o identificador textual padronizado da modalidade de pagamento
     * para registo e persistência.
     *
     * @return O nome identificador da forma de pagamento (ex: {@code "PIX"}, {@code "CARTAO"}, {@code "DINHEIRO"}).
     */
    String getNome();
}
