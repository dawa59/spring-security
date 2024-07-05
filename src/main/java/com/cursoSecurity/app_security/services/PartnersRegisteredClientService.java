package com.cursoSecurity.app_security.services;

import com.cursoSecurity.app_security.entities.PartnersEntity;
import com.cursoSecurity.app_security.repository.PartnersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PartnersRegisteredClientService implements RegisteredClientRepository {


  private PartnersRepository partnersRepository;
  @Override
  public RegisteredClient findByClientId(String clientId) {
    Optional<PartnersEntity> partnersOpt = partnersRepository.findByClientId(clientId);

    return (RegisteredClient) partnersOpt.map(partner -> {
      List<AuthorizationGrantType> authorizationGrantTypes = Arrays.stream(partner.getGrantTypes().split(","))
              .map(AuthorizationGrantType::new)
              .toList();

      List<ClientAuthenticationMethod> clientAuthenticationMethods = Arrays.stream(partner.getAuthenticationMethods().split(","))
              .map(ClientAuthenticationMethod::new)
              .toList();

      List<String> scopes = Arrays.stream(partner.getScopes().split(",")).toList();
      return RegisteredClient
              .withId(partner.getId().toString())
              .clientSecret(partner.getClientSecret())
              .clientName(partner.getClientName())
              .redirectUri(partner.getRedirectUri())
              .postLogoutRedirectUri(partner.getRedirectUriLogout())
              .clientAuthenticationMethod(clientAuthenticationMethods.get(0))
              .clientAuthenticationMethod(clientAuthenticationMethods.get(1))
              .scope(scopes.get(0))
              .scope(scopes.get(1))
              .authorizationGrantType(authorizationGrantTypes.get(0))
              .authorizationGrantType(authorizationGrantTypes.get(1))
              .tokenSettings(tokenSettings())
              .build();

    }).orElseThrow(() -> new BadCredentialsException("Client not exist"));
  }

  private TokenSettings tokenSettings (){
    return TokenSettings.builder()
            .accessTokenTimeToLive(Duration.ofHours(8))
            .build();
  }


  @Override
  public void save(RegisteredClient registeredClient) {

  }

  @Override
  public RegisteredClient findById(String id) {
    return null;
  }

}
