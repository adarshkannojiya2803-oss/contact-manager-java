
package com.adarsh;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileContactStorage implements ContactStorage {

    private final ObjectMapper mapper;
    public FileContactStorage(ObjectMapper mapper){
        this.mapper =mapper;
    }

    public FileContactStorage(){
        this.mapper =  new ObjectMapper();
    }
    private  final File file = new File("contacts.json");

    public  void saveContacts(List<Contact> contacts) throws Exception{
        mapper.writerWithDefaultPrettyPrinter().writeValue(file,contacts);
    }

    public  List<Contact> loadContacts() throws Exception{
        if (!file.exists() || file.length()==0){
            return new ArrayList<>();
        }
        return mapper.readValue(file, new TypeReference<List<Contact>>() {});
    }
}