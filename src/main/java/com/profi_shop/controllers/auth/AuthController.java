package com.profi_shop.controllers.auth;

import com.profi_shop.auth.requests.LoginRequest;
import com.profi_shop.auth.requests.SignUpRequest;
import com.profi_shop.model.Category;
import com.profi_shop.model.User;
import com.profi_shop.model.enums.Role;
import com.profi_shop.security.JwtTokenProvider;
import com.profi_shop.services.CategoryService;
import com.profi_shop.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.management.RuntimeErrorException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final CategoryService categoryService;

    @Autowired
    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, CategoryService categoryService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.categoryService = categoryService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "message",required = false) Optional<String> message,
                        Model model){
        List<Category> categories = categoryService.getMainCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("errorMessage",message.orElse(null));
        return "shop/login";
    }

    @GetMapping("/register")
    public String register(Model model){
        List<Category> categories = categoryService.getMainCategories();
        model.addAttribute("categories", categories);
        return "shop/register";
    }

    @GetMapping("/passwordReset")
    public String passwordReset(Model model){
        List<Category> categories = categoryService.getMainCategories();
        model.addAttribute("categories", categories);
        return "shop/passwordReset";
    }


    @PostMapping("/signIn")
    public ResponseEntity<Map<String,String>> authenticateUser(@Valid LoginRequest loginRequest,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {

        Map<String,String> responseMessage = new HashMap<>();
        // Проверка наличия ошибок валидации

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            ));
        } catch (Exception e) {
            if (e instanceof DisabledException) {
                responseMessage.put("error", "Ваш аккаунт заблокирован!");
                return new ResponseEntity<>(responseMessage, HttpStatus.UNAUTHORIZED);
            }
            responseMessage.put("error", "Неправильные логин или пароль!");
            return new ResponseEntity<>(responseMessage,HttpStatus.BAD_REQUEST);
        }

        User user = userService.getUserByUsername(loginRequest.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt =  jwtTokenProvider.generateToken(authentication);
        Cookie cookie = new Cookie("jwtToken", jwt);
        cookie.setHttpOnly(true); // Устанавливаем флаг HttpOnly для защиты от XSS атак
        cookie.setMaxAge(3*3600); // Устанавливаем срок действия куки в секундах (здесь 1 час)
        cookie.setPath("/");
        response.addCookie(cookie);
        responseMessage.put("message", "Success");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity<Map<String,String>> register(@Valid SignUpRequest signUpRequest,
                                                       BindingResult bindingResult) {

        Map<String, String> response = new HashMap<>();
        if(bindingResult.hasErrors()){
            response.put("error", "Пароли не совпадают");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            userService.signUp(signUpRequest);
            response.put("message", "Регистрация завершилась успешно");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String username) {
        try {
            System.out.println("request to reset password of user " + username);
            userService.resetPassword(username);
            System.out.println("password is reset");
            return new ResponseEntity<>("Success",HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>("ERROR",HttpStatus.BAD_REQUEST);
        }
    }
}
