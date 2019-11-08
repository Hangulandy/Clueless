import javax.smartcardio.Card;

public class Player
{
   private final int playerNum;
   public static int numPlayers = 0; // class variable, not instance variable
   private String _userName;
   private String _characterName;
   private boolean _isActive = true;
   private GameController _gc;

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
         this.move();
         this.suggest();
         this.accuse();
      } else
      {
         // TODO return to GameController
      }
      return true; // TODO logic of accusation success
   }


   private void move()
   {
      // TODO
   }


   private void suggest()
   {
      // TODO
   }


   private void accuse()
   {
      // TODO prompt messageClient to make accusation or pass (Andy)

      // TODO prompt messageClient for suspect (Andy)

      // TODO prompt messageClient for weapon (Andy)

      // TODO prompt messageClient for room (Andy)

      // TODO show messageClient the cards (Andy)

      String suspect = "";
      String weapon = "";
      String room = "";

      if (_gc._deckController.checkAccusation(suspect, weapon, room))
      {
         // TODO win
      } else
      {
         this._isActive = false;
      }
   }

   private String promptForSuspect(){
      // TODO Andy implement with messageClient
      return "";
   }


}