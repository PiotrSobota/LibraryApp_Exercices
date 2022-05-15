package pl.library.model;

import java.util.Comparator;
import java.util.Map;

public interface BookComparators {

    public static Comparator<Map.Entry<Book, Integer>> compareBooksByTitle()
    {
        return Comparator.comparing(book -> book.getKey().getTitle());
    }

    public static Comparator<Map.Entry<Book, Integer>> compareBooksByIsbn()
    {
        return Comparator.comparing(book -> book.getKey().getIsbn());
    }

    public static Comparator<Map.Entry<Book, Integer>> compareBooksByAuthor()
    {
        return Comparator.comparing(book -> book.getKey().getAuthor());
    }

    public static Comparator<Map.Entry<Book, Integer>> compareBooksByDateFromLowest()
    {
        return Comparator.comparing(book -> book.getKey().getReleaseDate());
    }

    public static Comparator<Map.Entry<Book, Integer>> compareBooksByDateFromHighest()
    {
        Comparator<Map.Entry<Book, Integer>> comparator = compareBooksByDateFromLowest();
        return comparator.reversed();
    }


}
