package com.jayden.springRabbitMq.listener;

import com.jayden.springRabbitMq.config.RabbitConfigReader;
import com.jayden.springRabbitMq.dto.UserDetails;
import com.jayden.springRabbitMq.util.ApplicationConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class MessageListener {

    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    @Autowired
    RabbitConfigReader rabbitConfigReader;

    /**
     * Message listener for testApp1
     * UserDetails - 주고받기 테스트용 메세지 Object
     */

    @RabbitListener(queues = "${app1.queue.name}")
    public void receiveMessageForApp1(final UserDetails data) {
        log.info("메세지 수신 : {} FROM testApp1 Queue.", data);

        try {

            //TODO: Code to make REST call
            // Note : 추후 프로그램 로직이 들어갈 부분

            log.info("<< 메세지 수신 성공 API call.");

        } catch(HttpClientErrorException ex) {

            if(ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.info("Delay...");
                try {
                    Thread.sleep(ApplicationConstant.MESSAGE_RETRY_DELAY);
                } catch (InterruptedException e) {
                    ;
                }

                log.info("Throwing exception so that message will be requed in the queue.");
                // Note : 특정 익셉션 처리가 필요하면 이곳에

                throw new RuntimeException();

            } else {
                throw new AmqpRejectAndDontRequeueException(ex);
            }

        } catch(Exception e) {
            log.error("Internal server error occurred in API call. Bypassing message requeue {}", e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

    /**
     * Message listener for testApp2
     * */
    @RabbitListener(queues = "${app2.queue.name}")
    public void receiveMessageForApp2(String reqObj) {
        log.info("메세지 수신 : {} FROM testApp2 queue.", reqObj);

        try {

            //TODO: Code to make REST call
            // Note : 추후 프로그램 로직이 들어갈 부분

            log.info("<< 메세지 수신 성공 API call.");

        } catch(HttpClientErrorException  ex) {

            if(ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.info("Delay...");
                try {
                    Thread.sleep(ApplicationConstant.MESSAGE_RETRY_DELAY);
                } catch (InterruptedException e) {
                    ;
                }

                log.info("Throwing exception so that message will be requed in the queue.");
                // Note: 특정 익셉션 처리가 필요하면 이곳에

                throw new RuntimeException();

            } else {
                throw new AmqpRejectAndDontRequeueException(ex);
            }

        } catch(Exception e) {
            log.error("Internal server error occurred in API call. Bypassing message requeue {}", e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

}
