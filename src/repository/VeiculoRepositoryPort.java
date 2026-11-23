package repository;

import model.Veiculo;

import java.util.List;
import java.util.Map;

public interface VeiculoRepositoryPort {
    List<Veiculo> buscarVeiculos(
            String tipoVeiculo,
            String statusVeiculo,
            String marca,
            String modelo,
            String placa
    );

    Veiculo cadastrarVeiculo(Veiculo novoVeiculo);

    Veiculo editarVeiculo(long id, Map<String, Object> dadosAtualizados);
}
