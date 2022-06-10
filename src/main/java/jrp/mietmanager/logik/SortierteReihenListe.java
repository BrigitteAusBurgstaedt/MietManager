package jrp.mietmanager.logik;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.UnaryOperator;

public class SortierteReihenListe<E extends Comparable<E>> extends ArrayList<E> {

    @Override
    public boolean add(E e) {
        this.add(0, e);
        return true;
    }

    @Override
    public void add(int ignoriert, E e) {
        int index = Collections.binarySearch(this, e);
        if (index < 0) index = ~index;
        super.add(index, e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) this.add(0, e);
        return true;
    }

    @Override
    public boolean addAll(int ignoriert, Collection<? extends E> c) {
        return this.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E set(int i, E e) {
        throw new UnsupportedOperationException();
    }

}
