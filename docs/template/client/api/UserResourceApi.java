package com.f4.reel.client.api;

import com.f4.reel.client.ApiClient;
import com.f4.reel.client.EncodingUtils;
import com.f4.reel.client.model.ApiResponse;

import java.util.UUID;
import com.f4.reel.client.model.UserDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-06-25T22:28:34.182979213+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.13.0")
public interface UserResourceApi extends ApiClient.Api {


  /**
   * 
   * 
   * @param userDTO  (required)
   * @return UserDTO
   */
  @RequestLine("POST /api/users")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  UserDTO createUser(@javax.annotation.Nonnull UserDTO userDTO);

  /**
   * 
   * Similar to <code>createUser</code> but it also returns the http response headers .
   * 
   * @param userDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/users")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<UserDTO> createUserWithHttpInfo(@javax.annotation.Nonnull UserDTO userDTO);



  /**
   * 
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/users/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteUser(@Param("id") @javax.annotation.Nonnull UUID id);

  /**
   * 
   * Similar to <code>deleteUser</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/users/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteUserWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id);



  /**
   * 
   * 
   * @return List&lt;UserDTO&gt;
   */
  @RequestLine("GET /api/users")
  @Headers({
    "Accept: */*",
  })
  List<UserDTO> getAllUsers();

  /**
   * 
   * Similar to <code>getAllUsers</code> but it also returns the http response headers .
   * 
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/users")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<UserDTO>> getAllUsersWithHttpInfo();



  /**
   * 
   * 
   * @param id  (required)
   * @return UserDTO
   */
  @RequestLine("GET /api/users/{id}")
  @Headers({
    "Accept: */*",
  })
  UserDTO getUser(@Param("id") @javax.annotation.Nonnull UUID id);

  /**
   * 
   * Similar to <code>getUser</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/users/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<UserDTO> getUserWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id);



  /**
   * 
   * 
   * @param id  (required)
   * @param userDTO  (required)
   * @return UserDTO
   */
  @RequestLine("PATCH /api/users/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  UserDTO partialUpdateUser(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull UserDTO userDTO);

  /**
   * 
   * Similar to <code>partialUpdateUser</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @param userDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PATCH /api/users/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<UserDTO> partialUpdateUserWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull UserDTO userDTO);



  /**
   * 
   * 
   * @param id  (required)
   * @param userDTO  (required)
   * @return UserDTO
   */
  @RequestLine("PUT /api/users/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  UserDTO updateUser(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull UserDTO userDTO);

  /**
   * 
   * Similar to <code>updateUser</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @param userDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /api/users/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<UserDTO> updateUserWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull UserDTO userDTO);


}
