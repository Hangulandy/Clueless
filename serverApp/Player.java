public class Player
{
   private final int playerNum;
   public static int numPlayers = 0; // class variable, not instance variable
   private String _userName;
   private String _characterName;

   Player(String userName)
   {
      _userName = userName;
      playerNum = ++numPlayers; // set this player number to current player count

      switch (playerNum)
      {
         case 1:
            _characterName = "Ms. Scarlett";
            break;
         case 2:
            _characterName = "Col. Mustard";
            break;
         case 3:
            _characterName = "Mrs. White";
            break;
         case 4:
            _characterName = "Mrs. Peacock";
            break;
         case 5:
            _characterName = "Prof. Plum";
            break;
         case 6:
            _characterName = "Mr. Green";
            break;
      }

      System.out.println("Player successfully created for " + userName);
   }

   public String getCharacterName()
   {
      return _characterName;
   }

   public void setPlayerName(String character)
   {
      this._characterName = character;
   }

   public String getUserID()
   {
      return _userName;
   }

   public String toString()
   {
      String out = getCharacterName() + " being played by " + _userName;
      return out;
   }

}