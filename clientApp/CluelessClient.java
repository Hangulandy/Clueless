import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class CluelessClient {

   private final String serverName;
   private final int serverPort;
   private Socket socket;
   private InputStream serverIn;
   private OutputStream serverOut;
   private BufferedReader bufferedIn;
   private String userName;

   private Scanner scanner = new Scanner(System.in);

   private ArrayList<UserStatusListener> userStatusListeners = new ArrayList<>();
   private ArrayList<MessageListener> messageListeners = new ArrayList<>();


   public CluelessClient(String serverName, int serverPort) {

      this.serverName = serverName;
      this.serverPort = serverPort;
   }


   public static void main(String[] args) throws IOException {

      CluelessClient client = new CluelessClient("localhost", 8818);
      client.addUserStatusListener(new UserStatusListener() {

         @Override
         public void online(String login) {

            System.out.println("ONLINE: " + login);
         }


         @Override
         public void offline(String login) {

            System.out.println("OFFLINE: " + login);
         }
      });

      client.addMessageListener(new MessageListener() {

         @Override
         public void onMessage(String fromLogin, String msgBody) {

            System.out.println("You got a message from " + fromLogin + " ===>" + msgBody);
         }
      });

      if (!client.connect()) {
         System.err.println("Connect failed.");
      } else {
         System.out.println("Connect successful");

         client.receiveUserName();

         if (client.login(client.userName)) {
            System.out.println("Login successful");

             System.out.println("Press ENTER to broadcast a message to the other clients");
             client.scanner.nextLine();

             client.broadcastMsg("This is a test of the broadcast method...");

         } else {
            System.err.println("Login failed");
         }

         //client.logoff();
      }
   }

    private void msg(String sendTo, String msgBody) throws IOException {
        String cmd = "msg " + sendTo + " " + msgBody + "\n";
        serverOut.write(cmd.getBytes());
    }

   private void receiveUserName() {

      System.out.println("Please enter a user name: ");
      userName = scanner.nextLine().trim();
   }


   private boolean login(String login) throws IOException {

      String cmd = "login " + login + "\n";
      serverOut.write(cmd.getBytes());

      String response = bufferedIn.readLine();
      System.out.println("Response Line:" + response);

      if ("ok login".equalsIgnoreCase(response)) {
         startMessageReader();
         return true;
      } else {
         return false;
      }
   }


   private void logoff() throws IOException {

      String cmd = "logoff\n";
      serverOut.write(cmd.getBytes());
   }


   private void startMessageReader() {

      Thread t = new Thread() {

         @Override
         public void run() {

            readMessageLoop();
         }
      };
      t.start();
   }


   private void readMessageLoop() {

      try {
         String line;
         while ((line = bufferedIn.readLine()) != null) {
            System.out.println("Message received from server: " + line);
            String[] tokens = line.split(" ");
            if (tokens != null && tokens.length > 0) {
               String cmd = tokens[0];
               if ("online".equalsIgnoreCase(cmd)) {
                  handleOnline(tokens);
               } else if ("offline".equalsIgnoreCase(cmd)) {
                  handleOffline(tokens);
               } else if ("msg".equalsIgnoreCase(cmd)) {
                  handleMessage(tokens);
               }
            }
         }
      } catch (Exception ex) {
         ex.printStackTrace();
         try {
            socket.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }


   private void handleMessage(String[] tokens) {

      String login = tokens[1];

      String msgBody = concatenateTokens(tokens, 2, tokens.length - 1);

      for (MessageListener listener : messageListeners) {
         listener.onMessage(login, msgBody);
      }
   }

   private String concatenateTokens(String[] tokens, int fromIndex, int toIndex) {

      StringBuilder output = new StringBuilder();

      for (int i = fromIndex; i <= toIndex; i++) {
         output.append(tokens[i]);
         output.append(" ");
      }

      return output.toString();
   }


   private void handleOffline(String[] tokens) {

      String login = tokens[1];
      for (UserStatusListener listener : userStatusListeners) {
         listener.offline(login);
      }
   }


   private void handleOnline(String[] tokens) {

      String login = tokens[1];
      for (UserStatusListener listener : userStatusListeners) {
         listener.online(login);
      }
   }


   private boolean connect() {

      try {
         this.socket = new Socket(serverName, serverPort);
         System.out.println("Client port is " + socket.getLocalPort());
         this.serverOut = socket.getOutputStream();
         this.serverIn = socket.getInputStream();
         this.bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
         return true;
      } catch (IOException e) {
         e.printStackTrace();
      }
      return false;
   }


   public void addUserStatusListener(UserStatusListener listener) {

      userStatusListeners.add(listener);
   }


   public void removeUserStatusListener(UserStatusListener listener) {

      userStatusListeners.remove(listener);
   }


   public void addMessageListener(MessageListener listener) {

      messageListeners.add(listener);
   }


   public void removeMessageListener(MessageListener listener) {

      messageListeners.remove(listener);
   }

   public void broadcastMsg(String msg) throws IOException {

      String cmd = "broadcast " + msg + "\n";
      serverOut.write(cmd.getBytes());

   }


}
