    package io.github.haebin827.hiphopreview.kr.controller;

    import io.github.haebin827.hiphopreview.kr.dto.UserDTO;
    import io.github.haebin827.hiphopreview.kr.service.AuthService;
    import io.github.haebin827.hiphopreview.kr.service.EmailService;
    import io.github.haebin827.hiphopreview.kr.service.EmailServiceImpl;
    import io.github.haebin827.hiphopreview.kr.service.UserService;
    import jakarta.servlet.http.HttpSession;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.log4j.Log4j2;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    import java.time.Duration;
    import java.time.LocalDateTime;
    import java.util.Objects;
    import java.util.UUID;
    import java.time.format.DateTimeFormatter;

    @Controller
    @RequestMapping("/auth")
    @Log4j2
    @RequiredArgsConstructor
    public class AuthController {

        private final AuthService as;
        private final UserService us;
        private final EmailService es;

        @PreAuthorize("isAnonymous()")
        @GetMapping("/login")
        public void loginGET(HttpSession session) {
            log.info("login GET.......................");
            session.removeAttribute("tempUser");
            session.removeAttribute("toggle");
            session.removeAttribute("userEmail");
            session.removeAttribute("requestSource");
        }

        @GetMapping("/register")
        public void registerGET(HttpSession session) {
            session.removeAttribute("tempUser");
            session.removeAttribute("toggle");
            session.removeAttribute("userEmail");
            session.removeAttribute("requestSource");
        }

        @PostMapping("/register")
        public String registerPOST(@Valid UserDTO userDTO,
                                   BindingResult result,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
            if (result.hasErrors()) {
                return "/auth/register";
            }

            String uuid = UUID.randomUUID().toString();
            userDTO.setUuid(uuid);

            // 임시 저장: 세션에 저장
            session.setAttribute("tempUser", userDTO);

            // 랜덤 인증 코드 생성
            String verificationCode = UUID.randomUUID().toString().substring(0, 6);
            session.setAttribute("verificationCode", verificationCode);
            session.setAttribute("codeCreatedTime", LocalDateTime.now());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String validityPeriod = LocalDateTime.now().plus(Duration.ofSeconds(180)).format(formatter);
            log.info("VALIDITY PERIOD: " + validityPeriod);
            session.setAttribute("validityPeriod", validityPeriod);
            session.setAttribute("requestSource", "register");

            // 이메일 전송
            es.sendVerificationEmail(userDTO.getEmail(), verificationCode);

            // 이메일 확인 페이지로 리다이렉트
            return "redirect:/auth/emailConfirm";
        }

        @PreAuthorize("@customSecurityService.hasRequestSource(authentication)")
        @GetMapping("/emailConfirm")
        public void emailConfirmGET(HttpSession session, Model model) {
            Object requestSource = session.getAttribute("requestSource");
            model.addAttribute("requestSource", requestSource);
        }

        /*@PostMapping("/emailConfirm")
        public String emailConfirmPOST(String inputCode,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {

            String verificationCode = (String) session.getAttribute("verificationCode");
            UserDTO tempUser = (UserDTO) session.getAttribute("tempUser");
            LocalDateTime codeCreatedTime = (LocalDateTime) session.getAttribute("codeCreatedTime");

            log.info("VERIFICATION CODE: " + verificationCode);
            log.info("TEMP USER: " + tempUser);
            log.info("CODE CREATED TIME: " + codeCreatedTime);
            log.info("LOCAL TIME: " + LocalDateTime.now());
            log.info("DIFFERENCE: " + Duration.between(codeCreatedTime, LocalDateTime.now()).toSeconds());

            if (verificationCode == null || tempUser == null ||
                    Duration.between(codeCreatedTime, LocalDateTime.now()).toSeconds() > 180) {
                log.info("EMAIL CONFIRMATION 인증시간 완료");
                session.invalidate(); // 세션 초기화
                redirectAttributes.addFlashAttribute("timeError", "인증코드 만료");
                return "redirect:/auth/emailConfirm";
            }

            if (!verificationCode.equals(inputCode)) {
                redirectAttributes.addFlashAttribute("error", "인증 코드가 잘못되었습니다.");
                return "redirect:/auth/emailConfirm";
            }

            // 회원가입 완료 처리
            as.registerUser(tempUser);

            // 세션 초기화
            session.removeAttribute("verificationCode");
            session.removeAttribute("tempUser");

            redirectAttributes.addFlashAttribute("nickname", tempUser.getNickname());
            return "redirect:/auth/registerConfirmed";
        }*/

        @PostMapping("/emailConfirm")
        public String emailConfirmPOST(String inputCode,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {

            log.info("emailConfirmPOST........................");

            String verificationCode = (String) session.getAttribute("verificationCode");
            LocalDateTime codeCreatedTime = (LocalDateTime) session.getAttribute("codeCreatedTime");
            UserDTO tempUser = (UserDTO) session.getAttribute("tempUser");
            String toggle = (String) session.getAttribute("toggle");
            String requestSource = (String) session.getAttribute("requestSource");

            log.info("REQUEST SOURCE: " + requestSource);
            log.info("TOGGLE: " + toggle);
            log.info("TEMP USER: " + tempUser);
            log.info("VERIFICATION CODE: " + verificationCode);
            log.info("CODE CREATED TIME: " + codeCreatedTime);
            log.info("LOCAL TIME: " + LocalDateTime.now());
            log.info("DIFFERENCE: " + Duration.between(codeCreatedTime, LocalDateTime.now()).toSeconds());

            // 인증코드 만료 또는 세션 데이터 부족 시 처리
            if (verificationCode == null || codeCreatedTime == null ||
                    Duration.between(codeCreatedTime, LocalDateTime.now()).toSeconds() > 180) {
                log.info("EMAIL CONFIRMATION 인증시간 완료");

                if(requestSource.equals("register")) {
                    log.info("ERROR: TEMP USER");
                    redirectAttributes.addFlashAttribute("timeError", "tempUser");
                } else if(requestSource.equals("find")) {
                    log.info("ERROR: TOGGLE");
                    redirectAttributes.addFlashAttribute("timeError", "toggle");
                }

                // 세션에서 필요한 속성 제거
                session.removeAttribute("tempUser");
                session.removeAttribute("toggle");
                session.removeAttribute("verificationCode");
                session.removeAttribute("codeCreatedTime");

                // 세션 무효화
                //session.invalidate();

                return "redirect:/auth/emailConfirm";
            }


            // 인증코드 검증 실패 시 처리
            if (!verificationCode.equals(inputCode)) {
                redirectAttributes.addFlashAttribute("error", "인증 코드가 잘못되었습니다.");
                return "redirect:/auth/emailConfirm";
            }

            // 회원가입 이메일 인증 처리
            if (requestSource.equals("register") && tempUser != null) {
                log.info("회원가입 이메일 인증 처리");
                as.registerUser(tempUser);

                // 세션 초기화
                session.removeAttribute("verificationCode");
                session.removeAttribute("tempUser");
                session.removeAttribute("codeCreatedTime");

                redirectAttributes.addFlashAttribute("nickname", tempUser.getNickname());
                return "redirect:/auth/registerConfirmed";
            }

            // 아이디/비밀번호 찾기 이메일 인증 처리
            if (requestSource.equals("find") && toggle != null) {
                log.info("아이디/비밀번호 찾기 이메일 인증 처리");

                redirectAttributes.addFlashAttribute("toggle", toggle);
                session.removeAttribute("verificationCode");
                session.removeAttribute("codeCreatedTime");
                session.removeAttribute("toggle");
                session.removeAttribute("userEmail");

                return "redirect:/auth/findInfo";
            }

            // 처리되지 않은 경우 기본 리다이렉트
            log.warn("처리되지 않은 요청");
            return "redirect:/auth/emailConfirm";
        }

        @PostMapping("/resendVerification")
        public String resendVerificationPOST(HttpSession session, RedirectAttributes redirectAttributes) {

            log.info("resendVerification POST........................");
            // 세션에서 필요한 데이터 가져오기
            UserDTO tempUser = (UserDTO) session.getAttribute("tempUser");
            String userEmail = (String) session.getAttribute("userEmail");
            String requestSource = (String) session.getAttribute("requestSource");


            // 새로운 인증 코드 생성 및 세션 저장
            String newVerificationCode = UUID.randomUUID().toString().substring(0, 6);
            session.setAttribute("verificationCode", newVerificationCode);
            session.setAttribute("codeCreatedTime", LocalDateTime.now());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String validityPeriod = LocalDateTime.now().plus(Duration.ofSeconds(180)).format(formatter);
            log.info("VALIDITY PERIOD: " + validityPeriod);
            session.setAttribute("validityPeriod", validityPeriod);

            if(requestSource.equals("register")) {
                // tempUser로 이메일 인증을 재전송하는 경우
                if (tempUser != null) {
                    log.info("Resending verification for registered user: " + tempUser.getEmail());
                    es.sendVerificationEmail(tempUser.getEmail(), newVerificationCode);
                    redirectAttributes.addFlashAttribute("message", "인증번호가 이메일로 재전송되었습니다.");
                } else {
                    log.info("ERROR: TEMP USER");

                    session.removeAttribute("tempUser");
                    session.removeAttribute("toggle");
                    session.removeAttribute("verificationCode");
                    session.removeAttribute("codeCreatedTime");

                    redirectAttributes.addFlashAttribute("timeError", "tempUser");
                }
            } else if(requestSource.equals("find")) {
                // 아이디/비밀번호 찾기용 이메일 인증 재전송
                if (userEmail != null) {
                    log.info("Resending verification for email: " + userEmail);
                    if (!userEmail.isEmpty()) {es.sendVerificationEmail(userEmail, newVerificationCode);}
                    redirectAttributes.addFlashAttribute("message", "인증번호가 이메일로 재전송되었습니다.");
                } else {
                    log.info("ERROR: TOGGLE");

                    session.removeAttribute("tempUser");
                    session.removeAttribute("toggle");
                    session.removeAttribute("verificationCode");
                    session.removeAttribute("codeCreatedTime");

                    redirectAttributes.addFlashAttribute("timeError", "toggle");
                }
            }
            return "redirect:/auth/emailConfirm";
        }

        @PreAuthorize("@customSecurityService.hasRequestSource(authentication)")
        @GetMapping("/registerConfirmed")
        public void registerConfirmedGET(HttpSession session) {
            session.removeAttribute("requestSource");
        }

        @GetMapping("/find")
        public void findGET(HttpSession session) {
            session.removeAttribute("tempUser");
            session.removeAttribute("toggle");
            session.removeAttribute("userEmail");
            session.removeAttribute("requestSource");
        }

        @PostMapping("/find")
        public String findPOST(@RequestParam("toggle") String toggle,
                               @RequestParam("email") String email,
                               HttpSession session,
                               RedirectAttributes redirectAttributes,
                               Model model) {

            String userEmail = us.findByEmail(email);

            // 랜덤 인증 코드 생성
            String verificationCode = UUID.randomUUID().toString().substring(0, 6);
            session.setAttribute("verificationCode", verificationCode);
            session.setAttribute("codeCreatedTime", LocalDateTime.now());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String validityPeriod = LocalDateTime.now().plus(Duration.ofSeconds(180)).format(formatter);
            log.info("VALIDITY PERIOD: " + validityPeriod);
            session.setAttribute("validityPeriod", validityPeriod);

            // 이메일 전송 (그 외 값 저장)
            session.setAttribute("userEmail", userEmail);
            session.setAttribute("toggle", toggle);
            session.setAttribute("requestSource", "find");

            if(!userEmail.isEmpty()) {
                log.info("EMAIL IS VALID");
                es.sendVerificationEmail(userEmail, verificationCode);
            }

            // 이메일 확인 페이지 인증 세션 설정
            session.setAttribute("authorizedForFindInfo", true);

            // 이메일 확인 페이지로 리다이렉트
            return "redirect:/auth/emailConfirm";
        }

        @PreAuthorize("@customSecurityService.hasRequestSource(authentication)")
        @GetMapping("/findInfo")
        public void findInfoGET(@ModelAttribute("toggle") String toggle,
                                HttpSession session) {
            log.info("FIND INFO Toggle: " + toggle);
            if(toggle.equals("id")) {
                //id 가져오기
            }
            //session.removeAttribute("requestSource");
        }


    }
