package battleship;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Represents a game score entry in the scoreboard.
 * @author O_Teu_Nome
 */
public class Score {

    private String date;
    private int shots;
    private String result;

    public Score() {}

    public Score(String date, int shots, String result) {
        this.date = date;
        this.shots = shots;
        this.result = result;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getShots() { return shots; }
    public void setShots(int shots) { this.shots = shots; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    @Override
    public String toString() {
        return "Data: " + date + " | Jogadas: " + shots + " | Resultado: " + result;
    }
}

