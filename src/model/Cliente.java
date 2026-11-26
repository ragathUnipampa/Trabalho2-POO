package model;

import com.google.gson.annotations.SerializedName;
import enums.CategoriaCNH;

/**
 * Representa um cliente cadastrado no sistema de locação de veículos.
 * Armazena dados pessoais, informações de habilitação (CNH) e credenciais de acesso.
 */
public class Cliente {

    private Long id;

    @SerializedName("created_at")
    private String createdAt;

    private String nome;
    private String telefone;
    private String cnh;

    @SerializedName("categoria_cnh")
    private CategoriaCNH categoriaCnh;

    private String cep;

    @SerializedName("senha_cliente")
    private String senhaCliente;

    // Construtor sem argumentos (obrigatório para o Gson)
    /**
     * Construtor padrão sem argumentos, utilizado para desserialização e instanciação vazia.
     */
    public Cliente() {
    }

    // Construtor completo para criar instâncias facilmente
    /**
     * Constrói uma instância de cliente com todos os dados cadastrais obrigatórios.
     *
     * @param nome         O nome completo do cliente.
     * @param telefone     O telefone de contato do cliente.
     * @param cnh          O número de registro da Carteira Nacional de Habilitação.
     * @param categoriaCnh A {@link CategoriaCNH} correspondente à habilitação do cliente.
     * @param cep          O Código de Endereçamento Postal da residência do cliente.
     * @param senhaCliente A senha de acesso do cliente ao sistema.
     */
    public Cliente(String nome, String telefone, String cnh, CategoriaCNH categoriaCnh, String cep, String senhaCliente) {
        this.nome = nome;
        this.telefone = telefone;
        this.cnh = cnh;
        this.categoriaCnh = categoriaCnh;
        this.cep = cep;
        this.senhaCliente = senhaCliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public CategoriaCNH getCategoriaCnh() {
        return categoriaCnh;
    }

    public void setCategoriaCnh(CategoriaCNH categoriaCnh) {
        this.categoriaCnh = categoriaCnh;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getSenhaCliente() {
        return senhaCliente;
    }

    public void setSenhaCliente(String senhaCliente) {
        this.senhaCliente = senhaCliente;
    }
}