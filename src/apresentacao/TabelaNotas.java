package apresentacao;

import negocio.*;
import dados.*;
import excecoes.*;
import persistencia.AlunoDAO;
import persistencia.NotaDAO;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TabelaNotas extends AbstractTableModel {
    private Sistema sistema;
    private Disciplina disciplina;
    private Avaliacao avaliacao;
    private String[] colunas = {"Aluno", "Nota"};
    private List<Aluno> alunos;

    public TabelaNotas(Sistema sistema, Disciplina disciplina, Avaliacao avaliacao) throws SQLException, ClassNotFoundException, SelectException {
        this.sistema = sistema;
        this.disciplina = disciplina;
        this.avaliacao = avaliacao;
        this.alunos = AlunoDAO.getInstance().selectAll(disciplina);
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public int getRowCount() {
        return alunos.size();
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        Aluno aluno = alunos.get(linha);

        if (coluna == 0) {
            return aluno.getNome();
        }
        else if (coluna == 1) {
            try {
                Nota nota = NotaDAO.getInstance().selectByAlunoAndAvaliacao(this.avaliacao, aluno);
                if (nota != null) {
                    return nota.getValor();
                } else {
                    return "";
                }

            } catch(SQLException | ClassNotFoundException | SelectException e) {
                System.err.println("Erro ao buscar nota: " + e.getMessage());
                return "Erro";
            }
        }

        return null;
    }

    public void salvarNotaNaTabela(Aluno aluno, Nota nota) throws Exception {
        sistema.atribuirOuAtualizarNota(this.disciplina, this.avaliacao, aluno, nota);
        fireTableDataChanged();
    }
}