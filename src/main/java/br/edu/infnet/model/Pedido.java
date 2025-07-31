package br.edu.infnet.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Long id;
    private String numeroPedido;
    private LocalDateTime dataCompra;
    private LocalDateTime previsaoEntrega;
    private BigDecimal valorTotal;
    private StatusPedido statusAtual;
    private String codigoRastreamento;
    private Cliente cliente;
    private List<StatusPedido> historicoStatus;
    private List<Notificacao> notificacoes;
    private Avaliacao avaliacao;

    public Pedido() {
        this.historicoStatus = new ArrayList<>();
        this.notificacoes = new ArrayList<>();
        this.dataCompra = LocalDateTime.now();
    }

    public Pedido(String numeroPedido, BigDecimal valorTotal, Cliente cliente) {
        this();
        this.numeroPedido = numeroPedido;
        this.valorTotal = valorTotal;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }

    public LocalDateTime getPrevisaoEntrega() {
        return previsaoEntrega;
    }

    public void setPrevisaoEntrega(LocalDateTime previsaoEntrega) {
        this.previsaoEntrega = previsaoEntrega;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public StatusPedido getStatusAtual() {
        return statusAtual;
    }

    public void setStatusAtual(StatusPedido statusAtual) {
        this.statusAtual = statusAtual;
    }

    public String getCodigoRastreamento() {
        return codigoRastreamento;
    }

    public void setCodigoRastreamento(String codigoRastreamento) {
        this.codigoRastreamento = codigoRastreamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<StatusPedido> getHistoricoStatus() {
        return historicoStatus;
    }

    public void setHistoricoStatus(List<StatusPedido> historicoStatus) {
        this.historicoStatus = historicoStatus;
    }

    public List<Notificacao> getNotificacoes() {
        return notificacoes;
    }

    public void setNotificacoes(List<Notificacao> notificacoes) {
        this.notificacoes = notificacoes;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public void atualizarStatus(StatusPedido novoStatus) {
        this.historicoStatus.add(this.statusAtual);
        this.statusAtual = novoStatus;
        novoStatus.setPedido(this);
    }

    public void adicionarNotificacao(Notificacao notificacao) {
        this.notificacoes.add(notificacao);
        notificacao.setPedido(this);
    }

    public boolean isEntregue() {
        return statusAtual != null && "ENTREGUE".equals(statusAtual.getStatus());
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", numeroPedido='" + numeroPedido + '\'' +
                ", dataCompra=" + dataCompra +
                ", previsaoEntrega=" + previsaoEntrega +
                ", valorTotal=" + valorTotal +
                ", statusAtual=" + (statusAtual != null ? statusAtual.getStatus() : "N/A") +
                ", codigoRastreamento='" + codigoRastreamento + '\'' +
                '}';
    }
}
