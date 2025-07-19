package dados;

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.List;

public class Professor {
    private int id;
    private String nome;
    private String login;
    private String senha;

    public Professor() {}

    public Professor(String nome, String login, String senha) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Professor p) {
            return this.login.equals(p.getLogin());
        }
        return false;
    }

}
