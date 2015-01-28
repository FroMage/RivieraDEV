package controllers;

import play.Logger;
import play.mvc.Http.Request;
import models.User;

public class Security extends Secure.Security {
    
    static boolean authenticate(String username, String password) {
        User user = User.find("byUserName", username).first();
        if(user != null && user.password.equals(password)){
        	return true;
        }
        logSecurityAction("failed login for %s", username);
        return false;
    }    
    
    private static void logSecurityAction(String message, Object... params) {
        Object[] newParams = new Object[params.length+1];
        System.arraycopy(params, 0, newParams, 1, params.length);
        newParams[0] = Request.current().remoteAddress;
        Logger.info("[SECURITY: %s]: "+message, newParams);
    }

}
