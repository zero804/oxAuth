package org.gluu.oxauth.client;

import org.apache.log4j.Logger;
import org.gluu.oxauth.model.common.AuthenticationMethod;
import org.gluu.oxauth.model.token.ClientAssertionType;
import org.jboss.resteasy.client.ClientRequest;


/**
 * @author Yuriy Zabrovarnyy
 */
public class ClientAuthnEnabler {

    private static final Logger LOG = Logger.getLogger(ClientAuthnEnabler.class);

    private ClientRequest clientRequest;

    public ClientAuthnEnabler(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

    public void exec(ClientAuthnRequest request){
        if (request.getAuthenticationMethod() == AuthenticationMethod.CLIENT_SECRET_BASIC
                && request.hasCredentials()) {
            clientRequest.header("Authorization", "Basic " + request.getEncodedCredentials());
            return;
        }

        if (request.getAuthenticationMethod() == AuthenticationMethod.CLIENT_SECRET_POST) {
            if (request.getAuthUsername() != null && !request.getAuthUsername().isEmpty()) {
                clientRequest.formParameter("client_id", request.getAuthUsername());
            }
            if (request.getAuthPassword() != null && !request.getAuthPassword().isEmpty()) {
                clientRequest.formParameter("client_secret", request.getAuthPassword());
            }
            return;
        }
        if (request.getAuthenticationMethod() == AuthenticationMethod.CLIENT_SECRET_JWT ||
                request.getAuthenticationMethod() == AuthenticationMethod.PRIVATE_KEY_JWT) {
            clientRequest.formParameter("client_assertion_type", ClientAssertionType.JWT_BEARER);
            if (request.getClientAssertion() != null) {
                clientRequest.formParameter("client_assertion", request.getClientAssertion());
            }
            if (request.getAuthUsername() != null && !request.getAuthUsername().isEmpty()) {
                clientRequest.formParameter("client_id", request.getAuthUsername());
            }
        }
    }
}
