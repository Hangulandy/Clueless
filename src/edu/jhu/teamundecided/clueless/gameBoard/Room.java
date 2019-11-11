// package edu.jhu.teamundecided.clueless.gameBoard;

import java.util.ArrayList;

public class Room
{
    private String _roomName;
    private ArrayList<Room> _adjacentRooms;
    private String _roomType;

    public Room(String name)
    {
        this._roomName = name;
    }

    public String getRoomName()
    {
        return _roomName;
    }

    public ArrayList<Room> getAdjacentRooms()
    {
        return _adjacentRooms;
    }

    public void setAdjacentRooms(ArrayList<Room> adjacentRooms)
    {
        this._adjacentRooms = adjacentRooms;
    }

}
