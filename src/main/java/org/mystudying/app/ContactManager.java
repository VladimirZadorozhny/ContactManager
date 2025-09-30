package org.mystudying.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ContactManager {

    private final static Path SCHEMA_PATH = Path.of("contacts.xsd");
    private List<Contact> contacts = new ArrayList<>();

    private ContactManager() {
    }

    private static class Helper {
        private final static ContactManager INSTANCE = new ContactManager();

    }

    public static ContactManager getInstance() {
        return Helper.INSTANCE;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    private List<Contact> globalImport(String filename, String extension) throws Exception {
        List<Contact> imported = new ArrayList<>();
        switch (extension) {
            case "json" -> {
                ObjectMapper mapper = new ObjectMapper();
                imported.addAll(Arrays.asList(mapper.readValue(new File(filename), Contact[].class)));
            }
            case "xml" -> {
                JAXBContext context = JAXBContext.newInstance(ContactList.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = factory.newSchema(SCHEMA_PATH.toFile());
                unmarshaller.setSchema(schema);
                ContactList wrapper = (ContactList) unmarshaller.unmarshal(new File(filename));
                imported = wrapper.getContacts();
            }
        }

        if (!imported.isEmpty()) {
            imported.removeAll(contacts);
        }
        return imported;
    }

    public void exportToJson(String filename) {
        try {
            List<Contact> imported = globalImport(filename, "json");
            if (!imported.isEmpty())
                contacts.addAll(imported);

            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), contacts);
            System.out.println("Export successfully to " + filename);
        } catch (Exception e) {
            System.out.println("Export error: " + e.getMessage());
        }
    }

    public void importFromJson(String filename) {
        try {
            List<Contact> imported = globalImport(filename, "json");
            if (!imported.isEmpty())
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

            List<Contact> imported = globalImport(filename, "xml");
            if (!imported.isEmpty())
                wrapper.getContacts().addAll(imported);

            marshaller.marshal(wrapper, new File(filename));
            System.out.println("Export successfully to " + filename);
        } catch (Exception e) {
            System.out.println("Export error: " + e.getMessage());
        }
    }

    public void importFromXml(String filename) {
        try {
            List<Contact> imported = globalImport(filename, "xml");

            if (!imported.isEmpty())
              contacts.addAll(imported);
            System.out.println(imported.size() + " contacts imported from file " + filename);
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

    public void xmlToHtml() throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File("contactsToHTML.xslt"));
        Transformer transformer = factory.newTransformer(xslt);

        Source text = new StreamSource(new File("contacts.xml"));
        transformer.transform(text, new StreamResult(new File("contacts.html")));

        System.out.println("HTML generated successfully!");
    }
}
