package br.edu.infnet.enums;

public enum StatusEnvio {
    PENDENTE("Pendente"),
    ENVIADO("Enviado"),
    ENTREGUE("Entregue"),
    LIDO("Lido"),
    FALHA("Falha no Envio"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusEnvio(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
