package battleship;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.List;

public class BoardView extends Application {

    private static List<String> jogadas;
    private static int tamanho = 10;

    public static void mostrar(List<String> jogadasRealizadas) {
        jogadas = jogadasRealizadas;
        launch();
    }

    @Override
    public void start(Stage stage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(2);
        grid.setVgap(2);

        for (int row = 0; row < tamanho; row++) {
            for (int col = 0; col < tamanho; col++) {
                Rectangle cell = new Rectangle(40, 40);
                String coord = (char)('A' + row) + String.valueOf(col + 1);
                if (jogadas != null && jogadas.stream().anyMatch(j -> j.contains(coord))) {
                    cell.setFill(Color.RED);   // tiro dado
                } else {
                    cell.setFill(Color.LIGHTBLUE); // água
                }
                cell.setStroke(Color.DARKBLUE);
                grid.add(cell, col, row);
            }
        }

        VBox root = new VBox(new Label("Tabuleiro de Jogo"), grid);
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        stage.setTitle("Battleship - Visualização");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
