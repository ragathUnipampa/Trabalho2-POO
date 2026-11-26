package exception;

/**
 * Exceção verificada (checked exception) específica do domínio da locadora de veículos.
 * Utilizada para sinalizar violações de regras de negócio, falhas de validação de entrada
 * e erros tratados na comunicação com a camada de persistência.
 */
public class LocacaoException extends Exception {

    /**
     * Constrói uma nova exceção de domínio com a mensagem descritiva do erro ocorrido.
     *
     * @param mensagem A descrição clara do motivo da falha ou regra violada.
     */
    public LocacaoException(String mensagem){

        super(mensagem);
    }
}
