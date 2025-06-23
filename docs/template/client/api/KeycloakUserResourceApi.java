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

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-06-23T12:26:39.485368339+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.13.0")
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
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return List&lt;KeycloakUserDTO&gt;
   */
  @RequestLine("GET /api/keycloak-users?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  List<KeycloakUserDTO> getAllKeycloakUsers(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);

  /**
   * 
   * Similar to <code>getAllKeycloakUsers</code> but it also returns the http response headers .
   * 
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/keycloak-users?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<KeycloakUserDTO>> getAllKeycloakUsersWithHttpInfo(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>getAllKeycloakUsers</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllKeycloakUsersQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
   *   <li>size - The size of the page to be returned (optional, default to 20)</li>
   *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
   *   </ul>
   * @return List&lt;KeycloakUserDTO&gt;
   */
  @RequestLine("GET /api/keycloak-users?page={page}&size={size}&sort={sort}")
  @Headers({
  "Accept: */*",
  })
  List<KeycloakUserDTO> getAllKeycloakUsers(@QueryMap(encoded=true) GetAllKeycloakUsersQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>getAllKeycloakUsers</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
          *   <li>size - The size of the page to be returned (optional, default to 20)</li>
          *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
      *   </ul>
          * @return List&lt;KeycloakUserDTO&gt;
      */
      @RequestLine("GET /api/keycloak-users?page={page}&size={size}&sort={sort}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<KeycloakUserDTO>> getAllKeycloakUsersWithHttpInfo(@QueryMap(encoded=true) GetAllKeycloakUsersQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllKeycloakUsers</code> method in a fluent style.
   */
  public static class GetAllKeycloakUsersQueryParams extends HashMap<String, Object> {
    public GetAllKeycloakUsersQueryParams page(@javax.annotation.Nullable final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAllKeycloakUsersQueryParams size(@javax.annotation.Nullable final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
    public GetAllKeycloakUsersQueryParams sort(@javax.annotation.Nullable final List<String> value) {
      put("sort", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

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
