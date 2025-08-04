package br.edu.infnet;

import br.edu.infnet.controller.SistemaAcompanhamento;
import br.edu.infnet.enums.TipoStatus;
import br.edu.infnet.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static SistemaAcompanhamento sistema;

    public static void main(String[] args) {
        sistema = new SistemaAcompanhamento();

        System.out.println("=================================================");
        System.out.println("    SISTEMA DE ACOMPANHAMENTO DE PEDIDOS");
        System.out.println("           Instituto Infnet - PB TP3");
        System.out.println("=================================================");
        System.out.println();

        if (args.length > 0) {
            sistema.carregarDadosCSV(args[0]);
        }

        boolean executando = true;

        while (executando) {
            if (sistema.getClienteLogado() == null) {
                executando = exibirMenuLogin();
            } else {
                executando = exibirMenuPrincipal();
            }
        }

        System.out.println("\nSistema encerrado!");
        scanner.close();
    }

    private static boolean exibirMenuLogin() {
        System.out.println("\n=== MENU DE ACESSO ===");
        System.out.println("1. Fazer Login");
        System.out.println("2. Carregar Dados CSV");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opcao: ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    realizarLogin();
                    break;
                case 2:
                    carregarDadosCSV();
                    break;
                case 0:
                    return false;
                default:
                    System.out.println("Opcao invalida!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Por favor, digite um numero valido!");
            scanner.nextLine();
        }

        return true;
    }

    private static boolean exibirMenuPrincipal() {
        Cliente cliente = sistema.getClienteLogado();
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("Bem-vindo(a), " + cliente.getNome() + "!");
        System.out.println();
        System.out.println("1. Visualizar Lista de Pedidos");
        System.out.println("2. Visualizar Detalhes de um Pedido");
        System.out.println("3. Filtrar/Buscar Pedidos");
        System.out.println("4. Avaliar Experiencia de Entrega");
        System.out.println("5. Atualizar Dados de Contato");
        System.out.println("6. Compartilhar Status de Pedido");
        System.out.println("7. Simular Atualizacao de Status");
        System.out.println("8. Salvar Dados em CSV");
        System.out.println("0. Logout");
        System.out.print("Escolha uma opcao: ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    visualizarListaPedidos();
                    break;
                case 2:
                    visualizarDetalhesPedido();
                    break;
                case 3:
                    filtrarBuscarPedidos();
                    break;
                case 4:
                    avaliarExperienciaEntrega();
                    break;
                case 5:
                    atualizarDadosContato();
                    break;
                case 6:
                    compartilharStatusPedido();
                    break;
                case 7:
                    simularAtualizacaoStatus();
                    break;
                case 8:
                    salvarDadosCSV();
                    break;
                case 0:
                    sistema.logout();
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Por favor, digite um numero valido!");
            scanner.nextLine();
        }

        return true;
    }

    private static void realizarLogin() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        if (!sistema.fazerLogin(email, senha)) {
            System.out.println("\nDica: Use joao@email.com / 123456 para os dados de exemplo");
        }
    }

    private static void visualizarListaPedidos() {
        System.out.println("\n=== LISTA DE PEDIDOS ===");
        List<Pedido> pedidos = sistema.visualizarListaPedidos();

        if (!pedidos.isEmpty()) {
            System.out.println("\nDeseja ver detalhes de algum pedido? (s/n)");
            if (scanner.nextLine().toLowerCase().startsWith("s")) {
                visualizarDetalhesPedido();
            }
        }
    }

    private static void visualizarDetalhesPedido() {
        System.out.println("\n=== DETALHES DO PEDIDO ===");
        System.out.print("Digite o numero do pedido: ");
        String numeroPedido = scanner.nextLine();

        Pedido pedido = sistema.visualizarDetalhesPedido(numeroPedido);

        if (pedido != null) {
            System.out.println("\nOpcoes:");
            System.out.println("1. Compartilhar este pedido");
            System.out.println("2. Avaliar experiencia (se entregue)");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        String link = sistema.compartilharStatusPedido(numeroPedido);
                        break;
                    case 2:
                        if (pedido.isEntregue()) {
                            avaliarPedidoEspecifico(numeroPedido);
                        } else {
                            System.out.println("Pedido ainda nao foi entregue.");
                        }
                        break;
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
        }
    }

    private static void filtrarBuscarPedidos() {
        System.out.println("\n=== FILTRAR/BUSCAR PEDIDOS ===");
        System.out.println("Deixe em branco para nao filtrar por esse criterio");

        System.out.print("Status (ex: EM_TRANSITO, ENTREGUE): ");
        String status = scanner.nextLine();
        if (status.trim().isEmpty()) status = null;

        LocalDateTime dataInicio = null;
        LocalDateTime dataFim = null;

        System.out.print("Data inicio (dd/MM/yyyy HH:mm): ");
        String dataInicioStr = scanner.nextLine();
        if (!dataInicioStr.trim().isEmpty()) {
            try {
                dataInicio = LocalDateTime.parse(dataInicioStr, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Data invalida, ignorando filtro de data inicio.");
            }
        }

        System.out.print("Data fim (dd/MM/yyyy HH:mm): ");
        String dataFimStr = scanner.nextLine();
        if (!dataFimStr.trim().isEmpty()) {
            try {
                dataFim = LocalDateTime.parse(dataFimStr, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Data invalida, ignorando filtro de data fim.");
            }
        }

        List<Pedido> pedidosFiltrados = sistema.filtrarPedidos(status, dataInicio, dataFim);

        System.out.println("\n=== RESULTADOS DA BUSCA ===");
        if (pedidosFiltrados.isEmpty()) {
            System.out.println("Nenhum pedido encontrado com os criterios especificados.");
        } else {
            for (Pedido pedido : pedidosFiltrados) {
                System.out.printf("Pedido: %s | Data: %s | Status: %s | Valor: R$ %s%n",
                        pedido.getNumeroPedido(),
                        pedido.getDataCompra().format(DATE_FORMATTER),
                        pedido.getStatusAtual() != null ? pedido.getStatusAtual().getStatus() : "N/A",
                        pedido.getValorTotal()
                );
            }
        }
    }

    private static void avaliarExperienciaEntrega() {
        System.out.println("\n=== AVALIAR EXPERIENCIA DE ENTREGA ===");
        System.out.print("Digite o numero do pedido: ");
        String numeroPedido = scanner.nextLine();

        avaliarPedidoEspecifico(numeroPedido);
    }

    private static void avaliarPedidoEspecifico(String numeroPedido) {
        try {
            System.out.print("Nota para acompanhamento (1-5): ");
            int notaAcompanhamento = scanner.nextInt();

            System.out.print("Nota para recebimento (1-5): ");
            int notaRecebimento = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Comentarios (opcional): ");
            String comentarios = scanner.nextLine();

            sistema.avaliarExperienciaEntrega(numeroPedido, notaAcompanhamento, notaRecebimento, comentarios);

        } catch (InputMismatchException e) {
            System.out.println("Por favor, digite numeros validos para as notas!");
            scanner.nextLine();
        }
    }

    private static void atualizarDadosContato() {
        System.out.println("\n=== ATUALIZAR DADOS DE CONTATO ===");
        System.out.println("Deixe em branco para manter o valor atual");

        System.out.print("Novo telefone: ");
        String telefone = scanner.nextLine();

        System.out.print("Novo e-mail: ");
        String email = scanner.nextLine();

        System.out.print("Novo endereco: ");
        String endereco = scanner.nextLine();

        sistema.atualizarDadosContato(telefone, email, endereco);
    }

    private static void compartilharStatusPedido() {
        System.out.println("\n=== COMPARTILHAR STATUS DO PEDIDO ===");
        System.out.print("Digite o numero do pedido: ");
        String numeroPedido = scanner.nextLine();

        String link = sistema.compartilharStatusPedido(numeroPedido);

        if (link != null) {
            System.out.println("\nOpcoes de compartilhamento:");
            System.out.println("1. Copiar link");
            System.out.println("2. Simular envio por e-mail");
            System.out.println("3. Simular envio por WhatsApp");
            System.out.print("Escolha: ");

            try {
                int opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        System.out.println("Link copiado para area de transferencia (simulado)");
                        break;
                    case 2:
                        System.out.print("E-mail destinatario: ");
                        String email = scanner.nextLine();
                        System.out.println("E-mail enviado para " + email + " (simulado)");
                        break;
                    case 3:
                        System.out.print("Numero WhatsApp: ");
                        String whatsapp = scanner.nextLine();
                        System.out.println("Mensagem enviada para " + whatsapp + " (simulado)");
                        break;
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
        }
    }

    private static void simularAtualizacaoStatus() {
        System.out.println("\n=== SIMULAR ATUALIZACAO DE STATUS ===");
        System.out.print("Digite o numero do pedido: ");
        String numeroPedido = scanner.nextLine();

        System.out.println("\nStatus disponiveis:");
        TipoStatus[] statusDisponiveis = TipoStatus.values();
        for (int i = 0; i < statusDisponiveis.length; i++) {
            System.out.printf("%d. %s - %s%n", i + 1,
                    statusDisponiveis[i].name(),
                    statusDisponiveis[i].getDescricao());
        }

        try {
            System.out.print("Escolha o novo status (numero): ");
            int escolha = scanner.nextInt() - 1;
            scanner.nextLine();

            if (escolha >= 0 && escolha < statusDisponiveis.length) {
                TipoStatus novoStatus = statusDisponiveis[escolha];

                System.out.print("Justificativa (opcional): ");
                String justificativa = scanner.nextLine();

                sistema.atualizarStatusPedido(numeroPedido, novoStatus, justificativa);

                System.out.println("\nStatus atualizado com sucesso!");
            } else {
                System.out.println("Opcao invalida!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Por favor, digite um numero valido!");
            scanner.nextLine();
        }
    }

    private static void carregarDadosCSV() {
        System.out.println("\n=== CARREGAR DADOS CSV ===");
        System.out.print("Digite o diretorio dos arquivos CSV: ");
        String diretorio = scanner.nextLine();

        if (diretorio.trim().isEmpty()) {
            diretorio = "./data";
        }

        sistema.carregarDadosCSV(diretorio);
    }

    private static void salvarDadosCSV() {
        System.out.println("\n=== SALVAR DADOS CSV ===");
        System.out.print("Digite o diretorio para salvar (Enter para ./data): ");
        String diretorio = scanner.nextLine();

        if (diretorio.trim().isEmpty()) {
            diretorio = "./data";
        }

        sistema.salvarDadosCSV(diretorio);
    }
}