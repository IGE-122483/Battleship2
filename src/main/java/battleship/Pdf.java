package battleship;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.List;

public class Pdf {

    public static void exportarJogadas(List<String> jogadas, String nomeFicheiro) {

        try {

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(nomeFicheiro));

            document.open();

            document.add(new Paragraph("Historico de Jogadas"));
            document.add(new Paragraph(" "));

            for (String jogada : jogadas) {
                document.add(new Paragraph(jogada));
            }

            document.close();

            System.out.println("PDF criado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}