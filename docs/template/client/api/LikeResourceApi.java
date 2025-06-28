package com.f4.reel.client.api;

import com.f4.reel.client.ApiClient;
import com.f4.reel.client.EncodingUtils;
import com.f4.reel.client.model.ApiResponse;

import com.f4.reel.client.model.LikeDTO;
import com.f4.reel.client.model.LikeWithRedisUserDTO;
import java.util.UUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-06-28T09:20:35.299334524+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.13.0")
public interface LikeResourceApi extends ApiClient.Api {


  /**
   * 
   * 
   * @param parentId  (required)
   * @param userId  (required)
   * @return Boolean
   */
  @RequestLine("GET /api/likes/exists?parentId={parentId}&userId={userId}")
  @Headers({
    "Accept: */*",
  })
  Boolean checkLikeExists(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("userId") @javax.annotation.Nonnull UUID userId);

  /**
   * 
   * Similar to <code>checkLikeExists</code> but it also returns the http response headers .
   * 
   * @param parentId  (required)
   * @param userId  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/likes/exists?parentId={parentId}&userId={userId}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<Boolean> checkLikeExistsWithHttpInfo(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("userId") @javax.annotation.Nonnull UUID userId);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>checkLikeExists</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link CheckLikeExistsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>parentId -  (required)</li>
   *   <li>userId -  (required)</li>
   *   </ul>
   * @return Boolean
   */
  @RequestLine("GET /api/likes/exists?parentId={parentId}&userId={userId}")
  @Headers({
  "Accept: */*",
  })
  Boolean checkLikeExists(@QueryMap(encoded=true) CheckLikeExistsQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>checkLikeExists</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>parentId -  (required)</li>
          *   <li>userId -  (required)</li>
      *   </ul>
          * @return Boolean
      */
      @RequestLine("GET /api/likes/exists?parentId={parentId}&userId={userId}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<Boolean> checkLikeExistsWithHttpInfo(@QueryMap(encoded=true) CheckLikeExistsQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>checkLikeExists</code> method in a fluent style.
   */
  public static class CheckLikeExistsQueryParams extends HashMap<String, Object> {
    public CheckLikeExistsQueryParams parentId(@javax.annotation.Nonnull final UUID value) {
      put("parentId", EncodingUtils.encode(value));
      return this;
    }
    public CheckLikeExistsQueryParams userId(@javax.annotation.Nonnull final UUID value) {
      put("userId", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * 
   * 
   * @param parentId  (required)
   * @param parentType  (required)
   * @return Long
   */
  @RequestLine("GET /api/likes/countByParentIdAndParentType?parentId={parentId}&parentType={parentType}")
  @Headers({
    "Accept: */*",
  })
  Long countByParentIdAndParentType(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("parentType") @javax.annotation.Nonnull String parentType);

  /**
   * 
   * Similar to <code>countByParentIdAndParentType</code> but it also returns the http response headers .
   * 
   * @param parentId  (required)
   * @param parentType  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/likes/countByParentIdAndParentType?parentId={parentId}&parentType={parentType}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<Long> countByParentIdAndParentTypeWithHttpInfo(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("parentType") @javax.annotation.Nonnull String parentType);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>countByParentIdAndParentType</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link CountByParentIdAndParentTypeQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>parentId -  (required)</li>
   *   <li>parentType -  (required)</li>
   *   </ul>
   * @return Long
   */
  @RequestLine("GET /api/likes/countByParentIdAndParentType?parentId={parentId}&parentType={parentType}")
  @Headers({
  "Accept: */*",
  })
  Long countByParentIdAndParentType(@QueryMap(encoded=true) CountByParentIdAndParentTypeQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>countByParentIdAndParentType</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>parentId -  (required)</li>
          *   <li>parentType -  (required)</li>
      *   </ul>
          * @return Long
      */
      @RequestLine("GET /api/likes/countByParentIdAndParentType?parentId={parentId}&parentType={parentType}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<Long> countByParentIdAndParentTypeWithHttpInfo(@QueryMap(encoded=true) CountByParentIdAndParentTypeQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>countByParentIdAndParentType</code> method in a fluent style.
   */
  public static class CountByParentIdAndParentTypeQueryParams extends HashMap<String, Object> {
    public CountByParentIdAndParentTypeQueryParams parentId(@javax.annotation.Nonnull final UUID value) {
      put("parentId", EncodingUtils.encode(value));
      return this;
    }
    public CountByParentIdAndParentTypeQueryParams parentType(@javax.annotation.Nonnull final String value) {
      put("parentType", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * 
   * 
   * @param parentIds  (required)
   * @param parentType  (required)
   * @return List&lt;Integer&gt;
   */
  @RequestLine("GET /api/likes/countLikesParentIdsAndParentType?parentIds={parentIds}&parentType={parentType}")
  @Headers({
    "Accept: */*",
  })
  List<Integer> countLikesParentIdsAndParentType(@Param("parentIds") @javax.annotation.Nonnull List<UUID> parentIds, @Param("parentType") @javax.annotation.Nonnull String parentType);

  /**
   * 
   * Similar to <code>countLikesParentIdsAndParentType</code> but it also returns the http response headers .
   * 
   * @param parentIds  (required)
   * @param parentType  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/likes/countLikesParentIdsAndParentType?parentIds={parentIds}&parentType={parentType}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<Integer>> countLikesParentIdsAndParentTypeWithHttpInfo(@Param("parentIds") @javax.annotation.Nonnull List<UUID> parentIds, @Param("parentType") @javax.annotation.Nonnull String parentType);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>countLikesParentIdsAndParentType</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link CountLikesParentIdsAndParentTypeQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>parentIds -  (required)</li>
   *   <li>parentType -  (required)</li>
   *   </ul>
   * @return List&lt;Integer&gt;
   */
  @RequestLine("GET /api/likes/countLikesParentIdsAndParentType?parentIds={parentIds}&parentType={parentType}")
  @Headers({
  "Accept: */*",
  })
  List<Integer> countLikesParentIdsAndParentType(@QueryMap(encoded=true) CountLikesParentIdsAndParentTypeQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>countLikesParentIdsAndParentType</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>parentIds -  (required)</li>
          *   <li>parentType -  (required)</li>
      *   </ul>
          * @return List&lt;Integer&gt;
      */
      @RequestLine("GET /api/likes/countLikesParentIdsAndParentType?parentIds={parentIds}&parentType={parentType}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<Integer>> countLikesParentIdsAndParentTypeWithHttpInfo(@QueryMap(encoded=true) CountLikesParentIdsAndParentTypeQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>countLikesParentIdsAndParentType</code> method in a fluent style.
   */
  public static class CountLikesParentIdsAndParentTypeQueryParams extends HashMap<String, Object> {
    public CountLikesParentIdsAndParentTypeQueryParams parentIds(@javax.annotation.Nonnull final List<UUID> value) {
      put("parentIds", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
    public CountLikesParentIdsAndParentTypeQueryParams parentType(@javax.annotation.Nonnull final String value) {
      put("parentType", EncodingUtils.encode(value));
      return this;
    }
  }

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
   * @param parentId  (required)
   * @param userId  (required)
   */
  @RequestLine("DELETE /api/likes/by-parent-and-user?parentId={parentId}&userId={userId}")
  @Headers({
    "Accept: application/json",
  })
  void deleteLikeByParentIdAndUserId(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("userId") @javax.annotation.Nonnull UUID userId);

  /**
   * 
   * Similar to <code>deleteLikeByParentIdAndUserId</code> but it also returns the http response headers .
   * 
   * @param parentId  (required)
   * @param userId  (required)
   */
  @RequestLine("DELETE /api/likes/by-parent-and-user?parentId={parentId}&userId={userId}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteLikeByParentIdAndUserIdWithHttpInfo(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("userId") @javax.annotation.Nonnull UUID userId);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>deleteLikeByParentIdAndUserId</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link DeleteLikeByParentIdAndUserIdQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>parentId -  (required)</li>
   *   <li>userId -  (required)</li>
   *   </ul>
   */
  @RequestLine("DELETE /api/likes/by-parent-and-user?parentId={parentId}&userId={userId}")
  @Headers({
  "Accept: application/json",
  })
  void deleteLikeByParentIdAndUserId(@QueryMap(encoded=true) DeleteLikeByParentIdAndUserIdQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>deleteLikeByParentIdAndUserId</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>parentId -  (required)</li>
          *   <li>userId -  (required)</li>
      *   </ul>
      */
      @RequestLine("DELETE /api/likes/by-parent-and-user?parentId={parentId}&userId={userId}")
      @Headers({
    "Accept: application/json",
      })
   ApiResponse<Void> deleteLikeByParentIdAndUserIdWithHttpInfo(@QueryMap(encoded=true) DeleteLikeByParentIdAndUserIdQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>deleteLikeByParentIdAndUserId</code> method in a fluent style.
   */
  public static class DeleteLikeByParentIdAndUserIdQueryParams extends HashMap<String, Object> {
    public DeleteLikeByParentIdAndUserIdQueryParams parentId(@javax.annotation.Nonnull final UUID value) {
      put("parentId", EncodingUtils.encode(value));
      return this;
    }
    public DeleteLikeByParentIdAndUserIdQueryParams userId(@javax.annotation.Nonnull final UUID value) {
      put("userId", EncodingUtils.encode(value));
      return this;
    }
  }

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
   * @param parentId  (required)
   * @param parentType  (required)
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return List&lt;LikeWithRedisUserDTO&gt;
   */
  @RequestLine("GET /api/likes/by-parent?parentId={parentId}&parentType={parentType}&page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  List<LikeWithRedisUserDTO> getLikesByParent(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("parentType") @javax.annotation.Nonnull String parentType, @Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);

  /**
   * 
   * Similar to <code>getLikesByParent</code> but it also returns the http response headers .
   * 
   * @param parentId  (required)
   * @param parentType  (required)
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/likes/by-parent?parentId={parentId}&parentType={parentType}&page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<LikeWithRedisUserDTO>> getLikesByParentWithHttpInfo(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("parentType") @javax.annotation.Nonnull String parentType, @Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>getLikesByParent</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetLikesByParentQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>parentId -  (required)</li>
   *   <li>parentType -  (required)</li>
   *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
   *   <li>size - The size of the page to be returned (optional, default to 20)</li>
   *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
   *   </ul>
   * @return List&lt;LikeWithRedisUserDTO&gt;
   */
  @RequestLine("GET /api/likes/by-parent?parentId={parentId}&parentType={parentType}&page={page}&size={size}&sort={sort}")
  @Headers({
  "Accept: */*",
  })
  List<LikeWithRedisUserDTO> getLikesByParent(@QueryMap(encoded=true) GetLikesByParentQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>getLikesByParent</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>parentId -  (required)</li>
          *   <li>parentType -  (required)</li>
          *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
          *   <li>size - The size of the page to be returned (optional, default to 20)</li>
          *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
      *   </ul>
          * @return List&lt;LikeWithRedisUserDTO&gt;
      */
      @RequestLine("GET /api/likes/by-parent?parentId={parentId}&parentType={parentType}&page={page}&size={size}&sort={sort}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<LikeWithRedisUserDTO>> getLikesByParentWithHttpInfo(@QueryMap(encoded=true) GetLikesByParentQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getLikesByParent</code> method in a fluent style.
   */
  public static class GetLikesByParentQueryParams extends HashMap<String, Object> {
    public GetLikesByParentQueryParams parentId(@javax.annotation.Nonnull final UUID value) {
      put("parentId", EncodingUtils.encode(value));
      return this;
    }
    public GetLikesByParentQueryParams parentType(@javax.annotation.Nonnull final String value) {
      put("parentType", EncodingUtils.encode(value));
      return this;
    }
    public GetLikesByParentQueryParams page(@javax.annotation.Nullable final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetLikesByParentQueryParams size(@javax.annotation.Nullable final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
    public GetLikesByParentQueryParams sort(@javax.annotation.Nullable final List<String> value) {
      put("sort", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

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
