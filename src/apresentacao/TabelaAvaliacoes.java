package apresentacao;

import negocio.*;
import dados.*;
import excecoes.*;
import persistencia.AvaliacaoDAO;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TabelaAvaliacoes extends AbstractTableModel {
    private Sistema sistema;
    private Disciplina disciplina;
    private String[] colunas = {"Avaliacoes"};
    private List<Avaliacao> avaliacoes;

    public TabelaAvaliacoes(Sistema sistema, Disciplina disciplina) {
        this.sistema = sistema;
        this.disciplina = disciplina;

        try {
            this.avaliacoes = AvaliacaoDAO.getInstance().selectAll(disciplina);
        } catch(SQLException | ClassNotFoundException | SelectException e) {
            System.err.println(e.getMessage());
            this.avaliacoes = new ArrayList<>();
        }
    }

    public int getColumnCount() {
        return 1;
    }

    public int getRowCount() {
        return avaliacoes.size();
    }

    public String getColumnName(int column) {
        return colunas[column];
    }

    public Object getValueAt(int linha, int coluna) {
        Avaliacao avaliacao = avaliacoes.get(linha);
        return avaliacao.getNome();
    }

    public void adicionarAvaliacaoTabela(Avaliacao avaliacao) throws EntidadeJaCadastradaException, SQLException, InsertException, SelectException, ClassNotFoundException {
        sistema.adicionarAvaliacao(disciplina, avaliacao);
        this.avaliacoes = AvaliacaoDAO.getInstance().selectAll(disciplina);
        fireTableStructureChanged();
    }

    public void removerAvaliacaoTabela(Avaliacao avaliacao) throws EntidadeNaoEncontradaException, SQLException, DeleteException, SelectException, ClassNotFoundException {
        sistema.removerAvaliacao(disciplina, avaliacao);
        this.avaliacoes = AvaliacaoDAO.getInstance().selectAll(disciplina);
        fireTableStructureChanged();
    }


}
