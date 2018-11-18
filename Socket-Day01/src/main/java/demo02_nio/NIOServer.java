package demo02_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created by qian on 2017/5/5.
 */
public class NIOServer {

    private static ServerSocketChannel serverSocketChannel;
    private static Selector selector;

    public static void main(String[] args) {
        try {
            // 创建ServerSocketChannel 监听端口
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(8080));
            // 设置阻塞形式 ： false为阻塞式
            serverSocketChannel.configureBlocking(false);
            // 设置为非阻塞模式
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            // 创建处理器
            Handler handler = new Handler(1024);
            while (true) {
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Handler {
        private int bufferSize = 1024;
        private String localCharset = "UTF-8";

        public Handler() {
        }

        public Handler(int bufferSize) {
            this(bufferSize, null);
        }

        public Handler(String localCharset) {
            this(-1, localCharset);
        }

        public Handler(int bufferSize, String localCharset) {
            if (bufferSize > 0) {
                this.bufferSize = bufferSize;
            }
            if (localCharset != null) {
                this.localCharset = localCharset;
            }
        }

        public void handleAccept(SelectionKey key) {
            // 获取channel
            SocketChannel socketChannel = (SocketChannel) key.channel();
            // 获取buffer并且进行重置
            ByteBuffer buffer = (ByteBuffer) key.attachment();
            buffer.clear();
            try {
                // 如果没有读取到内容进行关闭
                if (socketChannel.read(buffer) == -1) {
                    socketChannel.close();
                } else {
                    // 将buffer转换为读状态
                    buffer.flip();
                    // 将buffer中接收到的值按照localCharset格式编码后保存到receivedString
                    String receivedString = Charset.forName(localCharset).newDecoder()
                            .decode(buffer).toString();
                    System.out.println("received from client : " + receivedString);

                    // 返回给客户端的数据
                    String sendString = "received data : " + receivedString;
                    buffer = ByteBuffer.wrap(sendString.getBytes(localCharset));
                    socketChannel.write(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
