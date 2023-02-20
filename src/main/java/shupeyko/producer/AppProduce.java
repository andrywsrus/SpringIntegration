package shupeyko.producer;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class AppProduce {
    public static void main(String[] args) {
        Producer producer = new Producer();
        try {
            producer.connect();
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println("Для публикации, обязательно укажите название темы:");
                String in = sc.nextLine();
                String[] msg = in.split(" ", 2);
                if (msg[0].trim().equals("exit")) {
                    producer.disconnect();
                    sc.close();
                    break;
                }
                producer.sendMessage(msg[0], msg[1]);
            }
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                producer.disconnect();
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
