package names;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.dns.DatagramDnsQuery;
import lombok.extern.slf4j.Slf4j;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;


@Slf4j
public class NioServer {
    public static void main(String[] args) {
        new NioServer().listen(8053);
    }

    public void listen(int port) {
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
                    protected void initChannel(NioDatagramChannel ch) {
                        // ch.pipeline().addLast(new DatagramDnsQueryDecoder());
                        // ch.pipeline().addLast(new DatagramDnsQueryEncoder());
                        // ch.pipeline().addLast(new DNSMessageHandler());
                        ch.pipeline().addLast(new ExampleUdpHandler());
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

    /** echo server */
    public static class ExampleUdpHandler extends SimpleChannelInboundHandler<DatagramPacket> {

        public ExampleUdpHandler() {
            super(DatagramPacket.class, true);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
            InetSocketAddress newDest = msg.sender();
            InetSocketAddress newSender = msg.recipient();
            ctx.writeAndFlush(new DatagramPacket(msg.content().retain(),
                    newDest,
                    newSender));
        }
    }

    public static class DNSMessageHandler extends SimpleChannelInboundHandler<DatagramDnsQuery> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramDnsQuery msg) throws Exception {
            Record aRecord = ARecord.newRecord(Name.fromString("localhost."),
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
