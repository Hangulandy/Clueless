package edu.jhu.teamundecided.clueless.serverApp;/*
The Game Flow subsystem will act as the messenger between the user interface and the other subsystems, the
abstraction of the whole system. Tracks which Player has a turn, queries other subsystems as to the viability
of the user's inputs. Offers options to user to Move, Guess, Deny, Accuse, etc.

Class written by Kira Ullman, edited by Andrew Johnson
*/

import edu.jhu.teamundecided.clueless.gameBoard.GameBoard;
import edu.jhu.teamundecided.clueless.player.Player;
import edu.jhu.teamundecided.clueless.deck.DeckController;
import java.util.ArrayList;

public class GameController
{

   private Server _server;
   private ArrayList<Player> _players; // TODO make into circular linked list
   private boolean _gameStarted;
   private GameBoard _gb;
   DeckController _deckController;
   private int turn;
   private boolean _gameOver;


   public GameController(Server server)
   {
      _server = server;
      _players = new ArrayList<Player>();
      _gb = new GameBoard();
      _deckController = new DeckController();
      turn = 0;
      _gameStarted = false;
   }


   public void startGame()
   {

      if (!_gameStarted)
      {

         ArrayList<ServerWorker> list = _server.getWorkerList();

         for (ServerWorker worker : list)
         {
            addPlayer(worker.getUserName());
         }

         _gameStarted = true;
      } else
      {
         _server.broadcast("Game has already been started...");
      }
      //deckcontroller.dealCards(playerList);
      turn = getFirstTurn();
      runGame();
   }

   public void addPlayer(String userID)
   {

      Player player = new Player(userID, this);
      System.out.println(player.toString());
      _players.add(player);
      _server.broadcast(player.toString());
   }

   public void runGame()
   {
      Player currentPlayer;
      while (!_gameOver)
      {
         currentPlayer = _players.get(turn);
         currentPlayer.executeTurn();
         turn++;
         turn = turn % 6;
      }
   }


   public void move(String room)
   {
      //TODO Need to refactor how Move Player works.
//      boolean success = _gb.movePlayer(_players.get(turn).getCharacterName(), room);

//      if (success)
//      {
//         _server.broadcast(_players.get(turn).getCharacterName() + " moves to the " + room);
//      } else
//      {
//         _server.broadcast(_players.get(turn).getCharacterName() + " invalid move");
//      }
   }


   public void suggest(String suspect, String weapon, String room)
   {

      String msg =
              _players.get(turn).getCharacterName() + " has suggested that " + suspect + " did it with the " + weapon +
                      " in " +
                      "the " + room + "!";

      System.out.println(msg);
      _server.broadcast(msg);

      int marker = turn + 1; //start with the user who preceded the suggester
      marker = marker % 6;
      String disprovingCard = null;
      while(marker != turn && disprovingCard == null){
         //TODO broadcast "asking marker if they can disprove"
         //TODO look into players' hands and check if they can disprove
         disprovingCard = _players.get(marker).disproveSuggestion(suspect, weapon, room);
         if (disprovingCard != null){
            _server.broadcast("The suggestion has been disproven by " + _players.get(marker).getUserID());
            _players.get(turn).receiveMessage(_players.get(marker).getUserID() + " has shown you the " + disprovingCard + " card");
         }
         else{
            //TODO tell everyone marker passed
         }
         marker = marker ++;
         marker = marker % 6;
      }
      if(disprovingCard == null){
         _server.broadcast("Nobody disproved the suggestion");
      }
   }


   public boolean accuse(String suspect, String weapon, String room)
   {

      String msg =
              _players.get(turn).getCharacterName() + " has accused " + suspect + " of killing Mr. Black with the " +
                      weapon +
                      " in the " + room + "!";

      System.out.println(msg);
      _server.broadcast(msg);

      boolean accusationCorrect = _deckController.checkAccusation(suspect, weapon, room);

      if (accusationCorrect)
      {
         msg = _players.get(turn).getCharacterName() + " is RIGHT!";
         _gameOver = true;
      } else
      {
         msg = _players.get(turn).getCharacterName() + " is WRONG!";
      }

      _server.broadcast(msg);
      return accusationCorrect;
   }

   public String getPlayerLocation(Player player){
      return _deckController.getLocation(player);
   }

   public boolean canMakeSuggestion(Player player){
      String location =_deckController.getLocation(player);
      return _deckController.isRoom(location);
   }

   private int getFirstTurn(){
      int index = 0;
      for (Player player: _players) {
         String name = player.getCharacterName();
         if (name == "Miss Scarlett"){
            index = _players.indexOf(player);
         }
      }
      return index;
   }

}