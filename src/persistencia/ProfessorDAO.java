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

public class ProfessorDAO {
    private static ProfessorDAO instance = null;
    private PreparedStatement selectNewId;
    private PreparedStatement insert;
    private PreparedStatement selectAll;
    private PreparedStatement select;

    public static ProfessorDAO getInstance() throws SQLException, ClassNotFoundException {
        if(instance == null)
            instance = new ProfessorDAO();
        return instance;
    }

    private ProfessorDAO() throws SQLException, ClassNotFoundException {
        Connection connection = Conexao.getConnection();
        selectNewId = connection.prepareStatement("SELECT nextval('id_professor_seq')");
        insert = connection.prepareStatement("INSERT INTO professor (id, nome, login, senha) VALUES (?, ?, ?, ?)");
        selectAll = connection.prepareStatement("SELECT * FROM professor");
        select = connection.prepareStatement("SELECT * FROM professor WHERE id = ?");
    }

    private int selectNewId() throws SelectException {
        try {
            ResultSet rs = selectNewId.executeQuery();
            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar novo id na tabela Professor");
        }
        return 0;
    }

    public void insert(Professor professor) throws InsertException, SelectException {
        try {
            int id = selectNewId();
            insert.setInt(1, id);
            insert.setString(2, professor.getNome());
            insert.setString(3, professor.getLogin());
            insert.setString(4, professor.getSenha());
            insert.executeUpdate();
            professor.setId(id);
        } catch(SQLException e) {
            throw new InsertException("Erro ao inserir professor");
        }
    }

    public List<Professor> selectAll() throws SelectException {
        List<Professor> professores = new ArrayList<>();
        try {
            ResultSet rs = selectAll.executeQuery();
            while(rs.next()) {
                Professor professor = new Professor();
                professor.setId(rs.getInt("id"));
                professor.setNome(rs.getString("nome"));
                professor.setLogin(rs.getString("login"));
                professor.setSenha(rs.getString("senha"));
                professores.add(professor);
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar todos os professores");
        }
        return professores;
    }

    public Professor select(int id) throws SelectException {
        try {
            select.setInt(1, id);
            ResultSet rs = select.executeQuery();
            if(rs.next()) {
                Professor professor = new Professor();
                professor.setId(rs.getInt("id"));
                professor.setNome(rs.getString("nome"));
                professor.setLogin(rs.getString("login"));
                professor.setSenha(rs.getString("senha"));

                return professor;
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar professor");
        }
        return null;
    }


}
