package magnit.test.task;

/**
 * Created by Антон on 21.09.2017.
 */
public class Main {
    public static void main(String[] args) {
        //------- Засекаем время ------
        long start = System.currentTimeMillis();

        Service service = new Service();
        service.setURL("jdbc:sqlite:magnit.db");
        service.setUserName("user");
        service.setPassword("pass");
        service.setNum((1000000));

        // -----указываем путь где будет созданы наши XML файл
        service.setFilePath("D:\\");

        service.runApp();

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;


        ///----------Вывод времени в милисекундах---------
        System.out.println(timeConsumedMillis);



    }
}
