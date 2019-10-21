import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;

public class ServerWorker extends Thread {

   private final Socket clientSocket;
   private final Server server;
   private String login = null;
   private OutputStream outputStream;
   private HashSet<String> topicSet = new HashSet<>();


   public ServerWorker(Server server, Socket clientSocket) {

      this.server = server;
      this.clientSocket = clientSocket;
   }


   @Override
   public void run() {

      try {
         handleClientSocket();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }


   private void handleClientSocket() throws IOException, InterruptedException {

      InputStream inputStream = clientSocket.getInputStream();
      this.outputStream = clientSocket.getOutputStream();

      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      while ((line = reader.readLine()) != null) {
         String[] tokens = line.split(" ");
         if (tokens != null && tokens.length > 0) {
            String cmd = tokens[0];
            if ("logoff".equals(cmd) || "quit".equalsIgnoreCase(cmd)) {
               handleLogoff();
               break;
            } else if ("login".equalsIgnoreCase(cmd)) {
               handleLogin(outputStream, tokens);
            } else if ("msg".equalsIgnoreCase(cmd)) {
               handleMessage(tokens);
            } else if ("broadcast".equalsIgnoreCase(cmd)) {
               handleBroadcast(tokens);
            } else {
               String msg = "unknown " + cmd + "\n";
               outputStream.write(msg.getBytes());
            }
         }
      }

      clientSocket.close();
   }


   private void handleBroadcast(String[] tokens) {

      String body = concatenateTokens(tokens, 1, tokens.length - 1);

      server.broadcast(body);
   }


   private String concatenateTokens(String[] tokens, int fromIndex, int toIndex) {

      StringBuilder output = new StringBuilder();

      for (int i = fromIndex; i <= toIndex; i++) {
         output.append(tokens[i]);
         output.append(" ");
      }

      return output.toString();
   }

   private void handleMessage(String[] tokens) throws IOException {

      String sendTo = tokens[1];

      String body = concatenateTokens(tokens, 2, tokens.length - 1);

      List<ServerWorker> workerList = server.getWorkerList();
      for (ServerWorker worker : workerList) {

         if (sendTo.equalsIgnoreCase(worker.getLogin())) {
            String outMsg = "msg " + this.login + " " + body + "\n";
            worker.send(outMsg);
         }
      }
   }


   private void handleLogoff() throws IOException {

      server.removeWorker(this);
      List<ServerWorker> workerList = server.getWorkerList();

      // send other online users current user's status
      String onlineMsg = "offline " + login + "\n";
      for (ServerWorker worker : workerList) {
         if (!login.equals(worker.getLogin())) {
            worker.send(onlineMsg);
         }
      }
      clientSocket.close();
   }


   public String getLogin() {

      return login;
   }


   private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {

      if (tokens.length > 1) {
         String login = tokens[1];

         if (login.equals("player1") || login.equals("player2")) {
            String msg = "ok login\n";
            outputStream.write(msg.getBytes());
            this.login = login;
            System.out.println("User logged in succesfully: " + login);

            List<ServerWorker> workerList = server.getWorkerList();

            // send current user all other online logins
            for (ServerWorker worker : workerList) {
               if (worker.getLogin() != null) {
                  if (!login.equals(worker.getLogin())) {
                     String msg2 = "online " + worker.getLogin() + "\n";
                     send(msg2);
                  }
               }
            }

            // send other online users current user's status
            String onlineMsg = "online " + login + "\n";
            for (ServerWorker worker : workerList) {
               if (!login.equals(worker.getLogin())) {
                  worker.send(onlineMsg);
               }
            }
         } else {
            String msg = "error login\n";
            outputStream.write(msg.getBytes());
            System.err.println("Login failed for " + login);
         }
      }
   }


   public void send(String outMsg) throws IOException {

      if (login != null) {
         System.out.println("login != null");
         System.out.println(outMsg);
         outputStream.write(outMsg.getBytes());
      }
   }


}
