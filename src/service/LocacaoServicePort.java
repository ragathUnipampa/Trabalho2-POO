package service;

import exception.LocacaoException;
import model.Cliente;
import model.Locacao;
import model.Veiculo;
import pagamento.FormaPagamento;

import java.util.List;

public interface LocacaoServicePort {
    Locacao realizarLocacao(
            Cliente cliente,
            Veiculo veiculo,
            Locacao locacao,
            FormaPagamento formaPagamento
    ) throws LocacaoException;

    List<Locacao> consultarLocacoesCliente(Long clienteId) throws LocacaoException;

    List<Locacao> consultarLocacoesVeiculo(String placa) throws LocacaoException;

    boolean podeAlugar(Cliente cliente, Veiculo veiculo);
}
