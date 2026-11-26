package service;

import exception.LocacaoException;
import model.Cliente;
import model.Gerente;
import model.Locacao;
import model.Veiculo;
import pagamento.FormaPagamento;

import java.util.List;
import java.util.Map;

/**
 * Fachada (Facade) central que agrupa e expõe os serviços de casos de uso da aplicação
 * ({@link ClienteServicePort}, {@link GerenteServicePort}, {@link VeiculoServicePort} e
 * {@link LocacaoServicePort}), oferecendo um ponto único de acesso para a camada de controle.
 */
public final class AppServices {
    private final ClienteServicePort clientes;
    private final GerenteServicePort gerentes;
    private final VeiculoServicePort veiculos;
    private final LocacaoServicePort locacoes;

    /**
     * Constrói a fachada inicializando todas as implementações padrão dos serviços de domínio.
     */
    public AppServices() {
        this.clientes = new ClienteService();
        this.gerentes = new GerenteService();
        this.veiculos = new VeiculoService();
        this.locacoes = new LocacaoService();
    }

    /**
     * Constrói a fachada recebendo externamente as implementações dos serviços (Injeção de Dependência),
     * facilitando testes unitários e substituição de componentes.
     *
     * @param clientes O serviço responsável pelas operações de clientes.
     * @param gerentes O serviço responsável pelas operações de gerentes.
     * @param veiculos O serviço responsável pelo gerenciamento da frota de veículos.
     * @param locacoes O serviço responsável pelo fluxo de locação de veículos.
     */
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

    /**
     * Autentica um cliente no sistema a partir da CNH e senha.
     *
     * @param cnh   O número da CNH do cliente.
     * @param senha A senha de acesso do cliente.
     * @return O {@link Cliente} autenticado, ou {@code null} se as credenciais não conferirem.
     * @throws LocacaoException Se os dados informados forem inválidos ou ocorrer falha na consulta.
     */
    public Cliente autenticarCliente(String cnh, String senha) throws LocacaoException {
        return clientes.autenticarCliente(cnh, senha);
    }

    /**
     * Autentica um gerente administrativo a partir do e-mail e senha.
     *
     * @param email O e-mail institucional do gerente.
     * @param senha A senha de acesso do gerente.
     * @return O {@link Gerente} autenticado, ou {@code null} se as credenciais não conferirem.
     * @throws LocacaoException Se os dados informados forem inválidos ou ocorrer falha na consulta.
     */
    public Gerente autenticarGerente(String email, String senha) throws LocacaoException {
        return gerentes.autenticarGerente(email, senha);
    }

    /**
     * Realiza o cadastro de um novo cliente aplicando as validações de negócio.
     *
     * @param cliente O objeto {@link Cliente} a ser cadastrado.
     * @return O {@link Cliente} persistido com seu identificador gerado.
     * @throws LocacaoException Se os dados obrigatórios estiverem ausentes, a CNH já existir ou ocorrer erro de persistência.
     */
    public Cliente cadastrarCliente(Cliente cliente) throws LocacaoException {
        return clientes.cadastrarCliente(cliente);
    }

    /**
     * Retorna a lista completa de clientes cadastrados no sistema.
     *
     * @return Uma {@link List} contendo todos os {@link Cliente} registrados.
     */
    public List<Cliente> buscarClientes() {
        return clientes.buscarClientes();
    }

    /**
     * Consulta veículos da frota filtrando opcionalmente por tipo e status de disponibilidade.
     *
     * @param tipo   O tipo do veículo ({@code "carro"} ou {@code "moto"}), ou {@code null} para todos.
     * @param status O status do veículo ({@code "disponivel"} ou {@code "indisponivel"}), ou {@code null} para todos.
     * @return Uma {@link List} de {@link Veiculo} que atendem aos filtros.
     * @throws LocacaoException Se ocorrer falha ao consultar os veículos.
     */
    public List<Veiculo> buscarVeiculos(String tipo, String status) throws LocacaoException {
        return veiculos.buscarVeiculos(tipo, status);
    }

    /**
     * Busca um veículo específico pela sua placa de identificação.
     *
     * @param placa A placa do veículo desejado.
     * @return O {@link Veiculo} correspondente à placa.
     * @throws LocacaoException Se a placa for inválida ou nenhum veículo for encontrado.
     */
    public Veiculo buscarVeiculo(String placa) throws LocacaoException {
        return veiculos.buscarVeiculo(placa);
    }

    /**
     * Verifica se um veículo encontra-se disponível para nova locação.
     *
     * @param placa A placa do veículo a ser verificado.
     * @return {@code true} se o veículo estiver com status {@code DISPONIVEL}; {@code false} caso contrário.
     * @throws LocacaoException Se a placa for inválida ou o veículo não existir.
     */
    public boolean verificarDisponibilidade(String placa) throws LocacaoException {
        return veiculos.verificarDisponibilidade(placa);
    }

    /**
     * Cadastra um novo veículo na frota da locadora após validar seus dados e unicidade de placa.
     *
     * @param veiculo O objeto {@link Veiculo} a ser registrado.
     * @return O {@link Veiculo} cadastrado.
     * @throws LocacaoException Se os dados forem inválidos, a placa já estiver em uso ou ocorrer falha no banco.
     */
    public Veiculo cadastrarVeiculo(Veiculo veiculo) throws LocacaoException {
        return veiculos.cadastrarVeiculo(veiculo);
    }

    /**
     * Atualiza parcialmente os dados de um veículo existente.
     *
     * @param id               O identificador único do veículo.
     * @param dadosAtualizados Um {@link Map} contendo os campos e valores a serem alterados.
     * @return O {@link Veiculo} atualizado.
     * @throws LocacaoException Se nenhuma alteração for informada ou ocorrer erro na atualização.
     */
    public Veiculo editarVeiculo(Long id, Map<String, Object> dadosAtualizados) throws LocacaoException {
        return veiculos.editarVeiculo(id, dadosAtualizados);
    }

    /**
     * Efetiva a locação de um veículo para um cliente, validando habilitação, disponibilidade
     * e aplicando o cálculo da modalidade de pagamento escolhida.
     *
     * @param cliente        O {@link Cliente} que está alugando o veículo.
     * @param veiculo        O {@link Veiculo} selecionado para locação.
     * @param locacao        O objeto {@link Locacao} contendo período e valor bruto.
     * @param formaPagamento A estratégia de {@link FormaPagamento} selecionada.
     * @return A {@link Locacao} registrada com valor final ajustado.
     * @throws LocacaoException Se alguma regra de negócio (CNH, disponibilidade, valor) for violada.
     */
    public Locacao realizarLocacao(model.Cliente cliente, Veiculo veiculo, Locacao locacao, FormaPagamento formaPagamento) throws LocacaoException {
        return locacoes.realizarLocacao(cliente, veiculo, locacao, formaPagamento);
    }

    /**
     * Consulta o histórico de locações vinculadas a um determinado cliente.
     *
     * @param clienteId O identificador único do cliente.
     * @return Uma {@link List} de {@link Locacao} associadas ao cliente.
     * @throws LocacaoException Se ocorrer falha na consulta ao repositório.
     */
    public List<Locacao> consultarLocacoesCliente(Long clienteId) throws LocacaoException {
        return locacoes.consultarLocacoesCliente(clienteId);
    }

    /**
     * Consulta o histórico de locações registradas para um determinado veículo.
     *
     * @param placa A placa do veículo.
     * @return Uma {@link List} de {@link Locacao} associadas à placa informada.
     * @throws LocacaoException Se ocorrer falha na consulta ao repositório.
     */
    public List<Locacao> consultarLocacoesVeiculo(String placa) throws LocacaoException {
        return locacoes.consultarLocacoesVeiculo(placa);
    }
}
