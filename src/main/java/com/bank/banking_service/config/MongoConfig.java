package com.bank.banking_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {


    @Override
    protected String getDatabaseName() {
        return "banking_db";
    }
}
