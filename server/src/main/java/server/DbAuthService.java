package server;

import java.sql.*;

public class DbAuthService implements AuthService {

    private static Connection conn;
    private final static String queryLogin = "select nick from users where upper(login)=upper(?) and passwd=?";
    private final static String queryRegister = "insert into users (login, nick, passwd) values (?, ?, ?)";

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        String nick = null;
        try {
            connect();
            PreparedStatement ps = conn.prepareStatement(queryLogin);
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nick = rs.getString("nick");
            }
            rs.close();
            ps.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        } finally {
            disconnect();
        }
        return nick;
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        int rows = 0;
        try {
            connect();
            PreparedStatement ps = conn.prepareStatement(queryRegister);
            ps.setString(1, login);
            ps.setString(2, nickname);
            ps.setString(3, password);
            rows = ps.executeUpdate();
            ps.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        } finally {
            disconnect();
        }
        return rows == 1;
    }

    private static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:geekchat.db");
    }

    private static void disconnect() {
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
