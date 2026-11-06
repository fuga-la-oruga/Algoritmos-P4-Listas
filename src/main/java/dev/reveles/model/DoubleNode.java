package dev.reveles.model;

public class DoubleNode<T>
{
    private T data;
    private DoubleNode<T> prev;
    private DoubleNode<T> next;

    public DoubleNode(T data)
    {
        this.data = data;
        prev = null;
        next = null;
    }

    public T getData() {return data;}
    public DoubleNode<T> getPrev() {return prev;}
    public DoubleNode<T> getNext() {return next;}

    public void setData(T data) {this.data = data;}
    public void setPrev(DoubleNode<T> prev) {this.prev = prev;}
    public void setNext(DoubleNode<T> next) {this.next = next;}
}