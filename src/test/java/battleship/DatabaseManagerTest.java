package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;

/**
 * Test class for DatabaseManager.
 * Author: IGE-122464
 * Date: 2026-05-02
 * Cyclomatic Complexity:
 * - createTable(): 1
 * - guardarJogada(): 1
 * - listarJogadas(): 1
 */
class DatabaseManagerTest {

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseManager.createTable();
    }

    @AfterEach
    void tearDown() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:battleship_games.db");
        conn.createStatement().execute("DROP TABLE IF EXISTS jogadas");
        conn.close();
    }

    @Test
    @DisplayName("createTable: tabela criada sem erros")
    void createTable() {
        assertDoesNotThrow(() -> DatabaseManager.createTable(),
                "Erro: createTable() não devia lançar exceção");
    }

    @Test
    @DisplayName("guardarJogada: jogada guardada sem erros")
    void guardarJogada() {
        assertDoesNotThrow(() -> DatabaseManager.guardarJogada(new JogadaData("A", "1", "água")),
                "Erro: guardarJogada() não devia lançar exceção");
    }

    @Test
    @DisplayName("listarJogadas: lista jogadas sem erros")
    void listarJogadas() throws SQLException {
        DatabaseManager.guardarJogada(new JogadaData("B", "2", "hit"));
        assertDoesNotThrow(() -> Scoreboard.listarJogadas(),
                "Erro: listarJogadas() não devia lançar exceção");
    }
}