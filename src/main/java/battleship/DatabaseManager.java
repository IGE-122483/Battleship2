package battleship;

import java.sql.*;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:battleship_games.db";

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void createTable() throws SQLException {
        Connection conn = getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS jogadas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "linha TEXT," +
                "coluna TEXT," +
                "resultado TEXT," +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
        conn.createStatement().execute(sql);
        conn.close();
    }

    public static void guardarJogada(JogadaData jogada) throws SQLException {
        Connection conn = getConnection();
        String sql = "INSERT INTO jogadas (linha, coluna, resultado) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, jogada.linha);
        pstmt.setString(2, jogada.coluna);
        pstmt.setString(3, jogada.resultado);
        pstmt.executeUpdate();
        conn.close();
    }

}

