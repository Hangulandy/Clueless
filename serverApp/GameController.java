import java.util.ArrayList;

//The Game Flow subsystem will act as the messenger between the user interface and the other subsystems, the abstraction of the whole system. Tracks which Player has a turn, queries other subsystems as to the viability of the user's inputs. Offers options to user to Move, Guess, Deny, Accuse, etc.



public class GameController
{

   private Server _server;
   private ArrayList<Player> _players;


   public GameController(Server server)
   {
      _server = server;
      _players = new ArrayList<Player>();
   }


   public void startGame()
   {

      ArrayList<ServerWorker> list = _server.getWorkerList();

      for (ServerWorker worker : list)
      {
         addPlayer(worker.getUserName());
      }
   }

   public void addPlayer(String userID)
   {
      Player player = new Player(userID);
      System.out.println(player.toString());
      _players.add(player);
      _server.broadcast(player.toString());
   }


   public void chooseCharacter()
   {

   }


   public void removePlayer()
   {
      // TODO
   }


   public void move(String player, String position)
   {
      // GameBoard.Move(player, position);
   }


   public void suggest(String suspect, String room, String weapon)
   {
      // boolean suggestionCorrect = _deckController.checkSusgestion(suspect, room, weapon);
   }


   public void accuse(String suspect, String room, String weapon)
   {
      // boolean accusationCorrect = _deckController.checkSusgestion(suspect, room, weapon);
   }


}