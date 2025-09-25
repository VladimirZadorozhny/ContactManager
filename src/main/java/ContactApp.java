import java.util.Scanner;

public class ContactApp {
    private static final String FILENAME = "contacts.json";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ContactManager manager = ContactManager.getInstance();


        while (true) {
            System.out.println("\n ---- Contact Manager ---- ");
            System.out.println("1. Add contact ->");
            System.out.println("2. Show all contacts ->");
            System.out.println("3. Export to JSON ->");
            System.out.println("0. Stop -X");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    try {
                        System.out.print("Enter the name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter the phone number: ");
                        String phone = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();

                        Contact contact = ContactFactory.createContact(name, phone, email);
                        manager.addContact(contact);
                        System.out.println("Contact added.");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println(" --- All contacts --- ");
                    for (Contact contact : manager.getContacts()) {
                        System.out.println(contact);
                    }
                    break;
                case 3:
                    manager.exportToJson(FILENAME);
                   break;
                case 0:
                    System.out.println("Good bye");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }

        }
    }
}
