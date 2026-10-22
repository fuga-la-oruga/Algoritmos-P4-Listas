package dev.reveles.model;

public class CircularDoubleList<T>
{
    private DoubleNode<T> start;
    private DoubleNode<T> end;

    // ------------------------------

    public CircularDoubleList()
    {
        start = null;
        end = null;
    }

    // ------------------------------

    public void addStart(T data)
    {
        DoubleNode<T> node = new DoubleNode<T>(data);
        if(start == null)
        {
            start = node;
            end = node;
            node.setNext(node);
            node.setPrev(node);
            return;
        }
        node.setNext(start);
        node.setPrev(end);
        start.setPrev(node);
        end.setNext(node);
        start = node;
    }

    public void addEnd(T data)
    {
        DoubleNode<T> node = new DoubleNode<T>(data);
        if(start == null)
        {
            start = node;
            end = node;
            node.setNext(node);
            node.setPrev(node);
            return;
        }
        node.setPrev(end);
        node.setNext(start);
        end.setNext(node);
        start.setPrev(node);
        end = node;
    }

    public T removeStart()
    {
        if(start == null)
        {
            return null;
        }
        T toReturn = start.getData();
        if(start == end)
        {
            start = null;
            end = null;
            return toReturn;
        }
        start = start.getNext();
        start.setPrev(end);
        end.setNext(start);
        return toReturn;
    }

    public T removeEnd()
    {
        if(end == null)
        {
            return null;
        }
        T toReturn = end.getData();
        if(start == end)
        {
            start = null;
            end = null;
            return toReturn;
        }
        end = end.getPrev();
        end.setNext(start);
        start.setPrev(end);
        return toReturn;
    }
}