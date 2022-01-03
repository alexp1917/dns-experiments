package names.monolith;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Accessors(chain = true)
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class Properties {
    private String prefix = "";
}
