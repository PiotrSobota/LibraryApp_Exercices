package pl.library.logic;

public enum Options {

    CLOSE(0, "Zakończ pracę programu"),
    ADD_NEW_BOOK(1, "Dodaj nową książkę"),
    DELETE_BOOK_BY_ISBN(2, "Usuń podając ISBN"),
    DELETE_BY_DATE(3, "Usuń podając datę wydania"),
    DELETE_BY_DATE_BETWEEN(4, "Usuń książki o dacie wydania pomiędzy..."),
    SHOW_ALL_ALPHABETICALLY(5, "Pokaż wszystkie alfabetycznie"),
    SHOW_BOOKS_SORTED_BY_ISBN(6, "Pokaż wszystkie posortowane po ISBN"),
    SHOW_BOOKS_SORTED_BY_AUTHOR(7, "Pokaż wszystkie posortowane według autora"),
    SHOW_BOOKS_SORTED_BY_TITLE(8, "Pokaż wszystkie posortowane po tytule"),
    SHOW_ELDEST(9, "Pokaż najnowsze książki"),
    SHOW_LATEST(10, "Pokaż najstarsze"),
    SHOW_ALL_BOOKS_WITH_TITLE(11, "Pokaże wszystkie książki o tytule"),
    SHOW_ALL_BOOKS_WITH_AUTHOR(12, "Pokaż wszystkie książki o autorze");

    private int numberOfOption;
    private String description;

    Options(int numberOfOption, String description) {
        this.numberOfOption = numberOfOption;
        this.description = description;
    }

    @Override
    public String toString() {
        return numberOfOption + " - " +description;
    }

    public static Options getOptionByNumber(int number)
    {
        for (Options value : Options.values()) {
            if (value.numberOfOption == number)
                return value;
        }
        return null;
    }
}
