package apresentacao;

import dados.*;
import negocio.*;
import excecoes.*;
import persistencia.AlunoDAO;
import persistencia.AvaliacaoDAO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MenuDisciplina extends JFrame {
    Sistema sistema;
    Disciplina disciplina;

    private JPanel painel = new JPanel();

    private JPanel painelAlunos = new JPanel();
    private JTextField campoAluno = new JTextField();
    private JButton botaoAdicionarAluno = new JButton("Adicionar");
    private JButton botaoRemoverAluno = new JButton("Remover");
    private JButton botaoNotaExame = new JButton("Exame");

    private JPanel painelAvaliacoes = new JPanel();
    private JLabel infoNomeAvaliacao = new JLabel("Nome da avaliacao:");
    private JTextField campoNomeAvaliacao = new JTextField();
    private JLabel infoDataAvaliacao = new JLabel("Data:");
    private JTextField campoDataAvaliacao = new JTextField();
    private JLabel infoPesoAvaliacao = new JLabel("Peso:");
    private JTextField campoPesoAvaliacao = new JTextField();
    private JButton botaoAdicionarAvaliacao = new JButton("Adicionar");
    private JButton botaoRemoverAvaliacao = new JButton("Remover");

    private JScrollPane painelScrollAlunos = new JScrollPane();
    private JTable tabelaAlunos;
    private TabelaAlunos alunos;

    private JScrollPane painelScrollAvaliacoes = new JScrollPane();
    private JTable tabelaAvaliacoes;
    private TabelaAvaliacoes avaliacoes;

    private JButton botaoDarNota = new JButton("Dar nota");
    private JButton botaoVoltar = new JButton("Voltar");

    public MenuDisciplina(Sistema sistema, Disciplina disciplina) {
        this.sistema = sistema;
        this.disciplina = disciplina;
        this.alunos = new TabelaAlunos(sistema, disciplina);
        this.avaliacoes = new TabelaAvaliacoes(sistema, disciplina);

        setTitle("Sistema Notas - Disciplina");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setContentPane(painel);
        painel.setLayout(null);

        // PAINEL ALUNOS
        painelAlunos.setBounds(460, 20, 310, 110);
        painelAlunos.setLayout(null);
        painelAlunos.setBorder(BorderFactory.createTitledBorder("Gerenciar alunos"));

        campoAluno.setBounds(10, 20, 290, 25);
        painelAlunos.add(campoAluno);

        botaoAdicionarAluno.setBounds(10, 55, 90, 30);
        botaoAdicionarAluno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nome = campoAluno.getText();
                    campoAluno.setText("");
                    if (nome.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Dados inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    } else {
                        alunos.adicionarAlunoTabela(new Aluno(nome));
                    }
                } catch (EntidadeJaCadastradaException | SQLException | InsertException | SelectException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        painelAlunos.add(botaoAdicionarAluno);

        botaoRemoverAluno.setBounds(110, 55, 90, 30);
        botaoRemoverAluno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nome = campoAluno.getText();
                    campoAluno.setText("");
                    alunos.removerAlunoTabela(sistema.buscarAlunoPorNomeEDisciplina(nome, disciplina));
                } catch (EntidadeNaoEncontradaException | SQLException | DeleteException | ClassNotFoundException | SelectException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        painelAlunos.add(botaoRemoverAluno);

        botaoNotaExame.setBounds(210, 55, 90, 30);
        botaoNotaExame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = campoAluno.getText();
                if (nome == null || nome.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Dados invalidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    String mensagem = sistema.notaNecessariaExame(disciplina, sistema.buscarAlunoPorNomeEDisciplina(nome, disciplina));
                    JOptionPane.showMessageDialog(null, mensagem, "Nota necessária no exame:", JOptionPane.PLAIN_MESSAGE);
                } catch (EntidadeNaoEncontradaException | SQLException | ClassNotFoundException | SelectException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        painelAlunos.add(botaoNotaExame);

        painel.add(painelAlunos);

        // PAINEL AVALIACOES
        painelAvaliacoes.setBounds(460, 140, 310, 200);
        painelAvaliacoes.setLayout(null);
        painelAvaliacoes.setBorder(BorderFactory.createTitledBorder("Gerenciar avaliacoes"));

        infoNomeAvaliacao.setBounds(10, 20, 150, 20);
        painelAvaliacoes.add(infoNomeAvaliacao);

        campoNomeAvaliacao.setBounds(10, 40, 290, 25);
        painelAvaliacoes.add(campoNomeAvaliacao);

        infoDataAvaliacao.setBounds(10, 70, 150, 20);
        painelAvaliacoes.add(infoDataAvaliacao);

        campoDataAvaliacao.setBounds(10, 90, 290, 25);
        painelAvaliacoes.add(campoDataAvaliacao);

        infoPesoAvaliacao.setBounds(10, 120, 150, 20);
        painelAvaliacoes.add(infoPesoAvaliacao);

        campoPesoAvaliacao.setBounds(10, 140, 290, 25);
        painelAvaliacoes.add(campoPesoAvaliacao);

        botaoAdicionarAvaliacao.setBounds(10, 170, 140, 25);
        botaoAdicionarAvaliacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nome = campoNomeAvaliacao.getText();
                    String data = campoDataAvaliacao.getText();
                    float peso = -1f;
                    try {
                        peso = Float.parseFloat(campoPesoAvaliacao.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Peso inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    campoNomeAvaliacao.setText("");
                    campoDataAvaliacao.setText("");
                    campoPesoAvaliacao.setText("");
                    if (nome.isEmpty() || data.isEmpty() || peso == -1f) {
                        JOptionPane.showMessageDialog(null, "Dados inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    } else {

                        avaliacoes.adicionarAvaliacaoTabela(new Avaliacao(nome, data, peso));
                    }
                } catch (EntidadeJaCadastradaException | SQLException | InsertException | SelectException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        painelAvaliacoes.add(botaoAdicionarAvaliacao);

        botaoRemoverAvaliacao.setBounds(160, 170, 140, 25);
        botaoRemoverAvaliacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nome = campoNomeAvaliacao.getText();
                    campoNomeAvaliacao.setText("");
                    Avaliacao avaliacao = sistema.buscarAvaliacaoPorNomeEDisciplina(nome, disciplina);
                    avaliacoes.removerAvaliacaoTabela(avaliacao);
                } catch (EntidadeNaoEncontradaException | SQLException | DeleteException | SelectException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        painelAvaliacoes.add(botaoRemoverAvaliacao);

        painel.add(painelAvaliacoes);

        // TABELA ALUNOS
        painelScrollAlunos.setBounds(20, 20, 200, 400);
        painel.add(painelScrollAlunos);

        tabelaAlunos = new JTable(alunos);
        painelScrollAlunos.setViewportView(tabelaAlunos);

        // TABELA AVALIACOES
        painelScrollAvaliacoes.setBounds(240, 20, 200, 400);
        painel.add(painelScrollAvaliacoes);

        tabelaAvaliacoes = new JTable(avaliacoes);
        painelScrollAvaliacoes.setViewportView(tabelaAvaliacoes);

        // OUTROS
        botaoDarNota.setBounds(490, 360, 140, 30);
        botaoDarNota.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String avaliacao = campoNomeAvaliacao.getText();
                    campoNomeAvaliacao.setText("");
                    boolean encontrou = false;

                    for (Avaliacao a : sistema.buscarAvaliacoesPorDisciplina(disciplina)) {
                        if (avaliacao.equals(a.toString())) {
                            encontrou = true;
                            MenuNotas menuNotas = new MenuNotas(sistema, disciplina, a);
                            menuNotas.setVisible(true);
                            dispose();
                            break;
                        }
                    }

                    if (!encontrou) {
                        JOptionPane.showMessageDialog(null, "Avaliacao nao encontrada", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException | ClassNotFoundException | SelectException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        painel.add(botaoDarNota);

        botaoVoltar.setBounds(640, 360, 100, 30);
        botaoVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuOpcoes menuOpcoes = new MenuOpcoes(sistema);
                menuOpcoes.setVisible(true);
                dispose();
            }
        });
        painel.add(botaoVoltar);

    }
}
