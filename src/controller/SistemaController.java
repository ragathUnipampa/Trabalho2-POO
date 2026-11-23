package controller;

import exception.LocacaoException;
import model.Cliente;
import model.Gerente;
import model.Locacao;
import model.Veiculo;
import pagamento.FormaPagamento;
import service.AppServices;

import java.util.List;
import java.util.Map;

public class SistemaController {
    private final AppServices appServices;

    public SistemaController() {
        this(new AppServices());
    }

    public SistemaController(AppServices appServices) {
        this.appServices = appServices;
    }

    public Cliente autenticarCliente(String cnh, String senha) throws LocacaoException {
        return appServices.autenticarCliente(cnh, senha);
    }

    public Gerente autenticarGerente(String email, String senha) throws LocacaoException {
        return appServices.autenticarGerente(email, senha);
    }

    public Cliente cadastrarCliente(Cliente cliente) throws LocacaoException {
        return appServices.cadastrarCliente(cliente);
    }

    public List<Cliente> buscarClientes() {
        return appServices.buscarClientes();
    }

    public List<Veiculo> buscarVeiculos(String tipo, String status) throws LocacaoException {
        return appServices.buscarVeiculos(tipo, status);
    }

    public Veiculo buscarVeiculo(String placa) throws LocacaoException {
        return appServices.buscarVeiculo(placa);
    }

    public boolean verificarDisponibilidade(String placa) throws LocacaoException {
        return appServices.verificarDisponibilidade(placa);
    }

    public Veiculo cadastrarVeiculo(Veiculo veiculo) throws LocacaoException {
        return appServices.cadastrarVeiculo(veiculo);
    }

    public Veiculo editarVeiculo(Long id, Map<String, Object> dadosAtualizados) throws LocacaoException {
        return appServices.editarVeiculo(id, dadosAtualizados);
    }

    public Locacao realizarLocacao(Cliente cliente, Veiculo veiculo, Locacao locacao, FormaPagamento formaPagamento) throws LocacaoException {
        return appServices.realizarLocacao(cliente, veiculo, locacao, formaPagamento);
    }

    public List<Locacao> consultarLocacoesCliente(Long clienteId) throws LocacaoException {
        return appServices.consultarLocacoesCliente(clienteId);
    }

    public List<Locacao> consultarLocacoesVeiculo(String placa) throws LocacaoException {
        return appServices.consultarLocacoesVeiculo(placa);
    }
}
