package demo01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by qian on 2017/5/5.
 */
public class Client {
    public static void main(String[] args) {
        String msg = "Client Data";
        Socket socket = null;
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        try {
            // 创建一个Socket，与本机的8080端口进行连接
            socket = new Socket("localhost", 8080);
            // 使用PrintWriter和BufferedReader进行写数据
            printWriter = new PrintWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 发送数据
            printWriter.println(msg);
            printWriter.flush();
            // 接受数据
            String line = bufferedReader.readLine();
            System.out.println("received from server : " + line);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                printWriter.close();
                bufferedReader.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
