package br.edu.infnet.model;

import br.edu.infnet.enums.TipoStatus;

import java.time.LocalDateTime;

public class StatusPedido {
    private Long id;
    private String status;
    private String descricao;
    private LocalDateTime dataHoraAtualizacao;
    private String justificativa;
    private Pedido pedido;

    public StatusPedido() {
        this.dataHoraAtualizacao = LocalDateTime.now();
    }

    public StatusPedido(TipoStatus tipoStatus, String descricao) {
        this();
        this.status = tipoStatus.name();
        this.descricao = descricao != null ? descricao : tipoStatus.getDescricao();
    }

    public StatusPedido(String status, String descricao, String justificativa) {
        this();
        this.status = status;
        this.descricao = descricao;
        this.justificativa = justificativa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataHoraAtualizacao() {
        return dataHoraAtualizacao;
    }

    public void setDataHoraAtualizacao(LocalDateTime dataHoraAtualizacao) {
        this.dataHoraAtualizacao = dataHoraAtualizacao;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public boolean isStatusFinal() {
        return "ENTREGUE".equals(status) || "CANCELADO".equals(status);
    }

    public boolean isAtraso() {
        return "ATRASADO".equals(status) || "PROBLEMA_ENTREGA".equals(status);
    }

    @Override
    public String toString() {
        return "StatusPedido{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataHoraAtualizacao=" + dataHoraAtualizacao +
                ", justificativa='" + justificativa + '\'' +
                '}';
    }
}
