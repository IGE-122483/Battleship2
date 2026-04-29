package battleship;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;

import java.util.List;

class PdfContentWriter {

    void preencherDocumento(Document document, List<String> jogadas) throws DocumentException {
        adicionarCabecalho(document);
        adicionarJogadasSeExistirem(document, jogadas);
    }

    void adicionarCabecalho(Document document) throws DocumentException {
        String titulo = "Historico de Jogadas";

        document.add(new Paragraph(titulo));
        document.add(new Paragraph(" "));
    }

    void adicionarJogadasAoDocumento(Document document, List<String> jogadas) throws DocumentException {
        for (String jogada : jogadas) {
            document.add(new Paragraph(jogada));
        }
    }

    void adicionarJogadasSeExistirem(Document document, List<String> jogadas) throws DocumentException {
        adicionarJogadasAoDocumento(document, jogadas);
    }
}
