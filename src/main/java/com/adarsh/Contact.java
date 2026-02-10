package com.adarsh;

public class Contact {

        private String name, phoneno, email;

        public Contact(){}

        public Contact(String name, String phoneno , String email){
            this.name= name;
            this.phoneno = phoneno;
            this.email=email;
        }
        //getters

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact c = (Contact) o;
        return name.equalsIgnoreCase(c.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }
        public String getName(){
            return name;
        }

    public String getPhoneno() {
        return phoneno;
    }

    public String getEmail(){
        return email;
    }
    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }
    public void setEmail(String email){
        this.email = email;
    }

    //to print objects
    public String toString(){
            return name+" | "+phoneno+" | "+email;
    }
}
