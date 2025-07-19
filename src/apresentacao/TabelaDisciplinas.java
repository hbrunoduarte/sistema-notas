package apresentacao;

import negocio.*;
import dados.*;
import excecoes.*;
import persistencia.DisciplinaDAO;
import persistencia.ProfessorDAO;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TabelaDisciplinas extends AbstractTableModel {
    Sistema sistema;
    private String[] colunas = {"Disciplinas"};
    private List<Disciplina> disciplinas;

    public TabelaDisciplinas(Sistema sistema) {
        this.sistema = sistema;
        try {
            this.disciplinas = DisciplinaDAO.getInstance().selectAll(sistema.getProfessor());
        } catch(SelectException | SQLException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            this.disciplinas = new ArrayList<>();
        }
    }

    public int getColumnCount() {
        return 1;
    }

    public int getRowCount() {
        return disciplinas.size();
    }

    public String getColumnName(int column) {
        return colunas[column];
    }

    public Object getValueAt(int linha, int coluna) {
        Disciplina disciplina = disciplinas.get(linha);
        return disciplina.getNome();
    }

    public void adicionarDisciplinaTabela(Disciplina disciplina) throws EntidadeJaCadastradaException, SQLException, InsertException, SelectException, ClassNotFoundException {
        sistema.adicionarDisciplina(disciplina);
        this.disciplinas = DisciplinaDAO.getInstance().selectAll(sistema.getProfessor());
        fireTableStructureChanged();
    }

    public void removerDisciplinaTabela(Disciplina disciplina) throws EntidadeNaoEncontradaException, SQLException, DeleteException, SelectException, ClassNotFoundException {
        sistema.removerDisciplina(disciplina);
        this.disciplinas = DisciplinaDAO.getInstance().selectAll(sistema.getProfessor());
        fireTableStructureChanged();
    }


}
