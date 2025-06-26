package com.f4.reel.client.api;

import com.f4.reel.client.ApiClient;
import com.f4.reel.client.EncodingUtils;
import com.f4.reel.client.model.ApiResponse;

import com.f4.reel.client.model.LikeDTO;
import java.util.UUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-06-26T10:46:36.107672237+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.13.0")
public interface LikeResourceApi extends ApiClient.Api {


  /**
   * 
   * 
   * @param likeDTO  (required)
   * @return LikeDTO
   */
  @RequestLine("POST /api/likes")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  LikeDTO createLike(@javax.annotation.Nonnull LikeDTO likeDTO);

  /**
   * 
   * Similar to <code>createLike</code> but it also returns the http response headers .
   * 
   * @param likeDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/likes")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<LikeDTO> createLikeWithHttpInfo(@javax.annotation.Nonnull LikeDTO likeDTO);



  /**
   * 
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/likes/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteLike(@Param("id") @javax.annotation.Nonnull UUID id);

  /**
   * 
   * Similar to <code>deleteLike</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/likes/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteLikeWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id);



  /**
   * 
   * 
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return List&lt;LikeDTO&gt;
   */
  @RequestLine("GET /api/likes?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  List<LikeDTO> getAllLikes(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);

  /**
   * 
   * Similar to <code>getAllLikes</code> but it also returns the http response headers .
   * 
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/likes?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<LikeDTO>> getAllLikesWithHttpInfo(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>getAllLikes</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllLikesQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
   *   <li>size - The size of the page to be returned (optional, default to 20)</li>
   *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
   *   </ul>
   * @return List&lt;LikeDTO&gt;
   */
  @RequestLine("GET /api/likes?page={page}&size={size}&sort={sort}")
  @Headers({
  "Accept: */*",
  })
  List<LikeDTO> getAllLikes(@QueryMap(encoded=true) GetAllLikesQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>getAllLikes</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
          *   <li>size - The size of the page to be returned (optional, default to 20)</li>
          *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
      *   </ul>
          * @return List&lt;LikeDTO&gt;
      */
      @RequestLine("GET /api/likes?page={page}&size={size}&sort={sort}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<LikeDTO>> getAllLikesWithHttpInfo(@QueryMap(encoded=true) GetAllLikesQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllLikes</code> method in a fluent style.
   */
  public static class GetAllLikesQueryParams extends HashMap<String, Object> {
    public GetAllLikesQueryParams page(@javax.annotation.Nullable final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAllLikesQueryParams size(@javax.annotation.Nullable final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
    public GetAllLikesQueryParams sort(@javax.annotation.Nullable final List<String> value) {
      put("sort", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * 
   * 
   * @param id  (required)
   * @return LikeDTO
   */
  @RequestLine("GET /api/likes/{id}")
  @Headers({
    "Accept: */*",
  })
  LikeDTO getLike(@Param("id") @javax.annotation.Nonnull UUID id);

  /**
   * 
   * Similar to <code>getLike</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/likes/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<LikeDTO> getLikeWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id);



  /**
   * 
   * 
   * @param id  (required)
   * @param likeDTO  (required)
   * @return LikeDTO
   */
  @RequestLine("PATCH /api/likes/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  LikeDTO partialUpdateLike(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull LikeDTO likeDTO);

  /**
   * 
   * Similar to <code>partialUpdateLike</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @param likeDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PATCH /api/likes/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<LikeDTO> partialUpdateLikeWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull LikeDTO likeDTO);



  /**
   * 
   * 
   * @param id  (required)
   * @param likeDTO  (required)
   * @return LikeDTO
   */
  @RequestLine("PUT /api/likes/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  LikeDTO updateLike(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull LikeDTO likeDTO);

  /**
   * 
   * Similar to <code>updateLike</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @param likeDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /api/likes/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<LikeDTO> updateLikeWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull LikeDTO likeDTO);


}
