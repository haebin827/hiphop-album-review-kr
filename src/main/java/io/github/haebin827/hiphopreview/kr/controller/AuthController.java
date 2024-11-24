package io.github.haebin827.hiphopreview.kr.controller;

import io.github.haebin827.hiphopreview.kr.dto.UserDTO;
import io.github.haebin827.hiphopreview.kr.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@Log4j2
@RequiredArgsConstructor
public class AuthController {

    private final AuthService as;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public void loginGET() {
        log.info("login GET.......................");
    }

    @GetMapping("/register")
    public void registerGET() {
    }

    @PostMapping("/register")
    public String registerPOST(@Valid UserDTO userDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        if (result.hasErrors()) {
            return "/auth/register";
        }
        as.registerUser(userDTO);

        redirectAttributes.addFlashAttribute("nickname", userDTO.getNickname());
        log.info("REGISTER CONFIRMED NICKNAME: " + userDTO.getNickname());
        /*session.setAttribute("isRegistered", true); // 플래그 설정*/
        return "redirect:/auth/registerConfirmed";
    }

    //@PreAuthorize("@customSecurityService.isUserRecentlyRegistered(authentication)")
    @GetMapping("/registerConfirmed")
    public void registerConfirmedGET() {}
}
