package org.butterfly.rpc.component.mina;

import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.butterfly.rpc.abs.Client;
import org.butterfly.rpc.abs.ClientConfig;
import org.butterfly.rpc.component.AbstractClient;
import org.butterfly.rpc.component.mina.encoder.ByteArrayCodecFactory;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Timmy on 2019/10/26.
 */
@Slf4j
public class MinaClient extends AbstractClient {
    private IoConnector connector;
    private static IoSession session;
    @Override
    protected void doInit() throws Throwable {

    }

    @Override
    protected void doConnect() throws Throwable {
        //创建一个过滤器对象
        connector = new NioSocketConnector();
        connector.setHandler(new MinaClientHandler(this.getConfig()));
        DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();

        ProtocolCodecFilter filter = new ProtocolCodecFilter( new ByteArrayCodecFactory());
        filterChain.addLast("codec", filter);

        ConnectFuture connFuture = connector.connect(
                new InetSocketAddress(this.config.getServerAddress(), this.config.getServerPort()));
        connFuture.awaitUninterruptibly();
        session = connFuture.getSession();
        log.info("mina客户端启动，端口{}",this.config.getServerPort());

    }
    private void releaseResource(){
        if(this.connector != null){
            this.connector.dispose();
        }
        if(this.session != null){
            this.session=null;
        }
    }
    @Override
    protected void doDisconnect() throws Throwable {
        this. releaseResource();
    }

    @Override
    public void send(byte[] msg) throws Exception {
        WriteFuture future = session.write(msg);
        future.addListener(new IoFutureListener<WriteFuture>() {
            // write操作完成后调用的回调函数
            @Override
            public void operationComplete(WriteFuture future) {
                if (future.isWritten()) {
                    //System.out.println("write操作成功");
                } else {

                    log.info("write操作失败");
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Client client = new MinaClient();
        ClientConfig config = new MinaClientConfig("mina测试客户端", "127.0.0.1", 8888);
        client.init(config);
        client.connect();
        //小包测试是否粘包
        for (int i = 0; i < 1000; i++) {
            String str = "hello" + i;
            //client.send(str.getBytes(StandardCharsets.UTF_8));
        }

        //大包测试是否半包,缓冲区设置小
        for (int i = 0; i < 1000; i++) {
            String str = "[hello1111122222333334444455555]" + i;
            client.send(str.getBytes(StandardCharsets.UTF_8));
            log.info("client send " + str);
        }
        countDownLatch.await();
        client.disconnect();
    }
}
