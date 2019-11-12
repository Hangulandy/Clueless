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
   private ArrayList<Player> _players;
   private boolean _gameStarted;
   private GameBoard _gb;
   DeckController _deckController;
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
         _turn = getNextTurn(_turn);
      }

      _server.broadcastTextMessage("This concludes the game!");
      _server.broadcastTextMessage("We will log you off now...Goodbye!");
      _server.broadcastCommand("logoff");
   }


   private void placePlayers()
   {

      for (Player player : _players)
      {
         switch (player.getCharacterName())
         {
            case "Ms._Scarlett":
               move(player, "hallway_2");
               break;
            case "Col._Mustard":
               move(player, "hallway_5");
               break;
            case "Mrs._White":
               move(player, "hallway_12");
               break;
            case "Mrs._Peacock":
               move(player, "hallway_11");
               break;
            case "Prof._Plum":
               move(player, "hallway_8");
               break;
            case "Mr._Green":
               move(player, "hallway_3");
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
         } else
         {
            // Player cannot make a suggestion because could not move
            _server.broadcastTextMessage("Because " + currentPlayer.getCharacterName() + " cannot move, " +
                    "they also cannot make a suggestion.");
         }

         _gameOver = accuseSequence(currentPlayer);

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

      if (currentPlayer.getLocation().getIsHall())
      {
         _server.broadcastTextMessage(currentPlayer.getUserName() + " cannot make a suggestion because they are in a " +
                 "hall.");
         return;
      }

      Suggestion suggestion = currentPlayer.getSuggestionCommand(_deckController.getSuggestionDeck());

      Player suggestedPlayer = getByCharacterName(suggestion.getSuspect());

      if (suggestedPlayer != null)
      {
         move(suggestedPlayer, currentPlayer.getLocation().getRoomName());
      }

      _server.broadcastTextMessage(currentPlayer + "has suggested that " + suggestion.toString());

      handleSuggest(suggestion);
   }


   private boolean accuseSequence(Player currentPlayer) throws IOException, InterruptedException
   {
      // Get suggestion object from player; will be null if player chose not to accuse
      Suggestion accusationCommand = currentPlayer.getAccusationCommand(_deckController.getSuggestionDeck());

      if (accusationCommand == null)
      {
         // Player did not make an accusation
         _server.broadcastTextMessage(currentPlayer.getCharacterName() + " chose not to make an accusation.");
         return false;
      } else
      {
         // Player made an accusation
         _server.broadcastTextMessage(
                 currentPlayer + "has made an accusation that " + accusationCommand.toString() + "!");

         boolean wasCorrect = _deckController.checkAccusation(accusationCommand);

         String msg;

         if (wasCorrect)
         {
            msg = currentPlayer.getCharacterName() + " is RIGHT!";
         } else
         {
            msg = currentPlayer.getCharacterName() + " is WRONG!";
            _players.get(_turn).setStatus(false);
         }

         _server.broadcastTextMessage(msg);
         return wasCorrect;
      }
   }


   public void move(Player player, String room)
   {

      boolean success = _gb.movePlayer(player, room);

      if (success)
      {
         _server.broadcastTextMessage(player.getCharacterName() + " moves to the " + room);
         String msg = _gb.getGameBoardData(_players);

         System.out.println(msg);

         _server.broadcastCommand(msg);

      } else
      {
         _server.broadcastTextMessage(player.getCharacterName() + " invalid move");
      }
   }


   public void handleSuggest(Suggestion sug) throws IOException
   {

      int marker = getNextTurn(_turn);

      String disprovingCard = null;

      while (marker != _turn && disprovingCard == null)
      {
         _server.broadcastTextMessage(
                 "Asking " + _players.get(marker).getUserName() + " if they can disprove suggestion.");

         ArrayList<Card> disprovingOptions = checkPlayerHand(_players.get(marker), sug);

         if (disprovingOptions.isEmpty())
         {
            _server.broadcastTextMessage(_players.get(marker).getUserName() + " cannot disprove suggestion");
         } else
         {
            disprovingCard = _players.get(marker).disproveSuggestion(disprovingOptions);
            _players.get(_turn).getServerWorker().send("msg system " + _players.get(marker).getUserName() + " showed " +
                    "you " + disprovingCard);
            _server.broadcastTextMessage(
                    _players.get(marker).getUserName() + " showed a card to " + _players.get(_turn).getUserName());
         }

         marker = getNextTurn(marker);
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


   private int getFirstTurn()
   {

      int index = 0;
      for (Player player : _players)
      {
         String name = player.getCharacterName();
         if (name == "Ms._Scarlett")
         {
            index = _players.indexOf(player);
         }
      }
      return index;
   }


   private int getNextTurn(int counter)
   {

      counter++;
      return counter % _players.size();

   }


   private Player getByCharacterName(String characterName)
   {

      for (Player player : _players)
      {
         if (player.getCharacterName().equalsIgnoreCase(characterName))
         {
            return player;
         }
      }

      return null;
   }


}