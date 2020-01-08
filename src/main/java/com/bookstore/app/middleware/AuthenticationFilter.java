package com.bookstore.app.middleware;

import com.bookstore.app.middleware.util.MiddlewareUtils;
import com.bookstore.app.repo.AuthenticationJWTTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/api/*", "/admin/*"})
@Component
@Order(1)
public class AuthenticationFilter implements Filter {
  @SuppressWarnings("SpringJavaAutowiringInspection")
  @Autowired
  private AuthenticationJWTTokenRepo authenticationJWTTokenRepo;

  @Override
  public void doFilter(ServletRequest servletRequest,
                       ServletResponse servletResponse,
                       FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest)servletRequest;
    HttpServletResponse response = (HttpServletResponse)servletResponse;

    if (request.getRequestURI().startsWith("/admin") || request.getRequestURI().startsWith("/api")) {
      String token = request.getHeader("authorization");

      if (token == null) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please log in to access this resource");
        return;
      }
      String email = MiddlewareUtils.getEmailFromToken(token);

      if (authenticationJWTTokenRepo.findByUserEmail(email).orElse(null) == null) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void destroy() {

  }
}
