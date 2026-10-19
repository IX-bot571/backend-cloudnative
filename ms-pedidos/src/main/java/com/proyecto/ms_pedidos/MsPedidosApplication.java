package com.proyecto.ms_pedidos;

import com.proyecto.ms_pedidos.config.RabbitNames;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RabbitNames.class)
public class MsPedidosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsPedidosApplication.class, args);
    }
}
