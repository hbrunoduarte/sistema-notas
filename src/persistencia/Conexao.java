package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    static private Connection connection = null;

    static public Connection getConnection() throws SQLException, ClassNotFoundException {
        if(connection == null) {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/SistemaNotas";
            String username = "postgres";
            String password = "********";
            connection = DriverManager.getConnection(url, username, password);
        }

        return connection;
    }
}
