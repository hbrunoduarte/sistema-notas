package excecoes;

import dados.*;

public class EntidadeNaoEncontradaException extends RuntimeException {
    public EntidadeNaoEncontradaException(Aluno aluno) {
        super("Aluno não encontrado!");
    }

    public EntidadeNaoEncontradaException(Avaliacao avaliacao) {
        super("Avaliação não encontrada!");
    }

    public EntidadeNaoEncontradaException(Disciplina disciplina) {
        super("Disciplina não encontrada!");
    }
}
