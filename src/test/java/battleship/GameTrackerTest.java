package battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTrackerTest {

    @BeforeEach
    void setUp() {
        GameTracker.limparJogadas();
    }

    @Test
    @DisplayName("Registar uma jogada adiciona-a à lista")
    void registarJogada() {
        GameTracker.registarJogada("Jogada 1 - B4 - Agua");
        List<String> jogadas = GameTracker.getJogadas();
        assertEquals(1, jogadas.size());
        assertEquals("Jogada 1 - B4 - Agua", jogadas.get(0));
    }

    @Test
    @DisplayName("Registar múltiplas jogadas mantém a ordem")
    void registarMultiplasJogadas() {
        GameTracker.registarJogada("Jogada 1 - B4 - Agua");
        GameTracker.registarJogada("Jogada 2 - C6 - Acertou");
        GameTracker.registarJogada("Jogada 3 - D6 - Afundou");
        List<String> jogadas = GameTracker.getJogadas();
        assertEquals(3, jogadas.size());
        assertEquals("Jogada 2 - C6 - Acertou", jogadas.get(1));
    }

    @Test
    @DisplayName("getJogadas retorna lista vazia quando não há jogadas")
    void getJogadasVazio() {
        List<String> jogadas = GameTracker.getJogadas();
        assertTrue(jogadas.isEmpty());
    }

    @Test
    @DisplayName("getJogadas retorna uma cópia independente da lista")
    void getJogadasRetornaCopia() {
        GameTracker.registarJogada("Jogada 1 - B4 - Agua");
        List<String> jogadas = GameTracker.getJogadas();
        jogadas.add("Jogada Extra");
        assertEquals(1, GameTracker.getJogadas().size());
    }

    @Test
    @DisplayName("limparJogadas remove todas as jogadas")
    void limparJogadas() {
        GameTracker.registarJogada("Jogada 1 - B4 - Agua");
        GameTracker.registarJogada("Jogada 2 - C6 - Acertou");
        GameTracker.limparJogadas();
        assertTrue(GameTracker.getJogadas().isEmpty());
    }
}