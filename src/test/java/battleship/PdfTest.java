package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PdfTest {

    private Pdf pdf;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        pdf = new Pdf();
    }

    @AfterEach
    void tearDown() {
        pdf = null;
    }

    @Test
    @DisplayName("constructor - deve criar uma instância de Pdf")
    void constructor() {
        assertNotNull(pdf, "Erro: a instância de Pdf não devia ser nula.");
    }

    @Test
    @DisplayName("exportarJogadas1 - deve criar um ficheiro PDF válido com lista de jogadas")
    void exportarJogadas1() throws Exception {
        Path ficheiro = tempDir.resolve("jogadas.pdf");
        List<String> jogadas = List.of("A1", "B2", "C3");

        assertDoesNotThrow(() -> Pdf.exportarJogadas(jogadas, ficheiro.toString()),
                "Erro: não devia lançar exceção ao criar um PDF válido.");

        assertTrue(Files.exists(ficheiro),
                "Erro: o ficheiro PDF devia ter sido criado.");

        assertTrue(Files.size(ficheiro) > 0,
                "Erro: o ficheiro PDF criado não devia estar vazio.");
    }

    @Test
    @DisplayName("exportarJogadas2 - deve criar um PDF mesmo com lista vazia")
    void exportarJogadas2() throws Exception {
        Path ficheiro = tempDir.resolve("jogadas_vazio.pdf");
        List<String> jogadas = List.of();

        assertDoesNotThrow(() -> Pdf.exportarJogadas(jogadas, ficheiro.toString()),
                "Erro: não devia lançar exceção ao criar um PDF com lista vazia.");

        assertTrue(Files.exists(ficheiro),
                "Erro: o ficheiro PDF devia ser criado mesmo com lista vazia.");

        assertTrue(Files.size(ficheiro) > 0,
                "Erro: o ficheiro PDF não devia estar vazio, porque contém pelo menos o cabeçalho.");
    }

    @Test
    @DisplayName("exportarJogadas3 - não deve propagar exceção quando o caminho do ficheiro é inválido")
    void exportarJogadas3() {
        Path ficheiroInvalido = tempDir.resolve("pasta_inexistente").resolve("jogadas.pdf");
        List<String> jogadas = List.of("A1", "B2");

        assertDoesNotThrow(() -> Pdf.exportarJogadas(jogadas, ficheiroInvalido.toString()),
                "Erro: o método não devia propagar exceção, porque trata internamente os erros.");

        assertFalse(Files.exists(ficheiroInvalido),
                "Erro: o ficheiro não devia existir quando o caminho é inválido.");
    }
}