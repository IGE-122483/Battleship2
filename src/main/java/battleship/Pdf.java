package battleship;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class Pdf {

    public static void exportarJogadas(List<String> jogadas, String nomeFicheiro) {
        try {
            Document document = criarDocumento(nomeFicheiro);
            PdfContentWriter contentWriter = new PdfContentWriter();
            contentWriter.preencherDocumento(document, jogadas);
            fecharDocumento(document);
            System.out.println("PDF criado com sucesso!");
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Document criarDocumento(String nomeFicheiro) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(nomeFicheiro));
        document.open();
        return document;
    }

    private static void fecharDocumento(Document document) {
        document.close();
    }
}