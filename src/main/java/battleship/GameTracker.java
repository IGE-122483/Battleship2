package battleship;

import java.util.ArrayList;
import java.util.List;

public class GameTracker {

    private static final List<String> jogadas = new ArrayList<>();

    public static void registarJogada(String jogada) {
        jogadas.add(jogada);
        BoardView.atualizar();
    }

    public static List<String> getJogadas() {
        return new ArrayList<>(jogadas);
    }

    public static void limparJogadas() {
        jogadas.clear();
        BoardView.atualizar();
    }
}