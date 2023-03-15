package mao.t7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

/**
 * Project name(项目名称)：Netty_Net_Programming
 * Package(包名): mao.t7
 * Class(类名): Client
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2023/3/15
 * Time(创建时间)： 13:41
 * Version(版本): 1.0
 * Description(描述)： UDP
 */

public class Client
{
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args)
    {
        try (DatagramChannel datagramChannel = DatagramChannel.open())
        {
            ByteBuffer buffer = StandardCharsets.UTF_8.encode("hello");
            datagramChannel.send(buffer, new InetSocketAddress("127.0.0.1", 8080));
            log.debug("消息已发送");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
