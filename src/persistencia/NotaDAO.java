package persistencia;

import dados.Aluno;
import dados.Avaliacao;
import dados.Nota;
import excecoes.InsertException;
import excecoes.SelectException;
import excecoes.DeleteException;
import excecoes.UpdateException;

import javax.swing.text.html.HTMLDocument;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotaDAO {
    private static NotaDAO instance = null;
    private PreparedStatement selectNewId;
    private PreparedStatement insert;
    private PreparedStatement update;
    private PreparedStatement select;
    private PreparedStatement selectAllByAvaliacao;
    private PreparedStatement deleteAllByAvaliacao;
    private PreparedStatement deleteAllByAluno;
    private PreparedStatement selectByAlunoAndAvaliacao;

    public static NotaDAO getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null)
            instance = new NotaDAO();
        return instance;
    }

    private NotaDAO() throws SQLException, ClassNotFoundException {
        Connection connection = Conexao.getConnection();
        selectNewId = connection.prepareStatement("SELECT nextval('id_nota_seq')");
        insert = connection.prepareStatement("INSERT INTO nota (id, avaliacao_id, aluno_id, valor) VALUES (?, ?, ?, ?)");
        update = connection.prepareStatement("UPDATE nota SET valor = ? WHERE avaliacao_id = ? AND aluno_id = ?");
        select = connection.prepareStatement("SELECT * FROM nota WHERE id = ?");
        selectAllByAvaliacao = connection.prepareStatement("SELECT * FROM nota WHERE avaliacao_id = ?");
        deleteAllByAvaliacao = connection.prepareStatement("DELETE FROM nota WHERE avaliacao_id = ?");
        deleteAllByAluno = connection.prepareStatement("DELETE FROM nota WHERE aluno_id = ?");
        selectByAlunoAndAvaliacao = connection.prepareStatement("SELECT * FROM nota WHERE avaliacao_id = ? AND aluno_id = ?");
    }

    private int selectNewId() throws SelectException {
        try {
            ResultSet rs = selectNewId.executeQuery();
            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar novo id na tabela");
        }
        return 0;
    }

    public void insert(Nota nota, Avaliacao avaliacao, Aluno aluno) throws InsertException, SelectException {
        try {
            int id = selectNewId();
            insert.setInt(1, id);
            insert.setInt(2, avaliacao.getId());
            insert.setInt(3, aluno.getId());
            insert.setFloat(4, nota.getValor());
            insert.executeUpdate();
            nota.setId(id);
        } catch (SQLException e) {
            throw new InsertException("Erro ao inserir nota");
        }
    }

    public void update(Nota nota, Aluno aluno, Avaliacao avaliacao) throws UpdateException {
        try {
            update.setFloat(1, nota.getValor());
            update.setInt(2, avaliacao.getId());
            update.setInt(3, aluno.getId());
            update.executeUpdate();
        } catch (SQLException e) {
            throw new UpdateException("Erro ao atualizar nota");
        }
    }

    public Nota select(int id) throws SelectException {
        try {
            select.setInt(1, id);
            ResultSet rs = select.executeQuery();
            if (rs.next()) {
                Nota nota = new Nota();
                nota.setId(rs.getInt("id"));
                nota.setValor(rs.getFloat("valor"));
                return nota;
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar nota");
        }
        return null;
    }

    public List<Nota> selectAllByAvaliacao(Avaliacao avaliacao) throws SelectException {
        List<Nota> notas = new ArrayList<>();
        try {
            selectAllByAvaliacao.setInt(1, avaliacao.getId());
            ResultSet rs = selectAllByAvaliacao.executeQuery();
            while (rs.next()) {
                Nota nota = new Nota();
                nota.setId(rs.getInt("id"));
                nota.setValor(rs.getFloat("valor"));
                notas.add(nota);
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar notas da avaliação");
        }
        return notas;
    }

    public void deleteAllByAluno(Aluno aluno) throws DeleteException {
        try {
            deleteAllByAluno.setInt(1, aluno.getId());
            deleteAllByAluno.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteException("Erro ao deletar todas as notas do aluno");
        }
    }

    public void deleteAllByAvaliacao(Avaliacao avaliacao) throws DeleteException {
        try {
            deleteAllByAvaliacao.setInt(1, avaliacao.getId());
            deleteAllByAvaliacao.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteException("Erro ao deletar todas as notas da avaliação");
        }
    }

    public Nota selectByAlunoAndAvaliacao(Avaliacao avaliacao, Aluno aluno) throws SelectException {
        try {
            selectByAlunoAndAvaliacao.setInt(1, avaliacao.getId());
            selectByAlunoAndAvaliacao.setInt(2, aluno.getId());
            ResultSet rs = selectByAlunoAndAvaliacao.executeQuery();
            if (rs.next()) {
                Nota nota = new Nota();
                nota.setId(rs.getInt("id"));
                nota.setValor(rs.getFloat("valor"));
                return nota;
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar nota do aluno na avaliação");
        }
        return null;
    }

    public void deleteNota(Avaliacao avaliacao, Aluno aluno) throws DeleteException {
        try {
            Connection con = Conexao.getConnection();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM nota WHERE avaliacao_id = ? AND aluno_id = ?");

            stmt.setInt(1, avaliacao.getId());
            stmt.setInt(2, aluno.getId());
            stmt.executeUpdate();

            stmt.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new DeleteException("Erro ao deletar nota específica do aluno na avaliação.");
        }
    }




}
