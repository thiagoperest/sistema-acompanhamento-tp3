package br.edu.infnet.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String endereco;
    private LocalDateTime dataCadastro;
    private List<Pedido> pedidos;
    private PreferenciasNotificacao preferenciasNotificacao;

    public Cliente() {
        this.pedidos = new ArrayList<>();
        this.dataCadastro = LocalDateTime.now();
    }

    public Cliente(String nome, String email, String senha) {
        this();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public PreferenciasNotificacao getPreferenciasNotificacao() {
        return preferenciasNotificacao;
    }

    public void setPreferenciasNotificacao(PreferenciasNotificacao preferenciasNotificacao) {
        this.preferenciasNotificacao = preferenciasNotificacao;
    }

    public void adicionarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
        pedido.setCliente(this);
    }

    public boolean autenticar(String email, String senha) {
        return this.email.equals(email) && this.senha.equals(senha);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", endereco='" + endereco + '\'' +
                ", dataCadastro=" + dataCadastro +
                '}';
    }
}
