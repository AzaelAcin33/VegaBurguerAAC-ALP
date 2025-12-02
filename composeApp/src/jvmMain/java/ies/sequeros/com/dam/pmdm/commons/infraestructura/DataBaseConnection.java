package ies.sequeros.com.dam.pmdm.commons.infraestructura;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseConnection {
    private String config_path;
    private Connection conexion;

    // Variables internas
    private String url;
    private String user;
    private String password;

    public DataBaseConnection() {
        try {
            // 1. IMPORTANTE: Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void open() throws Exception {
        Properties props = new Properties();

        // 2. CAMBIO CLAVE: Leer desde 'resources' usando getResourceAsStream
        // Esto busca el archivo dentro del JAR/compilado, no en la ruta del usuario.
        InputStream input = getClass().getResourceAsStream(this.config_path);

        // Si no lo encuentra, probamos añadiendo una barra al principio por si acaso
        if (input == null && !this.config_path.startsWith("/")) {
            input = getClass().getResourceAsStream("/" + this.config_path);
        }

        if (input != null) {
            props.load(input);

            // 3. Leer las variables limpias
            this.url = props.getProperty("database.path");
            this.user = props.getProperty("database.user");
            this.password = props.getProperty("database.password");

            // 4. CONEXIÓN MYSQL: Se pasan 3 argumentos separados
            // NO se usa la sintaxis de Derby (url + ";user=" + ...)
            this.conexion = DriverManager.getConnection(this.url, this.user, this.password);

            System.out.println("✅ Conectado a MySQL correctamente: " + this.url);
        } else {
            throw new Exception("❌ No se encontró el archivo: " + this.config_path + " en la carpeta resources.");
        }
    }

    public Connection getConnection() {
        return this.conexion;
    }

    public void close() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
        // 5. IMPORTANTE: Eliminada la línea de "shutdown=true".
        // A MySQL no se le manda apagar desde la aplicación.
    }

    public String getConfig_path() {
        return config_path;
    }

    public void setConfig_path(String config_path) {
        this.config_path = config_path;
    }
}