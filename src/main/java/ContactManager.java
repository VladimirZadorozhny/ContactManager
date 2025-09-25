import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContactManager {
    private static ContactManager instance;
    private List<Contact> contacts = new ArrayList<>();

    private ContactManager() {

    }

    public static ContactManager getInstance() {
        if (instance == null) {
            instance = new ContactManager();
        }
        return instance;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void exportToJson(String filename) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), contacts);
            System.out.println("Export successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Export error: " + e.getMessage());
        }
    }
}
