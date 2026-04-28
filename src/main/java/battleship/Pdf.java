package battleship;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.List;

public class Pdf {

    public static void exportarJogadas(List<String> jogadas, String nomeFicheiro) {
        try {
            Document document = criarDocumento(nomeFicheiro);
            adicionarCabecalho(document);
            adicionarJogadasAoDocumento(document, jogadas);
            fecharDocumento(document);
            System.out.println("PDF criado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Document criarDocumento(String nomeFicheiro) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(nomeFicheiro));
        document.open();
        return document;
    }

    private static void adicionarCabecalho(Document document) throws Exception {
        document.add(new com.lowagie.text.Paragraph("Historico de Jogadas"));
        document.add(new com.lowagie.text.Paragraph(" "));
    }

    private static void adicionarJogadasAoDocumento(Document document, List<String> jogadas) throws Exception {
        for (String jogada : jogadas) {
            document.add(new com.lowagie.text.Paragraph(jogada));
        }
    }

    private static void fecharDocumento(Document document) {
        document.close();
    }
}