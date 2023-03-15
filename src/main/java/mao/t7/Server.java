package mao.t7;

import mao.utils.ByteBufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Project name(项目名称)：Netty_Net_Programming
 * Package(包名): mao.t7
 * Class(类名): Server
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2023/3/15
 * Time(创建时间)： 13:40
 * Version(版本): 1.0
 * Description(描述)： UDP
 */

public class Server
{
    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args)
    {
        try (DatagramChannel datagramChannel = DatagramChannel.open())
        {
            //绑定端口
            datagramChannel.bind(new InetSocketAddress(8080));
            log.debug("服务已启动");
            ByteBuffer buffer = ByteBuffer.allocate(16);
            //等待接收数据
            datagramChannel.receive(buffer);
            buffer.flip();
            ByteBufferUtil.debugAll(buffer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
