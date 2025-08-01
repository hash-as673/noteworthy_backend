package com.hashas673.noteworthy.web.controller;



import com.hashas673.noteworthy.auth.dto.RegisterRequest;
import com.hashas673.noteworthy.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthWebController {

    private final AuthService authService;

    public AuthWebController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registerRequest") @Valid RegisterRequest request,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            authService.register(request);
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            result.rejectValue("username", "error.username", e.getMessage());
            return "register";
        }
    }
}

