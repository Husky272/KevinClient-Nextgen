// Decompiled with: CFR 0.152
// Class Version: 8
package kevin.via;

import com.viaversion.viaversion.util.PipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public final class CommonTransformer {
    @NotNull
    public static final CommonTransformer INSTANCE = new CommonTransformer();
    @NotNull
    public static final String HANDLER_DECODER_NAME = "via-decoder";
    @NotNull
    public static final String HANDLER_ENCODER_NAME = "via-encoder";

    private CommonTransformer() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void decompress(@NotNull ChannelHandlerContext ctx, @NotNull ByteBuf buf) throws InvocationTargetException {
        ByteBuf byteBuf;
        // noinspection UnnecessaryLocalVariable
        ChannelHandler channelHandler = ctx.pipeline().get("decompress");
        // noinspection UnnecessaryLocalVariable
        ChannelHandler handler = channelHandler;
        Object e;
        if (handler instanceof MessageToMessageDecoder) {
            // noinspection rawtypes
            e = PipelineUtil.callDecode((MessageToMessageDecoder) handler, ctx, buf).get(0);
        } else {
            e = PipelineUtil.callDecode((ByteToMessageDecoder) handler, ctx, buf).get(0);
        }
        byteBuf = (ByteBuf) e;
        ByteBuf decompressed = byteBuf;
        try {
            buf.clear().writeBytes(decompressed);
        } finally {
            decompressed.release();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void compress(@NotNull ChannelHandlerContext ctx, @NotNull ByteBuf buf) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        try {
            ChannelHandler channelHandler = ctx.pipeline().get("compress");
            PipelineUtil.callEncode((MessageToByteEncoder) channelHandler, ctx, buf, byteBuf);
            buf.clear().writeBytes(byteBuf);
        } finally {
            byteBuf.release();
        }
    }
}
