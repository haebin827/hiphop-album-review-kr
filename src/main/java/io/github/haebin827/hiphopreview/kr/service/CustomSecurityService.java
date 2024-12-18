package io.github.haebin827.hiphopreview.kr.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CustomSecurityService {

    private final HttpServletRequest request;

    public CustomSecurityService(HttpServletRequest request) {
        this.request = request;
    }

    public boolean hasRequestSource(Authentication authentication) {
        HttpSession session = request.getSession(false); // 세션이 없으면 null 반환
        if (session == null) {
            return false;
        }
        Object requestSource = session.getAttribute("requestSource");
        //log.info("CUSTOM SECURITY SERVICE requestSource: " + requestSource);
        //log.info("CUSTOM SECURITY SERVICE result: " + (requestSource != null));
        return requestSource != null;
    }
}
