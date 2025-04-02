package javacon;
import java.sql.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class Javacon {

    private static final String USUARIO = "root";
    private static final String PASSWORD = "";
    private static final String URL = "jdbc:mysql://localhost:3306/prueba";
    private static Connection conexion;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer conexión con la base de datos
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);

            // Menú de opciones
            int opcion;
            do {
                System.out.println("\n--- Menú de Opciones ---");
                System.out.println("1. Insertar Usuario");
                System.out.println("2. Actualizar Usuario");
                System.out.println("3. Eliminar Usuario");
                System.out.println("4. Consultar Usuarios");
                System.out.println("5. Salir");
                System.out.print("Seleccione una opción: ");
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
                
                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese username para el nuevo usuario: ");
                        String username = scanner.nextLine();
                        System.out.print("Ingrese password: ");
                        String password = scanner.nextLine();
                        insertarUsuario(username, password);
                        break;
                    case 2:
                        System.out.print("Ingrese ID del usuario a actualizar: ");
                        int idActualizar = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Nuevo username existente: ");
                        String nuevoUsername = scanner.nextLine();
                        System.out.print("Nuevo password: ");
                        String nuevoPassword = scanner.nextLine();
                        actualizarUsuario(idActualizar, nuevoUsername, nuevoPassword);
                        break;
                    case 3:
                        System.out.print("Ingrese ID del usuario a eliminar: ");
                        int idEliminar = scanner.nextInt();
                        eliminarUsuario(idEliminar);
                        break;
                    case 4:
                        consultarUsuarios();
                        break;
                    case 5:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción inválida. Intente de nuevo.");
                }
            } while (opcion != 5);
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Javacon.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

    // Método para insertar usuario
    public static void insertarUsuario(String username, String password) {
        String sql = "INSERT INTO usuarios (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Usuario insertado correctamente. Filas afectadas: " + filasAfectadas);
        } catch (SQLException ex) {
            Logger.getLogger(Javacon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para actualizar usuario
    public static void actualizarUsuario(int id, String nuevoUsername, String nuevoPassword) {
        String sql = "UPDATE usuarios SET username = ?, password = ? WHERE userid = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, nuevoUsername);
            pstmt.setString(2, nuevoPassword);
            pstmt.setInt(3, id);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Usuario actualizado correctamente.");
            } else {
                System.out.println("No se encontró el usuario con ID: " + id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Javacon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para eliminar usuario
    public static void eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE userid = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Usuario eliminado correctamente.");
            } else {
                System.out.println("No se encontró el usuario con ID: " + id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Javacon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para consultar usuarios
    public static void consultarUsuarios() {
        String sql = "SELECT * FROM usuarios";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n--- Lista de Usuarios ---");
            while (rs.next()) {
                System.out.println(rs.getInt("userid") + " :: " + rs.getString("username"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Javacon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para cerrar conexión
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Javacon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
