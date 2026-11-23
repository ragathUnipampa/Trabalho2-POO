package service;

import exception.LocacaoException;
import model.Gerente;

import java.util.List;

public interface GerenteServicePort {
    Gerente autenticarGerente(String email, String senha) throws LocacaoException;

    List<Gerente> buscarGerentes();
}
