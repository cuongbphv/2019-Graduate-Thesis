//package com.graduate.thesis.backend.security;
//
//import com.auth0.jwt.JWT;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.graduate.thesis.backend.entity.UserAccount;
//import com.graduate.thesis.backend.util.Constant;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import javax.servlet.FilterChain;;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//
//import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
//import static com.graduate.thesis.backend.security.SecurityConstants.*;
//import static com.graduate.thesis.backend.security.SecurityConstants.EXPIRATION_TIME;
//
//public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private AuthenticationManager authenticationManager;
//
//    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(Constant.LOGIN_API, "POST"));
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest req,
//                                                HttpServletResponse res)
//            throws AuthenticationException {
//
//        try {
//            UserAccount userAccount = new ObjectMapper()
//                    .readValue(req.getInputStream(), UserAccount.class);
//
//            return authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            userAccount.getUsername(),
//                            userAccount.getPassword(),
//                            new ArrayList<>())
//            );
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest req,
//                                            HttpServletResponse res,
//                                            FilterChain chain,
//                                            Authentication auth) {
//
//        String token = JWT.create()
//                .withSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .sign(HMAC512(SECRET.getBytes()));
//        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
//    }
//}
