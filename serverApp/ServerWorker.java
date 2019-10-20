import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerWorker extends Thread {

   private final Socket clientSocket;
   private final Server server;
   private InputStream inputStream;
   private OutputStream outputStream;
   private String login = null;


   public ServerWorker(Server server, Socket clientSocket) {

      this.server = server;
      this.clientSocket = new Socket();
   }


   @Override
   public void run() {

      try {
         handleClientSocket();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }


   private void handleClientSocket() throws IOException {

      this.inputStream = clientSocket.getInputStream();
      this.outputStream = clientSocket.getOutputStream();

      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      String line;

      while ((line = reader.readLine()) != null) {
         String[] tokens = line.split(" ");
         if (tokens != null && tokens.length > 0) {
            String cmd = tokens[0];
            if ("logoff".equalsIgnoreCase(cmd) || "quit".equalsIgnoreCase(cmd)) {
               handleLogoff();
               break;
            } else if ("login".equalsIgnoreCase(cmd)) {
               handleLogin(tokens);
            } else if ("messge".equalsIgnoreCase(cmd) || "msg".equalsIgnoreCase(cmd)) {
               handleMessage(tokens);
            } else {
               String msg = "unknown command: " + cmd + "\\n";
            }
            String msg = "You typed " + line + "\\n";
            outputStream.write(msg.getBytes());
         }
      }
   }


   private void handleMessage(String[] tokens) {

      boolean success = false;

      if (tokens.length > 1) {
         String sendTo = tokens[1];
         StringBuilder body = new StringBuilder();

         for (int i = 2; i < tokens.length; i++) {
            body.append(tokens[i]);
            body.append(" ");
         }

         List<ServerWorker> list = server.getWorkerList();
         for (ServerWorker worker : list) {
            if (sendTo.equalsIgnoreCase(worker.getLogin())) {
               try {
                  worker.sendMsg(body.toString());
                  success = true;
               } catch (IOException e) {
                  // TODO
               }
            }
         }
      }

      if (!success) {
         // TODO notify sender that message failed
      }
   }


   private void handleLogoff() throws IOException {

      server.removeWorker(this);
      clientSocket.close();

   }


   private void handleLogin(String[] tokens) throws IOException {

      if (tokens.length == 3) {
         String login = tokens[1];
         String password = tokens[2];


         StringBuilder msg = new StringBuilder();

         if (login.equals("guest") && password.equals("guest")) {
            msg.append("ok login");
            this.login = login;
         } else {
            msg.append("error login");
         }

         outputStream.write(msg.toString().getBytes());
      }
   }


   public void sendMsg(String msg) throws IOException {

      outputStream.write(msg.getBytes());
   }


   public String getLogin() {

      return this.login;
   }

}
