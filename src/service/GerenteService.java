package service;

import exception.LocacaoException;
import model.Gerente;
import repository.GerenteRepository;
import repository.GerenteRepositoryPort;

import java.util.List;

/**
 * Implementação concreta de {@link GerenteServicePort} responsável pelas validações
 * de acesso e consulta dos administradores do sistema.
 */
public class GerenteService implements GerenteServicePort {
    private final GerenteRepositoryPort gerenteRepository;

    /**
     * Constrói o serviço de gerentes utilizando a implementação padrão de {@link GerenteRepository}.
     */
    public GerenteService() {
        this(new GerenteRepository());
    }

    /**
     * Constrói o serviço de gerentes recebendo a porta do repositório por injeção de dependência.
     *
     * @param gerenteRepository A implementação de {@link GerenteRepositoryPort} a ser utilizada.
     */
    public GerenteService(GerenteRepositoryPort gerenteRepository) {
        this.gerenteRepository = gerenteRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Gerente autenticarGerente(String email, String senha) throws LocacaoException {
        if (email == null || email.isBlank()) {
            throw new LocacaoException("E-mail inválido para autenticação.");
        }
        if (senha == null || senha.isBlank()) {
            throw new LocacaoException("Senha inválida para autenticação.");
        }

        try {
            return gerenteRepository.autenticarGerente(email, senha);
        } catch (RuntimeException e) {
            throw new LocacaoException("Erro ao autenticar gerente: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException Se ocorrer falha inesperada na camada de persistência ao consultar os gerentes.
     */
    @Override
    public List<Gerente> buscarGerentes() {
        try {
            return gerenteRepository.buscarGerentes(null);
        } catch (RuntimeException e) {
            throw new IllegalStateException("Erro ao buscar gerentes: " + e.getMessage());
        }
    }
}
