package negocio;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import dados.*;
import excecoes.*;
import persistencia.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Sistema {
    private Professor professor;

    public Sistema(Professor professor) {
        this.professor = professor;
    }

    public Sistema() {}

    public Professor getProfessor() {
        return professor;
    }

    public void adicionarDisciplina(Disciplina disciplina) throws EntidadeJaCadastradaException, SQLException, ClassNotFoundException, SelectException, InsertException {
        List<Disciplina> disciplinas = DisciplinaDAO.getInstance().selectAll(professor);
        if(disciplinas.contains(disciplina)) {
            throw new EntidadeJaCadastradaException(disciplina);
        } else {
            DisciplinaDAO.getInstance().insert(disciplina, professor);
        }
    }

    public void removerDisciplina(Disciplina disciplina) throws EntidadeNaoEncontradaException, SQLException, ClassNotFoundException, DeleteException, SelectException {
        List<Disciplina> disciplinas = DisciplinaDAO.getInstance().selectAll(professor);
        if(disciplinas.contains(disciplina)) {
            DisciplinaDAO.getInstance().delete(disciplina, professor);
        } else {
            throw new EntidadeNaoEncontradaException(disciplina);
        }
    }

    public void adicionarAluno(Disciplina disciplina, Aluno aluno) throws EntidadeJaCadastradaException, SQLException, ClassNotFoundException, SelectException, InsertException {
        List<Aluno> alunos = AlunoDAO.getInstance().selectAll(disciplina);
        if (alunos.contains(aluno)) {
            throw new EntidadeJaCadastradaException(aluno);
        } else {
            AlunoDAO.getInstance().insert(aluno, disciplina);
        }
    }

    public void removerAluno(Disciplina disciplina, Aluno aluno) throws EntidadeNaoEncontradaException, SQLException, ClassNotFoundException, DeleteException, SelectException {
        List<Aluno> alunos = AlunoDAO.getInstance().selectAll(disciplina);
        if(alunos.contains(aluno)) {
            AlunoDAO.getInstance().delete(aluno, disciplina);
        } else {
            throw new EntidadeNaoEncontradaException(aluno);
        }
    }

    public float calculaMedia(Disciplina disciplina, Aluno aluno) throws SQLException, ClassNotFoundException, SelectException {
        float mediaFinal = 0;
        float somaPesos = 0;

        AvaliacaoDAO avaliacaoDAO = AvaliacaoDAO.getInstance();
        NotaDAO notaDAO = NotaDAO.getInstance();

        for(Avaliacao avaliacao : AvaliacaoDAO.getInstance().selectAll(disciplina)) {
            Nota nota = notaDAO.selectByAlunoAndAvaliacao(avaliacao, aluno);
            if (nota == null) continue;

            somaPesos += avaliacao.getPeso();
            mediaFinal += nota.getValor() * avaliacao.getPeso();
        }

        if(somaPesos > 0) {
            return mediaFinal/somaPesos;
        }

        return 0;
    }

    public String notaNecessariaExame(Disciplina disciplina, Aluno aluno) throws EntidadeNaoEncontradaException, SQLException, ClassNotFoundException, SelectException {
        if(!AlunoDAO.getInstance().selectAll(disciplina).contains(aluno)) {
            throw new EntidadeNaoEncontradaException(aluno);
        }

        if (AvaliacaoDAO.getInstance().selectAll(disciplina).isEmpty()) {
            return "Sem avaliações cadastradas";
        }

        float mediaFinal = calculaMedia(disciplina, aluno);

        if (mediaFinal < 1.7) {
            return "Já está reprovado!";
        } else if (mediaFinal >= 7) {
            return "Já está passado!";
        } else {
            double notaNecessaria = -1.5 * mediaFinal + 12.5;
            return "Nota necessária no exame: " + notaNecessaria;
        }
    }

    public void adicionarAvaliacao(Disciplina disciplina, Avaliacao avaliacao) throws EntidadeJaCadastradaException, SQLException, ClassNotFoundException, InsertException, SelectException {
        List<Avaliacao> avaliacoes = AvaliacaoDAO.getInstance().selectAll(disciplina);
        if(avaliacoes.contains(avaliacao)) {
            throw new EntidadeJaCadastradaException(avaliacao);
        } else {
            AvaliacaoDAO.getInstance().insert(avaliacao, disciplina);
        }
    }

    public void removerAvaliacao(Disciplina disciplina, Avaliacao avaliacao) throws EntidadeNaoEncontradaException, SQLException, ClassNotFoundException, SelectException, DeleteException {
        List<Avaliacao> avaliacoes = AvaliacaoDAO.getInstance().selectAll(disciplina);
        if(avaliacoes.contains(avaliacao)) {
            AvaliacaoDAO.getInstance().delete(avaliacao, disciplina);
        } else {
            throw new EntidadeNaoEncontradaException(avaliacao);
        }
    }

    public void atribuirOuAtualizarNota(Disciplina disciplina, Avaliacao avaliacao, Aluno aluno, Nota nota) throws SQLException, ClassNotFoundException, SelectException, InsertException, UpdateException, EntidadeNaoEncontradaException {
        List<Aluno> alunosDaDisciplina = AlunoDAO.getInstance().selectAll(disciplina);
        if (!alunosDaDisciplina.contains(aluno)) {
            throw new EntidadeNaoEncontradaException(aluno);
        }

        NotaDAO notaDAO = NotaDAO.getInstance();
        Nota notaExistente = notaDAO.selectByAlunoAndAvaliacao(avaliacao, aluno);

        if (notaExistente != null) {
            notaDAO.update(nota, aluno, avaliacao);
        } else {
            notaDAO.insert(nota, avaliacao, aluno);
        }
    }

    public List<Disciplina> getDisciplinasDoProfessor() throws SQLException, ClassNotFoundException, SelectException {
        return DisciplinaDAO.getInstance().selectAll(this.professor);
    }

    public Disciplina buscarDisciplinaPorNomeEProfessor(String nome) throws SQLException, SelectException, ClassNotFoundException {
        return DisciplinaDAO.getInstance().buscarPorNome(nome, this.professor);
    }

    public Aluno buscarAlunoPorNomeEDisciplina(String nome, Disciplina disciplina) throws SQLException, ClassNotFoundException, SelectException {
        return AlunoDAO.getInstance().buscarPorNome(nome, disciplina);
    }

    public Avaliacao buscarAvaliacaoPorNomeEDisciplina(String nome, Disciplina disciplina) throws SQLException, ClassNotFoundException, SelectException {
        return AvaliacaoDAO.getInstance().buscarPorNome(nome, disciplina);
    }

    public List<Avaliacao> buscarAvaliacoesPorDisciplina(Disciplina disciplina) throws SQLException, ClassNotFoundException, SelectException {
        return AvaliacaoDAO.getInstance().selectAll(disciplina);
    }

    public void gerarPDF() throws IOException, SQLException, ClassNotFoundException, SelectException {
        String dest = "src\\documentos\\TabelaDeNotas.pdf";
        PdfWriter writer;
        writer = new PdfWriter(dest);

        PdfDocument pdf = new PdfDocument(writer);

        Document doc = new Document(pdf);

        float[] pointColumnWidths = {150F, 150F, 150F};
        Table table = new Table(pointColumnWidths);

        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        Paragraph titulo = new Paragraph("Tabela de Médias")
                .setFont(boldFont)
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        doc.add(titulo);

        String[] headers = {"Disciplina", "Nome", "Média"};
        for (String h : headers) {
            table.addHeaderCell(new Cell()
                    .add(new Paragraph(h))
                    .setFont(boldFont)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorder(new SolidBorder(ColorConstants.BLACK, 1)));
        }

        List<Disciplina> disciplinas = DisciplinaDAO.getInstance().selectAll(professor);
        for (Disciplina disciplina : disciplinas) {
            List<Aluno> alunos = AlunoDAO.getInstance().selectAll(disciplina);
            if (alunos.isEmpty()) continue;

            for (Aluno aluno : alunos) {
                table.addCell(new Cell().add(new Paragraph(disciplina.getNome()))).setTextAlignment(TextAlignment.CENTER);
                table.addCell(new Cell().add(new Paragraph(aluno.getNome()))).setTextAlignment(TextAlignment.CENTER);
                table.addCell(new Cell().add(new Paragraph(String.format("%.1f", calculaMedia(disciplina, aluno))))).setTextAlignment(TextAlignment.CENTER);
            }
        }

        doc.add(table);
        doc.close();
    }
}