package dev.reveles.model;

public class Node<T>
{
    private T data;
    private Node<T> next;

    // ------------------------------

    public Node(T data)
    {
        this.data = data;
        next = null;
    }

    // ------------------------------

    public void setData(T newData) {data = newData;}
    public void setNext(Node<T> newNext) {next = newNext;}

    public T getData() {return data;}
    public Node<T> getNext() {return next;}
}