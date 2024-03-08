package BusinessLayer;

import DataLayer.Book;
import EntityConstructors.BookCrud;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class BookService {

    private final Book bookData;

    public BookService() {
        this.bookData = new Book();
    }

    public List<BookCrud> getAllBooks() throws SQLException {
        return bookData.getAllBooks();
    }

    public List<BookCrud> getBookById(int bookId) throws SQLException {
        return bookData.getBookById(bookId);
    }

    public boolean addBook(String bookName, Date dop, int authId, int genId, String damage, String repairStatus)
            throws SQLException {
        return bookData.addBook(bookName, dop, authId, genId, damage, repairStatus);
    }

    public boolean updateBook(int bookId, String bookName, Date dop, int authId, int genId, String damage,
            String repairStatus) throws SQLException {
        return bookData.updateBook(bookId, bookName, dop, authId, genId, damage, repairStatus);
    }

    public boolean deleteBook(int bookId) throws SQLException {
        return bookData.deleteBook(bookId);
    }

    public List<BookCrud> manageRepair() throws SQLException {
        return bookData.manageRepair();
    }

    public void updateStatus(List<BookCrud> books) throws SQLException {
        bookData.updateStatus(books);
    }
}
