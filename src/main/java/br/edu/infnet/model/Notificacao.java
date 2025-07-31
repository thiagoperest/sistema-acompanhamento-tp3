package br.edu.infnet.model;

import br.edu.infnet.enums.StatusEnvio;
import br.edu.infnet.enums.TipoCanal;

import java.time.LocalDateTime;

public class Notificacao {
    private Long id;
    private String titulo;
    private String mensagem;
    private TipoCanal canal;
    private StatusEnvio statusEnvio;
    private LocalDateTime dataHoraEnvio;
    private LocalDateTime dataHoraLeitura;
    private int tentativasEnvio;
    private Pedido pedido;
    private Cliente cliente;

    public Notificacao() {
        this.statusEnvio = StatusEnvio.PENDENTE;
        this.tentativasEnvio = 0;
    }

    public Notificacao(String titulo, String mensagem, TipoCanal canal, Cliente cliente) {
        this();
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.canal = canal;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public TipoCanal getCanal() {
        return canal;
    }

    public void setCanal(TipoCanal canal) {
        this.canal = canal;
    }

    public StatusEnvio getStatusEnvio() {
        return statusEnvio;
    }

    public void setStatusEnvio(StatusEnvio statusEnvio) {
        this.statusEnvio = statusEnvio;
    }

    public LocalDateTime getDataHoraEnvio() {
        return dataHoraEnvio;
    }

    public void setDataHoraEnvio(LocalDateTime dataHoraEnvio) {
        this.dataHoraEnvio = dataHoraEnvio;
    }

    public LocalDateTime getDataHoraLeitura() {
        return dataHoraLeitura;
    }

    public void setDataHoraLeitura(LocalDateTime dataHoraLeitura) {
        this.dataHoraLeitura = dataHoraLeitura;
    }

    public int getTentativasEnvio() {
        return tentativasEnvio;
    }

    public void setTentativasEnvio(int tentativasEnvio) {
        this.tentativasEnvio = tentativasEnvio;
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

    public void marcarComoEnviado() {
        this.statusEnvio = StatusEnvio.ENVIADO;
        this.dataHoraEnvio = LocalDateTime.now();
        this.tentativasEnvio++;
    }

    public void marcarComoLido() {
        this.statusEnvio = StatusEnvio.LIDO;
        this.dataHoraLeitura = LocalDateTime.now();
    }

    public void incrementarTentativas() {
        this.tentativasEnvio++;
    }

    public boolean podeReenviar() {
        return tentativasEnvio < 3 && statusEnvio == StatusEnvio.FALHA;
    }

    @Override
    public String toString() {
        return "Notificacao{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", canal=" + canal +
                ", statusEnvio=" + statusEnvio +
                ", dataHoraEnvio=" + dataHoraEnvio +
                ", tentativasEnvio=" + tentativasEnvio +
                '}';
    }
}
