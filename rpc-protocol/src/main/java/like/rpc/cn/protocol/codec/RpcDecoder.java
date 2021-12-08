package like.rpc.cn.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import like.rpc.cn.protocol.SerializationUtil;

import java.util.List;

/**
 * rpc解码器
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/6 18:23
 */
public class RpcDecoder<T> extends ByteToMessageDecoder {

    private Class<T> clazz;

    public RpcDecoder(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }

        in.markReaderIndex();
        int dataLength = in.readInt();

        // if (dataLength <= 0) {
        //     ctx.close();
        // }

        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        T obj = SerializationUtil.deserialize(data, clazz);
        out.add(obj);
    }
}
