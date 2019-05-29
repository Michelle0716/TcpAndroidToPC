

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

//mpos   server  tool
public class Server implements Runnable {
   public static final int SERVERPORT = 3333;
   public void run() {
       try {
           System.out.println("Server: Connecting...");
           ServerSocket serverSocket = new ServerSocket(SERVERPORT);
           while (true) {
               System.out.println("Server:Connected.");
               Socket client = serverSocket.accept();
               System.out.println("Server: Receiving...");
               try {
                   BufferedReader in = new BufferedReader(
                   new InputStreamReader(client.getInputStream()));
                   String str = in.readLine();
                   System.out.println("Server: Received: '" + str + "'");
                   try
                   {
                   Thread.currentThread().sleep(4000);//毫秒
                   }
                   catch(Exception e){
                	   e.printStackTrace();
                   }
                   
                
                   
                   // 发送给客户端的消息 
                   PrintWriter out = new PrintWriter(new BufferedWriter(
                   		new OutputStreamWriter(client.getOutputStream())),true);
                   
                   out.println("sent to android message is:" +11);
                   out.flush();



               } catch (Exception e) {
                   System.out.println("Server: Error");
                   e.printStackTrace();
               } finally {
                   client.close();
                   System.out.println("Server: Done.");
               }
           }
       } catch (Exception e) {
           System.out.println("Server: Error");
           e.printStackTrace();
       }
   }
   public static void main(String a[]) {
       Thread desktopServerThread = new Thread(new Server());
       desktopServerThread.start();

   }
}