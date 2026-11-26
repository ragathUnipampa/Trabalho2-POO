package repository;

import model.Gerente;

import java.util.List;

/**
 * Interface de porta (contrato) que define as operações de consulta e autenticação
 * para a entidade {@link Gerente}.
 */
public interface GerenteRepositoryPort {

    /**
     * Busca gerentes cadastrados filtrando pelo endereço de e-mail.
     *
     * @param email O e-mail do gerente a ser consultado (ou {@code null} para listar todos).
     * @return Uma lista de {@link Gerente} correspondentes ao filtro.
     */
    List<Gerente> buscarGerentes(String email);

    /**
     * Valida as credenciais de acesso de um gerente no sistema.
     *
     * @param email O e-mail institucional do gerente.
     * @param senha A senha de acesso informada.
     * @return O objeto {@link Gerente} autenticado, ou {@code null} se o e-mail não existir ou a senha estiver incorreta.
     */
    Gerente autenticarGerente(String email, String senha);
}
