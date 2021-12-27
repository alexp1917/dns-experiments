package names;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.dns.*;
import io.netty.resolver.AddressResolver;
import io.netty.resolver.AddressResolverGroup;
import io.netty.util.concurrent.EventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;


@Slf4j
public class NioServer {
    public void listen(int port) {
        // Bootstrap b = new Bootstrap();
        // ChannelFuture bindFuture = b.bind(port);
        //
        // bindFuture.addListener((Future<Void> cf) -> {
        //
        // });

        AddressResolverGroup<InetSocketAddress> group1 =
                new AddressResolverGroup<>() {
            @Override
            protected AddressResolver<InetSocketAddress> newResolver(EventExecutor executor) throws Exception {
                return null;
            }
        };

        EventLoopGroup group = new NioEventLoopGroup(5,
                Executors.defaultThreadFactory());
        Bootstrap b = new Bootstrap()
                .group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_RCVBUF, 8192)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(8192))
                .option(ChannelOption.AUTO_CLOSE, true)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<NioDatagramChannel>() {

                    @Override
                    protected void initChannel(NioDatagramChannel ch) throws Exception {
                        ch.pipeline().addLast(new DatagramDnsQueryDecoder());
                        ch.pipeline().addLast(new DatagramDnsQueryEncoder());
                        ch.pipeline().addLast(new DNSMessageHandler());
                    }
                });
        ChannelFuture futureBindUDP = b.bind(port);
        futureBindUDP.addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                log.info("UDP Channel bound");
            } else {
                log.info("UDP bind attempt failed");

            }

        });
    }

    public static class DNSMessageHandler extends SimpleChannelInboundHandler<DatagramDnsQuery> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramDnsQuery msg) throws Exception {
            Record aRecord = ARecord.newRecord(Name.fromString("localhost"),
                    1, 1);

            byte[] bytes = aRecord.toWireCanonical();

            ctx.writeAndFlush(bytes);
            // DatagramDnsResponse response = new DatagramDnsResponse(msg.recipient(), msg.sender(), msg.id());
            // ByteBuf buf = Unpooled.buffer();
            // buf.writeShort(0); // priority
            // buf.writeShort(0); // weight
            // buf.writeShort(993); // port
            // encodeName("my.domain.tld", buf); // target (special encoding: https://tools.ietf.org/html/rfc1101)
            // response.addRecord(DnsSection.ANSWER,
            //         new DefaultDnsRawRecord(
            //                 /* requested domain */
            //                 "_myprotocol._tcp.domain.tld.",
            //                 DnsRecordType.SRV,
            //                 /* ttl */
            //                 30,
            //                 buf));
            //
            //
            // ctx.writeAndFlush(response);
        }
    }
}
