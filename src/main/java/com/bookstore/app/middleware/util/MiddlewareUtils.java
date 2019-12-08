package com.bookstore.app.middleware.util;

import com.bookstore.app.controller.util.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;

public class MiddlewareUtils {

  public static String getEmailFromToken(String token) {
    Jws<Claims> jws = Jwts
        .parser()
        .requireSubject("auth")
        .setSigningKey(TextCodec.BASE64.decode(Constants.KEY))
        .parseClaimsJws(token);

    return jws.getBody().get("email", String.class);
  }
}
