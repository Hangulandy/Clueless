package edu.jhu.teamundecided.clueless.player;

import edu.jhu.teamundecided.clueless.deck.Card;
import edu.jhu.teamundecided.clueless.deck.Deck;
import edu.jhu.teamundecided.clueless.deck.Suggestion;
import edu.jhu.teamundecided.clueless.gameBoard.Room;
import edu.jhu.teamundecided.clueless.serverApp.ServerWorker;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Player
{

   private final int _playerNum;
   private static int numPlayers = 0; // class variable, not instance variable
   private String _characterName = "";
   private Room _currentLocation;
   private String _userName;
   private ServerWorker _serverWorker;
   private boolean _isActive;
   private Hand _playerHand;


   public Player()
   {

      _isActive = true;
      _playerNum = ++numPlayers; // set this player number to current player count
      _playerHand = new Hand();

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
   }


   public Player(ServerWorker serverWorker)
   {

      this();
      _userName = serverWorker.getUserName();
      _serverWorker = serverWorker;

      System.out.println("Player successfully created for " + _userName);
   }


   /*
   Gets the possible rooms to move, prompts the user to choose a room to move based on these, awaits the response,
   and returns the response as a String
   */
   public String getMoveCommand() throws IOException, InterruptedException
   {

      ArrayList<Room> possibleMoves = _currentLocation.getPossibleMoves();

      if (possibleMoves.size() == 0)
      {
         return null;
      }

      sendMovePrompt(possibleMoves);
      String[] response = getResponse();

      System.out.println(response[1]);

      return response[1];

   }


   /*
   Prompts the user to choose a room to move based on the parameter possibleMoves
    */
   private void sendMovePrompt(ArrayList<Room> possibleMoves) throws IOException
   {

      StringBuilder msg = new StringBuilder();

      msg.append("move");

      for (Room room : possibleMoves)
      {
         msg.append(" ");
         msg.append(room.getRoomName());
      }

      _serverWorker.send(msg.toString());

      System.out.println("Awaiting reply from user...");
   }


   /*
   Uses the serverWorker BufferedReader object to get the response from the client here, tokenize it, and return the
   tokenized string (String[])
    */
   private String[] getResponse() throws IOException
   {

      BufferedReader reader = _serverWorker.getReader();

      String response = reader.readLine();

      System.out.println("Response: " + response);

      String[] output = response.split(" ");

      return output;
   }


   /*
   Prompts the user to suggest a suspect, awaits response, suggest a weapon, awaits response, and then finally
   creates and returns a Suggestion object with the cardNames for those two and the current room
    */
   public Suggestion getSuggestionCommand(Deck deck) throws IOException
   {

      _serverWorker.send("msg system It is time to make a suggestion...");

      sendSuggestPrompt(deck, Card.CardType.Suspect);
      String suspect = getResponse()[1];

      sendSuggestPrompt(deck, Card.CardType.Weapon);
      String weapon = getResponse()[1];

      return new Suggestion(suspect, weapon, _currentLocation.getRoomName());
   }


   /*
   Sends the prompt command to get user suggestion based on the card type being asked.
    */
   private void sendSuggestPrompt(Deck deck, Card.CardType cardType) throws IOException
   {

      StringBuilder msg = new StringBuilder();

      msg.append(cardType);

      for (Card card : deck.getCards())
      {
         if (card.getType() == cardType)
         {
            msg.append(" ");
            msg.append(card.getCardName());
         }
      }

      _serverWorker.send(msg.toString());
   }


   /*
   Prompts the user to choose whether or not he wants to make an accusation and receives response. If "yes", get
   suggestion for suspect, weapon, and room and await responses (in turn). Then create and return a Suggestion object
    with the cardNames for those three chosen cards.
    */
   public Suggestion getAccusationCommand(Deck deck) throws IOException, InterruptedException
   {

      askIfWantAccuse();
      String response = getResponse()[1];

      if (response.equalsIgnoreCase("no"))
      {
         return null;
      }

      _serverWorker.sendTextMessage("It is time to make an accusation...");

      sendSuggestPrompt(deck, Card.CardType.Suspect);
      String suspect = getResponse()[1];

      sendSuggestPrompt(deck, Card.CardType.Weapon);
      String weapon = getResponse()[1];

      sendSuggestPrompt(deck, Card.CardType.Room);
      String room = getResponse()[1];

      return new Suggestion(suspect, weapon, room);

   }


   /*
   Prompts the user to choose whether or not to make an accusation
    */
   private void askIfWantAccuse() throws IOException
   {

      _serverWorker.send("askAccuse Would you like to make an accusation?");
   }


   /*
   Prompts the user to choose one of the cards that matches the suggestion (disprovingCards), receives the user
   response, then passes the cardName to the GameController
    */
   public String disproveSuggestion(ArrayList<Card> disprovingCards) throws IOException
   {

      _serverWorker.sendTextMessage("You have a card that can disprove the suggestion.");

      StringBuilder msg = new StringBuilder();

      msg.append("refutation");

      for (Card card : disprovingCards)
      {
         msg.append(" ");
         msg.append(card.getCardName());
      }

      _serverWorker.send(msg.toString());

      String response = getResponse()[1];

      return response;
   }


   public void sendHandToClient() throws IOException
   {

      StringBuilder msg = new StringBuilder();

      msg.append("sendHand");

      for (Card card : _playerHand.getCards())
      {
         msg.append(" ");
         msg.append(card.getCardName());
         msg.append(" ");
         msg.append(card.getType());
      }

      _serverWorker.send(msg.toString());

   }


   /*
   These are self-explanatory
    */


   public Room getLocation()
   {

      return _currentLocation;

   }


   public void setLocation(Room room)
   {

      this._currentLocation = room;
   }


   public String getCharacterName()
   {

      return _characterName;
   }


   public boolean getStatus()
   {

      return _isActive;
   }


   public void setStatus(boolean status)
   {

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


   public ServerWorker getServerWorker()
   {

      return _serverWorker;
   }


   public String getUserName()
   {

      return _userName;
   }


   public String toString()
   {

      return _characterName + " being played by " + _userName;
   }


}