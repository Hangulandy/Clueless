/*
The Game Flow subsystem will act as the messenger between the user interface and the other subsystems, the
abstraction of the whole system. Tracks which Player has a turn, queries other subsystems as to the viability
of the user's inputs. Offers options to user to Move, Guess, Deny, Accuse, etc.

Class written by Kira Ullman, edited by Andrew Johnson
*/

import java.util.ArrayList;

public class GameController
{

   private Server _server;
   private ArrayList<Player> _players; // TODO make into circular linked list
   private boolean _gameStarted;
   private GameBoardPlaceHolder _gb;
   DeckControllerPlaceHolder _deckController;
   private int turn;
   private boolean _gameOver;


   public GameController(Server server)
   {
      _server = server;
      _players = new ArrayList<Player>();
      _gb = new GameBoardPlaceHolder();
      _deckController = new DeckControllerPlaceHolder();
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
      boolean success = _gb.movePlayer(_players.get(turn).getCharacterName(), room);

      if (success)
      {
         _server.broadcast(_players.get(turn).getCharacterName() + " moves to the " + room);
      } else
      {
         _server.broadcast(_players.get(turn).getCharacterName() + " invalid move");
      }
   }


   public void suggest(String suspect, String weapon, String room)
   {

      String msg =
              _players.get(turn).getCharacterName() + " has suggested that " + suspect + " did it with the " + weapon +
                      " in " +
                      "the " + room + "!";

      System.out.println(msg);
      _server.broadcast(msg);

      int marker = turn + 5; //start with the user who preceded the suggester
      marker = marker % 6;
      String disprovingCard = null;
      while(marker != turn && disprovingCard == null){
         disprovingCard = _players.get(marker).disproveSuggestion(suspect, weapon, room);
         if (disprovingCard != null){
            _server.broadcast("The suggestion has been disproven by " + _players.get(marker).getUserID());
            _players.get(turn).receiveMessage(_players.get(marker).getUserID() + " has shown you the " + disprovingCard + " card");
         }
         marker = marker + 6;
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