import java.io.IOException;
import java.util.ArrayList;

public class Player
{

   private final int _playerNum;
   public static int numPlayers = 0; // class variable, not instance variable
   private String _characterName = "";
   private Room _currentLocation;
   private String _userName;


   public ServerWorker get_serverWorker()
   {

      return _serverWorker;
   }


   private ServerWorker _serverWorker;
   private boolean _isActive;
   private ArrayList<GameCard> _hand;
   private String[] _recentReply = null;


   public ArrayList<GameCard> getPlayerHand()
   {

      return _hand;
   }


   Player(ServerWorker serverWorker)
   {

      _userName = serverWorker.getUserName();
      _serverWorker = serverWorker;
      _isActive = true;
      _playerNum = ++numPlayers; // set this player number to current player count
      _hand = new ArrayList<GameCard>();

      switch (_playerNum)
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

      System.out.println("Player successfully created for " + _userName);
   }


   public String getUserID()
   {

      return _userName;
   }


   public String toString()
   {

      String out = _characterName + " being played by " + _userName;
      return out;
   }


<<<<<<< HEAD
   public boolean executeTurn(ArrayList<GameCard> deck) throws InterruptedException
   {

      try
      {
         getMoveCommand();
         // getSuggestionCommand(deck);
         // getAccusationCommand(deck);
      } catch (IOException e)
      {
         e.printStackTrace();
      }

      return true;
   }


   private String getMoveCommand() throws IOException, InterruptedException
   {

      StringBuilder msg = new StringBuilder();

      msg.append("move");

      ArrayList<Room> possibleMove = _currentLocation.getPossibleMoves();

      if (possibleMove.size() == 0)
      {
         return null;
      }

      for (Room room : possibleMove)
      {
         msg.append(" " + room.get_roomName());
      }

      msg.append("\n");

      _serverWorker.sendForReply(msg.toString(), this);

      return null;

   }


   private Suggestion getSuggestionCommand(ArrayList<GameCard> deck) throws IOException
   {

      _serverWorker.send("It is time to make a suggestion...\n");

      getSuggest(deck, GameCard.CardType.Suspect);
      // getSuggest(deck, GameCard.CardType.Weapon);

      return new Suggestion("", "", _currentLocation.get_roomName());
   }


   private void getSuggest(ArrayList<GameCard> deck, GameCard.CardType cardType) throws IOException
   {

      StringBuilder msg = new StringBuilder();

      msg.append(cardType);
      msg.append(" ");

      for (GameCard card : deck)
      {
         if (card.getType() == cardType)
         {
            msg.append(card.getCardName());
            msg.append(" ");
         }
      }

      msg.append("\n");

      _serverWorker.sendForReply(msg.toString(), this);
   }


   private Suggestion getAccusationCommand(ArrayList<GameCard> deck) throws IOException, InterruptedException
   {

      return new Suggestion("", "", this._currentLocation.get_roomName());

=======
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
>>>>>>> origin/master
   }


   private void askIfWantAccuse() throws IOException
   {

      _serverWorker.sendForReply("askAccuse Would you like to make an accusation?\n", this);
   }


   public void disproveSuggestion(ArrayList<GameCard> matchingCards) throws IOException
   {

      StringBuilder msg = new StringBuilder();

      msg.append("disprove");

      for (GameCard card : matchingCards)
      {
         msg.append(card.getCardName());
         msg.append(" ");
      }

      msg.append("\n");

      _serverWorker.sendForReply(msg.toString(), this);
   }


   public void receiveReplyFromServerWorker(String[] tokens)
   {

      _recentReply = tokens;
      System.out.println("Received Message : " + tokens[0]);
   }

<<<<<<< HEAD

   public Room getLocation()
   {

      return _currentLocation;
=======
   public String getLocation(){
      return location;
>>>>>>> origin/master
   }


   public String getCharacterName()
   {

      return _characterName;
   }

<<<<<<< HEAD
=======
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
>>>>>>> origin/master

}