package model;

import com.google.gson.annotations.SerializedName;

/**
 * Representa um administrador (gerente) da locadora de veículos.
 * Possui credenciais para autenticação no painel administrativo e gerenciamento da frota.
 */
public class Gerente {

    private Long id;

    @SerializedName("created_at")
    private String createdAt;

    private String nome;
    private String email;

    @SerializedName("senha_gerente")
    private String senhaGerente;

    /**
     * Construtor padrão sem argumentos, utilizado para desserialização e instanciação vazia.
     */
    public Gerente() {
    }

    /**
     * Constrói uma instância de gerente com nome, e-mail institucional e senha de acesso.
     *
     * @param nome         O nome completo do gerente.
     * @param email        O endereço de e-mail utilizado como login administrativo.
     * @param senhaGerente A senha de acesso do gerente.
     */
    public Gerente(String nome, String email, String senhaGerente) {
        this.nome = nome;
        this.email = email;
        this.senhaGerente = senhaGerente;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaGerente() {
        return senhaGerente;
    }

    public void setSenhaGerente(String senhaGerente) {
        this.senhaGerente = senhaGerente;
    }
}