package fpt.capstone.buildingmanagementsystem;

import fpt.capstone.buildingmanagementsystem.service.InitializationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BuildingManagementSystemApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BuildingManagementSystemApplication.class);
        InitializationService initDB = context.getBean(InitializationService.class);
        initDB.init();
    }

}