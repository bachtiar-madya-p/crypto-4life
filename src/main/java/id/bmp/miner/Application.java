package id.bmp.miner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@SpringBootApplication
@Component
public class Application {

    public static void main(String[] args) {

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jakarta"));
        SpringApplication.run(Application.class, args);
    }

}
