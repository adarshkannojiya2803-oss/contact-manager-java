package com.adarsh;

import java.util.List;

public interface ContactStorage {
    void saveContacts(List<Contact> contacts) throws Exception;
    List<Contact> loadContacts() throws Exception;
}
