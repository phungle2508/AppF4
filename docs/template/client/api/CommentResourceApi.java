package com.f4.reel.client.api;

import com.f4.reel.client.ApiClient;
import com.f4.reel.client.EncodingUtils;
import com.f4.reel.client.model.ApiResponse;

import com.f4.reel.client.model.CommentDTO;
import com.f4.reel.client.model.CommentWithRedisUserDTO;
import java.util.UUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-06-27T09:24:56.232976487+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.13.0")
public interface CommentResourceApi extends ApiClient.Api {


  /**
   * 
   * 
   * @param parentId  (required)
   * @param parentType  (required)
   * @return Long
   */
  @RequestLine("GET /api/comments/countByParentIdAndParentType?parentId={parentId}&parentType={parentType}")
  @Headers({
    "Accept: */*",
  })
  Long countByParentIdAndParentType1(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("parentType") @javax.annotation.Nonnull String parentType);

  /**
   * 
   * Similar to <code>countByParentIdAndParentType1</code> but it also returns the http response headers .
   * 
   * @param parentId  (required)
   * @param parentType  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/comments/countByParentIdAndParentType?parentId={parentId}&parentType={parentType}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<Long> countByParentIdAndParentType1WithHttpInfo(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("parentType") @javax.annotation.Nonnull String parentType);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>countByParentIdAndParentType1</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link CountByParentIdAndParentType1QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>parentId -  (required)</li>
   *   <li>parentType -  (required)</li>
   *   </ul>
   * @return Long
   */
  @RequestLine("GET /api/comments/countByParentIdAndParentType?parentId={parentId}&parentType={parentType}")
  @Headers({
  "Accept: */*",
  })
  Long countByParentIdAndParentType1(@QueryMap(encoded=true) CountByParentIdAndParentType1QueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>countByParentIdAndParentType1</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>parentId -  (required)</li>
          *   <li>parentType -  (required)</li>
      *   </ul>
          * @return Long
      */
      @RequestLine("GET /api/comments/countByParentIdAndParentType?parentId={parentId}&parentType={parentType}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<Long> countByParentIdAndParentType1WithHttpInfo(@QueryMap(encoded=true) CountByParentIdAndParentType1QueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>countByParentIdAndParentType1</code> method in a fluent style.
   */
  public static class CountByParentIdAndParentType1QueryParams extends HashMap<String, Object> {
    public CountByParentIdAndParentType1QueryParams parentId(@javax.annotation.Nonnull final UUID value) {
      put("parentId", EncodingUtils.encode(value));
      return this;
    }
    public CountByParentIdAndParentType1QueryParams parentType(@javax.annotation.Nonnull final String value) {
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
  @RequestLine("GET /api/comments/countCommentsParentIdsAndParentType?parentIds={parentIds}&parentType={parentType}")
  @Headers({
    "Accept: */*",
  })
  List<Integer> countCommentsParentIdsAndParentType(@Param("parentIds") @javax.annotation.Nonnull List<UUID> parentIds, @Param("parentType") @javax.annotation.Nonnull String parentType);

  /**
   * 
   * Similar to <code>countCommentsParentIdsAndParentType</code> but it also returns the http response headers .
   * 
   * @param parentIds  (required)
   * @param parentType  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/comments/countCommentsParentIdsAndParentType?parentIds={parentIds}&parentType={parentType}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<Integer>> countCommentsParentIdsAndParentTypeWithHttpInfo(@Param("parentIds") @javax.annotation.Nonnull List<UUID> parentIds, @Param("parentType") @javax.annotation.Nonnull String parentType);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>countCommentsParentIdsAndParentType</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link CountCommentsParentIdsAndParentTypeQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>parentIds -  (required)</li>
   *   <li>parentType -  (required)</li>
   *   </ul>
   * @return List&lt;Integer&gt;
   */
  @RequestLine("GET /api/comments/countCommentsParentIdsAndParentType?parentIds={parentIds}&parentType={parentType}")
  @Headers({
  "Accept: */*",
  })
  List<Integer> countCommentsParentIdsAndParentType(@QueryMap(encoded=true) CountCommentsParentIdsAndParentTypeQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>countCommentsParentIdsAndParentType</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>parentIds -  (required)</li>
          *   <li>parentType -  (required)</li>
      *   </ul>
          * @return List&lt;Integer&gt;
      */
      @RequestLine("GET /api/comments/countCommentsParentIdsAndParentType?parentIds={parentIds}&parentType={parentType}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<Integer>> countCommentsParentIdsAndParentTypeWithHttpInfo(@QueryMap(encoded=true) CountCommentsParentIdsAndParentTypeQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>countCommentsParentIdsAndParentType</code> method in a fluent style.
   */
  public static class CountCommentsParentIdsAndParentTypeQueryParams extends HashMap<String, Object> {
    public CountCommentsParentIdsAndParentTypeQueryParams parentIds(@javax.annotation.Nonnull final List<UUID> value) {
      put("parentIds", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
    public CountCommentsParentIdsAndParentTypeQueryParams parentType(@javax.annotation.Nonnull final String value) {
      put("parentType", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * 
   * 
   * @param commentDTO  (required)
   * @return CommentDTO
   */
  @RequestLine("POST /api/comments")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  CommentDTO createComment(@javax.annotation.Nonnull CommentDTO commentDTO);

  /**
   * 
   * Similar to <code>createComment</code> but it also returns the http response headers .
   * 
   * @param commentDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/comments")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<CommentDTO> createCommentWithHttpInfo(@javax.annotation.Nonnull CommentDTO commentDTO);



  /**
   * 
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/comments/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteComment(@Param("id") @javax.annotation.Nonnull UUID id);

  /**
   * 
   * Similar to <code>deleteComment</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/comments/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteCommentWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id);



  /**
   * 
   * 
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return List&lt;CommentDTO&gt;
   */
  @RequestLine("GET /api/comments?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  List<CommentDTO> getAllComments(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);

  /**
   * 
   * Similar to <code>getAllComments</code> but it also returns the http response headers .
   * 
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/comments?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<CommentDTO>> getAllCommentsWithHttpInfo(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>getAllComments</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllCommentsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
   *   <li>size - The size of the page to be returned (optional, default to 20)</li>
   *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
   *   </ul>
   * @return List&lt;CommentDTO&gt;
   */
  @RequestLine("GET /api/comments?page={page}&size={size}&sort={sort}")
  @Headers({
  "Accept: */*",
  })
  List<CommentDTO> getAllComments(@QueryMap(encoded=true) GetAllCommentsQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>getAllComments</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
          *   <li>size - The size of the page to be returned (optional, default to 20)</li>
          *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
      *   </ul>
          * @return List&lt;CommentDTO&gt;
      */
      @RequestLine("GET /api/comments?page={page}&size={size}&sort={sort}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<CommentDTO>> getAllCommentsWithHttpInfo(@QueryMap(encoded=true) GetAllCommentsQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllComments</code> method in a fluent style.
   */
  public static class GetAllCommentsQueryParams extends HashMap<String, Object> {
    public GetAllCommentsQueryParams page(@javax.annotation.Nullable final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAllCommentsQueryParams size(@javax.annotation.Nullable final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
    public GetAllCommentsQueryParams sort(@javax.annotation.Nullable final List<String> value) {
      put("sort", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * 
   * 
   * @param id  (required)
   * @return CommentDTO
   */
  @RequestLine("GET /api/comments/{id}")
  @Headers({
    "Accept: */*",
  })
  CommentDTO getComment(@Param("id") @javax.annotation.Nonnull UUID id);

  /**
   * 
   * Similar to <code>getComment</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/comments/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<CommentDTO> getCommentWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id);



  /**
   * 
   * 
   * @param parentId  (required)
   * @param parentType  (required)
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return List&lt;CommentWithRedisUserDTO&gt;
   */
  @RequestLine("GET /api/comments/by-parent?parentId={parentId}&parentType={parentType}&page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  List<CommentWithRedisUserDTO> getCommentsByParent(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("parentType") @javax.annotation.Nonnull String parentType, @Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);

  /**
   * 
   * Similar to <code>getCommentsByParent</code> but it also returns the http response headers .
   * 
   * @param parentId  (required)
   * @param parentType  (required)
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/comments/by-parent?parentId={parentId}&parentType={parentType}&page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<CommentWithRedisUserDTO>> getCommentsByParentWithHttpInfo(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("parentType") @javax.annotation.Nonnull String parentType, @Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>getCommentsByParent</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetCommentsByParentQueryParams} class that allows for
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
   * @return List&lt;CommentWithRedisUserDTO&gt;
   */
  @RequestLine("GET /api/comments/by-parent?parentId={parentId}&parentType={parentType}&page={page}&size={size}&sort={sort}")
  @Headers({
  "Accept: */*",
  })
  List<CommentWithRedisUserDTO> getCommentsByParent(@QueryMap(encoded=true) GetCommentsByParentQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>getCommentsByParent</code> that receives the query parameters as a map,
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
          * @return List&lt;CommentWithRedisUserDTO&gt;
      */
      @RequestLine("GET /api/comments/by-parent?parentId={parentId}&parentType={parentType}&page={page}&size={size}&sort={sort}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<CommentWithRedisUserDTO>> getCommentsByParentWithHttpInfo(@QueryMap(encoded=true) GetCommentsByParentQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getCommentsByParent</code> method in a fluent style.
   */
  public static class GetCommentsByParentQueryParams extends HashMap<String, Object> {
    public GetCommentsByParentQueryParams parentId(@javax.annotation.Nonnull final UUID value) {
      put("parentId", EncodingUtils.encode(value));
      return this;
    }
    public GetCommentsByParentQueryParams parentType(@javax.annotation.Nonnull final String value) {
      put("parentType", EncodingUtils.encode(value));
      return this;
    }
    public GetCommentsByParentQueryParams page(@javax.annotation.Nullable final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetCommentsByParentQueryParams size(@javax.annotation.Nullable final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
    public GetCommentsByParentQueryParams sort(@javax.annotation.Nullable final List<String> value) {
      put("sort", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * 
   * 
   * @param id  (required)
   * @param commentDTO  (required)
   * @return CommentDTO
   */
  @RequestLine("PATCH /api/comments/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  CommentDTO partialUpdateComment(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull CommentDTO commentDTO);

  /**
   * 
   * Similar to <code>partialUpdateComment</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @param commentDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PATCH /api/comments/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<CommentDTO> partialUpdateCommentWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull CommentDTO commentDTO);



  /**
   * 
   * 
   * @param id  (required)
   * @param commentDTO  (required)
   * @return CommentDTO
   */
  @RequestLine("PUT /api/comments/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  CommentDTO updateComment(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull CommentDTO commentDTO);

  /**
   * 
   * Similar to <code>updateComment</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @param commentDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /api/comments/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<CommentDTO> updateCommentWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull CommentDTO commentDTO);


}
