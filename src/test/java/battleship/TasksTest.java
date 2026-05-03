package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Scanner;

/**
 * Test class for Tasks.
 * Author: IGE-122464
 * Date: 2026-05-02
 * Cyclomatic Complexity:
 * - menuHelp(): 1
 * - buildFleet(): 3
 * - readShip(): 1
 * - readPosition(): 1
 * - readClassicPosition(): 5
 */
class TasksTest {

    /**
     * Gera uma string com uma frota válida usando Fleet.createRandom()
     */
    private String generateValidFleetInput() {
        Fleet randomFleet = (Fleet) Fleet.createRandom();
        StringBuilder sb = new StringBuilder();
        for (IShip s : randomFleet.getShips()) {
            sb.append(s.getCategory().toLowerCase())
                    .append(" ")
                    .append(s.getPosition().getRow())
                    .append(" ")
                    .append(s.getPosition().getColumn())
                    .append(" ")
                    .append(s.getBearing().getDirection())
                    .append(" ");
        }
        return sb.toString();
    }

    @BeforeEach
    void setUp() {}

    @AfterEach
    void tearDown() {}

    @Test
    @DisplayName("menuHelp: imprime ajuda sem erros")
    void menuHelp() {
        assertDoesNotThrow(() -> Tasks.menuHelp(),
                "Erro: menuHelp() não devia lançar exceção");
    }

    @Test
    @DisplayName("readPosition: lê posição válida")
    void readPosition() {
        Scanner in = new Scanner("3 5");
        Position pos = Tasks.readPosition(in);
        assertAll(
                () -> assertEquals(3, pos.getRow(), "Erro: linha devia ser 3"),
                () -> assertEquals(5, pos.getColumn(), "Erro: coluna devia ser 5")
        );
    }

    @Test
    @DisplayName("readShip: lê navio válido")
    void readShip() {
        Scanner in = new Scanner("galeao 1 1 n");
        Ship ship = Tasks.readShip(in);
        assertNotNull(ship, "Erro: navio não devia ser null");
    }

    @Test
    @DisplayName("buildFleet1: frota construída com sucesso")
    void buildFleet1() {
        Scanner in = new Scanner(generateValidFleetInput());
        Fleet fleet = Tasks.buildFleet(in);
        assertNotNull(fleet, "Erro: frota não devia ser null");
    }

    @Test
    @DisplayName("buildFleet2: frota com navio inválido ignorado")
    void buildFleet2() {
        String input = "tipoInvalido 1 1 n " + generateValidFleetInput();
        Scanner in = new Scanner(input);
        Fleet fleet = Tasks.buildFleet(in);
        assertNotNull(fleet, "Erro: frota não devia ser null mesmo com navio inválido");
    }

    @Test
    @DisplayName("buildFleet3: frota com colisão ignorada")
    void buildFleet3() {
        String input = "galeao 0 0 e galeao 0 0 e " + generateValidFleetInput();
        Scanner in = new Scanner(input);
        Fleet fleet = Tasks.buildFleet(in);
        assertNotNull(fleet, "Erro: frota não devia ser null mesmo com colisão");
    }

    @Test
    @DisplayName("readClassicPosition1: formato compacto A3")
    void readClassicPosition1() {
        Scanner in = new Scanner("A3");
        IPosition pos = Tasks.readClassicPosition(in);
        assertNotNull(pos, "Erro: posição não devia ser null");
    }

    @Test
    @DisplayName("readClassicPosition2: formato com espaço A 3")
    void readClassicPosition2() {
        Scanner in = new Scanner("A 3");
        IPosition pos = Tasks.readClassicPosition(in);
        assertNotNull(pos, "Erro: posição não devia ser null");
    }

    @Test
    @DisplayName("readClassicPosition3: formato minúsculas a3")
    void readClassicPosition3() {
        Scanner in = new Scanner("a3");
        IPosition pos = Tasks.readClassicPosition(in);
        assertNotNull(pos, "Erro: posição não devia ser null");
    }

    @Test
    @DisplayName("readClassicPosition4: formato inválido lança exceção")
    void readClassicPosition4() {
        Scanner in = new Scanner("123");
        assertThrows(IllegalArgumentException.class, () -> Tasks.readClassicPosition(in),
                "Erro: devia lançar IllegalArgumentException para formato inválido");
    }

    @Test
    @DisplayName("readClassicPosition5: scanner vazio lança exceção")
    void readClassicPosition5() {
        Scanner in = new Scanner("");
        assertThrows(IllegalArgumentException.class, () -> Tasks.readClassicPosition(in),
                "Erro: devia lançar IllegalArgumentException para scanner vazio");
    }
}