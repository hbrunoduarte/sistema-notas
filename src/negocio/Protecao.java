package negocio;

import dados.Professor;
import excecoes.DadosIncorretosException;
import excecoes.EntidadeJaCadastradaException;
import excecoes.InsertException;
import excecoes.SelectException;
import persistencia.ProfessorDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Protecao {
    private static Protecao instancia;

    private Protecao() {}

    public static Protecao getInstancia() {
        if (instancia == null) {
            instancia = new Protecao();
        }
        return instancia;
    }

    public List<Professor> getProfessores() throws SQLException, ClassNotFoundException, SelectException {
        return ProfessorDAO.getInstance().selectAll();
    }

    public void cadastrarProfessor(Professor professor) throws EntidadeJaCadastradaException, SQLException, ClassNotFoundException, InsertException, SelectException {
        List<Professor> professores = ProfessorDAO.getInstance().selectAll();
        for(Professor p : professores) {
            if(p.equals(professor)) {
                throw new EntidadeJaCadastradaException(professor);
            }
        }
        ProfessorDAO.getInstance().insert(professor);
    }

    public Sistema validaLogin(String login, String senha) throws DadosIncorretosException, SQLException, ClassNotFoundException, SelectException {
        List<Professor> professores = ProfessorDAO.getInstance().selectAll();
        for(Professor p : professores) {
            if(p.getLogin().equals(login) && p.getSenha().equals(senha)){
                return new Sistema(p);
            }
        }
        throw new DadosIncorretosException("Login ou senha incorretos");
    }

}
