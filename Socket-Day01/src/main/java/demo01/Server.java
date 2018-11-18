package demo01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by qian on 2017/5/5.
 */
public class Server {
    public static void main(String[] args) {
        // 创建一个ServerSocker监听8080端口
        ServerSocket serverSocket = null;
        Socket socket = null;
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            serverSocket = new ServerSocket(8080);
            // 等待请求
            socket = serverSocket.accept();
            // 接受到请求后使用socker进行通信，创建BufferedReader用于读取数据
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = br.readLine();
            System.out.println("recevied from client : " + line);
            // PrintWriter 用于数据发送
            pw = new PrintWriter(socket.getOutputStream());
            pw.println("recevied data : " + line);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                pw.close();
                br.close();
                socket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
