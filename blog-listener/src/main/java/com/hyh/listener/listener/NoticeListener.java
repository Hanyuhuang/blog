package com.hyh.listener.listener;

import com.hyh.listener.component.WebSocket;
import com.hyh.pojo.Bo.NoticeBo;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NoticeListener {

    @Autowired
    private WebSocket webSocket;

    @RabbitListener(bindings = @QueueBinding(value = @Queue("notice.queue"),
            exchange = @Exchange(
                    value = "blog-notice-exchange",
                    type = ExchangeTypes.TOPIC
            ),
            key = "notice"))
    public void sendNotice(NoticeBo noticeBo){
        webSocket.sendMessage(noticeBo);
    }
}
