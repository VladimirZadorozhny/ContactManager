import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void importFromJson(String filename) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Contact> imported = Arrays.asList(mapper.readValue(new File(filename), Contact[].class));
            contacts.addAll(imported);
            System.out.println(imported.size() + " contacts imported from file " + filename);
        } catch (Exception e) {
            System.out.println("JSON import error: " + e.getMessage());
        }
    }

    public void exportToXml(String filename) {
        try {
            JAXBContext context = JAXBContext.newInstance(ContactList.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            ContactList wrapper = new ContactList(contacts);
            marshaller.marshal(wrapper, new File(filename));
            System.out.println("Export successfully to " + filename);
        } catch (Exception e) {
            System.out.println("Export error: " + e.getMessage());
        }
    }

    public void importFromXml(String filename) {
        try {
            JAXBContext context = JAXBContext.newInstance(ContactList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ContactList wrapper = (ContactList) unmarshaller.unmarshal(new File(filename));
            contacts.addAll(wrapper.getContacts());
            System.out.println(wrapper.getContacts().size() + " contacts imported from file " + filename);
        } catch (Exception e) {
            System.out.println("XML import error: " + e.getMessage());
        }
    }
}
