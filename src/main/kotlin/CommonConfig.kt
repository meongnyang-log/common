import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@AutoConfiguration
@EnableJpaRepositories(basePackages = ["auth"])
@EntityScan(basePackages = ["auth"])
@ComponentScan(basePackages = ["auth", "config", "error"])
class CommonConfig
