import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerWorker extends Thread
{

   private final Socket _clientSocket;
   private final Server _server;
   private String _userName = null;
   private OutputStream _outputStream;


   public ServerWorker(Server server, Socket clientSocket)
   {

      _server = server;
      _clientSocket = clientSocket;
   }


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
            } else
            {
               String msg = "unknown " + cmd + "\n";
               _outputStream.write(msg.getBytes());
            }
         }
      }

      _clientSocket.close();
   }


   private void handleStartGame()
   {
      _server.requestGameStart();
   }


   private void handleBroadcast(String[] tokens)
   {

      String body = concatenateTokens(tokens, 1, tokens.length - 1);

      _server.broadcast(body);
   }


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


   public String getUserName()
   {

      return _userName;
   }


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


   public void send(String outMsg) throws IOException
   {

      if (_userName != null)
      {
         _outputStream.write(outMsg.getBytes());
      }
   }


}
