package com.example.notification_service;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationIntegrationTest {

    private static GreenMail greenMail;

    @BeforeAll
    static void startMailServer() {
        greenMail = new GreenMail(ServerSetup.SMTP);
        greenMail.start();
    }

    @AfterAll
    static void stopMailServer() {
        greenMail.stop();
    }

    @DynamicPropertySource
    static void mailProps(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> greenMail.getSmtp().getPort());
        registry.add("spring.mail.properties.mail.smtp.auth", () -> "false");
        registry.add("spring.mail.properties.mail.smtp.starttls.enable", () -> "false");
    }

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    @BeforeEach
    void purge() {
        greenMail.reset();
    }

    @Test
    void shouldSendEmailViaApi() throws Exception {
        String url = "http://localhost:" + port
                + "/api/notifications?email=test@test.com&operation=CREATE";

        ResponseEntity<Void> resp = rest.postForEntity(url, null, Void.class);
        assertThat(resp.getStatusCode().value()).isEqualTo(200);

        greenMail.waitForIncomingEmail(1);

        MimeMessage[] msgs = greenMail.getReceivedMessages();
        assertThat(msgs).hasSize(1);
        assertThat(msgs[0].getAllRecipients()[0].toString()).isEqualTo("test@test.com");
        assertThat(msgs[0].getSubject()).isEqualTo("Уведомление");

        String body = (String) msgs[0].getContent();
        assertThat(body).contains("успешно создан");
    }
}