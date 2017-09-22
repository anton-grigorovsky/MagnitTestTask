package magnit.test.task;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Антон on 21.09.2017.
 */
public class Service {
    private String URL;
    private String userName;
    private String password;
    private int num;
    private String filePath;



    /*Основной паблик метод, запускающий приложение.
    Остальные методы приват. */

    public void runApp() {

        // ------------ Заполняем БД ---------------
        fillDB();
        // ------------ Достаем данные -------------
        ResultSet data = getData();
        // -------------- Создаем и заполняем XML---------
        createAndFillXML(data);
        // ------------- Переписываем с изменениями -----------
        rewriteXML();
        // ------------- Парсим и выводим сумму на экран --------
        System.out.println(parseAndSum());


    }

    private void createAndFillXML(ResultSet resultSet) {

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document doc = factory.newDocumentBuilder().newDocument();

            Element rootElement =
                    doc.createElement("entries");

            doc.appendChild(rootElement);

            while (resultSet.next()) {
                Element child1 = doc.createElement("entry");
                Element child2 = doc.createElement("field");

                child2.appendChild(doc.createTextNode("" + resultSet.getInt(1)));
                child1.appendChild(child2);
                rootElement.appendChild(child1);
            }

            resultSet.close();


            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);


            StreamResult file = new StreamResult(new File(filePath + "1.xml"));

            transformer.transform(source, file);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void fillDB()
    {
        DBConnectivity dbc = new DBConnectivity(URL, userName, password);

        try {

            Connection connection = dbc.getConnection();
            Statement statement = connection.createStatement();
            statement.addBatch("TRUNCATE TABLE test;");
            for(int i = 1; i<= num; i++) {
                statement.addBatch("INSERT INTO test(field) VALUES (" + i + ")");
            }
            statement.executeBatch();
            statement.clearBatch();
            connection.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println("Проблемы с подключением к БД.");
        }

    }

    private ResultSet getData() {
        ResultSet data = null;
        try {
            DBConnectivity dbc = new DBConnectivity(URL, userName, password);
            Connection connection = dbc.getConnection();
            Statement statement = connection.createStatement();
            data = statement.executeQuery("SELECT * FROM test");

            connection.close();
            statement.close();


        } catch (SQLException e) {
            System.out.println("Проблема с подключением к БД");
        }

        return data;
    }

    private void rewriteXML() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(new StreamSource("src\\main\\resources\\transformStyle.xsl"));
            transformer.transform(new StreamSource(filePath + "1.xml"), new StreamResult(filePath + "2.xml"));

        }
        catch (Exception e) {
            System.out.println("Невозможно трансформировать XML файл.");;
        }

        }

    private Long parseAndSum() {
        Long sum = 0l;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document doc = factory.newDocumentBuilder().parse(new File(filePath + "2.xml"));
            NodeList nodeList = doc.getElementsByTagName("entry");
            for(int i = 0; i < nodeList.getLength(); i++) {
                sum += Long.parseLong(((Element)nodeList.item(i)).getAttribute("field").trim());
            }
        }
        catch (Exception e) {
            System.out.println("Невозможно спарсить документ.");
        }

        return sum;
    }




    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
