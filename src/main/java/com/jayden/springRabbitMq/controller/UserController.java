package com.jayden.springRabbitMq.controller;


import com.jayden.springRabbitMq.config.RabbitConfigReader;
import com.jayden.springRabbitMq.dto.UserDetails;
import com.jayden.springRabbitMq.listener.MessageListener;
import com.jayden.springRabbitMq.sender.MessageSender;
import com.jayden.springRabbitMq.util.ApplicationConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/userservice")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    private final RabbitTemplate rabbitTemplate;
    private RabbitConfigReader rabbitConfig;
    private MessageSender messageSender;

    @Autowired
    public void setApplicationConfig(RabbitConfigReader rabbitConfig) {
        this.rabbitConfig = rabbitConfig;
    }

    @Autowired
    public UserController(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public MessageSender getMessageSender() {
        return messageSender;
    }

    public RabbitConfigReader getApplicationConfig() {
        return rabbitConfig;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> sendMessage(@RequestParam("userMsg") String userMsg) {

        // 라우팅키는 나중에 따로 받아서 각 키별로 보낼 수 있다. 여기서는 고정.
        String routingKey = getApplicationConfig().getTest1AppRoutingKey();
        String exchange = getApplicationConfig().getTest1AppExchange();

        UserDetails user = new UserDetails();
        user.setSupplierId("UserSender");
        user.setSupplierName(userMsg);
        user.setSupplierUrl("www.google.com");

        /* Sending to Message Queue */
        try {

            messageSender.sendMessage(rabbitTemplate, exchange, routingKey, user);
            return new ResponseEntity<String>(ApplicationConstant.IN_QUEUE, HttpStatus.OK);

        } catch (Exception ex) {

            log.error("Exception occurred while sending message to the queue. Exception= {}", ex);
            return new ResponseEntity(ApplicationConstant.MESSAGE_QUEUE_SEND_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
