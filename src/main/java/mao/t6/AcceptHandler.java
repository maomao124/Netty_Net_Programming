package mao.t6;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Project name(项目名称)：Netty_Net_Programming
 * Package(包名): mao.t6
 * Class(类名): AcceptHandler
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2023/3/14
 * Time(创建时间)： 22:23
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class AcceptHandler implements Runnable
{

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(AcceptHandler.class);

    /**
     * 选择器
     */
    private Selector selector;

    /**
     * 工人处理程序
     */
    private WorkerHandler[] workerHandlers;

    /**
     * 是否已经注册
     */
    private volatile boolean isRegister = false;

    /**
     * 原子Long，cas方式累加
     */
    private final AtomicLong atomicLong = new AtomicLong(0);

    /**
     * 注册
     *
     * @param port 端口号
     * @throws IOException ioexception
     */
    public void register(int port) throws IOException
    {
        //判断是否已经注册过
        if (!isRegister)
        {
            //还没有注册过
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //绑定端口
            serverSocketChannel.bind(new InetSocketAddress(port));
            //非阻塞
            serverSocketChannel.configureBlocking(false);
            //创建Selector
            selector = Selector.open();
            //注册到Selector
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            //创建WorkerHandler
            workerHandlers = initWorkerHandlers();
            log.debug("服务启动");
            new Thread(this, "Accept").start();
            isRegister = true;
        }
    }

    public WorkerHandler[] initWorkerHandlers()
    {
        int processors = Runtime.getRuntime().availableProcessors();
        log.debug("线程数量：" + processors);
        WorkerHandler[] workerHandlers = new WorkerHandler[processors];
        for (int i = 0; i < processors; i++)
        {
            log.debug("初始化WorkerHandler" + i);
            workerHandlers[i] = new WorkerHandler(i);
        }
        return workerHandlers;
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                int select = selector.select();
                //iterator
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext())
                {
                    //得到selectionKey
                    SelectionKey selectionKey = iterator.next();

                    //连接事件
                    if (selectionKey.isAcceptable())
                    {
                        try
                        {
                            //得到ServerSocketChannel
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                            log.debug("注册事件：" + serverSocketChannel);
                            //得到SocketChannel
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            //非阻塞
                            socketChannel.configureBlocking(false);
                            //轮询负载均衡注册到每个workerHandler
                            workerHandlers[Math.toIntExact((atomicLong.getAndIncrement() %
                                    workerHandlers.length))].register(socketChannel);
                        }
                        catch (Exception e)
                        {
                            selectionKey.cancel();
                        }
                    }
                    //移除
                    iterator.remove();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
