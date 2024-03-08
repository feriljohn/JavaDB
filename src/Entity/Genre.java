package Entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DbConnection.DbManager;
import EntityConstructors.GenreCrud;

public class Genre {

    public List<GenreCrud> getAllGenres() {
        List<GenreCrud> genres = new ArrayList<>();
        try {
            Connection con = DbManager.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM genres");
            while (rs.next()) {
                int genId = rs.getInt("gen_id");
                String genName = rs.getString("gen_name");
                GenreCrud genre = new GenreCrud(genId, genName);
                genres.add(genre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }
}
