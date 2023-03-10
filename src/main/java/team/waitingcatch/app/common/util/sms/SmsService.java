package team.waitingcatch.app.common.util.sms;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import team.waitingcatch.app.common.util.sms.dto.MessageRequest;
import team.waitingcatch.app.common.util.sms.dto.SmsRequest;
import team.waitingcatch.app.common.util.sms.dto.SmsResponse;

@Service
public class SmsService {
	@Value("${ncloud.sms.access.key}")
	private String accessKey;

	@Value("${ncloud.sms.secret.key}")
	private String secretKey;

	@Value("${ncloud.sms.service.id}")
	private String serviceId;

	@Value("${ncloud.sms.sender.phone}")
	private String senderPhone;

	public SmsResponse sendSms(MessageRequest messageRequest) throws
		JsonProcessingException,
		URISyntaxException,
		UnsupportedEncodingException,
		NoSuchAlgorithmException,
		InvalidKeyException {

		Long time = System.currentTimeMillis();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-ncp-apigw-timestamp", time.toString());
		headers.set("x-ncp-iam-access-key", accessKey);
		headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

		List<MessageRequest> messages = new ArrayList<>();
		messages.add(messageRequest);

		SmsRequest smsRequest = new SmsRequest("LMS", "COMM", "82", senderPhone, messageRequest.getContent(),
			messages);

		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(smsRequest);
		HttpEntity<String> request = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

		return restTemplate.postForObject(
			new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + serviceId + "/messages"),
			request,
			SmsResponse.class);
	}

	private String makeSignature(Long time) throws
		NoSuchAlgorithmException,
		InvalidKeyException {
		String space = " ";
		String newLine = "\n";
		String method = "POST";
		String url = "/sms/v2/services/" + serviceId + "/messages";
		String timestamp = time.toString();
		String accessKey = this.accessKey;
		String secretKey = this.secretKey;

		String message = new StringBuilder()
			.append(method)
			.append(space)
			.append(url)
			.append(newLine)
			.append(timestamp)
			.append(newLine)
			.append(accessKey)
			.toString();

		SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(signingKey);

		byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

		return Base64.encodeBase64String(rawHmac);
	}
}