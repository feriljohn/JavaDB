package MainMethods;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import DataLayer.Book;
import Entity.Author;
import Entity.Genre;
import EntityConstructors.AuthorCrud;
import EntityConstructors.BookCrud;
import EntityConstructors.GenreCrud;
import Querys.Query;

public class AppMain {

    public static void main(String[] args) throws SQLException {

        Book bookHandler = new Book();
        Author authorHandler = new Author();
        Genre genreHandler = new Genre();

        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
            System.out.println("Connected to the database");
            System.out.println("Choose an option:");
            System.out.println("1: Books");
            System.out.println("2: Authors");
            System.out.println("3: Genres");
            System.out.println("4: Exit");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    handleBookOptions(bookHandler, scanner);
                    break;

                case 2:
                    handleAuthorOptions(authorHandler, scanner);
                    break;

                case 3:
                    handleGenreOptions(genreHandler, scanner);
                    break;

                case 4:
                    System.out.println("Exiting the application.");
                    ;

                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
                    break;
            }

        } while (choice != 4);
        scanner.close();

    }

    private static void handleBookOptions(Book bookHandler, Scanner scanner) throws SQLException {
        int bookChoice;
        do {
            System.out.println("Choose an option for Books:");
            System.out.println("1: All Books");
            System.out.println("2: Insert Book");
            System.out.println("3: Delete Book");
            System.out.println("4: Update Book");
            System.out.println("5: Back to Main Menu");

            bookChoice = scanner.nextInt();
            scanner.nextLine();

            switch (bookChoice) {
                case 1:
                    List<BookCrud> allBooks = bookHandler.getAllBooks();
                    System.out.printf("%-10s%-30s%-20s%-15s%-15s\n", "Book ID", "Book Name", "Date of Publish",
                            "Author ID", "Genre ID");
                    System.out.println("--------------------------------------------------------------------------");
                    for (BookCrud b : allBooks) {
                        System.out.printf("%-10s%-30s%-20s%-15s%-15s\n",
                                b.getBookId(), b.getBookName(), b.getDop(), b.getAuthId(), b.getGenId());
                    }
                    break;

                case 2:
                    System.out.println("Enter Book Name:");
                    String newBookName = scanner.nextLine();
                    System.out.println("Enter Date of Publication (YYYY-MM-DD):");
                    String dopString = scanner.nextLine();
                    Date newDop = Date.valueOf(dopString);
                    System.out.println("Enter Author ID:");
                    int newAuthId = scanner.nextInt();
                    System.out.println("Enter Genre ID:");
                    int newGenId = scanner.nextInt();
                    System.out.println("Enter new damage:");
                    String damage1 = scanner.nextLine();
                    String repStatus1 = "";

                    if (bookHandler.addBook(newBookName, newDop, newAuthId, newGenId,damage1,repStatus1)) {
                        System.out.println("Book added successfully.");
                    } else {
                        System.out.println("Failed to add the book.");
                    }
                    break;

                case 3:
                    System.out.println("Enter Book ID to delete:");
                    int bookIdToDelete = scanner.nextInt();
                    boolean deleted = bookHandler.deleteBook(bookIdToDelete);
                    if (deleted) {
                        System.out.println("Book deleted successfully.");
                    } else {
                        System.out.println("Failed to delete the book.");
                    }
                    break;

                case 4:
                    System.out.println("Enter Book ID to update:");
                    int bookIdToUpdate = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    System.out.println("Enter new Book Name:");
                    String updatedBookName = scanner.nextLine();
                    System.out.println("Enter new Date of Publication (YYYY-MM-DD):");
                    String updatedDopString = scanner.nextLine();
                    Date updatedDop = Date.valueOf(updatedDopString);
                    System.out.println("Enter new Author ID:");
                    int updatedAuthId = scanner.nextInt();
                    System.out.println("Enter new Genre ID:");
                    int updatedGenId = scanner.nextInt();
                    System.out.println("Enter new damage:");
                    String damage = scanner.nextLine();
                    String repStatus = "";

                    boolean updated = bookHandler.updateBook(bookIdToUpdate, updatedBookName, updatedDop, updatedAuthId,
                            updatedGenId,damage,repStatus);
                    if (updated) {
                        System.out.println("Book updated successfully.");
                    } else {
                        System.out.println("Failed to update the book.");
                    }
                    break;

                case 5:
                    System.out.println("Returning to the main menu.");
                    break;

                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
                    break;
            }

        } while (bookChoice != 5);
    }

    private static void handleAuthorOptions(Author authorHandler, Scanner scanner) {
        int authorChoice;
        do {

            System.out.println("Choose an option:");
            System.out.println("1: All Authors");
            System.out.println("2: Insert Author");
            System.out.println("3: Delete Author");
            System.out.println("4: Soft Delete Author");
            System.out.println("5: Update Author");
            System.out.println("6: Query");
            System.out.println("7: Back to Main Menu");

            authorChoice = scanner.nextInt();
            scanner.nextLine();

            switch (authorChoice) {
                case 1:
                    List<AuthorCrud> allAuthors = authorHandler.getAllAuthors();
                    System.out.printf("%-10s %-20s%n", "Author ID", "Author Name");
                    System.out.println("----------------------------");

                    for (AuthorCrud a : allAuthors) {
                        System.out.printf("%-10d %-20s%n", a.getAuthId(), a.getAuthName());
                    }
                    break;

                case 2:
                    System.out.println("Enter Author Name to insert:");
                    String newAuthorName = scanner.nextLine();
                    AuthorCrud newAuthor = new AuthorCrud(newAuthorName);

                    if (authorHandler.addAuthor(newAuthor)) {
                        System.out.println("Author inserted successfully.");
                    } else {
                        System.out.println("Failed to insert author.");
                    }
                    break;

                case 3:
                    System.out.println("Enter Author ID to delete:");
                    int authIdToDelete = scanner.nextInt();
                    boolean deleted = authorHandler.deleteAuthor(authIdToDelete);
                    if (deleted) {
                        System.out.println("Author deleted successfully.");
                    } else {
                        System.out.println("Failed to delete author.");
                    }
                    break;

                case 4:
                    System.out.println("Enter Author ID to delete:");
                    int authIdToSDelete = scanner.nextInt();
                    boolean deletedS = authorHandler.softDeleteAuthor(authIdToSDelete);
                    if (deletedS) {
                        System.out.println("Author deleted successfully.");
                    } else {
                        System.out.println("Failed to delete author.");
                    }
                    break;

                case 5:
                    System.out.println("Enter Author ID to update:");
                    int authIdToUpdate = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    System.out.println("Enter new Author Name:");
                    String updatedAuthorName = scanner.nextLine();
                    AuthorCrud updatedAuthor = new AuthorCrud(authIdToUpdate, updatedAuthorName);

                    boolean updated = authorHandler.updateAuthor(updatedAuthor);
                    if (updated) {
                        System.out.println("Author updated successfully.");
                    } else {
                        System.out.println("Failed to update author.");
                    }
                    break;

                case 6:
                    System.out.println(" Select Queryies:");
                    Query bookDetails = new Query();
                    bookDetails.displayBookDetails();
                    break;

                case 7:
                    System.out.println("Back to Main Menu");
                    break;

                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
                    break;

            }

        } while (authorChoice != 7);

    }

    private static void handleGenreOptions(Genre genreHandler, Scanner scanner) {

        List<GenreCrud> allGenres = genreHandler.getAllGenres();
        System.out.printf("%-10s %-20s%n", "Genre ID", "Genre Name");
        System.out.println("----------------------------");

        for (GenreCrud a : allGenres) {
            System.out.printf("%-10d %-20s%n", a.getGenId(), a.getGenName());
        }

    }

}