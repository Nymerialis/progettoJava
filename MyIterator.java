import java.util.ArrayList;
import java.util.Iterator;

public class MyIterator<E> implements Iterator<E> {
    private Iterator<E> myIterator;

    public MyIterator(ArrayList<E> a) {
        ArrayList<E> b = new ArrayList<>(a);
        myIterator = b.iterator();
    }

    @Override
    public boolean hasNext() {
        return myIterator.hasNext();
    }

    @Override
    public E next() {
        return myIterator.next();
    }

    @Override
    public void remove() { }
}