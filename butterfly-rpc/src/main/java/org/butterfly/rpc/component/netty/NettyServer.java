package org.butterfly.rpc.component.netty;

import io.netty.channel.EventLoopGroup;
import org.butterfly.rpc.component.AbstractServer;

/**
 * netty服务器
 * @author alfredcao
 * @date 2019-10-11 23:00
 */
public class NettyServer extends AbstractServer {
    private EventLoopGroup boss;
    private EventLoopGroup worker;

    @Override
    protected void doStart() throws Throwable {

    }

    @Override
    protected void doStop() throws Throwable {

    }
}
