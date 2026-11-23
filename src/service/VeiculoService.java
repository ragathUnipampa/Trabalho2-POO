package service;

import enums.StatusVeiculo;
import enums.TipoVeiculo;
import exception.LocacaoException;
import model.Veiculo;
import repository.VeiculoRepository;
import repository.VeiculoRepositoryPort;

import java.util.List;
import java.util.Map;

public class VeiculoService implements VeiculoServicePort {
    private final VeiculoRepositoryPort veiculoRepository;

    public VeiculoService() {
        this(new VeiculoRepository());
    }

    public VeiculoService(VeiculoRepositoryPort veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public List<Veiculo> buscarVeiculos(String tipoVeiculo, String statusVeiculo) throws LocacaoException {
        try {
            return veiculoRepository.buscarVeiculos(tipoVeiculo, statusVeiculo, null, null, null);
        } catch (RuntimeException e) {
            throw new LocacaoException("Erro ao buscar veículos: " + e.getMessage());
        }
    }

    public Veiculo buscarVeiculo(String placa) throws LocacaoException {
        if (placa == null || placa.isBlank()) {
            throw new LocacaoException("Placa inválida para buscar.");
        }

        try {
            List<Veiculo> veiculos = veiculoRepository.buscarVeiculos(null, null, null, null, placa);
            if (veiculos.isEmpty()) {
                throw new LocacaoException("Placa inválida, veículo não encontrado.");
            }
            return veiculos.get(0);
        } catch (RuntimeException e) {
            throw new LocacaoException("Erro ao buscar veículo: " + e.getMessage());
        }
    }

    public Veiculo cadastrarVeiculo(Veiculo veiculo) throws LocacaoException {
        validarVeiculo(veiculo);

        try {
            List<Veiculo> existentes = veiculoRepository.buscarVeiculos(null, null, null, null, veiculo.getPlaca());
            if (!existentes.isEmpty()) {
                throw new LocacaoException("Já existe um veículo com essa placa!");
            }
            return veiculoRepository.cadastrarVeiculo(veiculo);
        } catch (RuntimeException e) {
            throw new LocacaoException("Erro ao cadastrar veículo no banco: " + e.getMessage());
        }
    }

    public Veiculo editarVeiculo(long id, Map<String, Object> dadosAtualizados) throws LocacaoException {
        if (dadosAtualizados == null || dadosAtualizados.isEmpty()) {
            throw new LocacaoException("Nenhuma alteração informada para o veículo.");
        }

        try {
            return veiculoRepository.editarVeiculo(id, dadosAtualizados);
        } catch (RuntimeException e) {
            throw new LocacaoException("Erro ao editar veículo: " + e.getMessage());
        }
    }

    public boolean verificarDisponibilidade(String placa) throws LocacaoException {
        Veiculo veiculo = buscarVeiculo(placa);
        return veiculo != null && StatusVeiculo.DISPONIVEL.equals(veiculo.getStatusVeiculoEnum());
    }

    private void validarVeiculo(Veiculo veiculo) throws LocacaoException {
        if (veiculo == null) {
            throw new LocacaoException("Não foi fornecido dados do veículo!");
        }
        if (veiculo.getPlaca() == null || veiculo.getPlaca().isBlank()) {
            throw new LocacaoException("A placa do veículo é obrigatória.");
        }
        if (veiculo.getTipoVeiculo() != null && !veiculo.getTipoVeiculo().isBlank()) {
            TipoVeiculo.fromValue(veiculo.getTipoVeiculo());
        }
        if (veiculo.getStatusVeiculo() != null && !veiculo.getStatusVeiculo().isBlank()) {
            StatusVeiculo.fromValue(veiculo.getStatusVeiculo());
        }
    }
}
