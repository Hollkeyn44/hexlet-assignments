package exercise.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "users.admins")
@Getter
@Setter
public class UserProperties {
    private List<String> emails;
    private List<String> names;
}
