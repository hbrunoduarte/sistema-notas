package excecoes;

import dados.*;

public class EntidadeJaCadastradaException extends Exception {
    public EntidadeJaCadastradaException(Professor professor) {
        super("Professor já cadastrado!");
    }

    public EntidadeJaCadastradaException(Disciplina disciplina) {
        super("Disciplina já cadastrada!");
    }

    public EntidadeJaCadastradaException(Avaliacao avaliacao) {
        super("Avaliação já cadastrada!");
    }

    public EntidadeJaCadastradaException(Aluno aluno) {
        super("Aluno já matriculado na disciplina!");
    }
}
