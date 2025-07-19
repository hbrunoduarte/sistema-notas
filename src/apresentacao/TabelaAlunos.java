package apresentacao;

import negocio.*;
import dados.*;
import excecoes.*;
import persistencia.AlunoDAO;
import persistencia.DisciplinaDAO;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TabelaAlunos extends AbstractTableModel {
    private Sistema sistema;
    private Disciplina disciplina;
    private String[] colunas = {"Alunos"};
    private List<Aluno> alunos;

    public TabelaAlunos(Sistema sistema, Disciplina disciplina) {
        this.sistema = sistema;
        this.disciplina = disciplina;

        try {
            this.alunos = AlunoDAO.getInstance().selectAll(disciplina);
        } catch (SQLException | ClassNotFoundException | SelectException e) {
            System.err.println(e.getMessage());
            this.alunos = new ArrayList<>();
        }
    }

    public int getColumnCount() {
        return 1;
    }

    public int getRowCount() {
        return alunos.size();
    }

    public String getColumnName(int column) {
        return colunas[column];
    }

    public Object getValueAt(int linha, int coluna) {
        Aluno aluno = alunos.get(linha);
        return aluno.getNome();
    }

    public void adicionarAlunoTabela(Aluno aluno) throws EntidadeJaCadastradaException, SQLException, InsertException, SelectException, ClassNotFoundException {
        // coisa
        sistema.adicionarAluno(disciplina, aluno);
        this.alunos = AlunoDAO.getInstance().selectAll(disciplina);
        fireTableStructureChanged();
    }

    public void removerAlunoTabela(Aluno aluno) throws EntidadeNaoEncontradaException, SQLException, DeleteException, SelectException, ClassNotFoundException {
        sistema.removerAluno(disciplina, aluno);
        this.alunos = AlunoDAO.getInstance().selectAll(disciplina);
        fireTableStructureChanged();
    }


}
