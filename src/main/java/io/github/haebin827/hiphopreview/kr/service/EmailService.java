package io.github.haebin827.hiphopreview.kr.service;

public interface EmailService {

    void sendVerificationEmail(String to, String verificationCode);
}
