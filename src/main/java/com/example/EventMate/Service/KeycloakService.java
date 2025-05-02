package com.example.EventMate.Service;

import com.example.EventMate.Model.User;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;
import java.util.Collections;

@Service // Marcăm clasa ca un service gestionat de Spring
public class KeycloakService {

    private static final String SERVER_URL = "http://localhost:8080"; // URL-ul serverului Keycloak
    private static final String REALM = "eventmate"; // Numele realm-ului
    private static final String CLIENT_ID = "admin-cli"; // ID-ul clientului pentru accesul administrator
    private static final String ADMIN_USERNAME = "andra1183"; // Utilizator Keycloak cu rol de admin
    private static final String ADMIN_PASSWORD = "YFhmqHJKj7w.UaE"; // Parola utilizatorului admin

    private final Keycloak keycloak;

    public KeycloakService() {
        // Inițializăm interacțiunea cu Keycloak Admin API
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm("master") // Realm-ul principal pentru autentificare cu admin-cli
                .clientId(CLIENT_ID)
                .username(ADMIN_USERNAME)
                .password(ADMIN_PASSWORD)
                .build();
    }

    public void createUser(String username, String email, String password) {
        // Creăm un obiect pentru utilizatorul nou
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true); // Activăm contul nou creat
        user.setEmailVerified(true);
        user.setCredentials(Collections.singletonList(
                new CredentialRepresentation() {{
                    setType(CredentialRepresentation.PASSWORD);
                    setValue(password); // Setăm parola utilizatorului
                }}
        ));

        try {
            // Adăugăm utilizatorul nou în Keycloak
            keycloak.realm(REALM).users().create(user);
        } catch (Exception ex) {
            // Gestionăm excepțiile folosind ResponseStatusException pentru erori HTTP
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Eroare la crearea utilizatorului în Keycloak: " + ex.getMessage(), ex);
        }
    }
}