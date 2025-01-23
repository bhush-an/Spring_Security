package com.app.filter;

import com.app.dto.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    public CustomAccessDeniedHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Log the exception
        System.out.println("Access Denied: " + accessDeniedException.getMessage());

        // Set custom response
        ResponseDTO errorResponse = ResponseDTO.builder()
                .message("Unauthorized! : You don't have permission to access this resource.")
                .errorMessage(accessDeniedException.getMessage())
                .build();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 401 Unauthorized
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}
