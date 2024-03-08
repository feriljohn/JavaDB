package Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DbConnection.DbManager;
import EntityConstructors.AuthorCrud;

public class Author {

    public List<AuthorCrud> getAllAuthors() {
        List<AuthorCrud> authors = new ArrayList<>();
        try {
            Connection con = DbManager.getConnection();
            Statement stmt = con.createStatement(); // used to execute SQL queries against a database
            ResultSet rs = stmt.executeQuery("SELECT * FROM authors");
            while (rs.next()) {
                int authId = rs.getInt("auth_id");
                String authorName = rs.getString("auth_name");
                AuthorCrud author = new AuthorCrud(authId, authorName);
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;

    }

    public boolean addAuthor(AuthorCrud author) {
        try {
            Connection con = DbManager.getConnection();
            if (author.getAuthName().length() > 50) {
                System.out.println("Author's name exceeds the maximum length of 50 characters.");
                return false;
            }
            if (isAuthorNameExists(author.getAuthName())) {
                System.out.println("Author with the same name already exists.");
                return false;
            }

            PreparedStatement stmt = con.prepareStatement("INSERT INTO authors(auth_name) VALUES (?)");
            stmt.setString(1, author.getAuthName());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isAuthorNameExists(String authName) {
        try {
            Connection con = DbManager.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM authors WHERE auth_name = ?");
            stmt.setString(1, authName);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean deleteAuthor(int authId) {
        try {
            Connection con = DbManager.getConnection();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM authors where auth_id=?");
            stmt.setInt(1, authId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean softDeleteAuthor(int authId) {
        try {
            Connection con = DbManager.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE authors SET Deletef = true WHERE auth_id = ?");
            stmt.setInt(1, authId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean updateAuthor(AuthorCrud author) {
        try {
            Connection con = DbManager.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE authors SET auth_name = ? WHERE auth_id = ?");
            stmt.setString(1, author.getAuthName());
            stmt.setInt(2, author.getAuthId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

}