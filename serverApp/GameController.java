//The Game Flow subsystem will act as the messenger between the user interface and the other subsystems, the abstraction of the whole system. Tracks which Player has a turn, queries other subsystems as to the viability of the user's inputs. Offers options to user to Move, Guess, Deny, Accuse, etc.

public class GameController
{
    //Turn queue
    private ArrayList<Player> _players;
    private int _turn;

    GameController()
    {
        // create the ordered player name list - used to track turns
        _Players = new ArrayList<>(Arrays.asList("Miss Scarlett", "Colonel Mustard", "Mrs. White", "Mr. Green", "Mrs. Peacock", "Professor Plum"));

        //For each
        //Create player

        turn = 0;
    }

    public void ExecuteTurn()
    {
        turn = turn + 1;
    }

    public string GetTurn()
    {
        Player current = _Players.index(turn);
        return current.getPlayerName;
    }

    public void Move(String player, String position)
    {
    //GameBoard.Move(player, position);
    }

    public boolean Suggest(String suspect, String room, String weapon)
    {
    //Deck.checkSuggestion(suspect, room, weapon);
    }

    public boolean Accuse(String suspect, String room, String weapon)
    {
        //Deck.checkAccusation(suspect, room, weapon);
    }       
}

