package io.github.kanedafromparis.ose.fakedatagen;


import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.company.Company;
import com.devskiller.jfairy.producer.payment.CreditCard;
import com.devskiller.jfairy.producer.person.Address;
import com.devskiller.jfairy.producer.person.Person;
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
        return this.generateFakeData(10, Boolean.FALSE, Boolean.FALSE);
    }

    public String generateFakeData(Integer deep, Boolean saveToDtb, Boolean saveToFile) {
        StringBuilder sb = new StringBuilder();
        Fairy fairy = Fairy.create();
        Calendar start = Calendar.getInstance();
        try {

            String rootfile = System.getenv("ROOT_FILE");
            if (StringUtils.isBlank(rootfile)) {
                rootfile = System.getProperty("user.dir")+"/target";
                System.out.println(MessageFormat.format("Using {0} ", rootfile));
            }

            Connection connection = null;
            if (saveToDtb){
                CreateDataGenerator create = new CreateDataGenerator();

                connection = create.getConnection();

                create.createTable(connection);
            }
            sb.append("{ company : [");

            while (deep > 0) {
                deep = deep -1;
                Integer nbPersonnes = RandomUtils.nextInt(0, 10000);
                Integer nbPersonnesComp = nbPersonnes.intValue();
                System.out.println(MessageFormat.format("Creating {0} user", nbPersonnesComp));
                Company company = fairy.company();

                String companyName = company.getName();
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
                    String firstname = person.getFirstName();
                    String lastname = person.getLastName();

                    CreditCard card = fairy.creditCard();
                    String cardVendor = card.getVendor();

                    Address adress = person.getAddress();

                    String stnum = adress.getStreetNumber();
                    String street = adress.getStreet();
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
                        //System.out.println("Save To File");
                        File dirCompany = new File(rootfile, companyName);
                        if (!dirCompany.exists()) {
                            dirCompany.mkdirs();
                        }
                        File filePersonnes = new File(dirCompany, person.getFullName().toLowerCase());
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
                        //System.out.println("Save To DTB");
                        String sql = "INSERT INTO PERSONES (FIRSTNAME, LASTNAME, PHONE,CBVALUE) VALUES (?,?,?,?)";

                        PreparedStatement pstmt = connection.prepareStatement(sql);
                        pstmt.setString(1, firstname);
                        pstmt.setString(2, lastname);
                        pstmt.setString(3, person.getTelephoneNumber());
                        pstmt.setString(4, cardVendor);

                        boolean execute = pstmt.execute();

                        sql = "INSERT INTO ADRESS (STREETNUM, STREETNAME, POSTALCODE, CITY) VALUES (?,?,?,?)";

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
                    String sql = "INSERT INTO COMPANY (COMPANYNAME, NBPERSONNES) VALUES (?,?)";

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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
    }
}
