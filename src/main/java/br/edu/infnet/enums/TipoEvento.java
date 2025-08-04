package br.edu.infnet.enums;

public enum TipoEvento {
    CONFIRMACAO_PEDIDO("Confirmação do Pedido"),
    ATUALIZACAO_STATUS("Atualização de Status"),
    SAIDA_ENTREGA("Saída para Entrega"),
    ENTREGA_REALIZADA("Entrega Realizada"),
    ATRASO_IDENTIFICADO("Atraso Identificado"),
    PROBLEMA_ENTREGA("Problema na Entrega");

    private final String descricao;

    TipoEvento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
