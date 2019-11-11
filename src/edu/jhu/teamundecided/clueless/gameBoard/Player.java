import java.util.*;

public class Player{
	public String name;
	public Room location;


	public Player(String name, Room room){
		this.name = name;
		this.location = room;
	}

	public Room getLocation(){
		return this.location;
	}

	public void setLocation(Room room){
		this.location = room;
	}

	public String getName(){
		return this.name;
	}
}
