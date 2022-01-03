package names.monolith.service;

import names.monolith.entity.Zone;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@Component
public class Zones {
    private final List<Zone> data;

    public Zones() {
        data = new ArrayList<>();
    }

    public Mono<ServerResponse> getAll(ServerRequest req) {
        return ServerResponse.ok().bodyValue(data);
    }

    public Mono<ServerResponse> addNew(ServerRequest req) {
        Mono<Zone> zoneMono = req.bodyToMono(Zone.class);
        return ServerResponse.ok().body(zoneMono.map(this::addNew_), Zone.class);
    }

    private Zone addNew_(Zone zone) {
        for (Zone datum : data) {
            if (datum.getName().equals(zone.getName())) {
                return zone;
            }
        }

        data.add(zone);
        return zone;
    }
}
