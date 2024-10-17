package hacker.util.iter;

import java.io.Closeable;
import java.util.Iterator;

public interface AutoclosableIterator<E> extends Iterator<E>, AutoCloseable {
}
