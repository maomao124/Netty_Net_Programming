package mao.t6;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Project name(项目名称)：Netty_Net_Programming
 * Package(包名): mao.t6
 * Class(类名): Client
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2023/3/14
 * Time(创建时间)： 23:14
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class Client
{
    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(mao.t6.Client.class);

    /**
     * main方法
     *
     * @param args 参数
     */
    public static void main(String[] args) throws IOException
    {
        while (true)
        {
            try
            {
                SocketChannel socketChannel = SocketChannel.open();
                socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
                Scanner input = new Scanner(System.in);
                input.nextLine();
                socketChannel.write(ByteBuffer.wrap("hello".getBytes(StandardCharsets.UTF_8)));
                input.nextLine();
                socketChannel.write(ByteBuffer.wrap("world".getBytes(StandardCharsets.UTF_8)));
                input.nextLine();
                socketChannel.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
