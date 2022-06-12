package com.mcn.ddb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;

@DynamoDbBean
public class Customer {

    private String id;
    private String name;
    private String email;
    private Instant regDate;

    @DynamoDbPartitionKey
    public String getId() {
        return this.id;
    };

    public void setId(String id) {

        this.id = id;
    }

    public String getCustName() {
        return this.name;
    }

    public void setCustName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getRegistrationDate() {
        return regDate;
    }
    public void setRegistrationDate(Instant registrationDate) {

        this.regDate = registrationDate;
    }
}
