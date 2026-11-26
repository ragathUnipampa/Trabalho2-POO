package model;

import com.google.gson.annotations.SerializedName;
import enums.StatusVeiculo;
import enums.TipoVeiculo;

import java.math.BigDecimal;

/**
 * Representa um veículo pertencente à frota da locadora.
 * Contém informações de identificação, categoria, disponibilidade e tarifa diária,
 * além da regra de cálculo do valor bruto de locação.
 */
public class Veiculo {
    private Long id;

    @SerializedName("created_at")
    private String createdAt;
    private String modelo;
    private String marca;
    private String placa;
    private Integer ano;

    @SerializedName("tipo_veiculo")
    private String tipoVeiculo;
    private String statusVeiculo;

    @SerializedName("valor_diario")
    private BigDecimal valorDiario;

    /**
     * Construtor padrão sem argumentos.
     */
    public Veiculo() {

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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipoVeiculo() {
        return tipoVeiculo;
    }

    /**
     * Define o tipo do veículo a partir de uma string, validando e normalizando
     * o valor por meio de {@link TipoVeiculo#fromValue(String)}.
     *
     * @param tipoVeiculo O texto representando o tipo do veículo, ou {@code null}.
     * @throws IllegalArgumentException Se o valor informado não corresponder a um tipo válido.
     */
    public void setTipoVeiculo(String tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo != null ? TipoVeiculo.fromValue(tipoVeiculo).getValor() : null;
    }

    /**
     * Define o tipo do veículo diretamente a partir da enumeração {@link TipoVeiculo}.
     *
     * @param tipoVeiculo A constante {@link TipoVeiculo}, ou {@code null}.
     */
    public void setTipoVeiculo(TipoVeiculo tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo == null ? null : tipoVeiculo.getValor();
    }

    /**
     * Converte e retorna o tipo atual do veículo como uma constante {@link TipoVeiculo}.
     *
     * @return A constante {@link TipoVeiculo} correspondente, ou {@code null} se não estiver definido.
     */
    public TipoVeiculo getTipoVeiculoEnum() {
        return tipoVeiculo == null ? null : TipoVeiculo.fromValue(tipoVeiculo);
    }

    public String getStatusVeiculo() {
        return statusVeiculo;
    }

    /**
     * Define o status de disponibilidade do veículo a partir de uma string, validando
     * e normalizando o valor por meio de {@link StatusVeiculo#fromValue(String)}.
     *
     * @param statusVeiculo O texto representando o status, ou {@code null}.
     * @throws IllegalArgumentException Se o valor informado não corresponder a um status válido.
     */
    public void setStatusVeiculo(String statusVeiculo) {
        this.statusVeiculo = statusVeiculo != null ? StatusVeiculo.fromValue(statusVeiculo).getValor() : null;
    }

    /**
     * Define o status de disponibilidade do veículo diretamente a partir de {@link StatusVeiculo}.
     *
     * @param statusVeiculo A constante {@link StatusVeiculo}, ou {@code null}.
     */
    public void setStatusVeiculo(StatusVeiculo statusVeiculo) {
        this.statusVeiculo = statusVeiculo == null ? null : statusVeiculo.getValor();
    }

    /**
     * Converte e retorna o status atual do veículo como uma constante {@link StatusVeiculo}.
     *
     * @return A constante {@link StatusVeiculo} correspondente, ou {@code null} se não estiver definido.
     */
    public StatusVeiculo getStatusVeiculoEnum() {
        return statusVeiculo == null ? null : StatusVeiculo.fromValue(statusVeiculo);
    }

    public BigDecimal getValorDiario() {
        return valorDiario;
    }

    public void setValorDiario(BigDecimal valorDiario) {
        this.valorDiario = valorDiario;
    }

    /**
     * Calcula o valor total bruto da locação multiplicando o valor da diária
     * pela quantidade de dias solicitados.
     *
     * @param dias A quantidade de diárias da locação.
     * @return O valor total calculado em {@link BigDecimal}.
     */
    public BigDecimal calcularValorLocacao(int dias) {
        return valorDiario.multiply(BigDecimal.valueOf(dias));
    }
}