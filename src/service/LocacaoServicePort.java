package service;

import exception.LocacaoException;
import model.Cliente;
import model.Locacao;
import model.Veiculo;
import pagamento.FormaPagamento;

import java.util.List;

/**
 * Interface de caso de uso que especifica as regras de negócio para contratação,
 * validação de elegibilidade e consulta de histórico de locações ({@link Locacao}).
 */
public interface LocacaoServicePort {

    /**
     * Processa e registra uma nova locação de veículo, verificando compatibilidade de CNH,
     * disponibilidade do veículo, validade do valor, aplicação da forma de pagamento e
     * atualização automática do status do veículo para indisponível.
     *
     * @param cliente        O {@link Cliente} locatário.
     * @param veiculo        O {@link Veiculo} a ser alugado.
     * @param locacao        O objeto {@link Locacao} com as datas, diárias e valor bruto calculado.
     * @param formaPagamento A estratégia de {@link FormaPagamento} escolhida pelo cliente.
     * @return A {@link Locacao} persistida com o valor final ajustado e forma de pagamento registrada.
     * @throws LocacaoException Se algum parâmetro for nulo, a CNH for incompatível com o veículo,
     *                          o veículo estiver indisponível, o valor for menor ou igual a zero
     *                          ou ocorrer erro ao persistir os dados.
     */
    Locacao realizarLocacao(
            Cliente cliente,
            Veiculo veiculo,
            Locacao locacao,
            FormaPagamento formaPagamento
    ) throws LocacaoException;

    /**
     * Consulta todas as locações já realizadas por um cliente específico.
     *
     * @param clienteId O identificador único do cliente.
     * @return Uma {@link List} de {@link Locacao} vinculadas ao cliente informado.
     * @throws LocacaoException Se ocorrer erro ao consultar o histórico na base de dados.
     */
    List<Locacao> consultarLocacoesCliente(Long clienteId) throws LocacaoException;

    /**
     * Consulta o histórico de locações de um veículo específico a partir de sua placa.
     *
     * @param placa A placa de identificação do veículo.
     * @return Uma {@link List} de {@link Locacao} vinculadas ao veículo informado.
     * @throws LocacaoException Se ocorrer erro ao consultar o histórico na base de dados.
     */
    List<Locacao> consultarLocacoesVeiculo(String placa) throws LocacaoException;

    /**
     * Verifica se a categoria da CNH do cliente permite conduzir o tipo do veículo informado.
     *
     * @param cliente O {@link Cliente} cuja habilitação será avaliada.
     * @param veiculo O {@link Veiculo} pretendido para locação.
     * @return {@code true} se o cliente estiver habilitado para o tipo de veículo; {@code false} caso contrário.
     */
    boolean podeAlugar(Cliente cliente, Veiculo veiculo);
}
