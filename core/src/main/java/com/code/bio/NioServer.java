package com.code.bio;

import cn.hutool.log.StaticLog;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

    public static void main(String[] args) throws IOException {
//        Logger logger = LoggerFactory.getLogger(SimpleServer.class);

        //创建服务端channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置channel非阻塞
        serverSocketChannel.configureBlocking(false);
        //获得selector
        Selector selector = Selector.open();
        //把channel注册到selector上,现在还没有给key设置感兴趣的事件
        SelectionKey selectionKey = serverSocketChannel.register(selector, 0, serverSocketChannel);
        //给key设置感兴趣的事件
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
        //绑定端口号
        serverSocketChannel.bind(new InetSocketAddress(8080));
        //然后开始接受连接,处理事件,整个处理都在一个死循环之中
        while (true) {
            //当没有事件到来的时候，这里是阻塞的,有事件的时候会自动运行
            selector.select();
            //如果有事件到来，这里可以得到注册到该selector上的所有的key，每一个key上都有一个channel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //得到集合的迭代器
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                //得到每一个key
                SelectionKey key = keyIterator.next();
                //首先要从集合中把key删除，否则会一直报告该key
                keyIterator.remove();
                //接下来就要处理事件，判断selector轮询到的是什么事件，并根据事件作出回应
                //如果是连接事件
                if (key.isAcceptable()) {
                    //得到服务端的channel,这里有两种方式获得服务端的channel，一种是直接获得,一种是通过attachment获得
                    //因为之前把服务端channel注册到selector上时，同时把serverSocketChannel放进去了
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    //ServerSocketChannel attachment = (ServerSocketChannel)key.attachment();
                    //得到客户端的channel ps:可以理解为 通讯端channel 类似socket类，这个Socket如果是从ServerSocket拿到的对象，那就是与服务器连接的那个Socket，如果是自己创建的Socket对象，那你就是客户端。
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);
                    //接下来就要管理客户端的channel了，和服务端的channel的做法相同，客户端的channel也应该被注册到selector上
                    //通过一次次的轮询来接受并处理channel上的相关事件
                    //把客户端的channel注册到之前已经创建好的selector上
                    SelectionKey socketChannelKey = socketChannel.register(selector, 0, socketChannel);
                    //给客户端的channel设置可读事件
                    socketChannelKey.interestOps(SelectionKey.OP_READ);
                    StaticLog.info("[{}] 客户端连接成功！", Thread.currentThread().getName());
                    //连接成功之后，用客户端的channel写回一条消息
                    socketChannel.write(ByteBuffer.wrap("我发送成功了".getBytes()));
                    StaticLog.info("[{}] 向客户端发送数据成功！", Thread.currentThread().getName());

                }
                //如果接受到的为可读事件，说明要用客户端的channel来处理
                if (key.isReadable()) {
                    //同样有两种方式得到客户端的channel，这里只列出一种
                    SocketChannel channel = (SocketChannel)key.channel();
                    //分配字节缓冲区来接受客户端传过来的数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    //向buffer写入客户端传来的数据
                    int len = channel.read(buffer);
                    StaticLog.info("[{}] 读到的字节数={}", Thread.currentThread().getName(),len);
                    if (len == -1) {
                        channel.close();
                        break;
                    }else{
                        //切换buffer的读模式
                        buffer.flip();
                        StaticLog.info("[{}] read={}", Thread.currentThread().getName(), Charset.defaultCharset().decode(buffer).toString());

                    }
                }
            }
        }
    }
}

