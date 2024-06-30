package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("/signup")
public class SignupController {
    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String signupView() {
        return "signup";
    }

    @PostMapping()
    public String signupUser(@ModelAttribute User user, Model model) {
        String signupError = null;

        // Check exist user
        if (!userService.isUsernameAvailable(user.getUsername())) {
            signupError = "The username already exists.";
        }

        if (signupError == null) {
            // Create new User
            int rowsAdded = userService.createUser(user);
            if (rowsAdded < 0) {
                // If create fail then set error message
                signupError = "There was an error signing you up. Please try again.";
            }
        }

        // Response message to client
        if (signupError == null) {
            model.addAttribute("signupSuccess", 1);
        } else {
                model.addAttribute("signupError", signupError);
                model.addAttribute("signupSuccess", 0);
        }

        return "signup";
    }
}
