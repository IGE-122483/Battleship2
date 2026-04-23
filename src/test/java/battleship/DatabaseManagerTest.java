package battleship;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    private static final String DB_URL = "jdbc:sqlite:battleship_games.db";

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseManager.createTable();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM jogadas");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM jogadas");
        }
    }

    @Test
    @DisplayName("constructor - deve criar uma instância de DatabaseManager")
    void constructor() {
        DatabaseManager dbManager = new DatabaseManager();
        assertNotNull(dbManager, "Erro: a instância de DatabaseManager não devia ser nula.");
    }

    @Test
    @DisplayName("createTable1 - deve criar a tabela jogadas")
    void createTable1() throws SQLException {
        assertDoesNotThrow(DatabaseManager::createTable,
                "Erro: não devia lançar exceção ao criar a tabela.");

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT name FROM sqlite_master WHERE type='table' AND name='jogadas'");
             ResultSet rs = pstmt.executeQuery()) {

            assertTrue(rs.next(), "Erro: a tabela 'jogadas' devia existir na base de dados.");
            assertEquals("jogadas", rs.getString("name"),
                    "Erro: o nome da tabela criada devia ser 'jogadas'.");
        }
    }

    @Test
    @DisplayName("guardarJogada1 - deve guardar uma jogada na base de dados")
    void guardarJogada1() throws SQLException {
        DatabaseManager.guardarJogada("A", "1", "Água");

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT linha, coluna, resultado FROM jogadas");
             ResultSet rs = pstmt.executeQuery()) {

            assertTrue(rs.next(), "Erro: devia existir uma jogada guardada na base de dados.");
            assertEquals("A", rs.getString("linha"),
                    "Erro: a linha guardada devia ser 'A'.");
            assertEquals("1", rs.getString("coluna"),
                    "Erro: a coluna guardada devia ser '1'.");
            assertEquals("Água", rs.getString("resultado"),
                    "Erro: o resultado guardado devia ser 'Água'.");
        }
    }

    @Test
    @DisplayName("guardarJogada2 - deve guardar várias jogadas")
    void guardarJogada2() throws SQLException {
        DatabaseManager.guardarJogada("A", "1", "Água");
        DatabaseManager.guardarJogada("B", "2", "Acerto");

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT COUNT(*) AS total FROM jogadas");
             ResultSet rs = pstmt.executeQuery()) {

            assertTrue(rs.next(), "Erro: devia ser possível contar as jogadas guardadas.");
            assertEquals(2, rs.getInt("total"),
                    "Erro: deviam existir 2 jogadas guardadas.");
        }
    }

    @Test
    @DisplayName("listarJogadas1 - deve imprimir as jogadas guardadas")
    void listarJogadas1() throws SQLException {
        DatabaseManager.guardarJogada("C", "3", "Água");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));

        try {
            assertDoesNotThrow(DatabaseManager::listarJogadas,
                    "Erro: não devia lançar exceção ao listar jogadas.");
        } finally {
            System.setOut(originalOut);
        }

        String texto = output.toString();

        assertTrue(texto.contains("Jogada #"),
                "Erro: a listagem devia conter o identificador da jogada.");
        assertTrue(texto.contains("Linha: C"),
                "Erro: a listagem devia conter a linha C.");
        assertTrue(texto.contains("Coluna: 3"),
                "Erro: a listagem devia conter a coluna 3.");
        assertTrue(texto.contains("Resultado: Água"),
                "Erro: a listagem devia conter o resultado Água.");
    }
}