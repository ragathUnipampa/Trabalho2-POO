package service;

import enums.StatusVeiculo;
import enums.TipoVeiculo;
import exception.LocacaoException;
import model.Cliente;
import model.Locacao;
import model.Veiculo;
import pagamento.FormaPagamento;
import repository.LocacaoRepository;
import repository.LocacaoRepositoryPort;
import repository.VeiculoRepository;
import repository.VeiculoRepositoryPort;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementação concreta de {@link LocacaoServicePort} que orquestra as regras de negócio
 * da locação de veículos, integrando validações de CNH, disponibilidade, pagamento e
 * atualização de estado da frota.
 */
public class LocacaoService implements LocacaoServicePort {
    private final LocacaoRepositoryPort locacaoRepository;
    private final VeiculoRepositoryPort veiculoRepository;
    private final VeiculoServicePort veiculoService;

    /**
     * Constrói o serviço de locações com as implementações padrão de repositórios e serviço de veículos.
     */
    public LocacaoService() {
        this(new LocacaoRepository(), new VeiculoRepository(), new VeiculoService());
    }

    /**
     * Constrói o serviço de locações recebendo suas dependências externamente (Injeção de Dependência).
     *
     * @param locacaoRepository O repositório para persistência de locações.
     * @param veiculoRepository O repositório para atualização de status do veículo.
     * @param veiculoService    O serviço para verificação de disponibilidade do veículo.
     */
    public LocacaoService(
            LocacaoRepositoryPort locacaoRepository,
            VeiculoRepositoryPort veiculoRepository,
            VeiculoServicePort veiculoService
    ) {
        this.locacaoRepository = locacaoRepository;
        this.veiculoRepository = veiculoRepository;
        this.veiculoService = veiculoService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Locacao realizarLocacao(Cliente cliente, Veiculo veiculo, Locacao locacao, FormaPagamento formaPagamento) throws LocacaoException {
        if (cliente == null || veiculo == null || locacao == null || formaPagamento == null) {
            throw new LocacaoException("Dados da locação inválidos, preencha corretamente!");
        }

        if (!podeAlugar(cliente, veiculo)) {
            throw new LocacaoException("Cliente com CNH " + cliente.getCategoriaCnh() + " não pode alugar um veículo do tipo " + veiculo.getTipoVeiculo());
        }
        if (!veiculoService.verificarDisponibilidade(veiculo.getPlaca())) {
            throw new LocacaoException("O veículo de placa " + veiculo.getPlaca() + " não está disponível.");
        }

        if (locacao.getValorTotal() == null || locacao.getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new LocacaoException("O valor da locação deve ser maior que zero.");
        }

        double valorFinal = formaPagamento.realizarPagamento(locacao.getValorTotal().doubleValue());
        locacao.setFormaPagamento(formaPagamento.getNome());
        locacao.setValorTotal(BigDecimal.valueOf(valorFinal));

        try {
            Locacao locacaoRealizada = locacaoRepository.cadastrarLocacao(locacao);
            Map<String, Object> alteracoes = new HashMap<>();
            alteracoes.put("statusVeiculo", StatusVeiculo.INDISPONIVEL.getValor());
            veiculoRepository.editarVeiculo(veiculo.getId(), alteracoes);
            return locacaoRealizada;
        } catch (RuntimeException e) {
            throw new LocacaoException("Erro ao registrar locação: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean podeAlugar(Cliente cliente, Veiculo veiculo) {
        if (cliente == null || veiculo == null) {
            return false;
        }

        if (cliente.getCategoriaCnh() == null || veiculo.getTipoVeiculo() == null) {
            return false;
        }

        TipoVeiculo tipo = TipoVeiculo.fromValue(veiculo.getTipoVeiculo());
        return cliente.getCategoriaCnh().podeAlugar(tipo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Locacao> consultarLocacoesCliente(Long clienteId) throws LocacaoException {
        try {
            return locacaoRepository.buscarLocacoes(clienteId, null, null, null, null);
        } catch (RuntimeException e) {
            throw new LocacaoException("Erro ao consultar histórico do cliente: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Locacao> consultarLocacoesVeiculo(String placa) throws LocacaoException {
        try {
            return locacaoRepository.buscarLocacoes(null, null, null, null, placa);
        } catch (RuntimeException e) {
            throw new LocacaoException("Erro ao consultar histórico do veículo: " + e.getMessage());
        }
    }
}
