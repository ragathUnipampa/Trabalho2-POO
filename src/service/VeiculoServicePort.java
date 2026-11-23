package service;

import exception.LocacaoException;
import model.Veiculo;

import java.util.List;
import java.util.Map;

public interface VeiculoServicePort {
    List<Veiculo> buscarVeiculos(String tipoVeiculo, String statusVeiculo) throws LocacaoException;

    Veiculo buscarVeiculo(String placa) throws LocacaoException;

    Veiculo cadastrarVeiculo(Veiculo veiculo) throws LocacaoException;

    Veiculo editarVeiculo(long id, Map<String, Object> dadosAtualizados) throws LocacaoException;

    boolean verificarDisponibilidade(String placa) throws LocacaoException;
}
