package br.senac.tads3.agenda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Agenda extends ConexaoBD {

    private static Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        Agenda instancia = new Agenda();

        do {
            System.out.println("\n***** DIGITE UMA OPÃ‡ÃƒO *****");
            System.out.println("(1) Listar contatos");
            System.out.println("(2) Incluir novo contato");
            System.out.println("(3) Alterar contato");
            System.out.println("(4) Deletar contato");
            System.out.println("(9) Sair\n");
            System.out.print("Opçãoo: ");

            String strOpcao = entrada.nextLine();
            int opcao = Integer.parseInt(strOpcao);
            switch (opcao) {
                case 1:
                    System.out.println("\n*** CONSULTA CONTATOS ***\n");
                    instancia.listar();
                    break;
                case 2:
                    System.out.println("\n*** CADASTRO DE CONTATO ***\n");
                    instancia.incluir();
                    break;
                case 3:
                    System.out.println("\n*** ALTERAR CONTATO ***");
                    instancia.alterar();
                    break;
                case 4:
                    System.out.println("\n*** DELETAR CONTATO");
                    instancia.deletar();
                    break;
                case 9:
                    System.exit(0);
                    break;
                default:
                    System.out.println("OPÃ‡ÃƒO INVÃ�LIDA");
            }
        } while (true);
    }

    public void incluir() {

        System.out.print("Digite o nome completo do contato: ");
        String nome = entrada.nextLine();

        System.out.print("Digite a data de nascimento no formato dd/mm/aaaa: ");
        String strDataNasc = entrada.nextLine();

        System.out.print("Digite o e-mail: ");
        String email = entrada.nextLine();

        System.out.print("Digite o telefone no formato 99 99999-9999: ");
        String telefone = entrada.nextLine();

        // 1) Abrir conexao
        PreparedStatement stmt = null;
        Connection conn = null;

        String sql = "INSERT INTO TB_CONTATO (NM_CONTATO, DT_NASCIMENTO, "
                + "VL_TELEFONE, VL_EMAIL, DT_CADASTRO) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            conn = obterConexao();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);

            DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
            Date dataNasc = null;
            try {
                dataNasc = formatador.parse(strDataNasc);
            } catch (ParseException ex) {
                System.out.println("Data de nascimento invÃ¡lida!");
                return;
            }
            stmt.setDate(2, new java.sql.Date(dataNasc.getTime()));
            stmt.setString(3, telefone);
            stmt.setString(4, email);
            stmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));

            // 2) Executar SQL
            stmt.executeUpdate();
            System.out.println("Contato cadastrado com sucesso");

        } catch (SQLException e) {
            System.out.println("NÃ£o foi possÃ­vel executar.");
        } catch (ClassNotFoundException e) {
            System.out.println("NÃ£o foi possÃ­vel executar.");
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar stmt.");
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar conn.");
                }
            }
        }
    }

    public void listar() {
        // 1) Abrir conexao
        PreparedStatement stmt = null;
        Connection conn = null;

        String sql = "SELECT NM_CONTATO, DT_NASCIMENTO, VL_TELEFONE, VL_EMAIL, DT_CADASTRO FROM TB_CONTATO";

        try {
            conn = obterConexao();
            stmt = conn.prepareStatement(sql);

            // 2) Executar SQL
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString("NM_CONTATO"));
                System.out.println(resultSet.getString("DT_NASCIMENTO"));
                System.out.println(resultSet.getString("VL_TELEFONE"));
                System.out.println(resultSet.getString("VL_EMAIL"));
                System.out.println(resultSet.getString("DT_CADASTRO"));
            }

        } catch (SQLException e) {
            System.out.println("NÃ£o foi possÃ­vel executar.");
        } catch (ClassNotFoundException e) {
            System.out.println("NÃ£o foi possÃ­vel executar.");
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar stmt.");
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar conn.");
                }
            }
        }
    }

    public void alterar() {
        // 1) Abrir conexao
        PreparedStatement stmt = null;
        Connection conn = null;

        System.out.print("Digite o nome do usuÃ¡rio que deseja alterar: ");
        String nomeAlterar = entrada.nextLine();

        System.out.print("Digite o novo nome do usuÃ¡rio que deseja alterar: ");
        String nome = entrada.nextLine();

        System.out.print("Digite a nova data de nascimento no formato dd/mm/aaaa: ");
        String strDataNasc = entrada.nextLine();

        System.out.print("Digite o novo e-mail: ");
        String email = entrada.nextLine();

        System.out.print("Digite o novo telefone no formato 99 99999-9999: ");
        String telefone = entrada.nextLine();

        String sql = "UPDATE TB_CONTATO SET NM_CONTATO = ?, "
                + " DT_NASCIMENTO = ?,"
                + " VL_TELEFONE  = ?,"
                + " VL_EMAIL = ?"
                + " WHERE NM_CONTATO = ?";

        try {
            conn = obterConexao();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);

            DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
            Date dataNasc = null;
            try {
                dataNasc = formatador.parse(strDataNasc);
            } catch (ParseException ex) {
                System.out.println("Data de nascimento invÃ¡lida.");
                return;
            }
            stmt.setDate(2, new java.sql.Date(dataNasc.getTime()));
            stmt.setString(3, telefone);
            stmt.setString(4, email);
            stmt.setString(5, nomeAlterar);

            // 2) Executar SQL
            stmt.executeUpdate();
            System.out.println("Contato alterado com sucesso");

        } catch (SQLException e) {
            System.out.println("NÃ£o foi possÃ­vel executar.");
        } catch (ClassNotFoundException e) {
            System.out.println("NÃ£o foi possÃ­vel executar.");
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar stmt.");
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar conn.");
                }
            }
        }
        // 3) Fechar conexao        
    }

    public void deletar() {
        // 1) Abrir conexao
        PreparedStatement stmt = null;
        Connection conn = null;

        System.out.print("Digite o nome do contato que deseja deletar: ");
        String nome = entrada.nextLine();
        String sql = "DELETE FROM TB_CONTATO WHERE NM_CONTATO LIKE '" + nome + "'";

        try {
            conn = obterConexao();
            stmt = conn.prepareStatement(sql);

            // 2) Executar SQL
            stmt.execute();
            System.out.println("Contato deletado com sucesso");
        } catch (SQLException e) {
            System.out.println("NÃ£o foi possÃ­vel executar.");
        } catch (ClassNotFoundException e) {
            System.out.println("NÃ£o foi possÃ­vel executar.");
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fechar stmt.");
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println("Erro ao fechar conn.");
                }
            }
        }
    }

}
