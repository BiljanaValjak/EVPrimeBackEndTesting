package database;

import model.request.PostPutEventsRequests;
import util.Configuration;

import java.sql.*;

public class DbClient {

    Connection conn;

    private void connect() throws SQLException {
        conn = DriverManager.getConnection(Configuration.DB_URL, Configuration.DB_USER, Configuration.DB_PASSWORD);
    }

    public PostPutEventsRequests getEventFromDB(String id) throws SQLException {
        connect();

        String sql = "SELECT * FROM events WHERE id = '" + id + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        rs.next();

        conn.close();

        return PostPutEventsRequests.builder()
                .title(rs.getString("title"))
                .image(rs.getString("image"))
                .date(rs.getString("date"))
                .location(rs.getString("location"))
                .description(rs.getString("description"))
                .build();

    }

    public boolean isEventDeletedFromDb(String id) throws SQLException {
        connect();

        String sql = "DELETE FROM events WHERE id = '" + id +"'";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        return pstmt.executeUpdate() == 1;
    }

}
