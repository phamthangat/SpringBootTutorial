package com.example.SpringTutorial.database;

import com.example.SpringTutorial.models.Product;
import com.example.SpringTutorial.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return new CommandLineRunner() { //CommandLineRunner là 1 interface -> tạo đối tượng để thực thi CommandLineRunner
            @Override
            public void run(String... args) throws Exception {
                Product productA = new Product("Macbook Pro", 2020, 2400.0,"");
                Product productB = new Product("Ipad Air", 2021, 599.0,"");
                logger.info("insert data :" + productRepository.save(productA));
                logger.info("insert data :" + productRepository.save(productB));
            }
        };
    }
}
