import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

   private final int serverPort;

   private ArrayList<ServerWorker> workerList = new ArrayList<>();


   public Server(int serverPort) {

      this.serverPort = serverPort;
   }


   public List<ServerWorker> getWorkerList() {

      return workerList;
   }


   @Override
   public void run() {

      try {
         ServerSocket serverSocket = new ServerSocket(serverPort);
         while (true) {
            System.out.println("About to accept client connection...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted connection from " + clientSocket);
            ServerWorker worker = new ServerWorker(this, clientSocket);
            workerList.add(worker);
            worker.start();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }


   public void removeWorker(ServerWorker serverWorker) {

      workerList.remove(serverWorker);
   }


   public void broadcast(String msg) {

      System.out.println("Broadcast called in server...");

      System.out.println("msg : " + msg);

      StringBuilder msgToSend = new StringBuilder();

      msgToSend.append("msg ");
      msgToSend.append("system ");
      msgToSend.append(msg);
      msgToSend.append("\n");

      System.out.println("modified msg : " + msgToSend);

      for (ServerWorker worker : workerList) {
         System.out.println("Found worker : " + worker.getLogin());
         try {
            worker.send(msgToSend.toString());
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }


}
