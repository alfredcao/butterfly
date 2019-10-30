package org.butterfly.rpc.component.mina.encoder;

import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.proxy.utils.ByteUtilities;
import org.butterfly.common.util.CheckUtil;
import org.butterfly.rpc.abs.codec.Deserializer;
import org.butterfly.rpc.abs.codec.Serializer;
import org.butterfly.rpc.model.dto.RpcMsg;

import java.nio.charset.StandardCharsets;


/**
 * Created by HUAWEI on 2019/10/26.
 */
@Slf4j
public class SimpleCumulaBytesDecoder extends CumulativeProtocolDecoder  {
    private Deserializer deserializer;

    public SimpleCumulaBytesDecoder(Deserializer deserializer){
        if(deserializer == null){
            // TODO 提供默认序列化器
        }
        CheckUtil.checkNotNull(deserializer, "序列化器");
        this.deserializer = deserializer;
    }


    /**
     * 这个方法的返回值是重点：
     * 1、当内容刚好时，返回 false，告知父类接收下一批内容
     *
     * 2、内容不够时需要下一批发过来的内容，此时返回 false，
     * 这样父类 CumulativeProtocolDecoder 会将内容放进 IoSession 中，
     * 等下次来数据后就自动拼装再交给本类的 doDecode
     *
     * 3、当内容多时，返回 true，因为需要再将本批数据进行读取，
     * 父类会将剩余的数据再次推送本类的doDecode
     */
    @Override
    public boolean doDecode(IoSession session, IoBuffer in,
                            ProtocolDecoderOutput out) throws Exception {
        int dataHeadLength = 4;
        if(in.remaining() > dataHeadLength){  //说明缓冲区中有数据
           //标记当前position，以便后继的reset操作能恢复position位置
            byte [] sizeBytes = new byte[dataHeadLength];
            //上面的get会改变remaining()的值
            in.mark();
            in.get(sizeBytes);//读取前4字节
            int dataLength= BytesUtil.bytes2Int(sizeBytes);
            if(in.remaining() <dataLength) {
                //内容不够， 重置position到操作前，进行下一轮接受新数据
                in.reset();
                return false;
            }else{
                //内容足够
                byte[] packArray = new byte[dataLength];
                in.get(packArray, 0, dataLength); //获取整条报文
                RpcMsg msg = deserializer.deserialize(packArray,RpcMsg.class);
                out.write(msg);
                //out.write(packArray); //发送出去 就算完成了
                if(in.remaining() > 0){//如果读取一个完整包内容后还粘了包，就让父类再调用一次，进行下一次解析
                    return true;
                }
            }
        }
        in.position(1);
        return false;
    }
}
