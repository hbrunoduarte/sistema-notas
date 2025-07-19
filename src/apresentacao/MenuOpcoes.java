package apresentacao;

import dados.Disciplina;
import excecoes.*;
import negocio.Sistema;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class MenuOpcoes extends JFrame {
    Sistema sistema;

    private JPanel painel = new JPanel();

    private JPanel painelOpcoes = new JPanel();
    private JTextField campoDisciplina = new JTextField();
    private JButton botaoGerenciarDisciplina = new JButton("gerenciar discip.");
    private JButton botaoAdicionarDisciplina = new JButton("adicionar discip.");
    private JButton botaoRemoverDisciplina = new JButton("remover disciplina");
    private JButton botaoGerarPDF = new JButton("gerar PDF");
    private JButton botaoLogOut = new JButton("logout");

    private JScrollPane painelScrollTabelaDisciplinas = new JScrollPane();
    private JTable tabelaDisciplinas;
    private TabelaDisciplinas disciplinas;

    public MenuOpcoes(Sistema sistema) {
        this.sistema = sistema;
        this.disciplinas = new TabelaDisciplinas(sistema);

        setTitle("Sistema Notas - Opções");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        setContentPane(painel);
        painel.setLayout(null);

        // PAINEL DE OPCOES
        painelOpcoes.setBounds(400, 50, 350, 300);
        painelOpcoes.setLayout(null);
        painelOpcoes.setBorder(BorderFactory.createTitledBorder("Opções"));
        painel.add(painelOpcoes);

        campoDisciplina.setBounds(20, 30, 300, 30);
        painelOpcoes.add(campoDisciplina);

        botaoGerenciarDisciplina.setBounds(20, 70, 140, 30);
        botaoGerenciarDisciplina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeDisciplina = campoDisciplina.getText();
                campoDisciplina.setText("");
                boolean encontrou = false;

                try {
                    for (Disciplina d : sistema.getDisciplinasDoProfessor()) {
                        if (d.getNome().equals(nomeDisciplina)) {
                            encontrou = true;
                            MenuDisciplina menuDisciplina = new MenuDisciplina(sistema, d);
                            menuDisciplina.setVisible(true);
                            dispose();
                            break;
                        }
                    }
                } catch(SQLException | ClassNotFoundException | SelectException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }

                if (!encontrou) {
                    JOptionPane.showMessageDialog(null, "Disciplina nao encontrada", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        painelOpcoes.add(botaoGerenciarDisciplina);

        botaoAdicionarDisciplina.setBounds(170, 70, 140, 30);
        botaoAdicionarDisciplina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nome = campoDisciplina.getText();
                    campoDisciplina.setText("");
                    if(nome.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Dados inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    } else {
                        Disciplina disciplina = new Disciplina();
                        disciplina.setNome(nome);
                        disciplinas.adicionarDisciplinaTabela(disciplina);
                    }
                } catch (EntidadeJaCadastradaException | SQLException | ClassNotFoundException | SelectException |
                         InsertException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        painelOpcoes.add(botaoAdicionarDisciplina);

        botaoRemoverDisciplina.setBounds(20, 110, 290, 30);
        botaoRemoverDisciplina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nome = campoDisciplina.getText();
                    campoDisciplina.setText("");
                    disciplinas.removerDisciplinaTabela(sistema.buscarDisciplinaPorNomeEProfessor(nome));
                } catch (EntidadeNaoEncontradaException | SQLException | DeleteException | SelectException |
                         ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        painelOpcoes.add(botaoRemoverDisciplina);

        botaoGerarPDF.setBounds(50, 200, 100, 40);
        botaoGerarPDF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sistema.gerarPDF();
                } catch (IOException | SelectException | SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
                JOptionPane.showMessageDialog(null, "PDF gerado com sucesso!", "Sucesso", JOptionPane.PLAIN_MESSAGE);
            }
        });
        painelOpcoes.add(botaoGerarPDF);

        botaoLogOut.setBounds(200, 200, 100, 40);
        botaoLogOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuLogin menuLogin = new MenuLogin();
                menuLogin.setVisible(true);
                dispose();
            }
        });
        painelOpcoes.add(botaoLogOut);

        // TABELA
        painelScrollTabelaDisciplinas.setBounds(50, 50, 300, 350);
        painel.add(painelScrollTabelaDisciplinas);

        tabelaDisciplinas = new JTable(disciplinas);
        painelScrollTabelaDisciplinas.setViewportView(tabelaDisciplinas);
    }
}
