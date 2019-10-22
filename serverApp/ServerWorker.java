/*
The ServerWorker class is a Thread that sends messages to and reads messages from the client. It parses the messages
and calls appropriate methods on the Server to communication with the rest of the system.

This implementation is borrowed from a tutorial at https://fullstackmastery.com/ep4 written by Jim Liao. It was
adapted for use in this system by Andrew Johnson.
 */

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerWorker extends Thread
{

   private final Socket _clientSocket;
   private final Server _server;
   private String _userName = null;
   private OutputStream _outputStream;


   /*
   The default constructor should be called by the Server. It will set this server to the calling server. It will set
   the client socket to the socket initiated by the server when the socket was accepted.
    */
   public ServerWorker(Server server, Socket clientSocket)
   {

      _server = server;
      _clientSocket = clientSocket;
   }


   /*
   This is called by the Server after calling the ServerWorker constructor
    */
   @Override
   public void run()
   {

      try
      {
         handleClientSocket();
      } catch (IOException e)
      {
         e.printStackTrace();
      } catch (InterruptedException e)
      {
         e.printStackTrace();
      }
   }


   /*
   The handleClientSocket method reads the input stream from the client socket and handles incoming communications by
    parsing them and calling the appropriate methods based on the content of the communication.
    */
   private void handleClientSocket() throws IOException, InterruptedException
   {

      InputStream inputStream = _clientSocket.getInputStream();
      this._outputStream = _clientSocket.getOutputStream();

      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      while ((line = reader.readLine()) != null)
      {
         String[] tokens = line.split(" ");
         if (tokens != null && tokens.length > 0)
         {
            String cmd = tokens[0];
            if ("logoff".equals(cmd) || "quit".equalsIgnoreCase(cmd))
            {
               handleLogoff();
               break;
            } else if ("login".equalsIgnoreCase(cmd))
            {
               handleLogin(_outputStream, tokens);
            } else if ("msg".equalsIgnoreCase(cmd))
            {
               handleMessage(tokens);
            } else if ("broadcast".equalsIgnoreCase(cmd))
            {
               handleBroadcast(tokens);
            } else if ("start".equalsIgnoreCase(cmd))
            {
               handleStartGame();
            } else if ("move".equalsIgnoreCase(cmd))
            {
               handleMove(tokens);
            } else if ("suggest".equalsIgnoreCase(cmd))
            {
               handleSuggest(tokens);
            } else if ("accuse".equalsIgnoreCase(cmd))
            {
               handleAccuse(tokens);
            }
         }
      }

      _clientSocket.close();
   }


   private void handleMove(String[] tokens)
   {

      _server.move(tokens);
   }


   private void handleAccuse(String[] tokens)
   {

      _server.accuse(tokens);
   }


   private void handleSuggest(String[] tokens)
   {

      _server.suggest(tokens);
   }


   /*
   The handleStartGame method will be called when a CluelessClient instance requests so. The rest is self explanatory.
    */
   private void handleStartGame()
   {

      _server.requestGameStart();
   }


   /*
   The handleBroadcast method passes a broadcast request, including the message body, to the server.
    */
   private void handleBroadcast(String[] tokens)
   {

      String body = concatenateTokens(tokens, 1, tokens.length - 1);

      _server.broadcast(body);
   }


   /*
   The concatenateTokens method is used to reassemble the body of a message.
    */
   private String concatenateTokens(String[] tokens, int fromIndex, int toIndex)
   {

      StringBuilder output = new StringBuilder();

      for (int i = fromIndex; i <= toIndex; i++)
      {
         output.append(tokens[i]);
         output.append(" ");
      }

      return output.toString();
   }


   /*
   The handleMessage method is called to parse and relay a text message to another ServerWorker instance.
    */
   private void handleMessage(String[] tokens) throws IOException
   {

      String sendTo = tokens[1];

      String body = concatenateTokens(tokens, 2, tokens.length - 1);

      List<ServerWorker> workerList = _server.getWorkerList();
      for (ServerWorker worker : workerList)
      {

         if (sendTo.equalsIgnoreCase(worker.getUserName()))
         {
            String outMsg = "msg " + this._userName + " " + body + "\n";
            worker.send(outMsg);
         }
      }
   }


   /*
   The handleLogoff method processes the CluelessClient request to logoff by first having the Server remove the
   worker, then sending a logoff message to the other ServerWorkers, and finally closing the clientSocket.
    */
   private void handleLogoff() throws IOException
   {

      _server.removeWorker(this);
      List<ServerWorker> workerList = _server.getWorkerList();

      // send other online users current user's status
      String onlineMsg = "offline " + _userName + "\n";
      for (ServerWorker worker : workerList)
      {
         if (!_userName.equals(worker.getUserName()))
         {
            worker.send(onlineMsg);
         }
      }

      _clientSocket.close();
      System.out.println(_userName + " has logged off...");

   }


   /*
   Self-explanatory
    */
   public String getUserName()
   {

      return _userName;
   }


   /*
   Processes a login request by setting the userName, sending this ServerWorker all other ServerWorker statuses, and
   sending this ServerWorker's online status messages to the other ServerWorkers
    */
   private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException
   {

      if (tokens.length > 1)
      {
         String userName = tokens[1];

         String msg = "ok login\n";
         outputStream.write(msg.getBytes());
         this._userName = userName;
         System.out.println("User logged in succesfully: " + userName);

         List<ServerWorker> workerList = _server.getWorkerList();

         // send current user all other online logins
         for (ServerWorker worker : workerList)
         {
            if (worker.getUserName() != null)
            {
               if (!userName.equals(worker.getUserName()))
               {
                  String msg2 = "online " + worker.getUserName() + "\n";
                  send(msg2);
               }
            }
         }

         // send other online users current user's status
         String onlineMsg = "online " + userName + "\n";
         for (ServerWorker worker : workerList)
         {
            if (!userName.equals(worker.getUserName()))
            {
               worker.send(onlineMsg);
            }
         }
      }
   }


   /*
   Writes a message to the outputStream that goes to the CluelessClient for this ServerWorker.
    */
   public void send(String outMsg) throws IOException
   {

      if (_userName != null)
      {
         _outputStream.write(outMsg.getBytes());
      }
   }


}
