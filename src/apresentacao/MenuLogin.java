package apresentacao;

import excecoes.DadosIncorretosException;
import excecoes.EntidadeJaCadastradaException;
import excecoes.InsertException;
import excecoes.SelectException;
import negocio.*;
import dados.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MenuLogin extends JFrame {
    Protecao protecao = Protecao.getInstancia();

    private JPanel painel = new JPanel();

    private JPanel painelLogin = new JPanel();
    private JLabel infoLoginEntrar = new JLabel("Login:");
    private JTextField campoLoginEntrar = new JTextField();
    private JLabel infoSenhaEntrar = new JLabel("Senha:");
    private JPasswordField campoSenhaEntrar = new JPasswordField();
    private JButton botaoLogar = new JButton("Entrar");

    private JPanel painelCadastro = new JPanel();
    private JLabel infoNomeCadastro = new JLabel("Nome:");
    private JTextField campoNomeCadastro = new JTextField();
    private JLabel infoLoginCadastro = new JLabel("Login:");
    private JTextField campoLoginCadastro = new JTextField();
    private JLabel infoSenhaCadastro = new JLabel("Senha:");
    private JPasswordField campoSenhaCadastro = new JPasswordField();
    private JButton botaoCadastrar = new JButton("Cadastrar");

    private JLabel titulo = new JLabel("Fazer login/cadastrar");

    public MenuLogin() {
        setTitle("Sistema Notas - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setContentPane(painel);
        painel.setLayout(null);

        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBounds(0, 10, 700, 30);
        titulo.setFont(titulo.getFont().deriveFont(18.0f));
        painel.add(titulo);

        // PAINEL DE LOGIN
        painelLogin.setBounds(100, 60, 500, 160);
        painelLogin.setLayout(null);
        painelLogin.setBorder(BorderFactory.createTitledBorder("Login"));

        infoLoginEntrar.setBounds(20, 30, 50, 20);
        painelLogin.add(infoLoginEntrar);

        campoLoginEntrar.setBounds(80, 30, 250, 25);
        painelLogin.add(campoLoginEntrar);

        infoSenhaEntrar.setBounds(20, 70, 50, 20);
        painelLogin.add(infoSenhaEntrar);

        campoSenhaEntrar.setBounds(80, 70, 250, 25);
        painelLogin.add(campoSenhaEntrar);

        botaoLogar.setBounds(350, 50, 100, 30);
        botaoLogar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String login = campoLoginEntrar.getText();
                    String senha = new String(campoSenhaEntrar.getPassword());
                    Sistema sistema = protecao.validaLogin(login, senha);

                    MenuOpcoes menuOpcoes = new MenuOpcoes(sistema);
                    menuOpcoes.setVisible(true);
                    dispose();
                } catch (DadosIncorretosException | SQLException | ClassNotFoundException | SelectException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        painelLogin.add(botaoLogar);

        painel.add(painelLogin);

        // PAINEL DE CADASTRO
        painelCadastro.setBounds(100, 240, 500, 160);
        painelCadastro.setLayout(null);
        painelCadastro.setBorder(BorderFactory.createTitledBorder("Cadastro"));

        infoNomeCadastro.setBounds(20, 30, 50, 20);
        painelCadastro.add(infoNomeCadastro);

        campoNomeCadastro.setBounds(80, 30, 250, 25);
        painelCadastro.add(campoNomeCadastro);

        infoLoginCadastro.setBounds(20, 70, 50, 20);
        painelCadastro.add(infoLoginCadastro);

        campoLoginCadastro.setBounds(80, 70, 250, 25);
        painelCadastro.add(campoLoginCadastro);

        infoSenhaCadastro.setBounds(20, 110, 50, 20);
        painelCadastro.add(infoSenhaCadastro);

        campoSenhaCadastro.setBounds(80, 110, 250, 25);
        painelCadastro.add(campoSenhaCadastro);

        botaoCadastrar.setBounds(350, 70, 100, 30);
        botaoCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nome = campoNomeCadastro.getText();
                    String login = campoLoginCadastro.getText();
                    String senha = new String(campoSenhaCadastro.getPassword());
                    if(nome.isEmpty() || login.isEmpty() || senha.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Dados inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    } else {
                        protecao.cadastrarProfessor(new Professor(nome, login, senha));
                        JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!", "Sucesso!", JOptionPane.PLAIN_MESSAGE);

                        campoNomeCadastro.setText("");
                        campoLoginCadastro.setText("");
                        campoSenhaCadastro.setText("");
                    }
                } catch (EntidadeJaCadastradaException | SQLException | ClassNotFoundException | InsertException |
                         SelectException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        painelCadastro.add(botaoCadastrar);

        painel.add(painelCadastro);
    }

    public static void main(String[] args) {
        MenuLogin tela = new MenuLogin();
        tela.setVisible(true);
    }
}
