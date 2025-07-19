package persistencia;

import dados.Aluno;
import dados.Avaliacao;
import dados.Disciplina;
import excecoes.DeleteException;
import excecoes.InsertException;
import excecoes.SelectException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
    private static AlunoDAO instance = null;
    private PreparedStatement selectNewId;;
    private PreparedStatement insert;
    private PreparedStatement insertRelacao;
    private PreparedStatement delete;
    private PreparedStatement select;
    private PreparedStatement selectAll;
    private PreparedStatement deleteAllAlunoByDisciplina;
    private PreparedStatement deleteAlunosSemDisciplina;
    private PreparedStatement buscarPorNome;
    private PreparedStatement buscarPorNomeGlobal;

    public static AlunoDAO getInstance() throws SQLException, ClassNotFoundException {
        if(instance == null)
            instance = new AlunoDAO();
        return instance;
    }

    private AlunoDAO() throws SQLException, ClassNotFoundException {
        Connection connection = Conexao.getConnection();
        selectNewId = connection.prepareStatement("SELECT nextval('id_aluno_seq')");
        insert = connection.prepareStatement("INSERT INTO aluno (id, nome) VALUES (?, ?)");
        insertRelacao = connection.prepareStatement("INSERT INTO disciplina_aluno (disciplina_id, aluno_id) VALUES (?, ?)");
        delete = connection.prepareStatement("DELETE FROM disciplina_aluno WHERE aluno_id = ? AND disciplina_id = ?");
        select = connection.prepareStatement("SELECT * FROM aluno WHERE id = ?");
        selectAll = connection.prepareStatement("SELECT a.id, a.nome\n" +
                "FROM aluno a\n" +
                "JOIN disciplina_aluno da ON a.id = da.aluno_id\n" +
                "WHERE da.disciplina_id = ?;");
        deleteAllAlunoByDisciplina = connection.prepareStatement("DELETE FROM disciplina_aluno WHERE disciplina_id = ?");
        deleteAlunosSemDisciplina = connection.prepareStatement("DELETE FROM aluno WHERE id NOT IN (SELECT aluno_id FROM disciplina_aluno)");
        buscarPorNome = connection.prepareStatement("SELECT a.id, a.nome " +
                "FROM aluno a " +
                "JOIN disciplina_aluno da ON a.id = da.aluno_id " +
                "WHERE a.nome = ? AND da.disciplina_id = ?");
        buscarPorNomeGlobal = connection.prepareStatement("SELECT * FROM aluno WHERE nome = ?");
    }

    private int selectNewId() throws SelectException {
        try {
            ResultSet rs = selectNewId.executeQuery();
            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar novo id na tabela aluno");
        }
        return 0;
    }

    public void insert(Aluno aluno, Disciplina disciplina) throws InsertException, SelectException {
        try {
            Aluno alunoExistente = buscarPorNomeGlobal(aluno.getNome());

            if (alunoExistente == null) {
                int newId = selectNewId();
                aluno.setId(newId);

                insert.setInt(1, newId);
                insert.setString(2, aluno.getNome());
                insert.executeUpdate();
            } else {
                aluno.setId(alunoExistente.getId());
            }

            insertRelacao.setInt(1, disciplina.getId());
            insertRelacao.setInt(2, aluno.getId());
            insertRelacao.executeUpdate();

        } catch (SQLException e) {
            throw new InsertException("Erro ao inserir aluno na disciplina");
        }
    }

    public void delete(Aluno aluno, Disciplina disciplina) throws DeleteException {
        try {
            AvaliacaoDAO avaliacaoDAO = AvaliacaoDAO.getInstance();
            NotaDAO notaDAO = NotaDAO.getInstance();

            List<Avaliacao> avaliacoesDaDisciplina = avaliacaoDAO.selectAll(disciplina);

            for (Avaliacao av : avaliacoesDaDisciplina) {
                notaDAO.deleteNota(av, aluno);
            }

            delete.setInt(1, aluno.getId());
            delete.setInt(2, disciplina.getId());
            delete.executeUpdate();
            
            deleteAlunosSemDisciplina.executeUpdate();

        } catch (SQLException | ClassNotFoundException | SelectException e) {
            throw new DeleteException("Erro ao deletar aluno: " + e.getMessage());
        }
    }

    public Aluno select(int id) throws SelectException {
        try {
            select.setInt(1, id);
            ResultSet rs = select.executeQuery();
            if(rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setId(rs.getInt("id"));
                aluno.setNome(rs.getString("nome"));
                return aluno;
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar aluno");
        }
        return null;
    }

    public List<Aluno> selectAll(Disciplina disciplina) throws SelectException {
        List<Aluno> alunos = new ArrayList<>();
        try {
            selectAll.setInt(1, disciplina.getId());
            ResultSet rs = selectAll.executeQuery();
            while(rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setId(rs.getInt("id"));
                aluno.setNome(rs.getString("nome"));
                alunos.add(aluno);
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar todos os alunos");
        }
        return alunos;
    }

    public void deleteAllAlunoByDisciplina(Disciplina disciplina) throws DeleteException {
        try {
            deleteAllAlunoByDisciplina.setInt(1, disciplina.getId());
            deleteAllAlunoByDisciplina.executeUpdate();

            deleteAlunosSemDisciplina.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteException("Erro ao deletar aluno");
        }
    }

    public Aluno buscarPorNome(String nome, Disciplina disciplina) throws SQLException, SelectException, ClassNotFoundException {
        buscarPorNome.setString(1, nome);
        buscarPorNome.setInt(2, disciplina.getId());
        ResultSet rs = buscarPorNome.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("id");
            String nomeAluno = rs.getString("nome");
            return new Aluno(id, nomeAluno);
        }

        return null;
    }

    public Aluno buscarPorNomeGlobal(String nome) {
        try {
            buscarPorNomeGlobal.setString(1, nome);
            ResultSet rs = buscarPorNomeGlobal.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String nomeAluno = rs.getString("nome");
                return new Aluno(id, nomeAluno);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fazer busca global por aluno");
        }
        return null;
    }



}
