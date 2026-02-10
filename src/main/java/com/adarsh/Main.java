package com.adarsh;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class Main{
    public static void main(String [] args){
        ObjectMapper mapper = new ObjectMapper();
        FileContactStorage storage = new FileContactStorage(mapper);
        ContactService service = new ContactService(storage);
        Scanner sc =new Scanner(System.in);
        System.out.println("Welcome to Contact Manager");
        int a=0;
        while(a!=6)
        {
            System.out.println("1. Add a contact");
            System.out.println("2. Read all contacts");
            System.out.println("3. Search contact by name");
            System.out.println("4. Delete a contact");
            System.out.println("5. Update a contact");
            System.out.println("6. Exit");
            System.out.print("\tEnter your choice : ");

            String z=sc.nextLine();
            try{
                a=Integer.parseInt(z);

            }catch (NumberFormatException e){
                System.out.println("❌ Please enter a number between 1 to 6 .");
                continue;
            }

            switch (a)
            {
                case 1:
                    try{
                        System.out.println("Enter name of contact: ");
                        String name= sc.nextLine();
                        System.out.println("Enter phone no: ");
                        String phoneno= sc.nextLine();
                        System.out.println("Enter Email Address: ");
                        String email= sc.nextLine();
                        service.writecontact(name,phoneno,email);
                                System.out.println("Contact successfully written to file\n");

                    }
                    catch (IllegalArgumentException s)
                    {
                        System.out.println(s.getMessage());
                    }
                    catch (Exception e){
                        System.out.println(e);
                    }
                    break;
                case 2:
                    try
                    {
                        service.readcontact();
                    }catch (Exception e){
                        System.out.println(e);
                    }
                    break;
                case 3:
                    try
                    {
                        System.out.print("Enter name of contact you want to search: ");
                        String name = sc.nextLine();
                        if (!service.searchcontact(name).isEmpty()){
                            System.out.println("Contacts found : ");
                            service.searchcontact(name).stream()
                                    .sorted(Comparator.comparing(Contact::getName))
                                    .forEach(System.out::println);
                        }
                        else {
                            System.out.println("No matching contact found. ❌");
                        }
                    }catch (Exception e){
                        System.out.println(e);
                    }
                    break;
                case 4:
                    try{
                        String tempfullname="";
                        System.out.println("Enter the Contact name you want to delete : ");
                        String name = sc.nextLine();
                        List<Contact> templist = service.searchcontact(name);
                        if (templist.isEmpty()){
                            System.out.println("No matching contact found !");
                            continue;
                        }
                        System.out.println("Contacts found: ");
                        templist.forEach(System.out::println);
                        if (templist.size()==1) {
                            System.out.println("Is this the contact you wanna delete ?");
                            System.out.println("Enter y for yes and n for no :—");
                            String what = sc.nextLine();
                            tempfullname = switch (what){
                                case "y":
                                    yield templist.getFirst().getName();
                                case "n":
                                    yield null;
                                default:
                                    System.out.println("Enter 'y' or 'n' only !");
                                    yield null;
                            };

                        } else if (templist.size()>1) {
                            System.out.println("Enter the full name of contact you wanna delete : ");
                            tempfullname = sc.nextLine();}
                        boolean deleted = service.deletecontact(tempfullname);
                        System.out.println(deleted==true?"Contact deleted succesfully":deleted==false&&tempfullname==null?"Ok, the contact will not be deleted, BYE !":"Contact not found");
                    }catch (Exception e){
                        System.out.println(e);
                    }
                    break;
                case 5:
                    try{
                        System.out.println("Enter the Contact name you want to update");
                        String n = sc.nextLine();
                        String tempfullname="";
                        String what="";
                        List<Contact> matches = service.searchcontact(n);

                        if (matches.isEmpty()) {
                            System.out.println("No matching contact !");
                            continue;
                        }
                        else if(matches.size()>1){
                            System.out.println("Contacts found : ");
                            matches.forEach(System.out::println);
                            System.out.println("Enter the full name of contact you want to update: ");
                            tempfullname = sc.nextLine();}

                        else {
                            System.out.println("Is this the contact you want to update? ");
                            System.out.println(matches);
                            System.out.println("Enter y for yes and n for No ");
                            what = sc.nextLine();
                            tempfullname = switch (what){
                                case "y":
                                    yield matches.getFirst().getName();

                                case "n":
                                    yield null;
                                default:
                                    yield null;
                            };

                        }
                        if (tempfullname == null){
                            if (what.equalsIgnoreCase("n")) {
                                System.out.println("Ok, so you don't wanna update this contact , BYE !");
                                continue;
                            }else {
                                System.out.println("Enter 'y' or 'n' !");
                                continue;
                            }
                        }
                        System.out.println("Please enter the new details for the contact: ");
                        System.out.print("Enter name : ");
                        String newname = sc.nextLine();
                        System.out.print("Enter phone no: ");
                        String newphone = sc.nextLine();
                        System.out.print("Enter new Email: ");
                        String newemail = sc.nextLine();

                        if (!newphone.matches("\\d{10}")){
                            System.out.println("Invalid Phoneno format");
                            System.out.println("Must be 10 digits only.");
                            continue;
                        }
                        boolean updated = service.updatecontact(tempfullname,newname,newphone,newemail);

                        System.out.println(updated ?"Contact updated succesfully":"Contact not found");


                    }
                    catch (IllegalArgumentException e)
                    {
                        System.out.println(e.getMessage());

                    } catch(Exception e){
                        System.out.println(e);
                    }
                    break;
                case 6:
                    System.out.println("Bye !");
                    break;
                default:
                    System.out.println("Wrong Choice !");
            }
        }
    }
}