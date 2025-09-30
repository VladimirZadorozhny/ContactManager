# Contact Manager

A small **Java application** for managing contacts.
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

Jackson for JSON export/import

XSD schema for validation

XSLT for XML → HTML transformation

## How to use

Clone the repository:
  git clone https://github.com/VladimirZadorozhny/ContactManager.git

Build and run with Maven/IDE.

Add or remove contacts in the application.

Save contacts → creates/updates contacts.xml or contacts.json

Export to HTML with the “Make HTML” option → generates contacts.html.
