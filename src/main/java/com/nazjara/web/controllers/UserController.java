package com.nazjara.web.controllers;

import com.nazjara.security.domain.User;
import com.nazjara.security.repository.UserRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final GoogleAuthenticator googleAuthenticator;

    @GetMapping("/register2fa")
    public String register2fa(Model model) {
        User user = getUser();

        String url = GoogleAuthenticatorQRGenerator.getOtpAuthURL("SFG", user.getUsername(),
                googleAuthenticator.createCredentials(user.getUsername()));

        log.debug("Google QR URL: " + url);

        model.addAttribute("googleurl", url);

        return "user/register2fa";
    }

    @PostMapping("/confirm2fa")
    public ModelAndView confirm2fa(@RequestParam Integer verifyCode) {
        User savedUser = getUser();

        log.debug("Entered code is: " + verifyCode);

        User user1 = userRepository.findById(savedUser.getId()).orElseThrow();

        if (googleAuthenticator.authorizeUser(savedUser.getUsername(), verifyCode)) {

            user1.setUseGoogle2fa(true);
            userRepository.save(user1);

            return new ModelAndView("/index");
        } else {
            return new ModelAndView("redirect:/user/register2fa");
        }
    }

    @GetMapping("/verify2fa")
    public String verify2fa() {
        return "user/verify2fa";
    }

    @PostMapping("/verify2fa")
    public String verify2fa(@RequestParam Integer verifyCode) {
        User user = getUser();

        if (googleAuthenticator.authorizeUser(user.getUsername(), verifyCode)) {
            user.setGoogle2faRequired(false);

            return "/index";
        } else {
            return "user/verify2fa";
        }
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
