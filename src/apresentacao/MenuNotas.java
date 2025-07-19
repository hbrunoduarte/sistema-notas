package apresentacao;

import dados.*;
import negocio.*;
import excecoes.*;
import persistencia.AlunoDAO;
import persistencia.NotaDAO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MenuNotas extends JFrame {
    Sistema sistema;
    Disciplina disciplina;
    Avaliacao avaliacao;

    private JPanel painel = new JPanel();

    private JPanel painelOpcoes = new JPanel();
    private JLabel infoNomeAluno = new JLabel("Nome do aluno:");
    private JTextField campoNome = new JTextField();
    private JLabel infoNota = new JLabel("Nota:");
    private JTextField campoNota = new JTextField();
    private JButton botaoOk = new JButton("Ok");

    JScrollPane painelScrollNotas = new JScrollPane();
    private JTable tabelaNotas;
    private TabelaNotas notas;

    private JButton botaoVoltar = new JButton("Voltar");

    public MenuNotas(Sistema sistema, Disciplina disciplina, Avaliacao avaliacao) throws SQLException, SelectException, ClassNotFoundException {
        this.sistema = sistema;
        this.disciplina = disciplina;
        this.avaliacao = avaliacao;
        this.notas = new TabelaNotas(sistema, disciplina, avaliacao);

        setTitle("Sistema Notas - Disciplina");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setContentPane(painel);
        painel.setLayout(null);

        // TABELA
        painelScrollNotas.setBounds(50, 50, 400, 350);
        painel.add(painelScrollNotas);

        tabelaNotas = new JTable(notas);
        painelScrollNotas.setViewportView(tabelaNotas);

        // OPCOES
        painelOpcoes.setBounds(470, 50, 280, 350);
        painelOpcoes.setLayout(null);
        painel.add(painelOpcoes);

        infoNomeAluno.setBounds(10, 10, 260, 20);
        painelOpcoes.add(infoNomeAluno);

        campoNome.setBounds(10, 35, 260, 30);
        painelOpcoes.add(campoNome);

        infoNota.setBounds(10, 75, 260, 20);
        painelOpcoes.add(infoNota);

        campoNota.setBounds(10, 100, 260, 30);
        painelOpcoes.add(campoNota);

        botaoOk.setBounds(95, 150, 80, 35);
        botaoOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = campoNome.getText();
                if (nome.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Entrada inválida! O nome não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    float valorNota = Float.parseFloat(campoNota.getText());
                    Nota nota = new Nota(valorNota);
                    Aluno aluno = sistema.buscarAlunoPorNomeEDisciplina(nome, disciplina);

                    if (aluno == null) {
                        JOptionPane.showMessageDialog(null, "Aluno '" + nome + "' não encontrado nesta disciplina.", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (nota.getValor() > 10 || nota.getValor() < 0) {
                        JOptionPane.showMessageDialog(null, "Valor da nota deve ser entre 0 e 10!", "Erro", JOptionPane.ERROR_MESSAGE);
                    } else {
                        notas.salvarNotaNaTabela(aluno, nota);
                    }
                    campoNome.setText("");
                    campoNota.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Informe um número válido para a nota!", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Ocorreu um erro ao salvar a nota: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        painelOpcoes.add(botaoOk);

        botaoVoltar.setBounds(185, 300, 80, 35);
        botaoVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuDisciplina menuDisciplina = new MenuDisciplina(sistema, disciplina);
                menuDisciplina.setVisible(true);
                dispose();
            }
        });
        painelOpcoes.add(botaoVoltar);

    }
}