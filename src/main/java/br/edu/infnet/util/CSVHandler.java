package br.edu.infnet.util;

import br.edu.infnet.model.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class CSVHandler {

    private static final String DELIMITER = ";";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<Cliente> lerClientes(String filePath) {
        List<Cliente> clientes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String linha = reader.readLine();

            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(DELIMITER);

                if (dados.length >= 5) {
                    Cliente cliente = new Cliente();
                    cliente.setId(Long.parseLong(dados[0]));
                    cliente.setNome(dados[1]);
                    cliente.setEmail(dados[2]);
                    cliente.setTelefone(dados[3]);
                    cliente.setEndereco(dados[4]);

                    if (dados.length > 5 && !dados[5].isEmpty()) {
                        cliente.setDataCadastro(LocalDateTime.parse(dados[5], DATE_FORMATTER));
                    }

                    clientes.add(cliente);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo de clientes: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro ao processar dados do cliente: " + e.getMessage());
        }

        return clientes;
    }

    public static void escreverClientes(List<Cliente> clientes, String filePath) {
        criarDiretorio(filePath);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("id;nome;email;telefone;endereco;dataCadastro");

            for (Cliente cliente : clientes) {
                writer.printf("%d;%s;%s;%s;%s;%s%n",
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getEmail(),
                        cliente.getTelefone() != null ? cliente.getTelefone() : "",
                        cliente.getEndereco() != null ? cliente.getEndereco() : "",
                        cliente.getDataCadastro().format(DATE_FORMATTER)
                );
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever arquivo de clientes: " + e.getMessage());
        }
    }

    public static List<Pedido> lerPedidos(String filePath, Map<Long, Cliente> clientes) {
        List<Pedido> pedidos = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String linha = reader.readLine();

            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(DELIMITER);

                if (dados.length >= 7) {
                    Pedido pedido = new Pedido();
                    pedido.setId(Long.parseLong(dados[0]));
                    pedido.setNumeroPedido(dados[1]);
                    pedido.setDataCompra(LocalDateTime.parse(dados[2], DATE_FORMATTER));

                    if (!dados[3].isEmpty()) {
                        pedido.setPrevisaoEntrega(LocalDateTime.parse(dados[3], DATE_FORMATTER));
                    }

                    pedido.setValorTotal(new BigDecimal(dados[4]));
                    pedido.setCodigoRastreamento(dados[5]);

                    Long clienteId = Long.parseLong(dados[6]);
                    Cliente cliente = clientes.get(clienteId);
                    if (cliente != null) {
                        pedido.setCliente(cliente);
                        cliente.adicionarPedido(pedido);
                    }

                    pedidos.add(pedido);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo de pedidos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro ao processar dados do pedido: " + e.getMessage());
        }

        return pedidos;
    }

    public static void escreverPedidos(List<Pedido> pedidos, String filePath) {
        criarDiretorio(filePath);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("id;numeroPedido;dataCompra;previsaoEntrega;valorTotal;codigoRastreamento;clienteId");

            for (Pedido pedido : pedidos) {
                writer.printf("%d;%s;%s;%s;%s;%s;%d%n",
                        pedido.getId(),
                        pedido.getNumeroPedido(),
                        pedido.getDataCompra().format(DATE_FORMATTER),
                        pedido.getPrevisaoEntrega() != null ? pedido.getPrevisaoEntrega().format(DATE_FORMATTER) : "",
                        pedido.getValorTotal(),
                        pedido.getCodigoRastreamento() != null ? pedido.getCodigoRastreamento() : "",
                        pedido.getCliente().getId()
                );
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever arquivo de pedidos: " + e.getMessage());
        }
    }

    public static List<StatusPedido> lerStatusPedidos(String filePath, Map<Long, Pedido> pedidos) {
        List<StatusPedido> statusList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String linha = reader.readLine();

            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(DELIMITER);

                if (dados.length >= 6) {
                    StatusPedido status = new StatusPedido();
                    status.setId(Long.parseLong(dados[0]));
                    status.setStatus(dados[1]);
                    status.setDescricao(dados[2]);
                    status.setDataHoraAtualizacao(LocalDateTime.parse(dados[3], DATE_FORMATTER));
                    status.setJustificativa(dados[4]);

                    Long pedidoId = Long.parseLong(dados[5]);
                    Pedido pedido = pedidos.get(pedidoId);
                    if (pedido != null) {
                        status.setPedido(pedido);
                        pedido.atualizarStatus(status);
                    }

                    statusList.add(status);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo de status: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro ao processar dados do status: " + e.getMessage());
        }

        return statusList;
    }

    public static void escreverStatusPedidos(List<StatusPedido> statusList, String filePath) {
        criarDiretorio(filePath);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("id;status;descricao;dataHoraAtualizacao;justificativa;pedidoId");

            for (StatusPedido status : statusList) {
                writer.printf("%d;%s;%s;%s;%s;%d%n",
                        status.getId(),
                        status.getStatus(),
                        status.getDescricao(),
                        status.getDataHoraAtualizacao().format(DATE_FORMATTER),
                        status.getJustificativa() != null ? status.getJustificativa() : "",
                        status.getPedido().getId()
                );
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever arquivo de status: " + e.getMessage());
        }
    }

    public static <T> Map<Long, T> criarMapaPorId(List<T> lista, java.util.function.Function<T, Long> getIdFunction) {
        Map<Long, T> mapa = new HashMap<>();
        for (T item : lista) {
            mapa.put(getIdFunction.apply(item), item);
        }
        return mapa;
    }

    private static void criarDiretorio(String filePath) {
        try {
            File arquivo = new File(filePath);
            File diretorio = arquivo.getParentFile();

            if (diretorio != null && !diretorio.exists()) {
                boolean criado = diretorio.mkdirs();
                if (criado) {
                    System.out.println("Diretorio criado: " + diretorio.getPath());
                } else {
                    System.err.println("Nao foi possivel criar o diretorio: " + diretorio.getPath());
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar diretorio: " + e.getMessage());
        }
    }

    public static boolean garantirDiretorioExiste(String diretorio) {
        try {
            File dir = new File(diretorio);

            if (!dir.exists()) {
                boolean criado = dir.mkdirs();
                if (criado) {
                    System.out.println("Diretorio criado: " + diretorio);
                    return true;
                } else {
                    System.err.println("Nao foi possivel criar o diretorio: " + diretorio);
                    return false;
                }
            }

            if (!dir.isDirectory()) {
                System.err.println("O caminho existe mas nao e um diretorio: " + diretorio);
                return false;
            }

            return true;
        } catch (Exception e) {
            System.err.println("Erro ao verificar/criar diretorio: " + e.getMessage());
            return false;
        }
    }
}
