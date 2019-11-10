package edu.jhu.teamundecided.clueless.player;

import edu.jhu.teamundecided.clueless.deck.Card;
import edu.jhu.teamundecided.clueless.deck.Suggestion;
import edu.jhu.teamundecided.clueless.gameBoard.Room;
import edu.jhu.teamundecided.clueless.serverApp.ServerWorker;

import java.io.IOException;
import java.util.ArrayList;

public class Player
{

   private final int _playerNum;
   private static int numPlayers = 0; // class variable, not instance variable
   private String _characterName = "";
   private Room _currentLocation;
   private String _userName;

   private Hand _playerHand;


   public ServerWorker get_serverWorker()
   {

      return _serverWorker;
   }


   private ServerWorker _serverWorker;
   private boolean _isActive;
   private ArrayList<Card> _hand;
   private String[] _recentReply = null;

   public Player(ServerWorker serverWorker)
   {

      _userName = serverWorker.getUserName();
      _serverWorker = serverWorker;
      _isActive = true;
      _playerNum = ++numPlayers; // set this player number to current player count
      _playerHand = new Hand();

      switch (_playerNum)
      {
         case 1:
            _characterName = "Ms._Scarlett";
            _currentLocation = new Room("S01");
            break;
         case 2:
            _characterName = "Col._Mustard";
            _currentLocation = new Room("S02");
            break;
         case 3:
            _characterName = "Mrs._White";
            _currentLocation = new Room("S03");
            break;
         case 4:
            _characterName = "Mrs._Peacock";
            _currentLocation = new Room("S04");
            break;
         case 5:
            _characterName = "Prof._Plum";
            _currentLocation = new Room("S05");
            break;
         case 6:
            _characterName = "Mr._Green";
            _currentLocation = new Room("S06");
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
      return _characterName + " being played by " + _userName;
   }


   public String getMoveCommand() throws IOException
   {

      StringBuilder msg = new StringBuilder();

      msg.append("move");

      ArrayList<Room> possibleMove = _currentLocation.getAdjcentRooms();

      if (possibleMove.size() == 0)
      {
         return null;
      }

      for (Room room : possibleMove)
      {
         msg.append(" ").append(room.getRoomName());
      }

      msg.append("\n");

      _serverWorker.sendForReply(msg.toString(), this);

      return null;

   }


   public Suggestion getSuggestionCommand(ArrayList<Card> deck) throws IOException
   {

      _serverWorker.send("It is time to make a suggestion...\n");

      getSuggest(deck, Card.CardType.Suspect);
      // getSuggest(deck, Card.CardType.Weapon);

      return new Suggestion("", "", _currentLocation.getRoomName());
   }


   private void getSuggest(ArrayList<Card> deck, Card.CardType cardType) throws IOException
   {

      StringBuilder msg = new StringBuilder();

      msg.append(cardType);
      msg.append(" ");

      for (Card card : deck)
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


   public Suggestion getAccusationCommand(ArrayList<Card> deck) throws IOException, InterruptedException
   {
      return new Suggestion("", "", this._currentLocation.getRoomName());
   }


   private void askIfWantAccuse() throws IOException
   {

      _serverWorker.sendForReply("askAccuse Would you like to make an accusation?\n", this);
   }


   public void disproveSuggestion(ArrayList<Card> matchingCards) throws IOException
   {

      StringBuilder msg = new StringBuilder();

      msg.append("disprove");

      for (Card card : matchingCards)
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

   public Room getLocation()
   {

      return _currentLocation;

   }

   public String getCharacterName()
   {
      return _characterName;
   }

   public boolean getStatus(){
      return _isActive;
   }

   public void setStatus(boolean status){
      _isActive = status;
   }

   public Hand getPlayerHand()
   {
      return this._playerHand;
   }

   public void setPlayerHand(Hand hand)
   {
      this._playerHand = hand;
   }

}