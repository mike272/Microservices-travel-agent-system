//package com.rsww.hotelService.config;
//
//import javax.sql.DataSource;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//
//@Configuration
//public class DataSourceConfig
//{
//
//    final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);
//
//    private String url = "jdbc:postgresql://postgres:5432/postgres";
//
//    private String username = "postgres";
//
//    private String password = "password";
//
//    @Bean
//    public DataSource dataSource()
//    {
//        logger.info("Creating datasource with url: {}", url);
//        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        return dataSource;
//    }
//}
