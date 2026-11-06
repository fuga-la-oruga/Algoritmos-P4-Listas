package dev.reveles.model;

public class SimpleList<T>
{
    private Node<T> start;
    private int size;

    public SimpleList()
    {
        start = null;
        size = 0;
    }

    public void addFirst(T data)
    {
        Node<T> node = new Node<T>(data);
        node.setNext(start);
        start = node;
        size ++;
    }

    public void addLast(T data)
    {
        Node<T> node = new Node<T>(data);

        if(start == null)
        {
            start = node;
        }
        else
        {
            Node<T> current = start;

            while(current.getNext() != null)
            {
                current = current.getNext();
            }

            current.setNext(node);
        }

        size++;
    }

    public T removeFirst()
    {
        if(start == null)
        {
            return null;
        }

        T data = start.getData();
        start = start.getNext();
        size --;

        return data;
    }

    public T removeLast()
    {
        if(start == null)
        {
            return null;
        }

        if(start.getNext() == null)
        {
            return removeFirst();
        }

        Node<T> current = start;

        while(current.getNext().getNext() != null)
        {
            current = current.getNext();
        }

        T data = current.getNext().getData();
        current.setNext(null);
        size--;

        return data;
    }

    public T getFirst()
    {
        return start == null ? null : start.getData();
    }

    public T getLast()
    {
        if(start == null)
        {
            return null;
        }

        Node<T> current = start;

        while(current.getNext() != null)
        {
            current = current.getNext();
        }

        return current.getData();
    }

    public T get(int index)
    {
        if(index < 0 || index >= size)
        {
            return null;
        }

        Node<T> current = start;

        for(int i = 0; i < index; i ++)
        {
            current = current.getNext();
        }

        return current.getData();
    }

    public boolean isEmpty()
    {
        return start == null;
    }

    public int size()
    {
        return size;
    }
}