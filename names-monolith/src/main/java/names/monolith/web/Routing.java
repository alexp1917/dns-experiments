package names.monolith.web;

import names.monolith.Properties;
import names.monolith.service.Zones;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class Routing {
    private static final String IW = "<h1>It Works</h1>";

    private final Zones zones;
    private final Properties properties;

    public Routing(Zones zones, Properties properties) {
        this.zones = zones;
        this.properties = properties;
    }

    private static Mono<ServerResponse> hello(ServerRequest req) {
        return ServerResponse.ok().bodyValue(IW);
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        String prefix = properties.getPrefix();
        return route()
                .GET(prefix, Routing::hello)
                .GET(prefix + "/zones", zones::getAll)
                .POST(prefix + "/zones", zones::addNew)
                .build();
    }
}
