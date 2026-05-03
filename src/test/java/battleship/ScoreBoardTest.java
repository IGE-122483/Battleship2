package battleship;

import org.junit.jupiter.api.*;
import java.io.File;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Scoreboard class.
 */
class ScoreboardTest {

    private static final String SCORES_FILE = "data/scores.json";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // Redirecionar stdout para capturar prints
        System.setOut(new PrintStream(outContent));
        // Limpar o ficheiro de scores antes de cada teste
        File file = new File(SCORES_FILE);
        if (file.exists()) file.delete();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        // Limpar o ficheiro de scores depois de cada teste
        File file = new File(SCORES_FILE);
        if (file.exists()) file.delete();
    }

    // ─── loadScores ───────────────────────────────────────────────

    @Test
    @DisplayName("loadScores deve retornar lista vazia quando o ficheiro não existe")
    void testLoadScores_FileNotExists_ReturnsEmptyList() {
        List<Score> scores = Scoreboard.loadScores();
        assertNotNull(scores);
        assertTrue(scores.isEmpty());
    }

    @Test
    @DisplayName("loadScores deve retornar lista com 1 score após guardar")
    void testLoadScores_AfterOneSave_ReturnsOneScore() {
        Scoreboard.saveScore(10, "Vitória");
        List<Score> scores = Scoreboard.loadScores();
        assertEquals(1, scores.size());
    }

    @Test
    @DisplayName("loadScores deve retornar lista vazia quando o ficheiro JSON está corrompido")
    void testLoadScores_CorruptFile_ReturnsEmptyList() throws Exception {
        File dataDir = new File("data");
        dataDir.mkdirs();
        File scoresFile = new File(SCORES_FILE);
        java.nio.file.Files.writeString(scoresFile.toPath(), "{ invalid json ]]");

        List<Score> scores = Scoreboard.loadScores();
        assertNotNull(scores);
        assertTrue(scores.isEmpty());
    }

    // ─── saveScore ────────────────────────────────────────────────

    @Test
    @DisplayName("saveScore deve guardar score com resultado Vitória")
    void testSaveScore_Victory_ResultIsPersisted() {
        Scoreboard.saveScore(15, "Vitória");
        List<Score> scores = Scoreboard.loadScores();
        assertEquals(1, scores.size());
        assertEquals("Vitória", scores.get(0).getResult());
        assertEquals(15, scores.get(0).getShots());
    }

    @Test
    @DisplayName("saveScore deve guardar score com resultado Desistência")
    void testSaveScore_Forfeit_ResultIsPersisted() {
        Scoreboard.saveScore(5, "Desistência");
        List<Score> scores = Scoreboard.loadScores();
        assertEquals(1, scores.size());
        assertEquals("Desistência", scores.get(0).getResult());
    }

    @Test
    @DisplayName("saveScore deve acumular múltiplos scores")
    void testSaveScore_MultipleScores_AllPersisted() {
        Scoreboard.saveScore(10, "Vitória");
        Scoreboard.saveScore(20, "Desistência");
        Scoreboard.saveScore(5, "Vitória");
        List<Score> scores = Scoreboard.loadScores();
        assertEquals(3, scores.size());
    }

    @Test
    @DisplayName("saveScore deve imprimir mensagem de sucesso")
    void testSaveScore_PrintsSuccessMessage() {
        Scoreboard.saveScore(10, "Vitória");
        assertTrue(outContent.toString().contains("Score guardado com sucesso!"));
    }

    @Test
    @DisplayName("saveScore com 0 tiros deve ser guardado corretamente")
    void testSaveScore_ZeroShots_IsSaved() {
        Scoreboard.saveScore(0, "Desistência");
        List<Score> scores = Scoreboard.loadScores();
        assertEquals(0, scores.get(0).getShots());
    }

    @Test
    @DisplayName("saveScore deve guardar a data no formato correto dd/MM/yyyy HH:mm")
    void testSaveScore_DateFormat_IsCorrect() {
        Scoreboard.saveScore(10, "Vitória");
        List<Score> scores = Scoreboard.loadScores();
        String date = scores.get(0).getDate();
        assertNotNull(date);
        assertTrue(date.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}"));
    }

    // ─── printScoreboard ──────────────────────────────────────────

    @Test
    @DisplayName("printScoreboard deve imprimir mensagem quando não há scores")
    void testPrintScoreboard_Empty_PrintsNoGamesMessage() {
        Scoreboard.printScoreboard();
        assertTrue(outContent.toString().contains("Ainda não existem jogos registados."));
    }

    @Test
    @DisplayName("printScoreboard deve imprimir cabeçalho SCOREBOARD quando há scores")
    void testPrintScoreboard_WithScores_PrintsHeader() {
        Scoreboard.saveScore(10, "Vitória");
        outContent.reset();
        Scoreboard.printScoreboard();
        assertTrue(outContent.toString().contains("SCOREBOARD"));
    }

    @Test
    @DisplayName("printScoreboard deve listar scores numerados a partir de 1")
    void testPrintScoreboard_WithMultipleScores_PrintsNumberedList() {
        Scoreboard.saveScore(10, "Vitória");
        Scoreboard.saveScore(20, "Desistência");
        outContent.reset();
        Scoreboard.printScoreboard();
        String output = outContent.toString();
        assertTrue(output.contains("1."));
        assertTrue(output.contains("2."));
    }

    @Test
    @DisplayName("printScoreboard deve imprimir o resultado de cada score")
    void testPrintScoreboard_WithScore_PrintsResult() {
        Scoreboard.saveScore(8, "Vitória");
        outContent.reset();
        Scoreboard.printScoreboard();
        assertTrue(outContent.toString().contains("Vitória"));
    }
}
