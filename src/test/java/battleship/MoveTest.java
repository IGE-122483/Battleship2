package battleship;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {

    private List<IPosition> shots;
    private List<IGame.ShotResult> results;
    private Move move;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        shots = List.of(
                new Position(0, 0),
                new Position(0, 1),
                new Position(0, 2)
        );

        results = List.of(
                new IGame.ShotResult(true, false, null, false),
                new IGame.ShotResult(true, false, null, false),
                new IGame.ShotResult(true, false, null, false)
        );

        move = new Move(1, shots, results);
    }

    @AfterEach
    void tearDown() {
        move = null;
        shots = null;
        results = null;
    }

    @Test
    @DisplayName("constructor - deve criar uma instância de Move")
    void constructor() {
        assertNotNull(move, "Erro: a instância de Move não devia ser nula.");
    }

    @Test
    @DisplayName("getNumber - deve devolver o número da jogada")
    void getNumber() {
        assertEquals(1, move.getNumber(),
                "Erro: o número da jogada devia ser 1.");
    }

    @Test
    @DisplayName("getShots - deve devolver a lista de tiros")
    void getShots() {
        assertEquals(shots, move.getShots(),
                "Erro: a lista de tiros devolvida não corresponde à esperada.");
    }

    @Test
    @DisplayName("getShotResults - deve devolver a lista de resultados dos tiros")
    void getShotResults() {
        assertEquals(results, move.getShotResults(),
                "Erro: a lista de resultados devolvida não corresponde à esperada.");
    }

    @Test
    @DisplayName("toString - deve devolver a representação textual da jogada")
    void toStringTest() {
        String text = move.toString();

        assertTrue(text.contains("number=1"),
                "Erro: o texto devia conter o número da jogada.");
        assertTrue(text.contains("shots=3"),
                "Erro: o texto devia conter o número de tiros.");
        assertTrue(text.contains("results=3"),
                "Erro: o texto devia conter o número de resultados.");
    }

    @Test
    @DisplayName("processEnemyFire1 - deve contar tiros repetidos")
    void processEnemyFire1() throws Exception {
        List<IGame.ShotResult> repeatedResults = List.of(
                new IGame.ShotResult(true, true, null, false),
                new IGame.ShotResult(true, true, null, false),
                new IGame.ShotResult(true, true, null, false)
        );

        Move repeatedMove = new Move(2, shots, repeatedResults);

        String json = assertDoesNotThrow(() -> repeatedMove.processEnemyFire(false),
                "Erro: não devia lançar exceção ao processar tiros repetidos.");

        JsonNode node = objectMapper.readTree(json);

        assertEquals(0, node.get("validShots").asInt(),
                "Erro: não devia contar tiros válidos neste cenário.");
        assertEquals(0, node.get("outsideShots").asInt(),
                "Erro: não devia contar tiros exteriores neste cenário.");
        assertEquals(3, node.get("repeatedShots").asInt(),
                "Erro: devia contar 3 tiros repetidos.");
        assertEquals(0, node.get("missedShots").asInt(),
                "Erro: não devia contar tiros na água neste cenário.");
    }

    @Test
    @DisplayName("processEnemyFire2 - deve contar um tiro válido e dois exteriores")
    void processEnemyFire2() throws Exception {
        List<IGame.ShotResult> mixedResults = List.of(
                new IGame.ShotResult(true, false, null, false),
                new IGame.ShotResult(false, false, null, false),
                new IGame.ShotResult(false, false, null, false)
        );

        Move mixedMove = new Move(3, shots, mixedResults);

        String json = assertDoesNotThrow(() -> mixedMove.processEnemyFire(false),
                "Erro: não devia lançar exceção ao processar tiros válidos/exteriores.");

        JsonNode node = objectMapper.readTree(json);

        assertEquals(1, node.get("validShots").asInt(),
                "Erro: devia contar 1 tiro válido.");
        assertEquals(2, node.get("outsideShots").asInt(),
                "Erro: devia contar 2 tiros exteriores.");
        assertEquals(0, node.get("repeatedShots").asInt(),
                "Erro: não devia contar tiros repetidos.");
        assertEquals(1, node.get("missedShots").asInt(),
                "Erro: devia contar 1 tiro na água.");
    }

    @Test
    @DisplayName("processEnemyFire3 - deve contar um acerto num barco não afundado")
    void processEnemyFire3() throws Exception {
        IShip ship = new Barge(Compass.NORTH, new Position(0, 0));

        List<IGame.ShotResult> hitResults = List.of(
                new IGame.ShotResult(true, false, ship, false),
                new IGame.ShotResult(true, false, null, false),
                new IGame.ShotResult(true, false, null, false)
        );

        Move hitMove = new Move(4, shots, hitResults);

        String json = assertDoesNotThrow(() -> hitMove.processEnemyFire(false),
                "Erro: não devia lançar exceção ao processar um acerto.");

        JsonNode node = objectMapper.readTree(json);

        assertEquals(3, node.get("validShots").asInt(),
                "Erro: devia contar 3 tiros válidos.");
        assertEquals(0, node.get("outsideShots").asInt(),
                "Erro: não devia contar tiros exteriores.");
        assertEquals(0, node.get("repeatedShots").asInt(),
                "Erro: não devia contar tiros repetidos.");
        assertEquals(2, node.get("missedShots").asInt(),
                "Erro: devia contar 2 tiros na água.");
        assertEquals(1, node.get("hitsOnBoats").size(),
                "Erro: devia existir 1 registo de acerto em barco.");
        assertEquals(1, node.get("hitsOnBoats").get(0).get("hits").asInt(),
                "Erro: devia existir 1 acerto nesse barco.");
    }

    @Test
    @DisplayName("processEnemyFire4 - deve contar um barco afundado")
    void processEnemyFire4() throws Exception {
        IShip ship = new Barge(Compass.NORTH, new Position(0, 0));

        List<IGame.ShotResult> sunkResults = List.of(
                new IGame.ShotResult(true, false, ship, true),
                new IGame.ShotResult(true, false, null, false),
                new IGame.ShotResult(true, false, null, false)
        );

        Move sunkMove = new Move(5, shots, sunkResults);

        String json = assertDoesNotThrow(() -> sunkMove.processEnemyFire(false),
                "Erro: não devia lançar exceção ao processar um afundamento.");

        JsonNode node = objectMapper.readTree(json);

        assertEquals(1, node.get("sunkBoats").size(),
                "Erro: devia existir 1 barco afundado.");
        assertEquals(1, node.get("sunkBoats").get(0).get("count").asInt(),
                "Erro: devia existir 1 afundamento registado.");
        assertEquals(0, node.get("hitsOnBoats").size(),
                "Erro: não devia haver acertos pendentes em barcos não afundados.");
    }
}