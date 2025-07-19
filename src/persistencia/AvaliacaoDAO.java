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

public class AvaliacaoDAO {
    private static AvaliacaoDAO instance = null;
    private PreparedStatement selectNewId;
    private PreparedStatement insert;
    private PreparedStatement delete;
    private PreparedStatement select;
    private PreparedStatement selectAll;
    private PreparedStatement deleteAllAvaliacoesByDisciplina;
    private PreparedStatement buscarPorNome;

    public static AvaliacaoDAO getInstance() throws SQLException, ClassNotFoundException {
        if(instance == null)
            instance = new AvaliacaoDAO();
        return instance;
    }

    private AvaliacaoDAO() throws SQLException, ClassNotFoundException {
        Connection connection = Conexao.getConnection();
        selectNewId = connection.prepareStatement("SELECT nextval('id_avaliacao_seq')");
        insert = connection.prepareStatement("INSERT INTO avaliacao (id, nome, data, peso, disciplina_id) VALUES (?, ?, ?, ?, ?)");
        delete = connection.prepareStatement("DELETE FROM avaliacao WHERE id = ? AND disciplina_id = ?");
        deleteAllAvaliacoesByDisciplina = connection.prepareStatement("DELETE FROM avaliacao WHERE disciplina_id = ?");
        select = connection.prepareStatement("SELECT * FROM avaliacao WHERE id = ?");
        selectAll = connection.prepareStatement("SELECT * FROM avaliacao WHERE disciplina_id = ?");
        buscarPorNome = connection.prepareStatement("SELECT id, nome, data, peso FROM avaliacao WHERE nome = ? AND disciplina_id = ?");
    }

    private int selectNewId() throws SelectException {
        try {
            ResultSet rs = selectNewId.executeQuery();
            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar novo id na tabela Avaliacao");
        }
        return 0;
    }

    public void insert(Avaliacao avaliacao, Disciplina disciplina) throws InsertException, SelectException {
        try {
            int id = selectNewId();
            insert.setInt(1, id);
            insert.setString(2, avaliacao.getNome());
            insert.setString(3, avaliacao.getData());
            insert.setFloat(4, avaliacao.getPeso());
            insert.setInt(5, disciplina.getId());
            insert.executeUpdate();
            avaliacao.setId(id);

        } catch (SQLException e) {
            throw new InsertException("Erro ao inserir avaliacao");
        }
    }

    public void delete(Avaliacao avaliacao, Disciplina disciplina) throws DeleteException {
        try {
            NotaDAO.getInstance().deleteAllByAvaliacao(avaliacao);
            delete.setInt(1, avaliacao.getId());
            delete.setInt(2, disciplina.getId());
            delete.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DeleteException("Erro ao deletar avaliacao");
        }
    }

    public Avaliacao select(int id) throws SelectException {
        try {
            select.setInt(1, id);
            ResultSet rs = select.executeQuery();
            if(rs.next()) {
                Avaliacao avaliacao = new Avaliacao();
                avaliacao.setId(rs.getInt("id"));
                avaliacao.setNome(rs.getString("nome"));
                avaliacao.setData(rs.getString("data"));
                avaliacao.setPeso(rs.getFloat("peso"));

                return avaliacao;
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar avaliacao");
        }
        return null;
    }

    public List<Avaliacao> selectAll(Disciplina disciplina) throws SelectException {
        List<Avaliacao> avaliacoes = new ArrayList<>();
        try {
            selectAll.setInt(1, disciplina.getId());
            ResultSet rs = selectAll.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                Avaliacao avaliacao = new Avaliacao();
                avaliacao.setId(id);
                avaliacao.setNome(rs.getString("nome"));
                avaliacao.setData(rs.getString("data"));
                avaliacao.setPeso(rs.getFloat("peso"));
                avaliacao.setId(id);

                avaliacoes.add(avaliacao);
            }
        } catch (SQLException e) {
            throw new SelectException("Erro ao buscar todas as avaliacoes");
        }
        return avaliacoes;
    }
    public void deleteAllAvaliacoesByDisciplina(Disciplina disciplina) throws DeleteException {
        try {
            List<Avaliacao> avaliacoesParaDeletar = this.selectAll(disciplina);
            NotaDAO notaDAO = NotaDAO.getInstance();

            for(Avaliacao a : avaliacoesParaDeletar) {
                notaDAO.deleteAllByAvaliacao(a);
            }

            deleteAllAvaliacoesByDisciplina.setInt(1, disciplina.getId());
            deleteAllAvaliacoesByDisciplina.executeUpdate();
        } catch (SQLException | SelectException | ClassNotFoundException e) {
            throw new DeleteException("Erro ao deletar todas as avaliacoes da disciplina");
        }
    }

    public Avaliacao buscarPorNome(String nome, Disciplina disciplina) throws SQLException, SelectException, ClassNotFoundException {
        buscarPorNome.setString(1, nome);
        buscarPorNome.setInt(2, disciplina.getId());

        ResultSet rs = buscarPorNome.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("id");
            String nomeAvaliacao = rs.getString("nome");
            String dataAvaliacao = rs.getString("data");
            float pesoAvaliacao = rs.getFloat("peso");
            return new Avaliacao(id, nomeAvaliacao, dataAvaliacao ,pesoAvaliacao);
        }

        return null;
    }


}
