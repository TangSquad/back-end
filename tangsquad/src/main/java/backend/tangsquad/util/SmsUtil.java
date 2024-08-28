package backend.tangsquad.util;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsUtil {

    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;
    @Value("${coolsms.phone.number}")
    private String phoneNumber;

    private DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        messageService = new DefaultMessageService(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    public SingleMessageSentResponse sendSms(String to, String verificationCode) {
        Message message = new Message();
        message.setFrom(phoneNumber);
        message.setTo(to);
        message.setText("[TangSquad] 인증번호는 " + verificationCode + "입니다.");

        return messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
