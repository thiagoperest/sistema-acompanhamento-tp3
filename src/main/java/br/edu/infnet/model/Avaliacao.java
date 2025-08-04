package br.edu.infnet.model;

import java.time.LocalDateTime;


public class Avaliacao {
    private Long id;
    private int notaAcompanhamento;
    private int notaRecebimento;
    private String comentarios;
    private LocalDateTime dataAvaliacao;
    private Pedido pedido;
    private Cliente cliente;

    public Avaliacao() {
        this.dataAvaliacao = LocalDateTime.now();
    }

    public Avaliacao(int notaAcompanhamento, int notaRecebimento, String comentarios, Pedido pedido) {
        this();
        this.notaAcompanhamento = notaAcompanhamento;
        this.notaRecebimento = notaRecebimento;
        this.comentarios = comentarios;
        this.pedido = pedido;
        this.cliente = pedido.getCliente();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNotaAcompanhamento() {
        return notaAcompanhamento;
    }

    public void setNotaAcompanhamento(int notaAcompanhamento) {
        if (notaAcompanhamento >= 1 && notaAcompanhamento <= 5) {
            this.notaAcompanhamento = notaAcompanhamento;
        } else {
            throw new IllegalArgumentException("Nota deve estar entre 1 e 5");
        }
    }

    public int getNotaRecebimento() {
        return notaRecebimento;
    }

    public void setNotaRecebimento(int notaRecebimento) {
        if (notaRecebimento >= 1 && notaRecebimento <= 5) {
            this.notaRecebimento = notaRecebimento;
        } else {
            throw new IllegalArgumentException("Nota deve estar entre 1 e 5");
        }
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public LocalDateTime getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(LocalDateTime dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getMediaNotas() {
        return (notaAcompanhamento + notaRecebimento) / 2.0;
    }

    public boolean isAvaliacaoPositiva() {
        return getMediaNotas() >= 3.0;
    }

    @Override
    public String toString() {
        return "Avaliacao{" +
                "id=" + id +
                ", notaAcompanhamento=" + notaAcompanhamento +
                ", notaRecebimento=" + notaRecebimento +
                ", comentarios='" + comentarios + '\'' +
                ", dataAvaliacao=" + dataAvaliacao +
                ", mediaNotas=" + getMediaNotas() +
                '}';
    }
}
