package br.edu.infnet.model;

import br.edu.infnet.enums.TipoCanal;
import br.edu.infnet.enums.TipoEvento;

import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Set;

public class PreferenciasNotificacao {
    private Long id;
    private Cliente cliente;
    private Set<TipoCanal> canaisPreferidos;
    private Set<TipoEvento> eventosParaNotificar;
    private LocalTime horarioInicioPreferido;
    private LocalTime horarioFimPreferido;
    private boolean receberNotificacaoAtraso;
    private boolean receberNotificacaoEntrega;
    private boolean receberNotificacaoStatus;

    public PreferenciasNotificacao() {
        this.canaisPreferidos = EnumSet.allOf(TipoCanal.class);
        this.eventosParaNotificar = EnumSet.allOf(TipoEvento.class);
        this.horarioInicioPreferido = LocalTime.of(8, 0);
        this.horarioFimPreferido = LocalTime.of(22, 0);
        this.receberNotificacaoAtraso = true;
        this.receberNotificacaoEntrega = true;
        this.receberNotificacaoStatus = true;
    }

    public PreferenciasNotificacao(Cliente cliente) {
        this();
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Set<TipoCanal> getCanaisPreferidos() {
        return canaisPreferidos;
    }

    public void setCanaisPreferidos(Set<TipoCanal> canaisPreferidos) {
        this.canaisPreferidos = canaisPreferidos;
    }

    public Set<TipoEvento> getEventosParaNotificar() {
        return eventosParaNotificar;
    }

    public void setEventosParaNotificar(Set<TipoEvento> eventosParaNotificar) {
        this.eventosParaNotificar = eventosParaNotificar;
    }

    public LocalTime getHorarioInicioPreferido() {
        return horarioInicioPreferido;
    }

    public void setHorarioInicioPreferido(LocalTime horarioInicioPreferido) {
        this.horarioInicioPreferido = horarioInicioPreferido;
    }

    public LocalTime getHorarioFimPreferido() {
        return horarioFimPreferido;
    }

    public void setHorarioFimPreferido(LocalTime horarioFimPreferido) {
        this.horarioFimPreferido = horarioFimPreferido;
    }

    public boolean isReceberNotificacaoAtraso() {
        return receberNotificacaoAtraso;
    }

    public void setReceberNotificacaoAtraso(boolean receberNotificacaoAtraso) {
        this.receberNotificacaoAtraso = receberNotificacaoAtraso;
    }

    public boolean isReceberNotificacaoEntrega() {
        return receberNotificacaoEntrega;
    }

    public void setReceberNotificacaoEntrega(boolean receberNotificacaoEntrega) {
        this.receberNotificacaoEntrega = receberNotificacaoEntrega;
    }

    public boolean isReceberNotificacaoStatus() {
        return receberNotificacaoStatus;
    }

    public void setReceberNotificacaoStatus(boolean receberNotificacaoStatus) {
        this.receberNotificacaoStatus = receberNotificacaoStatus;
    }

    public void adicionarCanalPreferido(TipoCanal canal) {
        this.canaisPreferidos.add(canal);
    }

    public void removerCanalPreferido(TipoCanal canal) {
        this.canaisPreferidos.remove(canal);
    }

    public void adicionarEventoParaNotificar(TipoEvento evento) {
        this.eventosParaNotificar.add(evento);
    }

    public void removerEventoParaNotificar(TipoEvento evento) {
        this.eventosParaNotificar.remove(evento);
    }

    public boolean deveNotificarEvento(TipoEvento evento) {
        return eventosParaNotificar.contains(evento);
    }

    public boolean deveNotificarPorCanal(TipoCanal canal) {
        return canaisPreferidos.contains(canal);
    }

    public boolean estaNoHorarioPreferido(LocalTime horario) {
        return !horario.isBefore(horarioInicioPreferido) && !horario.isAfter(horarioFimPreferido);
    }

    @Override
    public String toString() {
        return "PreferenciasNotificacao{" +
                "id=" + id +
                ", canaisPreferidos=" + canaisPreferidos +
                ", eventosParaNotificar=" + eventosParaNotificar +
                ", horarioInicioPreferido=" + horarioInicioPreferido +
                ", horarioFimPreferido=" + horarioFimPreferido +
                ", receberNotificacaoAtraso=" + receberNotificacaoAtraso +
                ", receberNotificacaoEntrega=" + receberNotificacaoEntrega +
                ", receberNotificacaoStatus=" + receberNotificacaoStatus +
                '}';
    }
}
