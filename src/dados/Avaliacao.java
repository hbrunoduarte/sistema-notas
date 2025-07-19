package dados;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Avaliacao {
    private int id;
    private String nome;
    private String data;
    private float peso;

    public Avaliacao(String nome) {
        this.nome = nome;
    }

    public Avaliacao(int id, String nome, String data, float peso) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.peso = peso;
    }

    public Avaliacao() {}

    public Avaliacao(String nome, String data, float peso) {
        this.nome = nome;
        this.data = data;
        this.peso = peso;
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

    public String getData() {
        return data;
    }

    public float getPeso() {
        return peso;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Avaliacao a) {
            return this.nome.equals(a.getNome());
        }
        return false;
    }

    @Override
    public String toString() {
        return nome;
    }

}
