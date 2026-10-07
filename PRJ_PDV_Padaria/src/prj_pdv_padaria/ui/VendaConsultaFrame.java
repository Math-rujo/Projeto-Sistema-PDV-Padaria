package prj_pdv_padaria.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import prj_pdv_padaria.dao.VendaDAO;

public class VendaConsultaFrame extends JFrame {

    private JTable tabelaVendas;
    private DefaultTableModel modeloTabela;

    private JLabel labelQuantidade;
    private JLabel labelTotal;

    private JComboBox<String> comboFiltro;

    private VendaDAO vendaDAO;

    public VendaConsultaFrame() {

        vendaDAO = new VendaDAO();

        configurarJanela();
        criarComponentes();
        carregarVendas();
    }

    private void configurarJanela() {

        setTitle("PDV Padaria - Verificar Vendas");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void criarComponentes() {

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titulo

        JLabel titulo = new JLabel("Verificar Vendas");

        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // Painel superior

        JPanel painelInformacoes = new JPanel(new BorderLayout(10, 10));

        JPanel painelResumo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        labelQuantidade = new JLabel("Vendas: 0");

        labelTotal = new JLabel("Total: R$ 0,00");

        labelQuantidade.setFont(new Font("Arial", Font.BOLD, 14));

        labelTotal.setFont(new Font("Arial", Font.BOLD, 14));

        painelResumo.add(labelQuantidade);
        painelResumo.add(labelTotal);

        // Filtro

        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JLabel labelFiltro = new JLabel("Período:");

        comboFiltro =
            new JComboBox<>(
                new String[]{
                    "Todas",
                    "Hoje",
                    "Este mês",
                    "Este ano"
                }
            );

        comboFiltro.addActionListener(e -> carregarVendas());

        painelFiltro.add(labelFiltro);
        painelFiltro.add(comboFiltro);

        painelInformacoes.add(painelResumo, BorderLayout.WEST);
        painelInformacoes.add(painelFiltro, BorderLayout.EAST);
        painelPrincipal.add(painelInformacoes, BorderLayout.CENTER);
        
        // Tabela

        modeloTabela =
            new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Data/Hora",
                    "Funcionário",
                    "Total"
                },
                0
            ) {

                @Override
                public boolean isCellEditable(
                    int row,
                    int column) {

                    return false;
                }
            };

        tabelaVendas = new JTable(modeloTabela);

        JScrollPane scrollPane = new JScrollPane(tabelaVendas);
        painelPrincipal.add(scrollPane, BorderLayout.SOUTH);
        
        // Organização

        JPanel painelCentro = new JPanel(new BorderLayout(10, 10));

        painelCentro.add(
            painelInformacoes,
            BorderLayout.NORTH
        );

        painelCentro.add(
            scrollPane,
            BorderLayout.CENTER
        );

        painelPrincipal.add(
            painelCentro,
            BorderLayout.CENTER
        );

        add(painelPrincipal);
    }

    private void carregarVendas() {

        modeloTabela.setRowCount(0);

        String filtro =
            (String) comboFiltro.getSelectedItem();

        List<Object[]> vendas;

        if ("Todas".equals(filtro)) {

            vendas =
                vendaDAO.listarVendasDetalhadas();

        } else {

            LocalDate hoje = LocalDate.now();
            LocalDateTime inicio;
            LocalDateTime fim;

            if ("Hoje".equals(filtro)) {
                inicio = hoje.atStartOfDay();
                fim = hoje.plusDays(1).atStartOfDay();

            } else if ("Este mês".equals(filtro)) {
                inicio = hoje.withDayOfMonth(1).atStartOfDay();
                fim = hoje.plusMonths(1).withDayOfMonth(1).atStartOfDay();

            } else {
                inicio = hoje.withDayOfYear(1).atStartOfDay();
                fim = hoje.plusYears(1).withDayOfYear(1).atStartOfDay();
            }
            vendas = vendaDAO.listarVendasPorPeriodo(Timestamp.valueOf(inicio), Timestamp.valueOf(fim));
        }

        preencherTabela(vendas);
    }

    private void preencherTabela(List<Object[]> vendas) {

        BigDecimal total = BigDecimal.ZERO;

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Object[] venda : vendas) {

            int idVenda = (int) venda[0];

            Timestamp dataVenda = (Timestamp) venda[1];
            String funcionario = (String) venda[2];
            BigDecimal valor = (BigDecimal) venda[3];

            modeloTabela.addRow(
                new Object[]{
                    idVenda,
                    formato.format(dataVenda),
                    funcionario,
                    "R$ " + valor
                }
            );

            total = total.add(valor);
        }

        labelQuantidade.setText("Vendas: " + vendas.size());
        labelTotal.setText("Total: R$ " + total);
    }
}