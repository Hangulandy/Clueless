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
   private ArrayList<Player> _players;
   private boolean _gameStarted = false;
   private GameBoardPlaceHolder _gb;
   private DeckControllerPlaceHolder _deckController;
   private int turn = 0;


   public GameController(Server server)
   {

      _server = server;
      _players = new ArrayList<Player>();
      _gb = new GameBoardPlaceHolder();
      _deckController = new DeckControllerPlaceHolder();
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

   }


   public void addPlayer(String userID)
   {

      Player player = new Player(userID);
      System.out.println(player.toString());
      _players.add(player);
      _server.broadcast(player.toString());
   }

   public void move(String room)
   {
      boolean success = _gb.movePlayer(_players.get(turn).getCharacterName(), room);

      if (success)
      {
         _server.broadcast(_players.get(turn).getCharacterName() + " moves to the " + room);
      } else
      {
         // TODO Contingency procedures
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

      boolean suggestionCorrect = _deckController.checkSuggestion(suspect, weapon, room);

      System.out.println("Returned : " + suggestionCorrect);

   }


   public void accuse(String suspect, String weapon, String room)
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
      } else
      {
         msg = _players.get(turn).getCharacterName() + " is WRONG!";
      }

      _server.broadcast(msg);
   }


}