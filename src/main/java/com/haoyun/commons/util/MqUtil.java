package com.haoyun.commons.util;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;


/**
 * activemq发送
 */
public class MqUtil {

    private static final Logger log = LoggerFactory.getLogger(MqUtil.class);

    public static void sendMqMessage(String queue_name, String msg){
        ConnectionFactory connectionFactory; // ConnectionFactory--连接工厂，JMS用它创建连接
        Connection connection = null; // Connection ：JMS 客户端到JMS
        Session session; // Session： 一个发送或接收消息的线程
        Destination destination; // Destination ：消息的目的地;消息发送给谁.
        MessageProducer producer; // MessageProducer：消息发送者
        // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
        connectionFactory = new ActiveMQConnectionFactory(PropertiesUtil.getString("mq_user"), PropertiesUtil
                .getString("mq_password"), PropertiesUtil.getString("brokerURL"));
        try { // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            if(queue_name == null){
                destination = session.createQueue(PropertiesUtil.getString("queue_name"));
            }else{
                destination = session.createQueue(queue_name);
            }
            // 得到消息生成者【发送者】
            producer = session.createProducer(destination);
            // 设置不持久化，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // 构造消息，此处写死，项目就是参数，或者方法获取
            // sendMessage(session, producer);
            sendMessage1(session, producer, msg);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {

            }
        }
    }

    public static void sendMessage1(Session session, MessageProducer producer, String msg) throws Exception {
        TextMessage message = session.createTextMessage(msg);
        // 发送消息到目的地方
        log.info("发送消息：" + "ActiveMq 发送的消息为: /n" + msg);
        producer.send(message);
    }
}
