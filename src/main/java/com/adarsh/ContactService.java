package com.adarsh;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ContactService {
    private ContactStorage storage;

    public ContactService(ContactStorage storage){
        this.storage = storage;
    }
    List<Contact> contacts;

    public boolean writecontact(String name,String phoneno, String email) throws Exception {
        if (!phoneno.matches("\\d{10}") /*|| phoneno.chars().allMatch(Character::isDigit)*/){
            throw new IllegalArgumentException("Invalid phone no format!");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format!");
        }
        contacts = storage.loadContacts();
        if (contacts==null){
            contacts = new ArrayList<>();
        }
        contacts.stream()
                .filter(n->n.getName().equalsIgnoreCase(name))
                .findFirst()
                .ifPresent(n-> {
                    throw new IllegalArgumentException("Contact with same name already exist! "+n);
                });
        contacts.add(new Contact(name,phoneno,email));

        storage.saveContacts(contacts);

        return true;
    }
//method to read all contacts
    public  void readcontact() throws Exception {
        List<Contact> contactlist = storage.loadContacts();
        if (contactlist == null||contactlist.isEmpty()  ){
            System.out.println("Please add a contact first !");
            return;
        }
        System.out.println("\t\tContacts");
        contactlist.sort(Comparator.comparing(Contact::getName,String.CASE_INSENSITIVE_ORDER));
        contactlist.forEach(System.out::println);

    }

    //method to search contacts by accepting user input
    public List<Contact> searchcontact(String name) throws Exception {
        String search=name.toLowerCase();
        List<Contact> searchfrom = storage.loadContacts();
        if (searchfrom.isEmpty()){
            return List.of();
        }

        return searchfrom.stream()
                .filter(n->n.getName().toLowerCase().contains(search))
                .toList();

    }

    //method to delete a contact
    public  boolean deletecontact(String tempfullname) throws Exception{
        List <Contact> contactslist = storage.loadContacts();
        String fullname;
        if (contactslist.isEmpty()){
            return false;
        }

            if (tempfullname == null)return false;
            fullname = tempfullname;
            boolean found = contactslist.removeIf(c -> c.getName().equalsIgnoreCase(fullname));
            if (found) {
                storage.saveContacts(contactslist);
                return true;
            } else {
                return false;            }

    }

    //method to update contact
    public  boolean updatecontact(String tempfullname, String newname,String newphoneno, String newemail) throws Exception{

        List<Contact> actualcontacts = storage.loadContacts();
        if (actualcontacts==null || actualcontacts.isEmpty()){
            return false;
        }Contact found = actualcontacts.stream()
                .filter(d -> d.getName().equalsIgnoreCase(tempfullname))
                .findFirst()
                .orElse(null);

            if (found == null)
                return false;
            found.setName(newname);
            found.setPhoneno(newphoneno);
            found.setEmail(newemail);
            storage.saveContacts(actualcontacts);
            return true;

        }


}
