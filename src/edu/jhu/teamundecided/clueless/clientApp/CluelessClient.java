/*
The CluelessClient class is an application that handles client-side operations, reads messages from the corresponding
 ServerWorker, and sends messages to the same.

This implementation is borrowed from a tutorial at https://fullstackmastery.com/ep4 written by Jim Liao. It was
adapted for use in this system by Andrew Johnson.
 */
package edu.jhu.teamundecided.clueless.clientApp;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class CluelessClient
{

   private final String _serverName;
   private final int _serverPort;
   private Socket _socket;
   private InputStream _serverIn;
   private OutputStream _serverOut;
   private BufferedReader _bufferedIn;
   private String _userName;
   private boolean keepAsking;

   private Scanner _scanner = new Scanner(System.in);

   private ArrayList<UserStatusListener> _userStatusListeners = new ArrayList<>();
   private ArrayList<MessageListener> _messageListeners = new ArrayList<>();


   public CluelessClient(String serverName, int serverPort)
   {

      this._serverName = serverName;
      this._serverPort = serverPort;
      this.keepAsking = true;
   }


   public static void main(String[] args) throws IOException
   {

      CluelessClient client = new CluelessClient("localhost", 8818);
      client.addUserStatusListener(new UserStatusListener()
      {

         @Override
         public void online(String login)
         {

            System.out.println("ONLINE: " + login);
         }


         @Override
         public void offline(String login)
         {

            System.out.println("OFFLINE: " + login);
         }
      });

      client.addMessageListener(new MessageListener()
      {

         @Override
         public void onMessage(String fromLogin, String msgBody)
         {

            System.out.println("You got a message from " + fromLogin + " ===> " + msgBody);
         }
      });

      if (!client.connect())
      {
         System.err.println("Connect failed.");
      } else
      {
         System.out.println("Connect successful");

         client.receiveUserName();

         if (client.login(client._userName))
         {
            while (client.keepAsking)
            {
               client.MenuSequence();
            }

         } else
         {
            System.err.println("Login failed");
         }

         // client.logoff();
      }
   }


   private void MenuSequence()
   {

      printMainMenu();
      int userChoice = askUserForN(1, 3);
      switch (userChoice)
      {
         case 1:
            broadcastMsg();
            break;
         case 2:
            keepAsking = false;
            requestStart();
            break;
         case 3:
            try
            {
               keepAsking = false;
               logoff();
            } catch (IOException e)
            {
               e.printStackTrace();
            }
            break;
      }
   }


   private void printMainMenu()
   {

      System.out.println("Menu Options: ");
      System.out.println("1. Send a message to everybody");
      System.out.println("2. Start game");
      System.out.println("3. Logoff");
   }


   private int askUserForN(int min, int max)
   {

      boolean valid = false;
      int userInput = 0;

      String errMSG = String.format("You should enter an integer from the menu.");

      do
      {
         System.out.println("Please Enter Your Choice: ");

         if (this._scanner.hasNextInt())
         {
            userInput = this._scanner.nextInt();

            if (userInput >= min && userInput <= max)
            {
               valid = true;
            } else
            {
               // choice is <= lower or >= upper
               System.out.println("\nYou entered " + userInput + ". ");
               System.out.println(errMSG);
            }
         } else
         {
            // not an integer
            System.out.println(errMSG);
            this._scanner.next();
         }

      } while (!(valid));

      this._scanner.nextLine(); // advance the scanner after integer input
      return userInput;
   }


   private void requestStart()
   {

      String cmd = "start\n";
      try
      {
         _serverOut.write(cmd.getBytes());
      } catch (IOException e)
      {
         System.out.println("The message failed to send.");
      }
   }



   private void receiveUserName()
   {

      System.out.println("Please enter a user name: ");
      _userName = _scanner.nextLine().trim();
   }


   private boolean login(String userName) throws IOException
   {

      String cmd = "login " + userName + "\n";
      _serverOut.write(cmd.getBytes());

      String response = _bufferedIn.readLine();
      System.out.println("Response Line:" + response);

      if ("ok login".equalsIgnoreCase(response))
      {
         startMessageReader();
         return true;
      } else
      {
         return false;
      }
   }


   private void logoff() throws IOException
   {

      String cmd = "logoff\n";
      _serverOut.write(cmd.getBytes());
   }


   private void startMessageReader()
   {

      Thread t = new Thread()
      {

         @Override
         public void run()
         {

            readMessageLoop();
         }
      };
      t.start();
   }


   /*
   The readMessageLoop method will receive and parse communications from the server and then call the appropriate
   methods in response.
    */
   private void readMessageLoop()
   {

      try
      {
         String line;
         while ((line = _bufferedIn.readLine()) != null)
         {
            System.out.println("Received message : " + line);
            String[] tokens = line.split(" ");
            if (tokens != null && tokens.length > 0)
            {
               String cmd = tokens[0];
               if ("online".equalsIgnoreCase(cmd))
               {
                  handleOnline(tokens);
               } else if ("offline".equalsIgnoreCase(cmd))
               {
                  handleOffline(tokens);
               } else if ("msg".equalsIgnoreCase(cmd))
               {
                  handleMessage(tokens);
               } else if ("move".equalsIgnoreCase(cmd))
               {
                  handleMultipleChoicePrompt(tokens);
               } else if ("suspect".equalsIgnoreCase(cmd))
               {
                  handleMultipleChoicePrompt(tokens);
               } else if ("weapon".equalsIgnoreCase(cmd))
               {
                  handleMultipleChoicePrompt(tokens);
               } else if ("room".equalsIgnoreCase(cmd))
               {
                  handleMultipleChoicePrompt(tokens);
               } else if ("askAccuse".equalsIgnoreCase(cmd))
               {
                  handleAskAccuse(tokens);
               }
            }
         }
      } catch (Exception ex)
      {
         ex.printStackTrace();
         try
         {
            _socket.close();
         } catch (IOException e)
         {
            e.printStackTrace();
         }
      }
   }


   private void handleMultipleChoicePrompt(String[] tokens)
   {

      int choice = generateMenu(tokens);

      sendReply(tokens[0], tokens[choice]);

   }


   private int generateMenu(String[] tokens)
   {

      System.out.println("\nPlease select a choice below for the " + tokens[0] + ":");

      for (int i = 1; i < tokens.length; i++)
      {
         System.out.println(i + " " + tokens[i]);
      }

      System.out.println();

      return askUserForN(1, tokens.length);
   }


   private void sendReply(String cmd, String chosenItem)
   {
      String outMsg = cmd + " " + chosenItem + "\n";
      try
      {
         _serverOut.write(outMsg.getBytes());
         System.out.println("Sent message : " + outMsg);
      } catch (IOException e)
      {
         System.out.println("The message failed to send.");
      }
   }



   private void handleAskAccuse(String[] tokens)
   {

      String prompt = concatenateTokens(tokens, 1, tokens.length - 1);

      System.out.println(prompt);

      String[] menuItems = {"answer", "Yes", "No"};

      int choice = generateMenu(menuItems);

      sendReply("answer", menuItems[choice]);

   }

   /*
   The handleMessage method processes a text message received from the ServerWorker
    */
   private void handleMessage(String[] tokens)
   {

      String login = tokens[1];

      String msgBody = concatenateTokens(tokens, 2, tokens.length - 1);

      for (MessageListener listener : _messageListeners)
      {
         listener.onMessage(login, msgBody);
      }
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
   Processes an offline message received from the ServerWorker Thread.
    */
   private void handleOffline(String[] tokens)
   {

      String login = tokens[1];
      for (UserStatusListener listener : _userStatusListeners)
      {
         listener.offline(login);
      }
   }


   /*
   Processes an online message received from the ServerWorker Thread.
    */
   private void handleOnline(String[] tokens)
   {

      String login = tokens[1];
      for (UserStatusListener listener : _userStatusListeners)
      {
         listener.online(login);
      }
   }


   private boolean connect()
   {

      try
      {
         this._socket = new Socket(_serverName, _serverPort);
         System.out.println("Client port is " + _socket.getLocalPort());
         this._serverOut = _socket.getOutputStream();
         this._serverIn = _socket.getInputStream();
         this._bufferedIn = new BufferedReader(new InputStreamReader(_serverIn));
         return true;
      } catch (IOException e)
      {
         e.printStackTrace();
      }
      return false;
   }


   public void addUserStatusListener(UserStatusListener listener)
   {

      _userStatusListeners.add(listener);
   }


   public void removeUserStatusListener(UserStatusListener listener)
   {

      _userStatusListeners.remove(listener);
   }


   public void addMessageListener(MessageListener listener)
   {

      _messageListeners.add(listener);
   }


   public void removeMessageListener(MessageListener listener)
   {

      _messageListeners.remove(listener);
   }


   public void broadcastMsg()
   {

      System.out.println("Please enter the message to broadcast:");
      String msg = _scanner.nextLine();

      String cmd = "broadcast " + msg + "\n";
      try
      {
         _serverOut.write(cmd.getBytes());
      } catch (IOException e)
      {
         System.out.println("The message failed to send.");
      }

   }


}
