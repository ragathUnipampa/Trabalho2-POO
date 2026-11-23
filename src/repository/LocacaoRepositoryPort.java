package repository;

import model.Locacao;

import java.util.List;

public interface LocacaoRepositoryPort {
    List<Locacao> buscarLocacoes(
            Long clienteId,
            String dataInicio,
            String dataFim,
            String formaPagamento,
            String veiculoPlaca
    );

    Locacao cadastrarLocacao(Locacao novaLocacao);
}
