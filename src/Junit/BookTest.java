package Junit;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import DataLayer.Book;
import EntityConstructors.BookCrud;

public class BookTest {

    private Book bookDataLayer;

    @Before
    public void setUp() {
        bookDataLayer = new Book();
    }

    @Test
    public void testGetAllBooks() throws SQLException {
        List<BookCrud> books = bookDataLayer.getAllBooks();
        assertNotNull(books);
        assertTrue(books.size() > 0);
    }

    @Test
    public void testGetBookById() throws SQLException {
        int bookId = 1;
        List<BookCrud> books = bookDataLayer.getBookById(bookId);
        assertNotNull(books);
        assertEquals(1, books.size());
    }

    @Test
    public void testAddBook() throws SQLException {
        String bookName = "Test Book";
        Date dop = Date.valueOf("2023-05-01");
        int authId = 1;
        int genId = 1;
        String damage="no damage";
        String repStatus="";
        

        assertTrue(bookDataLayer.addBook(bookName, dop, authId, genId,damage,repStatus));

       int addedBookId = getAddedBookId(bookName);
        if (addedBookId > 0) {
            bookDataLayer.deleteBook(addedBookId);
        }
    }

    @Test
    public void testUpdateBook() throws SQLException {
        int bookId = 42;
        String updatedBookName = "Updated Test Book";
        Date updatedDop = Date.valueOf("2022-06-01");
        int updatedAuthId = 1;
        int updatedGenId = 1;
        String damage="torn pages";
        String repStatus="";

        assertTrue(bookDataLayer.updateBook(bookId, updatedBookName, updatedDop, updatedAuthId, updatedGenId,damage,repStatus));
    }

    @Test
    public void testDeleteBook() throws SQLException {
        int bookId = 41;
        assertTrue(bookDataLayer.deleteBook(bookId));
    }


    private int getAddedBookId(String bookName) throws SQLException {
        List<BookCrud> books = bookDataLayer.getAllBooks();
        for (BookCrud book : books) {
            if (book.getBookName().equals(bookName)) {
                return book.getBookId();
            }
        }
        return -1;
    }
}
