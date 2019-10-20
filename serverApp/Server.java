import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

   private final int serverPort;
   private ArrayList<ServerWorker> workerArrayList = new ArrayList<ServerWorker>();


   public Server(int port) {

      this.serverPort = port;
   }


   public List<ServerWorker> getWorkerList() {
      return workerArrayList;
   }

   @Override


   public void run() {

      try {
         ServerSocket serverSocket = new ServerSocket(serverPort);

         while (true) {
            System.out.println("About to accept connection...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted connection from " + clientSocket);
            ServerWorker serverWorker = new ServerWorker(this, clientSocket);
            workerArrayList.add(serverWorker);
            serverWorker.start();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void broadcastMsg(String msg) {

      for (ServerWorker worker : workerArrayList) {
         try {
            worker.sendMsg(msg);
         } catch (IOException e) {
            System.out.println("Message to " + worker.getLogin() + " failed...");
         }
      }
   }


   public void removeWorker(ServerWorker serverWorker) {

      workerArrayList.remove(serverWorker);
   }


}
