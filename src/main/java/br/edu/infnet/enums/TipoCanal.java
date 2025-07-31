package br.edu.infnet.enums;

public enum TipoCanal {
    EMAIL("E-mail"),
    SMS("SMS"),
    PUSH("Push Notification"),
    WHATSAPP("WhatsApp");

    private final String descricao;

    TipoCanal(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
