package org.butterfly.rpc.component.mina;

import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
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
import org.butterfly.rpc.model.dto.RpcMsg;
import org.butterfly.rpc.model.enumeration.RpcMsgType;

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
    /**
     * 30秒后超时
     */
    private static final int IDELTIMEOUT = 15;
    /**
     * 15秒发送一次心跳包
     */
    private static final int HEARTBEATRATE = 1;
    /**
     * 心跳包内容
     */
    private static final String HEARTBEATREQUEST = "0x11";
    private static final String HEARTBEATRESPONSE = "0x12";

    public MinaServer(Serializer serializer, Deserializer deserializer) {
        super(serializer, deserializer);
        byteArrayCodecFactory = new ByteArrayCodecFactory(serializer, deserializer);

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
        KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl();
        //下面注释掉的是自定义Handler方式
        KeepAliveRequestTimeoutHandler heartBeatHandler = new
                KeepAliveRequestTimeoutHandlerImpl();

        //每间隔1秒发心跳包，5秒收不到响应就任务客户端挂了
        KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory,
                IdleStatus.WRITER_IDLE,heartBeatHandler, HEARTBEATRATE, IDELTIMEOUT);
        //KeepAliveFilter设置了RequestTimeout，这个设置会不生效
        //acceptor.getSessionConfig().setIdleTime();

        //设置是否forward到下一个filterddddddEEEeeeeeffff
        //heartBeat.setForwardEvent(true);
        acceptor.getFilterChain().addLast("heartbeat", heartBeat);

        try {
            //绑定端口,并且启动服务器，立刻返回，不会堵塞
            acceptor.bind(new InetSocketAddress(config.getPort()));

        } catch (IOException e) {
            throw e;
        }
        log.info("MINA服务器已经开启,端口号：" + config.getPort());
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
    protected void finalize() throws Throwable {
        log.info("mina server will close by finalize method");
        this.releaseResource();
    }

    private void releaseResource() {
        if (this.acceptor != null) {
            this.acceptor.unbind();
            this.acceptor.dispose();
        }

    }

    /**
     * 对应上面的注释	 * KeepAliveFilter(heartBeatFactory,
     * IdleStatus.BOTH_IDLE,heartBeatHandler)	 * 心跳超时处理	 * KeepAliveFilter
     * 在没有收到心跳消息的响应时，会报告给的KeepAliveRequestTimeoutHandler。
     * * 默认的处理是 KeepAliveRequestTimeoutHandler.CLOSE	 *
     * （即如果不给handler参数，则会使用默认的从而Close这个Session）
     * * @author cruise	 *
     */

    private class KeepAliveRequestTimeoutHandlerImpl implements KeepAliveRequestTimeoutHandler {
        @Override
        public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
            log.info("心跳超时！");
            session.closeNow();
        }

    }




private class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {
    @Override
    public boolean isRequest(IoSession session, Object message) {
        // log.info("请求心跳包信息: " + message);

        RpcMsg msg = (RpcMsg) message;
        if (msg.getHeader().getType() == RpcMsgType.HEARTBEAT_REQ.getCode())
            return true;
        return false;
    }

    @Override
    public boolean isResponse(IoSession session, Object message) {

        RpcMsg msg = (RpcMsg) message;
        if (msg.getHeader().getType() == RpcMsgType.HEARTBEAT_REQ.getCode()){
            log.info("收到客户端心跳回复: " + message);
            return true;
        }

        return false;
    }

    @Override
    public Object getRequest(IoSession session) {
        /** 返回预设语句 */
        return RpcMsg.heartBeatReqRpcMsg(String.valueOf(session.getId()), "111");
    }

    @Override
    public Object getResponse(IoSession session, Object request) {
        //服务端为了性能不发回复包
        return null;
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
