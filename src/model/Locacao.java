package model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representa uma operação de aluguel (locação) de veículo realizada por um cliente.
 * Vincula o cliente ao veículo locado, registrando o período de vigência,
 * quantidade de diárias, valor total calculado e forma de pagamento utilizada.
 */
public class Locacao {

    private Long id;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("cliente_id")
    private Long clienteId;

    @SerializedName("data_inicio")
    private LocalDate dataInicio;

    @SerializedName("data_fim")
    private LocalDate dataFim;

    @SerializedName("dias_alugados")
    private Integer diasAlugados;

    @SerializedName("valor_total")
    private BigDecimal valorTotal;

    @SerializedName("forma_pagamento")
    private String formaPagamento;

    @SerializedName("veiculo_placa")
    private String veiculoPlaca;

    /**
     * Construtor padrão sem argumentos.
     */
    public Locacao() {
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

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * Define a data de início da locação convertendo uma string no padrão ISO ({@code AAAA-MM-DD}).
     *
     * @param dataInicio A data inicial em formato textual, ou {@code null}/vazio para limpar o campo.
     * @throws java.time.format.DateTimeParseException Se o texto não estiver em um formato de data válido.
     */
    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio == null || dataInicio.isBlank() ? null : LocalDate.parse(dataInicio);
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    /**
     * Define a data de término da locação convertendo uma string no padrão ISO ({@code AAAA-MM-DD}).
     *
     * @param dataFim A data final em formato textual, ou {@code null}/vazio para limpar o campo.
     * @throws java.time.format.DateTimeParseException Se o texto não estiver em um formato de data válido.
     */
    public void setDataFim(String dataFim) {
        this.dataFim = dataFim == null || dataFim.isBlank() ? null : LocalDate.parse(dataFim);
    }

    public Integer getDiasAlugados() {
        return diasAlugados;
    }

    public void setDiasAlugados(Integer diasAlugados) {
        this.diasAlugados = diasAlugados;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getVeiculoPlaca() {
        return veiculoPlaca;
    }

    public void setVeiculoPlaca(String veiculoPlaca) {
        this.veiculoPlaca = veiculoPlaca;
    }
}
