package com.profi_shop.controllers.auth;

import com.profi_shop.auth.requests.LoginRequest;
import com.profi_shop.auth.requests.SignUpRequest;
import com.profi_shop.model.User;
import com.profi_shop.model.enums.Role;
import com.profi_shop.security.JwtTokenProvider;
import com.profi_shop.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "message",required = false) Optional<String> message,
                        Model model){
        model.addAttribute("errorMessage",message.orElse(null));
        return "shop/login";
    }

    @GetMapping("/register")
    public String register(){
        return "shop/register";
    }


    @PostMapping("/signIn")
    public String authenticateUser(@Valid LoginRequest loginRequest,
                                   HttpServletRequest request,
                                   HttpServletResponse response,
                                   BindingResult bindingResult) {

        // Проверка наличия ошибок валидации
        if (bindingResult.hasErrors()) {
            return "shop/error";
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            ));
        } catch (Exception e) {
            if (e instanceof DisabledException) {
                return "shop/error";
            }
            return "shop/error";
        }

        User user = userService.getUserByUsername(loginRequest.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt =  jwtTokenProvider.generateToken(authentication);
        Cookie cookie = new Cookie("jwtToken", jwt);
        cookie.setHttpOnly(true); // Устанавливаем флаг HttpOnly для защиты от XSS атак
        cookie.setMaxAge(3600); // Устанавливаем срок действия куки в секундах (здесь 1 час)
        cookie.setPath("/");
        response.addCookie(cookie);

        if(user.getRole() != Role.ROLE_CUSTOMER){
            return "redirect:/admin/dashboard";
        }
        System.out.println("cookie set");
        return "redirect:/account";
    }

    @PostMapping("/signUp")
    public String register(@Valid SignUpRequest signUpRequest) {
        try {
            userService.signUp(signUpRequest);
            return "shop/account";
        }catch (Exception e){
            return "shop/register";
        }
    }

}
