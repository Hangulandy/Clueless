import javax.smartcardio.Card;

public class Player
{
   private final int playerNum;
   public static int numPlayers = 0; // class variable, not instance variable
   private String _userName;
   private String _characterName;
   private boolean _isActive = true;
   private GameController _gc;
   private String guessedSuspect;
   private String guessedWeapon;
   private String guessedRoom;

   Player(String userName, GameController gameController)
   {
      _userName = userName;
      playerNum = ++numPlayers; // set this player number to current player count

      switch (playerNum)
      {
         case 1:
            _characterName = "Ms._Scarlett";
            break;
         case 2:
            _characterName = "Col._Mustard";
            break;
         case 3:
            _characterName = "Mrs._White";
            break;
         case 4:
            _characterName = "Mrs._Peacock";
            break;
         case 5:
            _characterName = "Prof._Plum";
            break;
         case 6:
            _characterName = "Mr._Green";
            break;
      }

      this._gc = gameController;

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


   public boolean executeTurn()
   {

      if (_isActive)
      {
         //prompt user for move command and then execute command
         String moveCMD = getMoveCommand();
         if (moveCMD != null){
            this._gc.move(moveCMD);
         }

         //first check if the player is in a room and therefore if they are allowed to make a suggestion
         //prompt user for suggestion and then execute command
         if(_gc.canMakeSuggestion(this)){
            if (getSuggestionCommand()){
               _gc.suggest(guessedSuspect, guessedWeapon, _gc.getPlayerLocation(this));
            }
         }

         //prompt user for accusation and then execute command
         if (getAccusationCommand()){
            _isActive = this._gc.accuse(guessedSuspect, guessedWeapon, guessedRoom);
         }
      } else
      {
         return false;
      }
      return true;
   }

   private String getMoveCommand(){
      //TODO Andy ask if user wants to move and get Move selection from User
      return "";
   }

   private boolean getSuggestionCommand(){
      //TODO Andy ask if user wants to suggest
      //TODO If so, set player variables guessedSuspect and guessedWeapon
      //TODO return true if user wants to suggest, return false if user does not
      return false;
   }

   private boolean getAccusationCommand(){
      //TODO Andy ask if user wants to accuse. If so, set player variables guessedSuspect, guessedWeapon, and guessedRoom
      //return true if user wants to suggest, return false if user does not
      return false;
   }

   public String disproveSuggestion(String suggestedSuspect, String suggestedWeapon, String suggestedRoom){
      //TODO Andy ask if user can disprove
      //If yes, return string of card the user "shows" to disprove
      //If no, return null;
      return null;
   }

   public void receiveMessage(String msg){
      //TODO Andy take message and broadcast to client
   }

}