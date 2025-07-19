package dados;

import java.util.ArrayList;
import java.util.List;

public class Disciplina {
    private int id;
    private String nome;

    public Disciplina() {}

    public Disciplina(String nome) {
        this.nome = nome;
    }

    public Disciplina(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Disciplina d) {
            return this.nome.equals(d.getNome());
        }
        return false;
    }

    @Override
    public String toString() {
        return this.nome;
    }

}
