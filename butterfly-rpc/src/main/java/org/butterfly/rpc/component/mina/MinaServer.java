package org.butterfly.rpc.component.mina;

import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.butterfly.rpc.abs.Server;
import org.butterfly.rpc.abs.ServerConfig;
import org.butterfly.rpc.abs.codec.Deserializer;
import org.butterfly.rpc.abs.codec.Serializer;
import org.butterfly.rpc.component.AbstractServer;
import org.butterfly.rpc.component.codec.hessian.HessianDeserializer;
import org.butterfly.rpc.component.codec.hessian.HessianSerializer;
import org.butterfly.rpc.component.mina.encoder.ByteArrayCodecFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Timmy on 2019/10/26.
 */
@Slf4j
public class MinaServer extends AbstractServer {
    SocketAcceptor acceptor;
    ByteArrayCodecFactory byteArrayCodecFactory;

    public MinaServer(Serializer serializer, Deserializer deserializer){
        super(serializer,deserializer);
        byteArrayCodecFactory = new ByteArrayCodecFactory(serializer,deserializer);

    }
    @Override
    public ServerConfig getConfig() {
        return super.getConfig();
    }

    @Override
    protected void doInit() throws Throwable {
        super.doInit();
    }

    @Override
    protected void doStart() throws Throwable {
        final ServerConfig config = this.config;
        //创建一个非堵塞的Server(Socker),[NIO模式]
        acceptor = new NioSocketAcceptor();
        //创建一个过滤器对象
       DefaultIoFilterChainBuilder filterChain = acceptor.getFilterChain();
        //设定一个过滤器，一行一行的读取数据(/r/n)

        ProtocolCodecFilter filter = new ProtocolCodecFilter(byteArrayCodecFactory);
        filterChain.addLast("codec", filter);
        //chain.addLast("myChain",new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));//表示传数据的数据是一个对象;
        //chain.addLast("myChain",new ProtocolCodecFilter(new TextLineCodecFactory()));
        acceptor.getSessionConfig().setReadBufferSize(50);
        acceptor.setHandler(new MinaServerHandler(this.getConfig()));
        try {
            //绑定端口,并且启动服务器，立刻返回，不会堵塞
            acceptor.bind(new InetSocketAddress(config.getPort()));

        } catch (IOException e) {
            throw e;
        }
       log.info("MINA服务器已经开启,端口号："+config.getPort());
    }

    @Override
    protected void doStop() throws Throwable {
        this.releaseResource();
    }

    @Override
    public StatusInfo status() {
        return super.status();
    }

    @Override
    protected  void  finalize() throws Throwable{
        log.info("mina server will close by finalize method");
        this.releaseResource();
    }
    private void releaseResource(){
        if(this.acceptor != null){
            this.acceptor.unbind();
            this.acceptor.dispose();
        }

    }
    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Server server = new MinaServer(new HessianSerializer(), new HessianDeserializer());
        ServerConfig config = new MinaServerConfig("测试mina服务器", 8888);
        server.init(config);
        server.start();
        countDownLatch.await();
    }
}
