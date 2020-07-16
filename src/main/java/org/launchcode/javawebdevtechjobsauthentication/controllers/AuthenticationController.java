package org.launchcode.javawebdevtechjobsauthentication.controllers;

import org.launchcode.javawebdevtechjobsauthentication.models.User;
import org.launchcode.javawebdevtechjobsauthentication.models.data.UserRepository;
import org.launchcode.javawebdevtechjobsauthentication.models.dto.LoginFormDTO;
import org.launchcode.javawebdevtechjobsauthentication.models.dto.RegisterFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthenticationController {
    @Autowired
    UserRepository userRepo;

    private static final String userSessionKey = "user";    // TODO: Make sure this is the right value.

    public User getUserFromSession(HttpSession session){
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if(userId == null){
            return null;
        }

        Optional<User> user = userRepo.findById(userId);

        if(user.isEmpty()){
            return null;
        }

        return user.get();
    }

    // NOTE: Back in coding_events, this was set private. What would happen if I set it public? (Would this compromise security?)
    public static void setUserInSession(HttpSession session, User user){
        session.setAttribute(userSessionKey, user.getId());
    }

    // NOTE: as much as it drives me nuts, we'll say "display" instead of "render" in this studio

    @GetMapping("/register")
    public String displayRegistrationForm(Model model){
        model.addAttribute(new RegisterFormDTO());
        // model.addAttribute("title","Register");  // TODO: Do we still have a model attribute?
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(
            @ModelAttribute @Valid RegisterFormDTO registerFormDTO,
            Errors errors,
            HttpServletRequest req,
            Model model
    ){
        if(errors.hasErrors()){
            //model.addAttribute("title","Register");
            return "register";
        }

        User existingUser = userRepo.findUserByUsername(registerFormDTO.getUsername());
        if(existingUser != null){
            errors.rejectValue(
                    "username",
                    "username.alreadyexists",
                    "Sorry, that username already exists. Pick something else and try again."
            );
            //model.addAttribute("title","Register");
            return "register";
        }

        String password       = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if(!password.equals(verifyPassword)){
            errors.rejectValue(
                    "password",
                    "password.mismatch",
                    "Sorry, the passwords don't match. Try entering them in again."
            );
            //model.addAttribute("title","Register");
            return "register";
        }

        User newUser = new User(registerFormDTO.getUsername(),registerFormDTO.getPassword());   // create new user
        userRepo.save(newUser);                                                                 // save user to the database
        setUserInSession(req.getSession(),newUser);                                               // create new user session
        return "redirect:";                                                                     // redirect to the homepage
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model){
        model.addAttribute(new LoginFormDTO());
        //model.addAttribute("title","Log In");
        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(
            @ModelAttribute @Valid LoginFormDTO loginFormDTO,
            Errors errors,
            HttpServletRequest req,
            Model model
    ){
        if(errors.hasErrors()){
            //model.addAttribute("title","Log In");
            return "login";
        }

        User theUser = userRepo.findUserByUsername(loginFormDTO.getUsername());
        if(theUser == null){
            errors.rejectValue(
                    "username",
                    "user.invalid",
                    "Sorry, that user does not exist."
            );
            //model.addAttribute("title","Log In");
            return "login";
        }

        String password = loginFormDTO.getPassword();
        if(!theUser.isMatchingPassword(password)){
            errors.rejectValue(
                    "password",
                    "password.invalid",
                    "Invalid password. Try again."
            );
            //model.addAttribute("title","Log In");
            return "login";
        }

        setUserInSession(req.getSession(),theUser);
        return "redirect:";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req){
        req.getSession().invalidate();
        return "redirect:/login";
    }
}
