package exercise.daytime;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Getter
public class Day implements Daytime {
    // BEGIN
    private String name = "day";

    // END
}
