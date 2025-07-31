package br.edu.infnet.enums;

public enum TipoStatus {
    PEDIDO_CONFIRMADO("Pedido Confirmado"),
    PAGAMENTO_APROVADO("Pagamento Aprovado"),
    PREPARANDO_ENVIO("Preparando para Envio"),
    ENVIADO("Enviado"),
    EM_TRANSITO("Em Trânsito"),
    SAIU_PARA_ENTREGA("Saiu para Entrega"),
    ENTREGUE("Entregue"),
    ATRASADO("Atrasado"),
    PROBLEMA_ENTREGA("Problema na Entrega"),
    CANCELADO("Cancelado");

    private final String descricao;

    TipoStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
