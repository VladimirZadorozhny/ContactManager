<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:org="http://www.mystudying.org/contactsManager"
                exclude-result-prefixes="org">

    <xsl:output method="html" indent="yes"/>

    <xsl:template match="org:contacts">
        <html>
            <head>
                <title>Contact List</title>
                <style>
                    table {
                    border-collapse: collapse;
                    width: 60%;
                    margin: 20px auto;
                    font-family: Arial, sans-serif;
                    }
                    th, td {
                    border: 1px solid black;
                    padding: 8px;
                    text-align: left;
                    }
                    th {
                    background-color: #f2f2f2;
                    text-align: center;
                    }
                    tr:nth-child(even) {
                    background-color: #fafafa;
                    }
                </style>
            </head>
            <body>
                <h1 style="text-align:center">Contacts</h1>
                <table>
                    <tr>
                        <th>Name</th>
                        <th>Phone</th>
                        <th>Email</th>
                    </tr>
                    <xsl:for-each select="org:contact">
                        <tr>
                            <td><xsl:value-of select="org:name"/></td>
                            <td><xsl:value-of select="org:phone"/></td>
                            <td><xsl:value-of select="@e-mail"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>