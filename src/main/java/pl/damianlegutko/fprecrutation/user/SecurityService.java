package pl.damianlegutko.fprecrutation.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SecurityService {
    String findLoggedInUsername();
    void signin(String username, String password);
    void logout(HttpServletRequest request, HttpServletResponse response);
}
