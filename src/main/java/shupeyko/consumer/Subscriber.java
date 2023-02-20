package shupeyko.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class Subscriber {
    private static final String EXCHANGE_NAME = "DoubleDirect";
    private Channel channel;
    private String queueName;

    public void connect() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        queueName = channel.queueDeclare().getQueue();
    }

    public void subscribe(String theme) throws IOException {
        channel.queueBind(queueName, EXCHANGE_NAME, theme);
        System.out.println(" [*] Ждём сообщение.");
    }

    public void unsubscribe(String theme) throws IOException {
        channel.queueUnbind(queueName, EXCHANGE_NAME, theme);
        System.out.println(" [*] Вы успешно отписались." + theme);
    }

    public void start() throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Опубликован новый материал: '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    public void disconnect() throws IOException, TimeoutException {
        channel.close();
    }
}