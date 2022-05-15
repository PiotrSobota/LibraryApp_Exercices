package pl.library.logic;

import pl.library.model.Book;
import pl.library.model.BookComparators;


import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

import static pl.library.logic.Printer.*;

public class LibraryLogic {

    //Zmienne
    private HashMap<Book, Integer> booksMap;

    public LibraryLogic() {
        this.booksMap = new HashMap<>();
    }

    private Function<Map.Entry<Book,Integer>,String> mappingFunction =
    book -> book.getKey().toString() + " - Ilość sztuk: " + book.getValue();

    //Metoda główna
    public void start() {

        //Tą metodę usunąć po stworzeniu zapisu w pliku CSV lub w sposób serializowany !!!
        initializeBooksMap();

        boolean exit = true;
        Scanner scanner = new Scanner(System.in);

        do {
            showOptionsToChoose();

            try {
                exit = chooseOption(scanner);
            } catch (InputMismatchException | IllegalArgumentException e) {
                print("\nWybrałeś nie istniejącą opcję lub nie wprowadziłeś cyfry całkowitej.");
                print("Spróbuj jeszcze raz.");
            }
        }
        while (exit);

        print("\nZakończyłeś pracę programu, do zobaczenia.");

        scanner.close();
    }

    //Metoda wyboru + wyświetlania opcji wyboru
    private boolean chooseOption(Scanner scanner) throws IllegalArgumentException {

        Options choose;
        int input = Integer.parseInt(scanner.nextLine());
        Optional<Options> option = Optional.ofNullable(Options.getOptionByNumber(input));

        if (option.isPresent()) {
            choose = option.get();

            switch (choose) {
                case CLOSE:
                    return false;
                case ADD_NEW_BOOK:
                    addNewBookOrIncreaseNumber(scanner);
                    break;
                case DELETE_BOOK_BY_ISBN:
                    deleteAllBooksByIsbn(scanner);
                    break;
                case DELETE_BY_DATE:
                    deleteAllBooksByDate(scanner);
                    break;
                case DELETE_BY_DATE_BETWEEN:
                    deleteAllBooksByDateBetween(scanner);
                    break;
                case SHOW_ALL_ALPHABETICALLY:
                    showAllSortedAlphabetically();
                    break;
                case SHOW_BOOKS_SORTED_BY_ISBN:
                    showAllSortedByIsbn();
                    break;
                case SHOW_BOOKS_SORTED_BY_AUTHOR:
                    showAllSortedByAuthor();
                    break;
                case SHOW_BOOKS_SORTED_BY_TITLE:
                    showAllSortedByTitle();
                    break;
                case SHOW_ELDEST:
                    showEldestBooks(scanner);
                    break;
                case SHOW_LATEST:
                    showLatestBooks(scanner);
                    break;
                case SHOW_ALL_BOOKS_WITH_TITLE:
                    showBooksByTitle(scanner);
                    break;
                case SHOW_ALL_BOOKS_WITH_AUTHOR:
                    showBooksByAuthor(scanner);
                    break;
            }

        }

        option.orElseThrow(IllegalArgumentException::new);
        return true;

    }

    private void showOptionsToChoose() {
        print("\nWybierz opcję z listy poniżej - podaj liczbę i potwierdź ENTER-em.");

        Options[] options = Options.values();

        for (Options option : options) {
            print(option.toString());
        }
    }

    //**//**//Opcje:

    //**//Dodawanie i usuwanie

    //Dodawanie ksiażek

    private void addNewBookOrIncreaseNumber(Scanner scanner) {
        Book.Builder bookBuilder = new Book.Builder();

        int newCountOfBooks;
        int countOfAddedBooks;
        int countOfExistingBooks;

        print("\nPodaj tytuł książki.");
        bookBuilder.withTitle(scanner.nextLine());

        print("\nPodaj imię i nazwisko autora i naciśnij enter.");
        bookBuilder.withAuthor(scanner.nextLine());

        print("\nPodaj numer ISBN.");
        bookBuilder.withIsbn(scanner.nextLine());

        print("\nPodaj datę wydania w następującej kolejności:");
        LocalDate date = createLocalDateFromInput(scanner);

        bookBuilder.withReleaseDate(date);

        print("\nPodaj ilość książek, które chcesz dodać do biblioteki.");
        countOfAddedBooks = Integer.parseInt(scanner.nextLine());

        Book newBook = bookBuilder.build();

        if (booksMap.containsKey(newBook)) {
            countOfExistingBooks = booksMap.get(newBook);
            newCountOfBooks = countOfExistingBooks + countOfAddedBooks;
            booksMap.put(newBook, newCountOfBooks);
        } else {
            booksMap.put(newBook, countOfAddedBooks);
        }

    }

    //Metody usuwające
    private void deleteAllBooksByDate(Scanner scanner) {

        print("\nPodaj datę wydania książek, które chcesz usunąć w następującej kolejności:");

        LocalDate date = createLocalDateFromInput(scanner);

        Map<Book, Integer> booksMapDuplicate = Map.copyOf(booksMap);

        booksMapDuplicate.entrySet().stream()
                .filter(book -> book.getKey().getReleaseDate().equals(date))
                .map(Map.Entry::getKey)
                .forEach(book -> booksMap.remove(book));
    }

    private void deleteAllBooksByIsbn(Scanner scanner) {

        print("\nPodaj numer ISBN książki, którą chcesz usunąć.");

        String isbnNumber = scanner.nextLine();

        Map<Book, Integer> booksMapDuplicate = Map.copyOf(booksMap);

        booksMapDuplicate.entrySet().stream()
                .filter(book -> book.getKey().getIsbn().equals(isbnNumber))
                .map(Map.Entry::getKey)
                .forEach(book -> booksMap.remove(book));
    }

    private void deleteAllBooksByDateBetween(Scanner scanner)
    {
        print("\nUwaga - poniższa funckja działa na zasadzie mniejsze/większe równe. Usuwa więc również ksiażki z podanych dat.");
        print("\nPodaj pierwszą (1) datę wydania książek, które chcesz usunąć w następującej kolejności:");

        LocalDate dateStart = createLocalDateFromInput(scanner);

        print("\nPodaj drugą (2) datę wydania książek, które chcesz usunąć w następującej kolejności:");

        LocalDate dateEnd = createLocalDateFromInput(scanner);

        if (dateStart.isAfter(dateEnd))
            throw new IllegalArgumentException();

        Map<Book, Integer> booksMapDuplicate = Map.copyOf(booksMap);

        booksMapDuplicate.entrySet().stream()
                .filter(book ->
                        {
                            LocalDate date = book.getKey().getReleaseDate();
                            return date.isAfter(dateStart) && date.isBefore(dateEnd)
                                    || date.isEqual(dateStart) || date.isEqual(dateEnd);
                        } )
                .map(Map.Entry::getKey)
                .forEach(book -> booksMap.remove(book));
    }

    //Metoda pomocnicza tworząca datę z inputów
    private LocalDate createLocalDateFromInput(Scanner scanner) {
        int year;
        int month;
        int day;

        print("\nPodaj rok wydania.");
        year = Integer.parseInt(scanner.nextLine());

        print("\nPodaj miesiąc wydania.");
        month = Integer.parseInt(scanner.nextLine());

        print("\nPodaj dzień (dzień miesiąca) wydania.");
        day = Integer.parseInt(scanner.nextLine());

        return LocalDate.of(year, month, day);
    }

    //**//**//Wyświetlanie

    //Metody wyświetlające książki w kolejności
    private void showAllSortedAlphabetically() {
        int i = 0;
        booksMap.entrySet().stream()
                .sorted(BookComparators
                        .compareBooksByTitle()
                        .thenComparing(BookComparators.compareBooksByAuthor())
                        .thenComparing(BookComparators.compareBooksByIsbn()))
                .map(mappingFunction)
                .forEach(Printer::print);
    }

    private void showAllSortedByAttribute(Comparator<Map.Entry<Book, Integer>> comparator) {
        int i = 0;
        booksMap.entrySet().stream()
                .sorted(comparator)
                .map(mappingFunction)
                .forEach(Printer::print);
    }

    private void showAllSortedByIsbn() {
        showAllSortedByAttribute(BookComparators.compareBooksByIsbn());
    }

    private void showAllSortedByAuthor() {
        showAllSortedByAttribute(BookComparators.compareBooksByAuthor());
    }

    private void showAllSortedByTitle() {
        showAllSortedByAttribute(BookComparators.compareBooksByTitle());
    }

    //Pokaż najnowsze, pokaż najstarsze

    private void showEldestBooks(Scanner scanner)
    {
        String comment = "Najstarsze książki to:";
        showBookByDateAndLimited(scanner,comment, BookComparators.compareBooksByDateFromHighest());
    }

    private void showLatestBooks(Scanner scanner)
    {
        String comment = "Najnowsze książki to:";
        showBookByDateAndLimited(scanner,comment, BookComparators.compareBooksByDateFromLowest());
    }

    private void showBookByDateAndLimited(Scanner scanner, String comment, Comparator<Map.Entry<Book, Integer>> comparator)
    {
        print("Podaj ilość (limit) książek do jakiej chcesz zawęzić wyświetlanie.");
        int limit = Integer.parseInt(scanner.nextLine());

        print(comment);

        booksMap.entrySet().stream()
                .sorted(comparator)
                .map(mappingFunction)
                .limit(limit)
                .forEach(Printer::print);
    }

    //Pokaż książki po...

    private void showBooksByTitle(Scanner scanner)
    {
        print("Podaj tytuł, dla którego chcesz wypisać książk i zatwierdź spacją: ");
        String author = scanner.nextLine();

        booksMap.entrySet().stream()
                .filter(book -> book.getKey().getTitle().equals(author))
                .map(mappingFunction)
                .forEach(Printer::print);
    }

    private void showBooksByAuthor(Scanner scanner)
    {
        print("Podaj autora, dla którego chcesz wypisać książki i zatwierdź spacją: ");
        String author = scanner.nextLine();

        booksMap.entrySet().stream()
                .filter(book -> book.getKey().getAuthor().equals(author))
                .map(mappingFunction)
                .forEach(Printer::print);
    }


    //**//**//Metody pomocnicze

    //Inicjalizacja danych - do usunięcia po wprowadzeniu zapisu
    private void initializeBooksMap()
    {
        Book b2 = new Book("Pan Tadeusz", "Adam Mickiewicz", "54321", LocalDate.now().minusYears(120));
        Book b3 = new Book("Balladyna", "Juliusz Słowacki", "54311", LocalDate.now().minusYears(130));
        Book b4 = new Book("W Pustyni i w Puszczy", "Henryk Sienkiewicz", "433222", LocalDate.now().minusYears(140));
        Book b5 = new Book("Dziady", "Adam Mickiewicz", "5434321", LocalDate.now().minusYears(114));
        Book b6 = new Book("Lokomotywa", "Jan Brzechwa", "54341111", LocalDate.now().minusYears(143));

        booksMap.put(b2, 2);
        booksMap.put(b3, 1);
        booksMap.put(b4, 5);
        booksMap.put(b5, 8);
        booksMap.put(b6, 1);

    }

}
