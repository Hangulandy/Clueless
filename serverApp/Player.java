public class Player
{
    private String _userID;
    private String _characterName;

    Player(String user, String character)
    {
        _userID = user;
        _characterName = character;
    }

    public String getPlayerName()
    {
        return _characterName;
    }

    public void setPlayerName(String character)
    {
        this._characterName = character;
    }

    public String getUserID()
    {
        return _userID;
    }

    public String toString()
    {
        String out = getPlayerName() + " being played by " + userID();
        return out;
    }

}
