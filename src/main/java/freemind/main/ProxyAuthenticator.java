package freemind.main;

import lombok.AllArgsConstructor;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import static org.apache.commons.lang3.StringUtils.defaultString;

@AllArgsConstructor
public final class ProxyAuthenticator extends Authenticator {

    private final String user, password;

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, defaultString(password).toCharArray());
    }
}
