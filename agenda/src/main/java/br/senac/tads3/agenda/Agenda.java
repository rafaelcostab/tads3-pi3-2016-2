package br.senac.tads3.agenda;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
            System.out.println("***** DIGITE UMA OPÇÃO *****");
            System.out.println("(1) Listar contatos");
            System.out.println("(2) Incluir novo contato");
            System.out.println("(3) Alterar contato");
            System.out.println("(4) Deletar contato");
            System.out.println("(9) Sair");
            System.out.print("Opção: ");

            String strOpcao = entrada.nextLine();
            int opcao = Integer.parseInt(strOpcao);
            switch (opcao) {
                case 1:
                    break;
                case 2:
                    instancia.incluir();
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 9:
                    System.exit(0);
                    break;
                default:
                    System.out.println("OPÇÃO INVÁLIDA");
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
                System.out.println("Data de nascimento inválida!");
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
            System.out.println("Não foi possível executar.");
        } catch (ClassNotFoundException e) {
            System.out.println("Não foi possível executar.");
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
