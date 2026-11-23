package service;

import exception.LocacaoException;
import model.Gerente;
import repository.GerenteRepository;
import repository.GerenteRepositoryPort;

import java.util.List;

public class GerenteService implements GerenteServicePort {
    private final GerenteRepositoryPort gerenteRepository;

    public GerenteService() {
        this(new GerenteRepository());
    }

    public GerenteService(GerenteRepositoryPort gerenteRepository) {
        this.gerenteRepository = gerenteRepository;
    }

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

    public List<Gerente> buscarGerentes() {
        try {
            return gerenteRepository.buscarGerentes(null);
        } catch (RuntimeException e) {
            throw new IllegalStateException("Erro ao buscar gerentes: " + e.getMessage());
        }
    }
}
