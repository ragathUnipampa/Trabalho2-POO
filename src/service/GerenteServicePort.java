package service;

import exception.LocacaoException;
import model.Gerente;

import java.util.List;

/**
 * Interface de caso de uso que define as operações de negócio referentes aos
 * administradores ({@link Gerente}) da locadora.
 */
public interface GerenteServicePort {

    /**
     * Valida as credenciais administrativas e autentica um gerente no sistema.
     *
     * @param email O e-mail institucional do gerente.
     * @param senha A senha de acesso informada.
     * @return O {@link Gerente} autenticado, ou {@code null} caso o e-mail ou senha sejam inválidos.
     * @throws LocacaoException Se o e-mail ou senha forem nulos/vazios ou ocorrer erro na comunicação com a base.
     */
    Gerente autenticarGerente(String email, String senha) throws LocacaoException;

    /**
     * Consulta todos os gerentes cadastrados no sistema.
     *
     * @return Uma {@link List} contendo os objetos {@link Gerente} encontrados.
     */
    List<Gerente> buscarGerentes();
}
