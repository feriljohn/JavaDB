package DataLayer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import DbConnection.DbManager;
import EntityConstructors.BookCrud;

public class Book {

    public List<BookCrud> getAllBooks() throws SQLException {
        List<BookCrud> books = new ArrayList<>();
        try (Connection con = DbManager.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM book")) {

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String bookName = rs.getString("book_name");
                Date dop = rs.getDate("dop");
                int authId = rs.getInt("auth_id");
                int genId = rs.getInt("gen_id");

                BookCrud book = new BookCrud(bookId, bookName, dop, authId, genId);
                books.add(book);
            }
        }
        return books;
    }

    public List<BookCrud> getBookById(int bookId) throws SQLException {
        List<BookCrud> books = new ArrayList<>();
        try (Connection con = DbManager.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM book WHERE book_id = ?")) {

            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int retrievedBookId = rs.getInt("book_id");
                    String bookName = rs.getString("book_name");
                    Date dop = rs.getDate("dop");
                    int authId = rs.getInt("auth_id");
                    int genId = rs.getInt("gen_id");
                    String damage = rs.getString("damage");
                    String repStatus = rs.getString("repair_status");

                    BookCrud book = new BookCrud(retrievedBookId, bookName, dop, authId, genId, damage, repStatus);
                    books.add(book);
                }
            }
        }
        return books;
    }

    public boolean addBook(String bookName, Date newDop, int authId, int genId, String damage, String repairStatus)
            throws SQLException {
        try (Connection con = DbManager.getConnection()) {
            if (bookName == null || bookName.isEmpty() || bookName.length() > 50 || newDop == null || authId <= 0
                    || genId <= 0 || !isDateUpToCurrent(newDop) || !isValidDamage(damage)) {
                System.out.println("Invalid input parameters for adding a book.");
                return false;
            }

            if (isBookNameExists(bookName)) {
                System.out.println("Book with the same name already exists.");
                return false;
            }

            String query = "INSERT INTO book(book_name, dop, auth_id, gen_id, damage, repair_status) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, bookName);
                stmt.setDate(2, newDop);
                stmt.setInt(3, authId);
                stmt.setInt(4, genId);
                stmt.setString(5, damage);
                stmt.setString(6, repairStatus);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Book added successfully.");
                    return true;
                } else {
                    System.out.println("Failed to add the book.");
                    return false;
                }
            }
        }
    }

    public boolean updateBook(int bookId, String bookName, Date dop, int authId, int genId, String damage,
            String repairStatus) throws SQLException {
        try (Connection con = DbManager.getConnection()) {
            if (bookName == null || bookName.isEmpty() || bookName.length() > 50 || dop == null || authId <= 0
                    || genId <= 0 || !isDateUpToCurrent(dop) || !isValidDamage(damage)) {
                System.out.println("Invalid input parameters for updating a book.");
                return false;
            }

            if (isBookNameExists(bookName, bookId)) {
                System.out.println("Book with the same name already exists.");
                return false;
            }

            String query = "UPDATE book SET book_name = ?, dop=?, auth_id = ?, gen_id = ?,damage=?, repair_status=? WHERE book_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, bookName);
                stmt.setDate(2, dop);
                stmt.setInt(3, authId);
                stmt.setInt(4, genId);
                stmt.setString(5, damage);
                stmt.setString(6, repairStatus);
                stmt.setInt(7, bookId);

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Book updated successfully.");
                    return true;
                } else {
                    System.out.println("Failed to update the book.");
                    return false;
                }
            }
        }
    }

    public boolean deleteBook(int bookId) throws SQLException {
        try (Connection con = DbManager.getConnection();
                PreparedStatement stmt = con.prepareStatement("DELETE FROM book WHERE book_id = ?")) {

            stmt.setInt(1, bookId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Deleted Successfully");
                return true;
            } else {
                System.out.println("Failed to Delete the book.");
                return false;
            }
        }
    }

    private boolean isBookNameExists(String bookName) throws SQLException {
        try (Connection con = DbManager.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM book WHERE book_name = ?")) {

            stmt.setString(1, bookName);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    private boolean isBookNameExists(String bookName, int bookId) throws SQLException {
        try (Connection con = DbManager.getConnection();
                PreparedStatement stmt = con
                        .prepareStatement("SELECT COUNT(*) FROM book WHERE book_name = ? AND book_id <> ?")) {

            stmt.setString(1, bookName);
            stmt.setInt(2, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    private boolean isDateUpToCurrent(Date date) {
        Date currentDate = new Date(System.currentTimeMillis());
        return !date.after(currentDate);
    }

    public boolean isValidDamage(String damage) {
        String[] validDamages = { "no damage", "torn pages", "damaged cover", "stains on pages", "water damage",
                "missing pages", "missing cover" };

        for (String validDamage : validDamages) {
            if (validDamage.equalsIgnoreCase(damage)) {
                return true;
            }
        }

        return false;
    }

    private int budget = 40;

    public List<BookCrud> manageRepair() throws SQLException {
        List<BookCrud> books = getRepairBook();
        List<BookCrud> addBook = new ArrayList<>();

        books.sort(Comparator.comparingInt(BookCrud::getRepairCost));
        int cumulativeRepairCost = 0;

        for (BookCrud book : books) {
            if (!book.getRepairStatus().equalsIgnoreCase("completed")) {
                int repairCost = getRepairCost(book.getDamageName());
                cumulativeRepairCost += repairCost;
                if (cumulativeRepairCost <= budget) {

                    book.setRepairStatus("completed");
                    addBook.add(book);
                } else {
                    book.setRepairStatus("pending");
                    addBook.add(book);
                }

            }
        }
        return books;
    }

    public List<BookCrud> getRepairBook() throws SQLException {
        List<BookCrud> books = new ArrayList<>();

        try (Connection con = DbManager.getConnection()) {
            String query = "SELECT * FROM book WHERE damage != ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, "no damage");

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int bookId = rs.getInt("book_id");
                        String bookName = rs.getString("book_name");
                        Date dop = rs.getDate("dop");
                        int authId = rs.getInt("auth_id");
                        int genId = rs.getInt("gen_id");
                        String damage = rs.getString("damage");
                        String repairStatus = rs.getString("repair_status");

                        BookCrud book = new BookCrud(bookId, bookName, dop, authId, genId, damage, repairStatus);
                        books.add(book);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public int getRepairCost(String damage) throws SQLException {
        int repairCost = 0;
        try (Connection con = DbManager.getConnection()) {
            String query = "SELECT repair_cost FROM repair WHERE damage = ?";

            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, damage);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        repairCost = resultSet.getInt("repair_cost");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return repairCost;
    }

    public void updateDataStatus(int bookId, String repairStatus) throws SQLException {
        try (Connection con = DbManager.getConnection()) {
            String query = "UPDATE book SET repair_status = ? WHERE book_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, repairStatus);
                stmt.setInt(2, bookId);
                stmt.executeUpdate();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(List<BookCrud> books) throws SQLException {
        for (BookCrud book : books) {
            updateDataStatus(book.getBookId(), book.getRepairStatus());
        }
    }

}
