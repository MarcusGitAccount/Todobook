package com.bookstore.app.middleware;

import com.bookstore.app.entity.User;
import com.bookstore.app.middleware.util.MiddlewareUtils;
import com.bookstore.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class AdminFilter implements Filter {
  @SuppressWarnings("SpringJavaAutowiringInspection")
  @Autowired
  private UserRepo userRepo;

  @Override
  public void doFilter(ServletRequest servletRequest,
                       ServletResponse servletResponse,
                       FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest)servletRequest;
    HttpServletResponse response = (HttpServletResponse)servletResponse;

    if (request.getRequestURI().startsWith("/admin")) {
      String token = request.getHeader("authorization");
      String email = MiddlewareUtils.getEmailFromToken(token);
      User user = userRepo.findByEmail(email).orElse(null);

      if (user == null || user.getIsAdmin() == null || !user.getIsAdmin()) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You're not an admin");
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
