package persistencia;

import dados.*;
import excecoes.DeleteException;
import excecoes.InsertException;
import excecoes.SelectException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO {
    private static DisciplinaDAO instance = null;
    private PreparedStatement selectNewId;
    private PreparedStatement insert;
    private PreparedStatement delete;
    private PreparedStatement select;
    private PreparedStatement selectAll;
    private PreparedStatement buscarPorNome;

    public static DisciplinaDAO getInstance() throws SQLException, ClassNotFoundException {
        if(instance == null)
            instance = new DisciplinaDAO();
        return instance;
    }

    private DisciplinaDAO() throws SQLException, ClassNotFoundException {
        Connection connection = Conexao.getConnection();

        selectNewId = connection.prepareStatement("SELECT nextval('id_disciplina_seq')");
        insert = connection.prepareStatement("INSERT INTO disciplina (id, nome, professor_id) VALUES (?, ?, ?)");
        delete = connection.prepareStatement("DELETE FROM disciplina WHERE id = ? AND professor_id = ?");
        select = connection.prepareStatement("SELECT * FROM disciplina WHERE id = ? AND professor_id = ?");
        selectAll = connection.prepareStatement("SELECT * FROM disciplina WHERE professor_id = ?");
        buscarPorNome = connection.prepareStatement("SELECT id, nome FROM disciplina WHERE nome = ? AND professor_id = ?");
    }

    private int selectNewId() throws SelectException {
        try {
            ResultSet rs = selectNewId.executeQuery();
            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar novo id na tabela disciplina");
        }
        return 0;
    }

    public void insert(Disciplina disciplina, Professor professor) throws InsertException, SelectException {
        try {
            int id = selectNewId();

            insert.setInt(1, id);
            insert.setString(2, disciplina.getNome());
            insert.setInt(3, professor.getId());
            insert.executeUpdate();
            disciplina.setId(id);

        } catch (SQLException e) {
            throw new InsertException("Erro ao inserir disciplina");
        }
    }

    public void delete(Disciplina disciplina, Professor professor) throws DeleteException {
        try {
            AvaliacaoDAO.getInstance().deleteAllAvaliacoesByDisciplina(disciplina);
            AlunoDAO.getInstance().deleteAllAlunoByDisciplina(disciplina);

            delete.setInt(1, disciplina.getId());
            delete.setInt(2, professor.getId());
            delete.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DeleteException("Erro ao deletar disciplina");
        }
    }

    public Disciplina select(int id, Professor professor) throws SelectException {
        try {
            select.setInt(1, id);
            select.setInt(2, professor.getId());
            ResultSet rs = select.executeQuery();
            if(rs.next()) {
                Disciplina disciplina = new Disciplina();
                disciplina.setNome(rs.getString("nome"));
                disciplina.setId(rs.getInt("id"));
                return disciplina;
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar disciplina");
        }
        return null;
    }

    public List<Disciplina> selectAll(Professor professor) throws SelectException {
        List<Disciplina> disciplinas = new ArrayList<>();
        try {
            selectAll.setInt(1, professor.getId());
            ResultSet rs = selectAll.executeQuery();
            while (rs.next()) {
                Disciplina d = new Disciplina();
                d.setId(rs.getInt("id"));
                d.setNome(rs.getString("nome"));
                disciplinas.add(d);
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar disciplinas");
        }
        return disciplinas;
    }

    public Disciplina buscarPorNome(String nome, Professor professor) throws SQLException, SelectException, ClassNotFoundException {
        buscarPorNome.setString(1, nome);
        buscarPorNome.setInt(2, professor.getId());
        ResultSet rs = buscarPorNome.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("id");
            String nomeDisciplina = rs.getString("nome");
            return new Disciplina(id, nomeDisciplina);
        }

        return null;
    }




}
