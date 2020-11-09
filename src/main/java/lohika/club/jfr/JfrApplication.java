package lohika.club.jfr;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import lohika.club.jfr.model.User;
import lohika.club.jfr.repository.UserRepository;
import lohika.club.jfr.service.PasswordManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.metrics.jfr.FlightRecorderApplicationStartup;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@SpringBootApplication
@EnableCaching
public class JfrApplication {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordManager passwordManager;

    @Value("${users.count:10}")
    int usersCount;

    public static void main(String[] args) {
        startRedis();
        startPostgresql();
        System.getProperties().list(System.out);

        new SpringApplicationBuilder()
                .applicationStartup(new FlightRecorderApplicationStartup())
                .sources(JfrApplication.class)
                .build()
                .run(args);

    }

    @EventListener(ContextRefreshedEvent.class)
    public void generateFakedUsers() {
        log.info("Generation users [count={}]", usersCount);
        Faker faker = new Faker();
        userRepository.saveAll(
                IntStream.range(0, usersCount)
                        .parallel()
                        .mapToObj(index -> {
                                    Name name = faker.name();
                                    return User.builder()
                                            .firstName(name.firstName())
                                            .lastName(name.lastName())
                                            .username("user" + index)
                                            .password(passwordManager.createHash(name.username()))
                                            .userUuid(UUID.randomUUID().toString())
                                            .build();
                                }
                        ).collect(Collectors.toList())
        );
    }

    private static void startPostgresql() {
        var postgreSQLContainer = new PostgreSQLContainer("postgres:11")
                .withDatabaseName("javaclub")
                .withUsername("javadev")
                .withPassword("secret");

        postgreSQLContainer.start();
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
    }

    private static void startRedis() {
        var redis = new GenericContainer<>(DockerImageName.parse("redis:latest"));
        redis.start();
        System.setProperty("spring.redis.port", Integer.toString(redis.getMappedPort(6379)));
    }

}
