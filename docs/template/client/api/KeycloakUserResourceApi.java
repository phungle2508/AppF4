package com.f4.reel.client.api;

import com.f4.reel.client.ApiClient;
import com.f4.reel.client.EncodingUtils;
import com.f4.reel.client.model.ApiResponse;

import com.f4.reel.client.model.KeycloakUserDTO;
import java.util.UUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-06-26T07:34:31.980696460+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.13.0")
public interface KeycloakUserResourceApi extends ApiClient.Api {


  /**
   * 
   * 
   * @param keycloakUserDTO  (required)
   * @return KeycloakUserDTO
   */
  @RequestLine("POST /api/keycloak-users")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  KeycloakUserDTO createKeycloakUser(@javax.annotation.Nonnull KeycloakUserDTO keycloakUserDTO);

  /**
   * 
   * Similar to <code>createKeycloakUser</code> but it also returns the http response headers .
   * 
   * @param keycloakUserDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/keycloak-users")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<KeycloakUserDTO> createKeycloakUserWithHttpInfo(@javax.annotation.Nonnull KeycloakUserDTO keycloakUserDTO);



  /**
   * 
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/keycloak-users/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteKeycloakUser(@Param("id") @javax.annotation.Nonnull UUID id);

  /**
   * 
   * Similar to <code>deleteKeycloakUser</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/keycloak-users/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteKeycloakUserWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id);



  /**
   * 
   * 
   * @return List&lt;KeycloakUserDTO&gt;
   */
  @RequestLine("GET /api/keycloak-users")
  @Headers({
    "Accept: */*",
  })
  List<KeycloakUserDTO> getAllKeycloakUsers();

  /**
   * 
   * Similar to <code>getAllKeycloakUsers</code> but it also returns the http response headers .
   * 
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/keycloak-users")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<KeycloakUserDTO>> getAllKeycloakUsersWithHttpInfo();



  /**
   * 
   * 
   * @param id  (required)
   * @return KeycloakUserDTO
   */
  @RequestLine("GET /api/keycloak-users/{id}")
  @Headers({
    "Accept: */*",
  })
  KeycloakUserDTO getKeycloakUser(@Param("id") @javax.annotation.Nonnull UUID id);

  /**
   * 
   * Similar to <code>getKeycloakUser</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/keycloak-users/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<KeycloakUserDTO> getKeycloakUserWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id);



  /**
   * 
   * 
   * @param id  (required)
   * @param keycloakUserDTO  (required)
   * @return KeycloakUserDTO
   */
  @RequestLine("PATCH /api/keycloak-users/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  KeycloakUserDTO partialUpdateKeycloakUser(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull KeycloakUserDTO keycloakUserDTO);

  /**
   * 
   * Similar to <code>partialUpdateKeycloakUser</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @param keycloakUserDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PATCH /api/keycloak-users/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<KeycloakUserDTO> partialUpdateKeycloakUserWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull KeycloakUserDTO keycloakUserDTO);



  /**
   * 
   * 
   * @param id  (required)
   * @param keycloakUserDTO  (required)
   * @return KeycloakUserDTO
   */
  @RequestLine("PUT /api/keycloak-users/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  KeycloakUserDTO updateKeycloakUser(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull KeycloakUserDTO keycloakUserDTO);

  /**
   * 
   * Similar to <code>updateKeycloakUser</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @param keycloakUserDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /api/keycloak-users/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<KeycloakUserDTO> updateKeycloakUserWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull KeycloakUserDTO keycloakUserDTO);


}
