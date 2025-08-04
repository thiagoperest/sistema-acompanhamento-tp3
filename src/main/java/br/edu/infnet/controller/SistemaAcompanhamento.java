package br.edu.infnet.controller;

import br.edu.infnet.enums.TipoStatus;
import br.edu.infnet.model.*;
import br.edu.infnet.util.CSVHandler;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SistemaAcompanhamento {

    private Map<Long, Cliente> clientes;
    private Map<Long, Pedido> pedidos;
    private Map<Long, StatusPedido> statusPedidos;
    private Map<Long, Avaliacao> avaliacoes;
    private Cliente clienteLogado;

    public SistemaAcompanhamento() {
        this.clientes = new HashMap<>();
        this.pedidos = new HashMap<>();
        this.statusPedidos = new HashMap<>();
        this.avaliacoes = new HashMap<>();
        carregarDadosIniciais();
    }

    public boolean fazerLogin(String email, String senha) {
        for (Cliente cliente : clientes.values()) {
            if (cliente.autenticar(email, senha)) {
                this.clienteLogado = cliente;
                System.out.println("Login realizado com sucesso para: " + cliente.getNome());
                return true;
            }
        }
        System.out.println("Credenciais invalidas!");
        return false;
    }

    public void logout() {
        this.clienteLogado = null;
        System.out.println("Logout realizado com sucesso!");
    }

    public List<Pedido> visualizarListaPedidos() {
        if (clienteLogado == null) {
            System.out.println("Usuario nao autenticado!");
            return new ArrayList<>();
        }

        List<Pedido> pedidosCliente = clienteLogado.getPedidos();

        if (pedidosCliente.isEmpty()) {
            System.out.println("Nenhum pedido encontrado para o cliente: " + clienteLogado.getNome());
        } else {
            System.out.println("\n=== LISTA DE PEDIDOS ===");
            for (Pedido pedido : pedidosCliente) {
                System.out.printf("Pedido: %s | Data: %s | Status: %s%n",
                        pedido.getNumeroPedido(),
                        pedido.getDataCompra().toLocalDate(),
                        pedido.getStatusAtual() != null ? pedido.getStatusAtual().getStatus() : "N/A"
                );
            }
        }

        return pedidosCliente;
    }

    public Pedido visualizarDetalhesPedido(String numeroPedido) {
        if (clienteLogado == null) {
            System.out.println("Usuario nao autenticado!");
            return null;
        }

        Pedido pedido = clienteLogado.getPedidos().stream()
                .filter(p -> p.getNumeroPedido().equals(numeroPedido))
                .findFirst()
                .orElse(null);

        if (pedido == null) {
            System.out.println("Pedido nao encontrado: " + numeroPedido);
            return null;
        }

        System.out.println("\n=== DETALHES DO PEDIDO ===");
        System.out.println("Numero: " + pedido.getNumeroPedido());
        System.out.println("Data da Compra: " + pedido.getDataCompra());
        System.out.println("Valor Total: R$ " + pedido.getValorTotal());
        System.out.println("Codigo de Rastreamento: " +
                (pedido.getCodigoRastreamento() != null ? pedido.getCodigoRastreamento() : "N/A"));
        System.out.println("Previsao de Entrega: " +
                (pedido.getPrevisaoEntrega() != null ? pedido.getPrevisaoEntrega() : "N/A"));

        if (pedido.getStatusAtual() != null) {
            System.out.println("Status Atual: " + pedido.getStatusAtual().getStatus());
            System.out.println("Descricao: " + pedido.getStatusAtual().getDescricao());
        }

        System.out.println("\n=== HISTORICO DE STATUS ===");
        for (StatusPedido status : pedido.getHistoricoStatus()) {
            System.out.printf("Horario: %s - Status: %s | Descricao: %s%n",
                    status.getDataHoraAtualizacao(),
                    status.getStatus(),
                    status.getDescricao()
            );
        }

        return pedido;
    }

    public List<Pedido> filtrarPedidos(String status, LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (clienteLogado == null) {
            System.out.println("Usuario nao autenticado!");
            return new ArrayList<>();
        }

        return clienteLogado.getPedidos().stream()
                .filter(pedido -> {
                    boolean matches = true;

                    if (status != null && !status.isEmpty()) {
                        matches = matches && pedido.getStatusAtual() != null &&
                                pedido.getStatusAtual().getStatus().equalsIgnoreCase(status);
                    }

                    if (dataInicio != null) {
                        matches = matches && !pedido.getDataCompra().isBefore(dataInicio);
                    }

                    if (dataFim != null) {
                        matches = matches && !pedido.getDataCompra().isAfter(dataFim);
                    }

                    return matches;
                })
                .collect(Collectors.toList());
    }

    public boolean avaliarExperienciaEntrega(String numeroPedido, int notaAcompanhamento,
                                             int notaRecebimento, String comentarios) {
        if (clienteLogado == null) {
            System.out.println("Usuario nao autenticado!");
            return false;
        }

        Pedido pedido = clienteLogado.getPedidos().stream()
                .filter(p -> p.getNumeroPedido().equals(numeroPedido))
                .findFirst()
                .orElse(null);

        if (pedido == null) {
            System.out.println("Pedido nao encontrado: " + numeroPedido);
            return false;
        }

        if (!pedido.isEntregue()) {
            System.out.println("Pedido ainda nao foi entregue. Nao e possivel avaliar.");
            return false;
        }

        if (pedido.getAvaliacao() != null) {
            System.out.println("Pedido ja foi avaliado anteriormente.");
            return false;
        }

        try {
            Avaliacao avaliacao = new Avaliacao(notaAcompanhamento, notaRecebimento, comentarios, pedido);
            avaliacao.setId(System.currentTimeMillis());
            pedido.setAvaliacao(avaliacao);
            avaliacoes.put(avaliacao.getId(), avaliacao);

            System.out.println("Avaliacao registrada com sucesso!");
            System.out.println("Media das notas: " + avaliacao.getMediaNotas());
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Erro na avaliacao: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizarDadosContato(String novoTelefone, String novoEmail, String novoEndereco) {
        if (clienteLogado == null) {
            System.out.println("Usuario nao autenticado!");
            return false;
        }

        if (novoTelefone != null && !novoTelefone.trim().isEmpty()) {
            clienteLogado.setTelefone(novoTelefone.trim());
        }

        if (novoEmail != null && !novoEmail.trim().isEmpty() && novoEmail.contains("@")) {
            clienteLogado.setEmail(novoEmail.trim());
        }

        if (novoEndereco != null && !novoEndereco.trim().isEmpty()) {
            clienteLogado.setEndereco(novoEndereco.trim());
        }

        System.out.println("Dados de contato atualizados com sucesso!");
        return true;
    }

    public String compartilharStatusPedido(String numeroPedido) {
        if (clienteLogado == null) {
            System.out.println("Usuario nao autenticado!");
            return null;
        }

        Pedido pedido = clienteLogado.getPedidos().stream()
                .filter(p -> p.getNumeroPedido().equals(numeroPedido))
                .findFirst()
                .orElse(null);

        if (pedido == null) {
            System.out.println("Pedido nao encontrado: " + numeroPedido);
            return null;
        }

        String linkCompartilhamento = "https://ecommerce-infnet.com/pedido/share/" +
                UUID.randomUUID().toString() +
                "?pedido=" + numeroPedido;

        System.out.println("Link de compartilhamento gerado:");
        System.out.println(linkCompartilhamento);
        System.out.println("Este link expira em 24 horas.");

        return linkCompartilhamento;
    }

    public void atualizarStatusPedido(String numeroPedido, TipoStatus novoStatus, String justificativa) {
        Pedido pedido = pedidos.values().stream()
                .filter(p -> p.getNumeroPedido().equals(numeroPedido))
                .findFirst()
                .orElse(null);

        if (pedido != null) {
            StatusPedido status = new StatusPedido(novoStatus, novoStatus.getDescricao());
            status.setId(System.currentTimeMillis());
            status.setJustificativa(justificativa);

            pedido.atualizarStatus(status);
            statusPedidos.put(status.getId(), status);

            System.out.println("Status do pedido " + numeroPedido + " atualizado para: " + novoStatus);
        }
    }

    private void carregarDadosIniciais() {
        criarDadosExemplo();
    }

    public void carregarDadosCSV(String diretorio) {
        try {
            List<Cliente> listaClientes = CSVHandler.lerClientes(diretorio + "/clientes.csv");
            List<Pedido> listaPedidos = new ArrayList<>();
            List<StatusPedido> listaStatus = new ArrayList<>();

            if (!listaClientes.isEmpty()) {
                this.clientes = CSVHandler.criarMapaPorId(listaClientes, Cliente::getId);

                listaPedidos = CSVHandler.lerPedidos(diretorio + "/pedidos.csv", this.clientes);
                this.pedidos = CSVHandler.criarMapaPorId(listaPedidos, Pedido::getId);

                listaStatus = CSVHandler.lerStatusPedidos(diretorio + "/status.csv", this.pedidos);
                this.statusPedidos = CSVHandler.criarMapaPorId(listaStatus, StatusPedido::getId);

                System.out.println("Dados carregados com sucesso do diretorio: " + diretorio);
                System.out.println("Estatisticas:");
                System.out.println("    - Clientes: " + clientes.size());
                System.out.println("    - Pedidos: " + pedidos.size());
                System.out.println("    - Status: " + statusPedidos.size());
            } else {
                System.out.println("Nenhum arquivo CSV valido encontrado no diretorio: " + diretorio);
                System.out.println("Mantendo dados de exemplo existentes.");
            }

        } catch (Exception e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            System.out.println("Mantendo dados de exemplo existentes.");
        }
    }

    public void salvarDadosCSV(String diretorio) {
        try {
            if (!CSVHandler.garantirDiretorioExiste(diretorio)) {
                System.err.println("Nao foi possivel criar o diretorio: " + diretorio);
                return;
            }

            CSVHandler.escreverClientes(new ArrayList<>(clientes.values()), diretorio + "/clientes.csv");
            CSVHandler.escreverPedidos(new ArrayList<>(pedidos.values()), diretorio + "/pedidos.csv");
            CSVHandler.escreverStatusPedidos(new ArrayList<>(statusPedidos.values()), diretorio + "/status.csv");

            System.out.println("Dados salvos com sucesso no diretorio: " + diretorio);
            System.out.println("Arquivos criados:");
            System.out.println("    - clientes.csv (" + clientes.size() + " registros)");
            System.out.println("    - pedidos.csv (" + pedidos.size() + " registros)");
            System.out.println("    - status.csv (" + statusPedidos.size() + " registros)");

        } catch (Exception e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void criarDadosExemplo() {
        Cliente cliente1 = new Cliente("Joao Silva", "joao@email.com", "123456");
        cliente1.setId(1L);
        cliente1.setTelefone("(11) 99999-9999");
        cliente1.setEndereco("Rua das Flores, 123, Sao Paulo, SP");

        clientes.put(1L, cliente1);

        Pedido pedido1 = new Pedido("PED-2025-001", new BigDecimal("299.99"), cliente1);
        pedido1.setId(1L);
        pedido1.setCodigoRastreamento("BR123456789");
        pedido1.setPrevisaoEntrega(LocalDateTime.now().plusDays(3));

        StatusPedido status1 = new StatusPedido(TipoStatus.EM_TRANSITO, "Produto em transito");
        status1.setId(1L);
        pedido1.atualizarStatus(status1);

        cliente1.adicionarPedido(pedido1);
        pedidos.put(1L, pedido1);
        statusPedidos.put(1L, status1);

        Pedido pedido2 = new Pedido("PED-2025-002", new BigDecimal("156.50"), cliente1);
        pedido2.setId(2L);
        pedido2.setCodigoRastreamento("BR987654321");
        pedido2.setPrevisaoEntrega(LocalDateTime.now().plusDays(5));

        StatusPedido status2 = new StatusPedido(TipoStatus.PREPARANDO_ENVIO, "Preparando para envio");
        status2.setId(2L);
        pedido2.atualizarStatus(status2);

        cliente1.adicionarPedido(pedido2);
        pedidos.put(2L, pedido2);
        statusPedidos.put(2L, status2);

        System.out.println("Credenciais de acesso:");
        System.out.println("    E-mail: " + cliente1.getEmail());
        System.out.println("    Senha: " + cliente1.getSenha());
    }

    public Cliente getClienteLogado() {
        return clienteLogado;
    }

    public Map<Long, Cliente> getClientes() {
        return clientes;
    }

    public Map<Long, Pedido> getPedidos() {
        return pedidos;
    }

    public Map<Long, Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public Map<Long, StatusPedido> getStatusPedidos() {
        return statusPedidos;
    }
}
