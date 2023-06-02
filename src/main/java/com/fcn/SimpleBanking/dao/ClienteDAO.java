package com.fcn.SimpleBanking.dao;

import com.fcn.Bank.models.Cliente;

import java.math.BigDecimal;
import java.sql.*;

public class ClienteDAO {
    private static final String DB_URL = "jdbc:h2:~/test"; // URL de conexão com o banco de dados H2
    private static final String DB_USERNAME = "username"; // Nome de usuário do banco de dados
    private static final String DB_PASSWORD = "password"; // Senha do banco de dados

    public Cliente consultarCliente(String numeroConta) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Cliente cliente = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String sql = "SELECT * FROM cliente WHERE numero_conta = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, numeroConta);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                boolean planoExclusive = rs.getBoolean("plano_exclusive");
                BigDecimal saldo = rs.getBigDecimal("saldo");
                String dataNascimento = rs.getString("data_nascimento");

                // Cria o objeto cliente com os dados do banco de dados
                cliente = new Cliente(nome, planoExclusive, saldo, numeroConta, dataNascimento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fechar conexões e recursos
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return cliente;
    }

    public BigDecimal consultarSaldo(String numeroConta) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        BigDecimal saldo = BigDecimal.ZERO;

        if (numeroConta == null || numeroConta.isEmpty()) {
            throw new IllegalArgumentException("Conta inválida.");
        }

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String sql = "SELECT saldo FROM cliente WHERE numero_conta = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, numeroConta);
            rs = stmt.executeQuery();

            if (rs.next()) {
                saldo = rs.getBigDecimal("saldo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fechar conexões e recursos
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return saldo;
    }

    public void realizarSaque(String numeroConta, BigDecimal valor) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        BigDecimal taxaSaque = BigDecimal.ZERO;

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser maior que zero.");
        }

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String sql = "SELECT plano_exclusive FROM cliente WHERE numero_conta = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, numeroConta);
            rs = stmt.executeQuery();

            if (rs.next()) {
                boolean planoExclusive = rs.getBoolean("plano_exclusive");

                if (!planoExclusive) {
                    if (valor.compareTo(BigDecimal.valueOf(100)) <= 0) {
                        taxaSaque = BigDecimal.ZERO;
                    } else if (valor.compareTo(BigDecimal.valueOf(300)) <= 0) {
                        taxaSaque = valor.multiply(BigDecimal.valueOf(0.004));
                    } else {
                        taxaSaque = valor.multiply(BigDecimal.valueOf(0.01));
                    }
                }
            }

            BigDecimal novoSaldo = consultarSaldo(numeroConta).subtract(valor).subtract(taxaSaque);
            atualizarSaldoCliente(numeroConta, novoSaldo);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fechar conexões e recursos
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void atualizarSaldoCliente(String numeroConta, BigDecimal novoSaldo) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String sql = "UPDATE cliente SET saldo = ? WHERE numero_conta = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setBigDecimal(1, novoSaldo);
            stmt.setString(2, numeroConta);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fechar conexões e recursos
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}