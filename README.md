# Contact Manager

A simple Java console application to manage contacts with support for JSON and XML storage, XSLT transformation to HTML, and data validation.
It was created as a learning project to practice Java, JAXB, XML, XSD validation, and XSLT transformations.

## Features

Store contacts with name, phone, and email.

Contacts are saved to and loaded from an XML or JSON file.

XML structure is validated against an XSD schema (checks valid phone number, email, name format).

Transform the XML contacts list into a readable HTML page using XSLT.

Command-line interface for adding, listing, deleting, importing and exporting contacts.


## Technologies

Java 17+

Jakarta JAXB for XML binding

Jackson for JSON serialization/deserialization

XSD schema for structure & validation of XML files

XSLT for "XML to HTML" transformation

RegEx for input validation for phone and email


## Design Patterns

**Simple Factory**
- ContactFactory centralizes the creation of Contact objects.
This makes it easy to add validation logic (e.g., RegEx checks for phone and email) in one place.

- Thread-safe Singleton (Bill Pugh approach)
ContactManager is implemented as a singleton using a static inner helper class.
This ensures:
1. Only one instance exists
2. Thread safety without explicit synchronization
3. Lazy initialization (created only when needed)


## Data Validation

Input is validated both in code and via XSD schema:

Email → [-.\w]+@([\w-]+\.)+[\w-]+

Phone → (0\d{8,9})|(\+32[^0]\d{8})

Name → minimum 2 characters

This guarantees consistent and valid data across all formats.


## Example Files

contacts.json – Stores contacts in JSON format

contacts.xml – Stores contacts in XML format (validated against XSD)

contacts.html – Generated HTML table of contacts (via XSLT)



## How to use

Clone the repository:
  git clone https://github.com/VladimirZadorozhny/ContactManager.git

Build and run with Maven/IDE.

Run the application (ContactApp)

Choose an option from the console menu:

Add a new contact
Show all contacts
Import/export contacts (JSON/XML)
Delete a contact
Generate an HTML page

Data is saved and validated automatically
