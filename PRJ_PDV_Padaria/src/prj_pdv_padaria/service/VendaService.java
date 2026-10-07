package prj_pdv_padaria.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.math.BigDecimal;

import prj_pdv_padaria.dao.Conexao;
import prj_pdv_padaria.dao.ItemVendaDAO;
import prj_pdv_padaria.dao.VendaDAO;
import prj_pdv_padaria.dao.ProdutoDAO;
import prj_pdv_padaria.model.ItemVenda;
import prj_pdv_padaria.model.Venda;
import prj_pdv_padaria.model.Produto;

public class VendaService {

    private VendaDAO vendaDAO;
    private ItemVendaDAO itemVendaDAO;
    private ProdutoDAO produtoDAO;

    public VendaService() {
        vendaDAO = new VendaDAO();
        itemVendaDAO = new ItemVendaDAO();
        produtoDAO = new ProdutoDAO();
    }

    public boolean registrarVenda(Venda venda, List<ItemVenda> itens) {

        if (itens == null || itens.isEmpty()) {
            System.out.println("Não é possível registrar uma venda sem itens.");
            return false;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (ItemVenda item : itens) {

            if (item.getQuantidade() <= 0) {
                System.out.println("A quantidade deve ser maior que zero.");
                return false;
            }

            Produto produto =
                produtoDAO.buscarPorId(item.getIdProduto());

            if (produto == null) {

                System.out.println(
                    "Produto não encontrado. ID: "
                    + item.getIdProduto()
                );

                return false;
            }

            if (item.getQuantidade() > produto.getEstoque()) {

                System.out.println(
                    "Estoque insuficiente para o produto: "
                    + produto.getNome()
                );

                return false;
            }

            BigDecimal subtotal =
                produto.getPreco().multiply(
                    BigDecimal.valueOf(
                        item.getQuantidade()
                    )
                );

            item.setPrecoUnitario(
                produto.getPreco()
            );

            item.setSubtotal(
                subtotal
            );

            total = total.add(subtotal);
        }

        venda.setTotal(total);

        try (Connection conexao = Conexao.conectar()) {

            conexao.setAutoCommit(false);

            try {

                int idVenda =
                    vendaDAO.cadastrar(
                        venda,
                        conexao
                    );

                for (ItemVenda item : itens) {

                    item.setIdVenda(idVenda);

                    itemVendaDAO.cadastrar(
                        item,
                        conexao
                    );

                    produtoDAO.baixarEstoque(
                        item.getIdProduto(),
                        item.getQuantidade(),
                        conexao
                    );
                }

                conexao.commit();

                System.out.println(
                    "Venda registrada com sucesso!"
                );

                return true;

            } catch (SQLException e) {

                conexao.rollback();

                System.out.println(
                    "Erro ao registrar venda."
                );

                System.out.println(
                    "A operação foi desfeita."
                );

                e.printStackTrace();

                return false;
            }

        } catch (SQLException e) {

            System.out.println(
                "Erro na conexão com o banco."
            );

            e.printStackTrace();

            return false;
        }
    }
    
    
}