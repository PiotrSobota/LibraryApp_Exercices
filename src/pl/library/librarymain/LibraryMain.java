package pl.library.librarymain;

import pl.library.logic.LibraryLogic;
import static pl.library.logic.Printer.*;

public class LibraryMain {

    public static void main(String[] args) {

        print("Witaj w programie biblioteka");
        LibraryLogic library = new LibraryLogic();
        library.start();

    }



}
