package io.github.haebin827.hiphopreview.kr.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String verificationCode) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("💽AlbumTalk💽 회원가입 인증 코드");

            // HTML 형식의 이메일 내용 (중앙 정렬 스타일 적용)
            String htmlContent = "<div style='text-align: center;'>"
                    + "<img src='cid:logoImage' alt='AlbumTalk 로고' style='width: 200px; height: auto; margin-top: -30px; margin-bottom: -30px;'>"
                    + "<h2>AlbumTalk 회원가입 인증</h2>"
                    + "<p>인증 코드: <strong>" + verificationCode + "</strong></p><br>"
                    + "<p>이 코드를 입력하여 인증을 완료하십쇼 (ㅡㅡ)/</p>"
                    + "</div>";

            helper.setText(htmlContent, true); // HTML 형식 사용

            // 이미지 첨부
            ClassPathResource logo = new ClassPathResource("static/images/logo.png");
            helper.addInline("logoImage", logo);

            mailSender.send(mimeMessage);
            log.info("이메일이 성공적으로 전송되었습니다: {}", to);
        } catch (MessagingException e) {
            log.error("이메일 전송 중 오류가 발생했습니다: {}", e.getMessage());
            throw new IllegalStateException("이메일 전송 실패", e);
        }
    }
}
