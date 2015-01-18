package com.wernerapps.tanks.players;


public class Pair<T1, T2>
{

    private T1 first;
    private T2 second;

    public Pair(T1 first, T2 second)
    {
        this.first = first;
        this.second = second;
    }

    public T1 getKey()
    {
        return first;
    }

    public T2 getValue()
    {
        return second;
    }

}
