package Querys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DbConnection.DbManager;

public class Query {


    public void displayBookDetails() {
        try {
            Connection con = DbManager.getConnection();
            String sqlQuery = "SELECT book_name, auth_name, gen_name " +
                    "FROM book " +
                    "JOIN genres ON genres.gen_id = book.gen_id " +
                    "JOIN authors ON authors.auth_id = book.auth_id";

            try (PreparedStatement statement = con.prepareStatement(sqlQuery);
                    ResultSet resultSet = statement.executeQuery()) {

                System.out.printf("%-20s %-20s %-20s%n", "Book Name", "Author Name", "Genre Name");
                System.out.println("------------------------------------------------------------");

                while (resultSet.next()) {
                    String bookName = resultSet.getString("book_name");
                    String authorName = resultSet.getString("auth_name");
                    String genreName = resultSet.getString("gen_name");

                    System.out.printf("%-20s %-20s %-20s%n", bookName, authorName, genreName);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
