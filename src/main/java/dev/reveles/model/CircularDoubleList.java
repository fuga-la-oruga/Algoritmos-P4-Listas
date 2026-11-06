package dev.reveles.model;

public class CircularDoubleList<T>
{
    private DoubleNode<T> start;
    private DoubleNode<T> end;
    private int size;

    // ------------------------------

    public CircularDoubleList()
    {
        start = null;
        end = null;
        size = 0;
    }

    // ------------------------------

    public void addFirst(T data)
    {
        DoubleNode<T> node = new DoubleNode<T>(data);

        if(start == null)
        {
            start = node;
            end = node;
            node.setNext(node);
            node.setPrev(node);
        }
        else
        {
            node.setNext(start);
            node.setPrev(end);
            start.setPrev(node);
            end.setNext(node);
            start = node;
        }

        size++;
    }

    public void addLast(T data)
    {
        DoubleNode<T> node = new DoubleNode<T>(data);

        if(start == null)
        {
            addFirst(data);
            return;
        }

        node.setPrev(end);
        node.setNext(start);
        end.setNext(node);
        start.setPrev(node);
        end = node;
        size++;
    }

    public T removeFirst()
    {
        if(start == null)
        {
            return null;
        }

        T data = start.getData();

        if(start == end)
        {
            start = null;
            end = null;
        }
        else
        {
            start = start.getNext();
            start.setPrev(end);
            end.setNext(start);
        }

        size--;
        return data;
    }

    public T removeLast()
    {
        if(end == null)
        {
            return null;
        }

        T data = end.getData();

        if(start == end)
        {
            start = null;
            end = null;
        }
        else
        {
            end = end.getPrev();
            end.setNext(start);
            start.setPrev(end);
        }

        size--;
        return data;
    }

    public T getFirst()
    {
        return start == null ? null : start.getData();
    }

    public T getLast()
    {
        return end == null ? null : end.getData();
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