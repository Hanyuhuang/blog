package com.hyh.listener.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;

@Component
public class MailListener {

    private static final String SENDER  = "577661890@qq.com";
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(bindings = @QueueBinding(value = @Queue("email.code.queue"),
                    exchange = @Exchange(
                            value = "blog-email-code-exchange",
                            type = ExchangeTypes.TOPIC
                    ),
                    key = "email.code"))
    public void sendCode(String email) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(SENDER);
        helper.setTo(email);
        helper.setSubject("用户注册");
        // 产生6位随机验证码
        int code = (int) (Math.random()*1000000);
        StringBuffer sb = new StringBuffer();
        sb.append("<h1>验证码</h1>")
                .append("<br/>")
                .append("<p>【博客】你好！你的验证码为：")
                .append("<span style='color:red;font-size:30px'>"+code +"</span>")
                .append(",5分钟内有效！</p>");
        helper.setText(sb.toString(), true);
        javaMailSender.send(message);
        // 验证码放入redis中 有效期5分钟
        redisTemplate.boundValueOps(email).set(code+"",5, TimeUnit.MINUTES);
    }
}
