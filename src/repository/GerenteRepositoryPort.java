package repository;

import model.Gerente;

import java.util.List;

public interface GerenteRepositoryPort {
    List<Gerente> buscarGerentes(String email);

    Gerente autenticarGerente(String email, String senha);
}
