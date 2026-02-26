package freemind.swing;


import javax.swing.*;
import java.util.*;

@SuppressWarnings("serial")
public class DefaultListModel<E> extends AbstractListModel<E> {
    private final ArrayList<E> delegate = new ArrayList<>();

    /**
     * Returns the number of components in this list.
     * <p>
     * This method is identical to <code>size</code>, which implements the
     * <code>List</code> interface defined in the 1.2 Collections framework.
     * This method exists in conjunction with <code>setSize</code> so that
     * <code>size</code> is identifiable as a JavaBean property.
     *
     * @return the number of components in this list
     * @see #size()
     */
    public int getSize() {
        return delegate.size();
    }

    /**
     * Returns the component at the specified index.
     * <blockquote>
     * <b>Note:</b> Although this method is not deprecated, the preferred
     * method to use is <code>get(int)</code>, which implements the
     * <code>List</code> interface defined in the 1.2 Collections framework.
     * </blockquote>
     *
     * @param index an index into this list
     * @return the component at the specified index
     * @throws ArrayIndexOutOfBoundsException if the <code>index</code>
     *                                        is negative or greater than the current size of this
     *                                        list
     * @see #get(int)
     */
    public E getElementAt(int index) {
        return delegate.get(index);
    }

    /**
     * Copies the components of this list into the specified array.
     * The array must be big enough to hold all the objects in this list,
     * else an <code>IndexOutOfBoundsException</code> is thrown.
     *
     * @param anArray the array into which the components get copied
     * @see ArrayList#toArray(Object[])
     */
    public void copyInto(Object[] anArray) {
        delegate.toArray(anArray);
    }

    /**
     * Trims the capacity of this list to be the list's current size.
     *
     * @see ArrayList#trimToSize()
     */
    public void trimToSize() {
        delegate.trimToSize();
    }

    /**
     * Increases the capacity of this list, if necessary, to ensure
     * that it can hold at least the number of components specified by
     * the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     * @see ArrayList#ensureCapacity(int)
     */
    public void ensureCapacity(int minCapacity) {
        delegate.ensureCapacity(minCapacity);
    }

    /**
     * Sets the size of this list.
     *
     * @param newSize the new size of this list
     */
    public void setSize(int newSize) {
        int oldSize = delegate.size();
        if (newSize < oldSize) {
            delegate.subList(newSize, oldSize).clear();
        } else {
            while (delegate.size() < newSize) delegate.add(null);
        }
        if (oldSize > newSize) {
            fireIntervalRemoved(this, newSize, oldSize - 1);
        } else if (oldSize < newSize) {
            fireIntervalAdded(this, oldSize, newSize - 1);
        }
    }

    /**
     * Returns the current capacity of this list.
     * <p>
     * Since ArrayList does not expose its internal capacity,
     * this method returns the current size as an approximation.
     *
     * @return the current size of the list
     */
    public int capacity() {
        return delegate.size();
    }

    /**
     * Returns the number of components in this list.
     *
     * @return the number of components in this list
     * @see ArrayList#size()
     */
    public int size() {
        return delegate.size();
    }

    /**
     * Tests whether this list has any components.
     *
     * @return <code>true</code> if and only if this list has
     * no components, that is, its size is zero;
     * <code>false</code> otherwise
     * @see ArrayList#isEmpty()
     */
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    /**
     * Returns an enumeration of the components of this list.
     *
     * @return an enumeration of the components of this list
     * @see Collections#enumeration(Collection)
     */
    public Enumeration<E> elements() {
        return Collections.enumeration(delegate);
    }

    /**
     * Tests whether the specified object is a component in this list.
     *
     * @param elem an object
     * @return <code>true</code> if the specified object
     * is the same as a component in this list
     * @see ArrayList#contains(Object)
     */
    public boolean contains(Object elem) {
        return delegate.contains(elem);
    }

    /**
     * Searches for the first occurrence of <code>elem</code>.
     *
     * @param elem an object
     * @return the index of the first occurrence of the argument in this
     * list; returns <code>-1</code> if the object is not found
     * @see ArrayList#indexOf(Object)
     */
    public int indexOf(Object elem) {
        return delegate.indexOf(elem);
    }

    /**
     * Searches for the first occurrence of <code>elem</code>, beginning
     * the search at <code>index</code>.
     *
     * @param elem  an desired component
     * @param index the index from which to begin searching
     * @return the index where the first occurrence of <code>elem</code>
     * is found after <code>index</code>; returns <code>-1</code>
     * if the <code>elem</code> is not found in the list
     */
    public int indexOf(Object elem, int index) {
        for (int i = index; i < delegate.size(); i++) {
            if (Objects.equals(elem, delegate.get(i))) return i;
        }
        return -1;
    }

    /**
     * Returns the index of the last occurrence of <code>elem</code>.
     *
     * @param elem the desired component
     * @return the index of the last occurrence of <code>elem</code>
     * in the list; returns <code>-1</code> if the object is not found
     * @see ArrayList#lastIndexOf(Object)
     */
    public int lastIndexOf(Object elem) {
        return delegate.lastIndexOf(elem);
    }

    /**
     * Searches backwards for <code>elem</code>, starting from the
     * specified index, and returns an index to it.
     *
     * @param elem  the desired component
     * @param index the index to start searching from
     * @return the index of the last occurrence of the <code>elem</code>
     * in this list at position less than <code>index</code>;
     * returns <code>-1</code> if the object is not found
     */
    public int lastIndexOf(Object elem, int index) {
        for (int i = index; i >= 0; i--) {
            if (Objects.equals(elem, delegate.get(i))) return i;
        }
        return -1;
    }

    /**
     * Returns the component at the specified index.
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index
     * is negative or not less than the size of the list.
     * <blockquote>
     * <b>Note:</b> Although this method is not deprecated, the preferred
     * method to use is <code>get(int)</code>, which implements the
     * <code>List</code> interface defined in the 1.2 Collections framework.
     * </blockquote>
     *
     * @param index an index into this list
     * @return the component at the specified index
     * @see #get(int)
     */
    public E elementAt(int index) {
        return delegate.get(index);
    }

    /**
     * Returns the first component of this list.
     * Throws a <code>NoSuchElementException</code> if this
     * list has no components.
     *
     * @return the first component of this list
     */
    public E firstElement() {
        if (delegate.isEmpty()) throw new NoSuchElementException();
        return delegate.get(0);
    }

    /**
     * Returns the last component of the list.
     * Throws a <code>NoSuchElementException</code> if this list
     * has no components.
     *
     * @return the last component of the list
     */
    public E lastElement() {
        if (delegate.isEmpty()) throw new NoSuchElementException();
        return delegate.get(delegate.size() - 1);
    }

    /**
     * Sets the component at the specified <code>index</code> of this
     * list to be the specified element. The previous component at that
     * position is discarded.
     * <p>
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index
     * is invalid.
     * <blockquote>
     * <b>Note:</b> Although this method is not deprecated, the preferred
     * method to use is <code>set(int,Object)</code>, which implements the
     * <code>List</code> interface defined in the 1.2 Collections framework.
     * </blockquote>
     *
     * @param element what the component is to be set to
     * @param index   the specified index
     * @see #set(int, Object)
     */
    public void setElementAt(E element, int index) {
        delegate.set(index, element);
        fireContentsChanged(this, index, index);
    }

    /**
     * Deletes the component at the specified index.
     * <p>
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index
     * is invalid.
     * <blockquote>
     * <b>Note:</b> Although this method is not deprecated, the preferred
     * method to use is <code>remove(int)</code>, which implements the
     * <code>List</code> interface defined in the 1.2 Collections framework.
     * </blockquote>
     *
     * @param index the index of the object to remove
     * @see #remove(int)
     */
    public void removeElementAt(int index) {
        delegate.remove(index);
        fireIntervalRemoved(this, index, index);
    }

    /**
     * Inserts the specified element as a component in this list at the
     * specified <code>index</code>.
     * <p>
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index
     * is invalid.
     * <blockquote>
     * <b>Note:</b> Although this method is not deprecated, the preferred
     * method to use is <code>add(int,Object)</code>, which implements the
     * <code>List</code> interface defined in the 1.2 Collections framework.
     * </blockquote>
     *
     * @param element the component to insert
     * @param index   where to insert the new component
     * @throws ArrayIndexOutOfBoundsException if the index was invalid
     * @see #add(int, Object)
     */
    public void insertElementAt(E element, int index) {
        delegate.add(index, element);
        fireIntervalAdded(this, index, index);
    }

    /**
     * Adds the specified component to the end of this list.
     *
     * @param element the component to be added
     */
    public void addElement(E element) {
        int index = delegate.size();
        delegate.add(element);
        fireIntervalAdded(this, index, index);
    }

    /**
     * Removes the first (lowest-indexed) occurrence of the argument
     * from this list.
     *
     * @param obj the component to be removed
     * @return <code>true</code> if the argument was a component of this
     * list; <code>false</code> otherwise
     */
    public boolean removeElement(Object obj) {
        int index = indexOf(obj);
        boolean rv = delegate.remove(obj);
        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
        }
        return rv;
    }


    /**
     * Removes all components from this list and sets its size to zero.
     * <blockquote>
     * <b>Note:</b> Although this method is not deprecated, the preferred
     * method to use is <code>clear</code>, which implements the
     * <code>List</code> interface defined in the 1.2 Collections framework.
     * </blockquote>
     *
     * @see #clear()
     */
    public void removeAllElements() {
        int index1 = delegate.size() - 1;
        delegate.clear();
        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1);
        }
    }


    /**
     * Returns a string that displays and identifies this
     * object's properties.
     *
     * @return a String representation of this object
     */
    public String toString() {
        return delegate.toString();
    }


    /* The remaining methods are included for compatibility with the
     * Java 2 platform Vector class.
     */

    /**
     * Returns an array containing all the elements in this list in the
     * correct order.
     *
     * @return an array containing the elements of the list
     */
    public Object[] toArray() {
        return delegate.toArray();
    }

    /**
     * Returns the element at the specified position in this list.
     * <p>
     * Throws an <code>ArrayIndexOutOfBoundsException</code>
     * if the index is out of range
     * (<code>index &lt; 0 || index &gt;= size()</code>).
     *
     * @param index index of element to return
     */
    public E get(int index) {
        return delegate.get(index);
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     * <p>
     * Throws an <code>ArrayIndexOutOfBoundsException</code>
     * if the index is out of range
     * (<code>index &lt; 0 || index &gt;= size()</code>).
     *
     * @param index   index of element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     */
    public E set(int index, E element) {
        E rv = delegate.get(index);
        delegate.set(index, element);
        fireContentsChanged(this, index, index);
        return rv;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * <p>
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the
     * index is out of range
     * (<code>index &lt; 0 || index &gt; size()</code>).
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    public void add(int index, E element) {
        delegate.add(index, element);
        fireIntervalAdded(this, index, index);
    }

    /**
     * Removes the element at the specified position in this list.
     * Returns the element that was removed from the list.
     * <p>
     * Throws an <code>ArrayIndexOutOfBoundsException</code>
     * if the index is out of range
     * (<code>index &lt; 0 || index &gt;= size()</code>).
     *
     * @param index the index of the element to removed
     * @return the element previously at the specified position
     */
    public E remove(int index) {
        E rv = delegate.get(index);
        delegate.remove(index);
        fireIntervalRemoved(this, index, index);
        return rv;
    }

    /**
     * Removes all the elements from this list.  The list will
     * be empty after this call returns (unless it throws an exception).
     */
    public void clear() {
        int index1 = delegate.size() - 1;
        delegate.clear();
        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1);
        }
    }

    /**
     * Deletes the components at the specified range of indexes.
     * The removal is inclusive, so specifying a range of (1,5)
     * removes the component at index 1 and the component at index 5,
     * as well as all components in between.
     * <p>
     * Throws an <code>ArrayIndexOutOfBoundsException</code>
     * if the index was invalid.
     * Throws an <code>IllegalArgumentException</code> if
     * <code>fromIndex &gt; toIndex</code>.
     *
     * @param fromIndex the index of the lower end of the range
     * @param toIndex   the index of the upper end of the range
     * @see #remove(int)
     */
    public void removeRange(int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex must be <= toIndex");
        }
        delegate.subList(fromIndex, toIndex + 1).clear();
        fireIntervalRemoved(this, fromIndex, toIndex);
    }

    public void addAll(Collection<E> c) {
        delegate.addAll(c);
    }

    public void addAll(int index, Collection<E> c) {
        delegate.addAll(index, c);
    }

    public List<E> unmodifiableList() {
        return Collections.unmodifiableList(delegate);
    }

}
