package io.github.kanedafromparis.ose.fakedatagen;


import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.company.Company;
import io.codearte.jfairy.producer.payment.CreditCard;
import io.codearte.jfairy.producer.person.Address;
import io.codearte.jfairy.producer.person.Person;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Calendar;

public class FakeDataGenerator {

    public String generateFakeData() {
        return this.generateFakeData(StringUtils.EMPTY, 10, Boolean.FALSE, Boolean.FALSE);
    }

    public String generateFakeData(String dir, Integer deep, Boolean saveToDtb, Boolean saveToFile) {
        StringBuilder sb = new StringBuilder();
        Fairy fairy = Fairy.create();
        Calendar start = Calendar.getInstance();
        try {
            if (StringUtils.isBlank(dir)) {
                dir = System.getProperty("user.home")+"/target";
            }
            String databaseURL = "jdbc:hsqlhb://";
            databaseURL += System.getenv("HSQLDB_SERVICE_HOST");
            databaseURL += "/" + System.getenv("HSQLDB_DATABASE");
            String username = System.getenv("HSQLDB_USER");
            String password = System.getenv("HSQLDB_PASSWORD");
            String rootfile = System.getenv("ROOT_FILE");
            Connection connection = null;
            if (saveToDtb){
                connection = DriverManager.getConnection(databaseURL, username, password);
            }
            sb.append("{ company : [");

            while (deep > 0) {
                deep = deep -1;
                Integer nbPersonnes = RandomUtils.nextInt(0, 10000);
                Integer nbPersonnesComp = nbPersonnes.intValue();
                System.out.println(MessageFormat.format("Creating {0} user", nbPersonnesComp));
                Company company = fairy.company();

                String companyName = company.name();
                String annotation = fairy.textProducer().sentence();
                sb.append("{");
                sb.append("companyName : \"");
                sb.append(companyName);
                sb.append("\",");
                sb.append("annotation : \"");
                sb.append(companyName);
                sb.append("\",");
                sb.append(", persones : [");
                StringBuilder sbPersonnes = null;
                while (nbPersonnes > 0) {
                    nbPersonnes = nbPersonnes - 1;
                    sbPersonnes = new StringBuilder();
                    Person person = fairy.person();
                    String firstname = person.firstName();
                    String lastname = person.lastName();

                    CreditCard card = fairy.creditCard();
                    String cardVendor = card.vendor();

                    Address adress = person.getAddress();

                    String stnum = adress.streetNumber();
                    String street = adress.street();
                    String postalCode = adress.getPostalCode();
                    String city = adress.getCity();

                    sbPersonnes.append("{");
                    sbPersonnes.append("firstname : \"");
                    sbPersonnes.append(firstname);
                    sbPersonnes.append("\",");
                    sbPersonnes.append("lastname : \"");
                    sbPersonnes.append(lastname);
                    sbPersonnes.append("\",");
                    sbPersonnes.append("cardVendor : \"");
                    sbPersonnes.append(cardVendor);
                    sbPersonnes.append("\",");


                    if (saveToFile) {
                        System.out.println("Save To File");
                        File dirCompany = new File(rootfile, companyName);
                        if (!dirCompany.exists()) {
                            dirCompany.mkdirs();
                        }
                        File filePersonnes = new File(companyName, person.fullName().toLowerCase());
                        StringBuilder sblocal = new StringBuilder();
                        sblocal.append(sbPersonnes);
                        sblocal.append("adress : {");
                        sblocal.append("stnum : \"");
                        sblocal.append(stnum);
                        sblocal.append("\",");
                        sblocal.append("street : \"");
                        sblocal.append(street);
                        sblocal.append("\",");
                        sblocal.append("postalCode : \"");
                        sblocal.append(postalCode);
                        sblocal.append("\",");
                        sblocal.append("city : \"");
                        sblocal.append(city);
                        sblocal.append("\"");
                        sblocal.append("}");
                        sblocal.append("}");
                        FileWriterWithEncoding writer;
                        writer = new FileWriterWithEncoding(filePersonnes, "UTF-8", Boolean.TRUE);
                        writer.write(sblocal.toString());
                        writer.flush();
                        writer.close();

                    }
                    if (saveToDtb) {
                        System.out.println("Save To DTB");
                        String sql = "INSERT PERSONES (FIRSTNAME, LASTNAME, PHONE,CBVALUE) VALUES (?,?,?)";

                        PreparedStatement pstmt = connection.prepareStatement(sql);
                        pstmt.setString(1, firstname);
                        pstmt.setString(2, lastname);
                        pstmt.setString(3, person.telephoneNumber());
                        pstmt.setString(4, cardVendor);

                        boolean execute = pstmt.execute();

                        sql = "INSERT ADRESS (STREETNUM, STREETNAME, POSTALCODE, CITY) VALUES (?,?,?)";

                        pstmt = connection.prepareStatement(sql);
                        pstmt.setString(1, stnum);
                        pstmt.setString(2, street);
                        pstmt.setString(3, postalCode);
                        pstmt.setString(4, city);

                        execute = pstmt.execute();


                    }
                    sbPersonnes.append("\"}");
                    sbPersonnes.append(",");
                }
                if (sbPersonnes != null) {
                    sbPersonnes.deleteCharAt(sbPersonnes.length()-1);
                }
                if (saveToDtb) {
                    String sql = "INSERT COMPANY (COMPANYNAME, NBPERSONNES) VALUES (?,?)";

                    PreparedStatement pstmt = connection.prepareStatement(sql);
                    pstmt.setString(1, companyName);
                    pstmt.setInt(2, nbPersonnesComp);
                    //rs =
                    boolean execute = pstmt.execute();
                }
            }
            sb.append("]}");
            if(connection != null) {
                connection.close();
            }
            Calendar end = Calendar.getInstance();
            long delta = end.getTimeInMillis() - start.getTimeInMillis();
            System.out.println(MessageFormat.format("Process took {0} Milliseconds", delta));
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        } catch (SQLException e) {
            e.printStackTrace();

            return e.getLocalizedMessage();
        }
    }
}
