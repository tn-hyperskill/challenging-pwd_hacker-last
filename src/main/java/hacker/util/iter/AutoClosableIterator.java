package hacker.util.iter;

import java.util.Iterator;

public interface AutoClosableIterator<E> extends Iterator<E>, AutoCloseable {

}
