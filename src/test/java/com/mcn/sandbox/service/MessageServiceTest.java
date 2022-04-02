package com.mcn.sandbox.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageServiceTest {

    @Test
    void testGetMessage() {
        Assertions.assertEquals("Hello World", new MessageService().getMessage());
    }
}