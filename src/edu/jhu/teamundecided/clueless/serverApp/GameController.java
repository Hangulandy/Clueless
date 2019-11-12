/*
The Game Flow subsystem will act as the messenger between the user interface and the other subsystems, the
abstraction of the whole system. Tracks which Player has a _turn, queries other subsystems as to the viability
of the user's inputs. Offers options to user to Move, Guess, Deny, Accuse, etc.

Class written by Kira Ullman, edited by Andrew Johnson
*/

package edu.jhu.teamundecided.clueless.serverApp;

import edu.jhu.teamundecided.clueless.deck.Card;
import edu.jhu.teamundecided.clueless.deck.DeckController;
import edu.jhu.teamundecided.clueless.deck.Suggestion;
import edu.jhu.teamundecided.clueless.gameBoard.GameBoard;
import edu.jhu.teamundecided.clueless.player.Player;

import java.io.IOException;
import java.util.ArrayList;

public class GameController
{

   private Server _server;
   private ArrayList<Player> _players; // TODO make into circular linked list
   private boolean _gameStarted;
   private GameBoard _gb;
   DeckController _deckController;
   private int numberOfPlayers;
   private int _turn;
   private boolean _gameOver;


   public GameController(Server server)
   {

      _server = server;
      _players = new ArrayList<Player>();
      _gb = new GameBoard();
      _deckController = new DeckController();
      _turn = 0;
      _gameStarted = false;
   }


   public void startGame() throws IOException, InterruptedException
   {

      if (!_gameStarted)
      {

         ArrayList<ServerWorker> list = _server.getWorkerList();

         for (ServerWorker worker : list)
         {
            addPlayer(worker);
         }

         _gameStarted = true;
      } else
      {
         _server.broadcastTextMessage("Game has already been started...");
      }
      _deckController.dealCards(_players);
      numberOfPlayers = _players.size();
      _turn = getFirstTurn();
      runGame();
   }


   public void addPlayer(ServerWorker worker)
   {

      Player player = new Player(worker);
      System.out.println(player.toString());
      _players.add(player);
      _server.broadcastTextMessage(player.toString());
   }


   public ArrayList<Player> getPlayers()
   {

      return _players;
   }


   public void runGame() throws IOException, InterruptedException
   {

      Player currentPlayer;

      placePlayers();

      while (!_gameOver)
      {
         currentPlayer = _players.get(_turn);
         executeTurn(currentPlayer);
         _turn++;
         _turn = _turn % numberOfPlayers;
      }
   }


   private void placePlayers()
   {

      for (Player player : _players)
      {
         switch (player.getCharacterName())
         {
            case "Ms._Scarlett":
               _gb.movePlayer(player, "hallway_2");
               break;
            case "Col._Mustard":
               _gb.movePlayer(player, "hallway_5");
               break;
            case "Mrs._White":
               _gb.movePlayer(player, "hallway_12");
               break;
            case "Mrs._Peacock":
               _gb.movePlayer(player, "hallway_11");
               break;
            case "Prof._Plum":
               _gb.movePlayer(player, "hallway_8");
               break;
            case "Mr._Green":
               _gb.movePlayer(player, "hallway_3");
               break;
         }
         _server.broadcastTextMessage(
                 player.getCharacterName() + " will start in " + player.getLocation().getRoomName());
      }

   }


   public void executeTurn(Player currentPlayer) throws IOException, InterruptedException
   {

      // If the player is still active
      if (currentPlayer.getStatus())
      {

         // If the player successfully moved
         if (moveSequence(currentPlayer))
         {
            // Make a suggestion if there are no possible moves
            suggestSequence(currentPlayer);
         } else {
            // Player cannot make a suggestion because could not move
            _server.broadcastTextMessage("Because " + currentPlayer.getCharacterName() + " cannot move, " +
                    "they also cannot make a suggestion.");
         }

         if (accuseSequence(currentPlayer))
         {
            // TODO Game over procedures
         }

         /*
         TODO This code functionality has been moved to other methods, but we need to check to make sure there are no
         errors
          */
         // Room currentLocation = currentPlayer.getLocation();
         // You can uncomment this if you want to make sure the program is successfully looping through here
         // _server.broadcastTextMessage(currentPlayer.getCharacterName() + " is currently in the " + currentLocation.getRoomName());
//         boolean canMove = true;
//         if (canMove){
//            String desiredLocation = currentPlayer.getMoveCommand();
//            move(currentPlayer, desiredLocation);
//         }

//         boolean canSuggest = true;
         // TODO canSuggest = _gb.isRoom(currentPlayer.getLocation());
//         Suggestion sug = currentPlayer.getSuggestionCommand(_deckController.getSuggestionDeck().getCards());
//         handleSuggest(sug);
//         _gb.movePlayer("", "");

         /*Accusation acc = currentPlayer.getAccusationCommand();
         if (acc != null){
            accuse(acc);
          */

//         currentPlayer.getAccusationCommand(_deckController.getSuggestionDeck().getCards());
      } else
      {
         // Player is not active
         _server.broadcastTextMessage(currentPlayer.getCharacterName() + " must pass because they incorrectly accused");
      }
      
   }


   private boolean moveSequence(Player currentPlayer) throws IOException, InterruptedException
   {
      _server.broadcastTextMessage(
              currentPlayer.getCharacterName() + " is currently in the " + currentPlayer.getLocation().getRoomName());

      String desiredRoom = currentPlayer.getMoveCommand();

      if (desiredRoom != null)
      {
         move(currentPlayer, desiredRoom);
         return true;
      } else
      {
         _server.broadcastTextMessage(currentPlayer.getCharacterName() + " has no place to move.");
         return false;
      }

   }


   private void suggestSequence(Player currentPlayer) throws IOException
   {

      Suggestion suggestion = currentPlayer.getSuggestionCommand(_deckController.getSuggestionDeck());

      _server.broadcastTextMessage(currentPlayer + "has suggested that " + suggestion.toString());

      handleSuggest(suggestion);
   }


   private boolean accuseSequence(Player currentPlayer) throws IOException, InterruptedException
   {
      // Get suggestion object from player; will be null if player chose not to accuse
      Suggestion accusationCommand = currentPlayer.getAccusationCommand(_deckController.getSuggestionDeck());

      if (accusationCommand != null)
      {
         // Player made an accusation
         _server.broadcastTextMessage(
                 currentPlayer + "has made an accusation that " + accusationCommand.toString() + "!");

         boolean wasCorrect = _deckController.checkAccusation(accusationCommand);

         // Trace and check this

         String msg;

         if (wasCorrect)
         {
            msg = currentPlayer.getCharacterName() + " is RIGHT!";
            _gameOver = true;
         } else
         {
            msg = currentPlayer.getCharacterName() + " is WRONG!";
            _players.get(_turn).setStatus(false);
         }

         _server.broadcastTextMessage(msg);
         return wasCorrect;

      } else
      {
         // Player did not make an accusation
         _server.broadcastTextMessage(currentPlayer.getCharacterName() + " chose not to make an accusation.");
         return false;
      }
   }


   public void move(Player player, String room)
   {

      boolean success = _gb.movePlayer(player, room);

      if (success)
      {
         _server.broadcastTextMessage(player.getCharacterName() + " moves to the " + room);
         /*
         This is handled by the gameboard class now
         player.setLocation(room);
          */
      } else
      {
         _server.broadcastTextMessage(player.getCharacterName() + " invalid move");
      }
   }


   public void handleSuggest(Suggestion sug) throws IOException
   {

      // This functionality already exists in the calling method

/*      String suspect = sug.getCard(Card.CardType.Suspect).getCardName();
      String weapon = sug.getCard(Card.CardType.Weapon).getCardName();
      String rm = sug.getCard(Card.CardType.Room).getCardName();
      String msg =
              _players.get(_turn).getCharacterName() + " has suggested that " + suspect + " did it with the " + weapon +
                      " in " +
                      "the " + rm + "!";

      System.out.println(msg);
      _server.broadcastTextMessage(msg);*/

      int marker = _turn + 1; //start with the next user to begin disproving
      marker = marker % numberOfPlayers;
      String disprovingCard = null;
      while (marker != _turn && disprovingCard == null)
      {
         _server.broadcastTextMessage(
                 "Asking " + _players.get(marker).getUserID() + " if they can disprove suggestion.");
         ArrayList<Card> disprovingOptions = checkPlayerHand(_players.get(marker), sug);
         if (disprovingOptions.isEmpty())
         {
            _server.broadcastTextMessage(_players.get(marker).getUserID() + " cannot disprove suggestion");
         } else
         {
            //TODO this code needs to be refactored
//            disprovingCard = _players.get(marker).disproveSuggestion(disprovingOptions);
//            _server.broadcastTextMessage("The suggestion has been disproven by " + _players.get(marker).getUserID());
//            _players.get(_turn).sendMessage(_players.get(marker).getUserID() + " has shown you the " + disprovingCard + " card");
         }

         marker++;
         marker %= numberOfPlayers;
      }
      if (disprovingCard == null)
      {
         _server.broadcastTextMessage("Nobody disproved the suggestion");
      }
   }


   private ArrayList<Card> checkPlayerHand(Player player, Suggestion sug)
   {

      ArrayList<Card> options = new ArrayList<>();

      for (Card card : player.getPlayerHand().getCards())
      {
         for (Card sugCard : sug.getSuggestedCards())
         {
            if (card.getCardName().equals(sugCard.getCardName()))
            {
               options.add(card);
            }
         }
      }

      return options;
   }


   /*
   TODO Replaced with accuseSequence; check to make sure no functionality has been lost
    */
/*   public boolean accuse(String suspect, String weapon, String room)
   {

      String msg =
              _players.get(_turn).getCharacterName() + " has accused " + suspect + " of killing Mr. Black with the " +
                      weapon +
                      " in the " + room + "!";

      System.out.println(msg);
      _server.broadcastTextMessage(msg);

      boolean accusationCorrect = _deckController.checkAccusation(new Suggestion(suspect, weapon, room));

      if (accusationCorrect)
      {
         msg = _players.get(_turn).getCharacterName() + " is RIGHT!";
         _gameOver = true;
      } else
      {
         msg = _players.get(_turn).getCharacterName() + " is WRONG!";
         _players.get(_turn).setStatus(false);
      }

      _server.broadcastTextMessage(msg);
      return accusationCorrect;
   }*/


   private int getFirstTurn()
   {

      int index = 0;
      for (Player player : _players)
      {
         String name = player.getCharacterName();
         if (name == "Miss Scarlett")
         {
            index = _players.indexOf(player);
         }
      }
      return index;
   }


}