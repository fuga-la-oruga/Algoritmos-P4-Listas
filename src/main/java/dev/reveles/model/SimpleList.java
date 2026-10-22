package dev.reveles.model;

public class SimpleList<T>
{
    private Node<T> start;

    // ------------------------------

    public SimpleList()
    {
        start = null;
    }

    // ------------------------------

    public void addStart(T data)
    {
        Node<T> node = new Node<T>(data);
        if(start != null)
        {
            node.setNext(start);
        }
        start = node;
    }

    public void addEnd(T data)
    {
        Node<T> node = new Node<T>(data);
        if(start == null)
        {
            start = node;
            return;
        }
        Node<T> i = start;
        while(i.getNext() != null)
        {
            i = i.getNext();
        }
        i.setNext(node);
    }

    public T removeStart()
    {
        if(start == null)
        {
            return null;
        }
        T toReturn = start.getData();
        if(start.getNext() == null)
        {
            start = null;
            return toReturn;
        }
        start = start.getNext();
        return toReturn;
    }

    public T removeEnd()
    {
        if(start == null)
        {
            return null;
        }
        if(start.getNext() == null)
        {
            T toReturn = start.getData();
            start = null;
            return toReturn;
        }
        Node<T> i = start;
        while(i.getNext().getNext() != null)
        {
            i = i.getNext();
        }
        T toReturn = i.getNext().getData();
        i.setNext(null);
        return toReturn;
    }
}