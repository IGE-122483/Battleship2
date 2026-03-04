package battleship;

import java.sql.*;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:battleship_games.db";

    public static void createTable() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        String sql = "CREATE TABLE IF NOT EXISTS jogadas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "linha TEXT," +
                "coluna TEXT," +
                "resultado TEXT," +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
        conn.createStatement().execute(sql);
        conn.close();
    }

    public static void guardarJogada(String linha, String coluna, String resultado) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        String sql = "INSERT INTO jogadas (linha, coluna, resultado) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, linha);
        pstmt.setString(2, coluna);
        pstmt.setString(3, resultado);
        pstmt.executeUpdate();
        conn.close();
    }

    public static void listarJogadas() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM jogadas");
        while (rs.next()) {
            System.out.println("Jogada #" + rs.getInt("id") +
                    " | Linha: " + rs.getString("linha") +
                    " | Coluna: " + rs.getString("coluna") +
                    " | Resultado: " + rs.getString("resultado") +
                    " | " + rs.getString("timestamp"));
        }
        conn.close();
    }
}

