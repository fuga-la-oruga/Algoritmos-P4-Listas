package dev.reveles.model;

public class DoubleNode<T>
{
    private T data;
    private Node<T> prev;
    private Node<T> next;

    // ------------------------------

    public DoubleNode(T data)
    {
        this.data = data;
        prev = null;
        next = null;
    }

    // ------------------------------

    public void setData(T newData) {data = newData;}
    public void setPrev(Node<T> newPrev) {prev = newPrev;}
    public void setNext(Node<T> newNext) {next = newNext;}

    public T getData() {return data;}
    public Node<T> getPrev() {return prev;}
    public Node<T> getNext() {return next;}
}