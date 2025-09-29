package org.mystudying.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ContactManager {
    private static ContactManager instance;
    private final static Path SCHEMA_PATH = Path.of("contacts.xsd");
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
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
                    "http://www.mystudying.org/contactsManager contacts.xsd");
            ContactList wrapper = new ContactList(contacts);

            StringWriter writer = new StringWriter();
            marshaller.marshal(wrapper, writer);

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(SCHEMA_PATH.toFile());
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(writer.toString())));

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
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(SCHEMA_PATH.toFile());
            unmarshaller.setSchema(schema);
            ContactList wrapper = (ContactList) unmarshaller.unmarshal(new File(filename));
            contacts.addAll(wrapper.getContacts());
            System.out.println(wrapper.getContacts().size() + " contacts imported from file " + filename);
        } catch (Exception e) {
            System.out.println("XML import error: " + e.getMessage());
        }
    }

    private Optional<Contact> findContact(String email) {
        return contacts
                .stream()
                .filter(e -> e.getEmail().equals(email))
                .findFirst();
    }

    public void deleteContact(String email) {
        var result = findContact(email).orElseThrow(() -> new IllegalArgumentException("Contact not found!"));
        contacts.remove(result);
    }
}
