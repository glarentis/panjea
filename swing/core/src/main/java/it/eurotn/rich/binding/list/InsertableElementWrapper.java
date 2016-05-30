package it.eurotn.rich.binding.list;

/**
 * @author leonardo
 */
public interface InsertableElementWrapper<T> {

    /**
     * @return the element to get
     */
    T getElement();

    /**
     * @param element
     *            the element to set
     */
    void setElement(T element);
}