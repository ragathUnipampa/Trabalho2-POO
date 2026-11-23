package service;

import exception.LocacaoException;
import model.Cliente;
import model.Gerente;
import model.Locacao;
import model.Veiculo;
import pagamento.FormaPagamento;

import java.util.List;
import java.util.Map;

public final class AppServices {
    private final ClienteServicePort clientes;
    private final GerenteServicePort gerentes;
    private final VeiculoServicePort veiculos;
    private final LocacaoServicePort locacoes;

    public AppServices() {
        this.clientes = new ClienteService();
        this.gerentes = new GerenteService();
        this.veiculos = new VeiculoService();
        this.locacoes = new LocacaoService();
    }

    public AppServices(
            ClienteServicePort clientes,
            GerenteServicePort gerentes,
            VeiculoServicePort veiculos,
            LocacaoServicePort locacoes
    ) {
        this.clientes = clientes;
        this.gerentes = gerentes;
        this.veiculos = veiculos;
        this.locacoes = locacoes;
    }

    public Cliente autenticarCliente(String cnh, String senha) throws LocacaoException {
        return clientes.autenticarCliente(cnh, senha);
    }

    public Gerente autenticarGerente(String email, String senha) throws LocacaoException {
        return gerentes.autenticarGerente(email, senha);
    }

    public Cliente cadastrarCliente(Cliente cliente) throws LocacaoException {
        return clientes.cadastrarCliente(cliente);
    }

    public List<Cliente> buscarClientes() {
        return clientes.buscarClientes();
    }

    public List<Veiculo> buscarVeiculos(String tipo, String status) throws LocacaoException {
        return veiculos.buscarVeiculos(tipo, status);
    }

    public Veiculo buscarVeiculo(String placa) throws LocacaoException {
        return veiculos.buscarVeiculo(placa);
    }

    public boolean verificarDisponibilidade(String placa) throws LocacaoException {
        return veiculos.verificarDisponibilidade(placa);
    }

    public Veiculo cadastrarVeiculo(Veiculo veiculo) throws LocacaoException {
        return veiculos.cadastrarVeiculo(veiculo);
    }

    public Veiculo editarVeiculo(Long id, Map<String, Object> dadosAtualizados) throws LocacaoException {
        return veiculos.editarVeiculo(id, dadosAtualizados);
    }

    public Locacao realizarLocacao(model.Cliente cliente, Veiculo veiculo, Locacao locacao, FormaPagamento formaPagamento) throws LocacaoException {
        return locacoes.realizarLocacao(cliente, veiculo, locacao, formaPagamento);
    }

    public List<Locacao> consultarLocacoesCliente(Long clienteId) throws LocacaoException {
        return locacoes.consultarLocacoesCliente(clienteId);
    }

    public List<Locacao> consultarLocacoesVeiculo(String placa) throws LocacaoException {
        return locacoes.consultarLocacoesVeiculo(placa);
    }
}
