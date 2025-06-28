package com.f4.reel.client.api;

import com.f4.reel.client.ApiClient;
import com.f4.reel.client.EncodingUtils;
import com.f4.reel.client.model.ApiResponse;

import com.f4.reel.client.model.ReelDTO;
import java.util.UUID;
import com.f4.reel.client.model.UserDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-06-28T09:20:35.299334524+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.13.0")
public interface ReelResourceApi extends ApiClient.Api {


  /**
   * 
   * 
   * @param reelDTO  (required)
   * @return ReelDTO
   */
  @RequestLine("POST /api/reels")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ReelDTO createReel(@javax.annotation.Nonnull ReelDTO reelDTO);

  /**
   * 
   * Similar to <code>createReel</code> but it also returns the http response headers .
   * 
   * @param reelDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/reels")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<ReelDTO> createReelWithHttpInfo(@javax.annotation.Nonnull ReelDTO reelDTO);



  /**
   * 
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/reels/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteReel(@Param("id") @javax.annotation.Nonnull UUID id);

  /**
   * 
   * Similar to <code>deleteReel</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/reels/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteReelWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id);



  /**
   * 
   * 
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return List&lt;ReelDTO&gt;
   */
  @RequestLine("GET /api/reels?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  List<ReelDTO> getAllReels(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);

  /**
   * 
   * Similar to <code>getAllReels</code> but it also returns the http response headers .
   * 
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/reels?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<ReelDTO>> getAllReelsWithHttpInfo(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>getAllReels</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllReelsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
   *   <li>size - The size of the page to be returned (optional, default to 20)</li>
   *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
   *   </ul>
   * @return List&lt;ReelDTO&gt;
   */
  @RequestLine("GET /api/reels?page={page}&size={size}&sort={sort}")
  @Headers({
  "Accept: */*",
  })
  List<ReelDTO> getAllReels(@QueryMap(encoded=true) GetAllReelsQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>getAllReels</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
          *   <li>size - The size of the page to be returned (optional, default to 20)</li>
          *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
      *   </ul>
          * @return List&lt;ReelDTO&gt;
      */
      @RequestLine("GET /api/reels?page={page}&size={size}&sort={sort}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<ReelDTO>> getAllReelsWithHttpInfo(@QueryMap(encoded=true) GetAllReelsQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllReels</code> method in a fluent style.
   */
  public static class GetAllReelsQueryParams extends HashMap<String, Object> {
    public GetAllReelsQueryParams page(@javax.annotation.Nullable final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAllReelsQueryParams size(@javax.annotation.Nullable final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
    public GetAllReelsQueryParams sort(@javax.annotation.Nullable final List<String> value) {
      put("sort", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * 
   * 
   * @param id  (required)
   * @return ReelDTO
   */
  @RequestLine("GET /api/reels/{id}")
  @Headers({
    "Accept: */*",
  })
  ReelDTO getReel(@Param("id") @javax.annotation.Nonnull UUID id);

  /**
   * 
   * Similar to <code>getReel</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/reels/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<ReelDTO> getReelWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id);



  /**
   * 
   * 
   * @param id  (required)
   * @param reelDTO  (required)
   * @return ReelDTO
   */
  @RequestLine("PATCH /api/reels/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ReelDTO partialUpdateReel(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull ReelDTO reelDTO);

  /**
   * 
   * Similar to <code>partialUpdateReel</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @param reelDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PATCH /api/reels/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<ReelDTO> partialUpdateReelWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull ReelDTO reelDTO);



  /**
   * 
   * 
   * @param query  (required)
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return List&lt;ReelDTO&gt;
   */
  @RequestLine("GET /api/reels/_search?query={query}&page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  List<ReelDTO> searchReels(@Param("query") @javax.annotation.Nonnull String query, @Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);

  /**
   * 
   * Similar to <code>searchReels</code> but it also returns the http response headers .
   * 
   * @param query  (required)
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/reels/_search?query={query}&page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<ReelDTO>> searchReelsWithHttpInfo(@Param("query") @javax.annotation.Nonnull String query, @Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>searchReels</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SearchReelsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>query -  (required)</li>
   *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
   *   <li>size - The size of the page to be returned (optional, default to 20)</li>
   *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
   *   </ul>
   * @return List&lt;ReelDTO&gt;
   */
  @RequestLine("GET /api/reels/_search?query={query}&page={page}&size={size}&sort={sort}")
  @Headers({
  "Accept: */*",
  })
  List<ReelDTO> searchReels(@QueryMap(encoded=true) SearchReelsQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>searchReels</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>query -  (required)</li>
          *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
          *   <li>size - The size of the page to be returned (optional, default to 20)</li>
          *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
      *   </ul>
          * @return List&lt;ReelDTO&gt;
      */
      @RequestLine("GET /api/reels/_search?query={query}&page={page}&size={size}&sort={sort}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<ReelDTO>> searchReelsWithHttpInfo(@QueryMap(encoded=true) SearchReelsQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>searchReels</code> method in a fluent style.
   */
  public static class SearchReelsQueryParams extends HashMap<String, Object> {
    public SearchReelsQueryParams query(@javax.annotation.Nonnull final String value) {
      put("query", EncodingUtils.encode(value));
      return this;
    }
    public SearchReelsQueryParams page(@javax.annotation.Nullable final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public SearchReelsQueryParams size(@javax.annotation.Nullable final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
    public SearchReelsQueryParams sort(@javax.annotation.Nullable final List<String> value) {
      put("sort", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * 
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 20)
   * @return List&lt;UserDTO&gt;
   */
  @RequestLine("GET /api/reels/test-users?page={page}&size={size}")
  @Headers({
    "Accept: */*",
  })
  List<UserDTO> testGetAllUsers(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size);

  /**
   * 
   * Similar to <code>testGetAllUsers</code> but it also returns the http response headers .
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 20)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/reels/test-users?page={page}&size={size}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<UserDTO>> testGetAllUsersWithHttpInfo(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>testGetAllUsers</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link TestGetAllUsersQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 20)</li>
   *   </ul>
   * @return List&lt;UserDTO&gt;
   */
  @RequestLine("GET /api/reels/test-users?page={page}&size={size}")
  @Headers({
  "Accept: */*",
  })
  List<UserDTO> testGetAllUsers(@QueryMap(encoded=true) TestGetAllUsersQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>testGetAllUsers</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>page -  (optional, default to 0)</li>
          *   <li>size -  (optional, default to 20)</li>
      *   </ul>
          * @return List&lt;UserDTO&gt;
      */
      @RequestLine("GET /api/reels/test-users?page={page}&size={size}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<UserDTO>> testGetAllUsersWithHttpInfo(@QueryMap(encoded=true) TestGetAllUsersQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>testGetAllUsers</code> method in a fluent style.
   */
  public static class TestGetAllUsersQueryParams extends HashMap<String, Object> {
    public TestGetAllUsersQueryParams page(@javax.annotation.Nullable final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public TestGetAllUsersQueryParams size(@javax.annotation.Nullable final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * 
   * 
   * @param id  (required)
   * @param reelDTO  (required)
   * @return ReelDTO
   */
  @RequestLine("PUT /api/reels/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ReelDTO updateReel(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull ReelDTO reelDTO);

  /**
   * 
   * Similar to <code>updateReel</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @param reelDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /api/reels/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<ReelDTO> updateReelWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull ReelDTO reelDTO);


}
