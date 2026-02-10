package com.adarsh;


import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ContactServiceTest {

    @InjectMocks
    ContactService service;

    @Mock
    ContactStorage storage;

    @Test
    void writeContact_shouldSaveContact() throws Exception{
       when(storage.loadContacts()).thenReturn(new ArrayList<>());
       boolean saved= service.writecontact("Adarsh","9586745210","adarsh@gmail.com");
       assertTrue(saved);
       verify(storage).loadContacts();
       verify(storage,times(1)).saveContacts(any(List.class));
    }
    @Test
    void testAddContact() throws Exception{
        when(storage.loadContacts()).thenReturn(new ArrayList<>());
        assertDoesNotThrow(
                ()->service.writecontact("Test","9854754200","ravi@gmail.com")
                );
        verify(storage).loadContacts();
        verify(storage).saveContacts(any());
    }

    @Test
    void testInvalidPhoneShouldThrowException() throws Exception{
        assertThrows(IllegalArgumentException.class,()->service.writecontact("Adarsh","123","adarsh@gmail.com"));
        verify(storage,never()).saveContacts(any());
    }

    @Test
    void testDuplicateContactShouldThrowException() throws Exception{
        List<Contact> fakedb = new ArrayList<>();
        when(storage.loadContacts()).thenReturn(fakedb);
        service.writecontact("Rahul","9650002145","duplicatename@gmail.com");
        assertThrows(IllegalArgumentException.class, ()->service.writecontact("Rahul","8745001200","amitji@gmail.com"));
        verify(storage).saveContacts(any());
        verify(storage,times(2)).loadContacts();
        // verify(storage,never()).saveContacts(any());
        //verify(storage).loadContacts();
    }

    @Test
    void testSearchContactFound() throws Exception{
        List<Contact> fakedb = new ArrayList<>();
        when(storage.loadContacts()).thenReturn(fakedb);
        service.writecontact("Rahul","9500213524","rahul@gmail.com");
        List <Contact> result = service.searchcontact("rah");
        assertFalse(result.isEmpty());
        assertEquals("Rahul",result.getFirst().getName());
        verify(storage,times(2)).loadContacts();
        verify(storage).saveContacts(argThat(list->list.size()==1 && list.getFirst().getName().equalsIgnoreCase("rahul")));
    }

    @Test
    void searchWhenListIsEmptyShouldReturnEmpty() throws Exception{
        when(storage.loadContacts()).thenReturn(new ArrayList<>());
        List <Contact> find = service.searchcontact("rah");
        assertTrue(find.isEmpty());
        verify(storage).loadContacts();
    }
    @Test
    void testSearchNotFound() throws Exception{
        when(storage.loadContacts()).thenReturn(new ArrayList<>());
        service.writecontact("Amit","9854750021","amit@gmail.com");
        List<Contact> find = service.searchcontact("rah");
        assertTrue(find.isEmpty());
        verify(storage,times(2)).loadContacts();
        verify(storage).saveContacts(argThat(list->list.size()==1 && list.getFirst().getName().equals("Amit")));
    }

    @Test
    void testSearchReturnsMultipleContacts() throws Exception{
        List<Contact> fakedb = new ArrayList<>();
        when(storage.loadContacts()).thenReturn(fakedb);

        service.writecontact("Rahul","9658745210","rahul@yahoo.com");
        service.writecontact("Ramesh","5800210057","ramesh@gmail.com");
        service.writecontact("Adarsh","7001452100","adarsh@hotmail.com");

        List<Contact> result = service.searchcontact("ra");

        //testing the size of list
        assertEquals(2,result.size());
        //testing the integrity
        assertTrue(result.stream().allMatch(c->c.getName().toLowerCase().contains("ra")));

        verify(storage,times(3)).saveContacts(fakedb);
        verify(storage,times(4)).loadContacts();
    }
    @Test
    void testDeleteContact() throws Exception{
        List<Contact> fakedb = new ArrayList<>();
        when(storage.loadContacts()).thenReturn(fakedb);
        service.writecontact("Rahul","8574554120","rahul@gmail.com");
        boolean deleted = service.deletecontact("rahul");
        assertTrue(deleted);
        //verifying from searchcontact
        List<Contact> result = service.searchcontact("rahul");
        assertTrue(result.isEmpty());

        verify(storage,times(3)).loadContacts();
        verify(storage,times(2)).saveContacts(fakedb);
    }

    @Test
    void testShouldReturnFalseWhenContactNotFound() throws Exception{
        List<Contact> fakedb = new ArrayList<>();
        when(storage.loadContacts()).thenReturn(fakedb);
        assertFalse(service.deletecontact("rahul")); //try to find when the list is empty.
        service.writecontact("Amit","9852001200","amit@gmail.com");
        assertFalse(service.deletecontact("rahul")); // the list is not empty but the contact was not found.
        verify(storage,times(3)).loadContacts();
        verify(storage).saveContacts(fakedb);
    }

        @Test
        void testUpdateContact() throws Exception {
            List<Contact> fakedb = new ArrayList<>();
            when(storage.loadContacts()).thenReturn(fakedb);
            service.writecontact("Amit","9850002147","amit@gmail.com");
            service.writecontact("Rahul","8007541245","rahul@gmail.com");
            boolean updated = service.updatecontact("amit","Amit","8590001421","amitsejwal@gmail.com");
            assertTrue(updated);

            //Matching if the contact details are changed
            List<Contact> result = service.searchcontact("amit");
            assertEquals("8590001421",result.getFirst().getPhoneno());
            assertEquals("amitsejwal@gmail.com",result.getFirst().getEmail());

            verify(storage,times(4)).loadContacts();
            verify(storage,times(3)).saveContacts(fakedb);
        }

        @Test
        void UpdateShouldReturnFalseWhenContactNotFound() throws Exception{
        List<Contact> fakedb = new ArrayList<>();
        when(storage.loadContacts()).thenReturn(fakedb);
            service.writecontact("Amit","9850002147","amit@gmail.com");
            boolean updated = service.updatecontact("rahul","Rahul","8590001421","rahulsejwal@gmail.com");
    assertFalse(updated);
    verify(storage,times(2)).loadContacts();
    verify(storage,times(1)).saveContacts(fakedb);
        }

        @Test
        void UpdateShouldReturnFalseWhenListIsEmpty() throws Exception {
        List<Contact> fakedb = new ArrayList<>();
        when(storage.loadContacts()).thenReturn(fakedb);
            boolean updated = service.updatecontact("rahul","Rahul","8590001421","rahulsejwal@gmail.com");
            assertFalse(updated);
            verify(storage,times(1)).loadContacts();
            verify(storage,never()).saveContacts(any());
        }
}
