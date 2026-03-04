package battleship;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the scoreboard by saving and loading game scores from a JSON file.
 * @author O_Teu_Nome
 */
public class Scoreboard {

    private static final String SCORES_FILE = "data/scores.json";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Saves a new score to the scoreboard file.
     * @param shots number of shots fired
     * @param result game result (e.g. "Vitória" or "Desistência")
     */
    public static void saveScore(int shots, String result) {
        List<Score> scores = loadScores();
        String date = LocalDateTime.now().format(FORMATTER);
        scores.add(new Score(date, shots, result));

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(new File(SCORES_FILE), scores);
            System.out.println("Score guardado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao guardar score: " + e.getMessage());
        }
    }

    /**
     * Loads all scores from the scoreboard file.
     * @return list of scores
     */
    public static List<Score> loadScores() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(SCORES_FILE);
        if (!file.exists()) return new ArrayList<>();
        try {
            return mapper.readValue(file, new TypeReference<List<Score>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Prints all scores to the console.
     */
    public static void printScoreboard() {
        List<Score> scores = loadScores();
        if (scores.isEmpty()) {
            System.out.println("Ainda não existem jogos registados.");
            return;
        }
        System.out.println("\n========== SCOREBOARD ==========");
        for (int i = 0; i < scores.size(); i++) {
            System.out.println((i + 1) + ". " + scores.get(i));
        }
        System.out.println("================================\n");
    }
}

