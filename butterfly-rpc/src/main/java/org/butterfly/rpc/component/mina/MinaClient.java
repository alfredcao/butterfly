package org.butterfly.rpc.component.mina;

import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.butterfly.rpc.abs.Client;
import org.butterfly.rpc.abs.ClientConfig;
import org.butterfly.rpc.abs.codec.Deserializer;
import org.butterfly.rpc.abs.codec.Serializer;
import org.butterfly.rpc.component.AbstractClient;
import org.butterfly.rpc.component.codec.hessian.HessianDeserializer;
import org.butterfly.rpc.component.codec.hessian.HessianSerializer;
import org.butterfly.rpc.component.mina.encoder.ByteArrayCodecFactory;
import org.butterfly.rpc.model.dto.RpcHeader;
import org.butterfly.rpc.model.dto.RpcMsg;
import org.butterfly.rpc.model.enumeration.RpcMsgType;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Timmy on 2019/10/26.
 */
@Slf4j
public class MinaClient extends AbstractClient {
    private IoConnector connector;
    private ProtocolCodecFilter filter;
    private static IoSession session;

    public MinaClient(Serializer serializer, Deserializer deserializer) {
        super(serializer, deserializer);
        filter = new ProtocolCodecFilter( new ByteArrayCodecFactory(serializer,deserializer));
    }
    @Override
    protected void doInit() throws Throwable {

    }

    @Override
    protected void doConnect() throws Throwable {
        //创建一个过滤器对象
        connector = new NioSocketConnector();
        connector.setHandler(new MinaClientHandler(this.getConfig()));
        DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();

        //ProtocolCodecFilter filter = new ProtocolCodecFilter( new ByteArrayCodecFactory());
        filterChain.addLast("codec", filter);
        KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryClientImpl();
        KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory);
        filterChain.addLast("heartbeat", heartBeat);

        connector.getFilterChain().addFirst("reconnection", new IoFilterAdapter() {
            @Override
            public void sessionClosed(NextFilter nextFilter, IoSession ioSession) throws Exception {
                for (; ; ) {
                    try {
                        Thread.sleep(3000);
                        ConnectFuture future = connector.connect();
                        future.awaitUninterruptibly();// 等待连接创建成功
                        session = future.getSession();
                        // 获取会话
                        if (session.isConnected()) {
                            log.info("断线重连["+ connector.getDefaultRemoteAddress() +":"+
                             connector.getDefaultRemoteAddress()+"]成功");
                            break;
                        }
                    } catch (Exception ex) {
                        log.info("重连服务器登录失败,3秒再连接一次:" + ex.getMessage());
                    }
                }
            }
        });

        connector.setDefaultRemoteAddress(
                new InetSocketAddress(this.config.getServerAddress(), this.config.getServerPort()));// 设置默认访问地址
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

    @Override
    public void send(RpcMsg rpcMsg) throws Exception {
        WriteFuture future = session.write(rpcMsg);
        future.addListener(new IoFutureListener<WriteFuture>() {
            // write操作完成后调用的回调函数
            @Override
            public void operationComplete(WriteFuture future) {
                if (future.isWritten()) {
                } else {

                    log.info("write操作失败");
                }
            }
        });
    }
    private class KeepAliveMessageFactoryClientImpl implements KeepAliveMessageFactory {
        @Override
        public boolean isRequest(IoSession session, Object message) {
            RpcMsg msg = (RpcMsg) message;
            if (msg.getHeader().getType() == RpcMsgType.HEARTBEAT_REQ.getCode())
                return true;
            return false;
        }

        @Override
        public boolean isResponse(IoSession session, Object message) {
            return false;
        }

        @Override
        public Object getRequest(IoSession session) {
            //log.info("发送心跳给客户端: " );
            /** 返回预设语句 */
           // return RpcMsg.heartBeatReqRpcMsg(String.valueOf(session.getId()), "111");
            return null;
        }

        @Override
        public Object getResponse(IoSession session, Object request) {
            //服务端为了性能不发回复包
            /** 返回预设语句 */
            return RpcMsg.heartBeatReqRpcMsg(String.valueOf(session.getId()), "111");
        }

    }


    private static RpcMsg getTestMsg(String str){
        RpcMsg rpcMsg = new RpcMsg();
        RpcHeader rpcHeader = new RpcHeader();
        rpcHeader.setType((byte)3);
        rpcHeader.setServiceId("serviceId");
        rpcHeader.setSessionId("sessionId");
        rpcHeader.setRequestId("requestId");
        Map<String, Object> extendAttributes = new HashMap<>();
        extendAttributes.put("key1", "value_" + str);
        rpcHeader.setExtendAttributes(extendAttributes);
        rpcMsg.setHeader(rpcHeader);
        rpcMsg.setBody("body"+str);
        return rpcMsg;
    }

    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Client client = new MinaClient(new HessianSerializer(), new HessianDeserializer());
        ClientConfig config = new MinaClientConfig("mina测试客户端", "127.0.0.1", 8888);
        client.init(config);
        client.connect();
        //小包测试是否粘包
        for (int i = 0; i < 1000; i++) {
            String str = "hello" + i;
        }
        //大包测试是否半包,缓冲区设置小
        for (int i = 0; i < 10000; i++) {
            String str = "[中文编码hello1111122222333334444455555]" + i;
            client.send(getTestMsg(str));
            Thread.sleep(1000);
        }
        countDownLatch.await();
        client.disconnect();
    }
}
