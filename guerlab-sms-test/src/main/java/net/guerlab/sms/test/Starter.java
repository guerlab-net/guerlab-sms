package net.guerlab.sms.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.guerlab.sms.server.annotation.EnableSmsServer;

@SpringBootApplication
@EnableSmsServer
public class Starter {

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
}
