package edu.jhu.teamundecided.clueless.gameBoard;

import java.util.ArrayList;

public class Room
{
    private String _roomName;
    private ArrayList<Room> _adjacentRooms;

    public Room(String name)
    {
        this._roomName = name;
    }

    public String getRoomName()
    {
        return _roomName;
    }

    public ArrayList<Room> getAdjcentRooms()
    {
        return _adjacentRooms;
    }
}
