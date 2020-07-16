package org.launchcode.javawebdevtechjobsauthentication;

import org.launchcode.javawebdevtechjobsauthentication.controllers.AuthenticationController;
import org.launchcode.javawebdevtechjobsauthentication.models.User;
import org.launchcode.javawebdevtechjobsauthentication.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter extends HandlerInterceptorAdapter {
    @Autowired
    UserRepository userRepo;

    @Autowired
    AuthenticationController authController;

    // What goes in the whitelist?
    // * The file names of the accessible files in resources/templates (except fragments)
    // * The file paths of the accessible static assets in resources/static
    public static final List<String> whitelist = Arrays.asList("/login","/register","/logout","/css","/js");

    private static boolean isWhitelisted(String path){
        // TODO: LAMBDA EXPRESSION! (What did Sean do?)
        for(String pathRoot : whitelist){
            if(path.startsWith(pathRoot)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws IOException {
        //return super.preHandle(req, res, handler);

        // Don't require sign-in for whitelisted pages
        if(isWhitelisted(req.getRequestURI())){         // check the whitelist status of the current request object
            return true;                                // request may process
        }

        HttpSession session = req.getSession();                     // grab the session information from a request object
        User user = authController.getUserFromSession(session);     // query the session data for a user

        if(user != null){   // user exists
            return true;    // the user is logged in
        }

        // The user is logged out
        res.sendRedirect("/login");     // make them log in.
        return false;
    }
}
