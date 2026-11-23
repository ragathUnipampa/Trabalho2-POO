package model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio == null || dataInicio.isBlank() ? null : LocalDate.parse(dataInicio);
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

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
