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
   private String location;
   //TODO private Hand playerHand;

   Player(String userName, GameController gameController)
   {
      _userName = userName;
      playerNum = ++numPlayers; // set this player number to current player count

      switch (playerNum)
      {
         case 1:
            _characterName = "Miss_Scarlett";
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


   public String getMoveCommand(){
      //TODO Andy ask if user wants to move and get Move selection from User
      return "";
   }

   public boolean getSuggestionCommand(){
      //TODO implement suggestion object (consists of three cards)
      //TODO Andy ask if user wants to suggest
      //TODO If so, set player variables guessedSuspect and guessedWeapon
      //TODO return suggestion object
      return false;
   }

   public String getAccusationCommand(){
      //TODO Andy ask if user wants to accuse. If so, set player variables guessedSuspect, guessedWeapon, and guessedRoom
      //should return a suggestion
      //return true if user wants to suggest, return false if user does not
      return null;
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

   public String getLocation(){
      return location;
   }

   public boolean setLocation(String loc){
      location = loc;
      return true;
   }

   public boolean getStatus(){
      if(_isActive){
         return true;
      }
      else{
         return false;
      }
   }

   public void setStatus(boolean status){
      _isActive = status;
   }

   //TODO getHand(){}
   //TODO setHand(){}

}