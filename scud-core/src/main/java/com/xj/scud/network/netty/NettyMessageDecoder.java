package com.xj.scud.network.netty;

import com.xj.scud.core.NetworkProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/02 16:49
 * 协议解码器
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {
    private final static Logger LOGGER = LoggerFactory.getLogger(NettyMessageDecoder.class);

    public NettyMessageDecoder() {
        super(Integer.MAX_VALUE, 7, 4, 0, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = null;
        try {
            frame = (ByteBuf) super.decode(ctx, in);
            if (frame != null) {
                return this.decode(frame, ctx);
            }
        } catch (Exception t) {
            LOGGER.error("Decode msg fail! remoteAddress:{}", ctx.channel().remoteAddress(), t);
            throw t;
        } finally {
            if (frame != null) {
                frame.release();
            }
        }
        return null;
    }

    protected NetworkProtocol decode(ByteBuf in, ChannelHandlerContext ctx) throws Exception {
        if (in.readableBytes() > 0) {
            byte head = in.readByte();
            if (head == 124) {
                byte version = in.readByte();
                byte type = in.readByte();
                int seq = in.readInt();
                int len = in.readInt();
                byte[] content = new byte[len];
                in.readBytes(content);
                //byte tail = in.readByte();
                // if (tail == 126) {
                NetworkProtocol protocol = new NetworkProtocol();
                protocol.setVersion(version);
                protocol.setType(type);
                protocol.setSequence(seq);
                protocol.setContent(content);
                return protocol;
                // } else {
                //     in.clear();
                //     LOGGER.error("Protocol tail parsing error ...");
                // }
            } else {
                in.clear();
                LOGGER.error("Protocol head parsing error ...");
            }
        }
        return null;
    }
}
