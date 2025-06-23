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

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-06-23T12:26:39.485368339+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.13.0")
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
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return List&lt;UserDTO&gt;
   */
  @RequestLine("GET /api/users?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  List<UserDTO> getAllUsers(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);

  /**
   * 
   * Similar to <code>getAllUsers</code> but it also returns the http response headers .
   * 
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/users?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<UserDTO>> getAllUsersWithHttpInfo(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>getAllUsers</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllUsersQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
   *   <li>size - The size of the page to be returned (optional, default to 20)</li>
   *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
   *   </ul>
   * @return List&lt;UserDTO&gt;
   */
  @RequestLine("GET /api/users?page={page}&size={size}&sort={sort}")
  @Headers({
  "Accept: */*",
  })
  List<UserDTO> getAllUsers(@QueryMap(encoded=true) GetAllUsersQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>getAllUsers</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
          *   <li>size - The size of the page to be returned (optional, default to 20)</li>
          *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
      *   </ul>
          * @return List&lt;UserDTO&gt;
      */
      @RequestLine("GET /api/users?page={page}&size={size}&sort={sort}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<UserDTO>> getAllUsersWithHttpInfo(@QueryMap(encoded=true) GetAllUsersQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllUsers</code> method in a fluent style.
   */
  public static class GetAllUsersQueryParams extends HashMap<String, Object> {
    public GetAllUsersQueryParams page(@javax.annotation.Nullable final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAllUsersQueryParams size(@javax.annotation.Nullable final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
    public GetAllUsersQueryParams sort(@javax.annotation.Nullable final List<String> value) {
      put("sort", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

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
