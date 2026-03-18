package battleship;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardView {

    private static final int TAMANHO = 10;
    private static boolean javafxIniciado = false;
    private static boolean janelaAberta = false;

    private static Stage stage;
    private static final Map<String, Rectangle> cells = new HashMap<>();

    public static void mostrar() {
        if (!javafxIniciado) {
            javafxIniciado = true;
            Platform.startup(BoardView::criarJanela);
        } else {
            Platform.runLater(() -> {
                if (!janelaAberta) {
                    criarJanela();
                } else {
                    stage.toFront();
                    atualizarTabuleiro();
                }
            });
        }
    }

    private static void criarJanela() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(2);
        grid.setVgap(2);

        cells.clear();

        for (int row = 0; row < TAMANHO; row++) {
            for (int col = 0; col < TAMANHO; col++) {
                Rectangle cell = new Rectangle(40, 40);
                cell.setFill(Color.LIGHTBLUE);
                cell.setStroke(Color.DARKBLUE);

                String coord = (char) ('A' + row) + String.valueOf(col + 1);
                cells.put(coord, cell);

                grid.add(cell, col, row);
            }
        }

        VBox root = new VBox(new Label("Tabuleiro de Jogo"), grid);
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        stage = new Stage();
        stage.setTitle("Battleship - Visualização");
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(e -> janelaAberta = false);
        stage.show();

        janelaAberta = true;
        atualizarTabuleiro();
    }

    public static void atualizar() {
        if (javafxIniciado && janelaAberta) {
            Platform.runLater(BoardView::atualizarTabuleiro);
        }
    }

    private static void atualizarTabuleiro() {
        for (Rectangle r : cells.values()) {
            r.setFill(Color.LIGHTBLUE);
        }

        List<String> jogadas = GameTracker.getJogadas();

        for (String jogada : jogadas) {
            String coord = extrairCoordenada(jogada);
            if (coord == null) {
                continue;
            }

            Rectangle cell = cells.get(coord);
            if (cell == null) {
                continue;
            }

            String jogadaLower = jogada.toLowerCase();

            if (jogadaLower.contains("afundou")) {
                cell.setFill(Color.RED);
            } else if (jogadaLower.contains("acertou")) {
                cell.setFill(Color.ORANGE);
            } else if (jogadaLower.contains("agua")) {
                cell.setFill(Color.GRAY);
            } else if (jogadaLower.contains("repetido")) {
                cell.setFill(Color.YELLOW);
            } else if (jogadaLower.contains("exterior")) {
                // Não pinta nada porque está fora do tabuleiro
            }
        }
    }

    private static String extrairCoordenada(String jogada) {
        String[] partes = jogada.split(" - ");
        for (String parte : partes) {
            String texto = parte.trim().toUpperCase();
            if (texto.matches("^[A-J](10|[1-9])$")) {
                return texto;
            }
        }
        return null;
    }
}