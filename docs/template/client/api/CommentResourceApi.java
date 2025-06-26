package com.f4.reel.client.api;

import com.f4.reel.client.ApiClient;
import com.f4.reel.client.EncodingUtils;
import com.f4.reel.client.model.ApiResponse;

import com.f4.reel.client.model.CommentDTO;
import java.util.UUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-06-26T08:01:46.903439394+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.13.0")
public interface CommentResourceApi extends ApiClient.Api {


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
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return List&lt;CommentDTO&gt;
   */
  @RequestLine("GET /api/comments/parent/{parentId}?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  List<CommentDTO> getCommentsByParentId(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);

  /**
   * 
   * Similar to <code>getCommentsByParentId</code> but it also returns the http response headers .
   * 
   * @param parentId  (required)
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/comments/parent/{parentId}?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<CommentDTO>> getCommentsByParentIdWithHttpInfo(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>getCommentsByParentId</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetCommentsByParentIdQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param parentId  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
   *   <li>size - The size of the page to be returned (optional, default to 20)</li>
   *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
   *   </ul>
   * @return List&lt;CommentDTO&gt;
   */
  @RequestLine("GET /api/comments/parent/{parentId}?page={page}&size={size}&sort={sort}")
  @Headers({
  "Accept: */*",
  })
  List<CommentDTO> getCommentsByParentId(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @QueryMap(encoded=true) GetCommentsByParentIdQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>getCommentsByParentId</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param parentId  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
          *   <li>size - The size of the page to be returned (optional, default to 20)</li>
          *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
      *   </ul>
          * @return List&lt;CommentDTO&gt;
      */
      @RequestLine("GET /api/comments/parent/{parentId}?page={page}&size={size}&sort={sort}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<CommentDTO>> getCommentsByParentIdWithHttpInfo(@Param("parentId") @javax.annotation.Nonnull UUID parentId, @QueryMap(encoded=true) GetCommentsByParentIdQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getCommentsByParentId</code> method in a fluent style.
   */
  public static class GetCommentsByParentIdQueryParams extends HashMap<String, Object> {
    public GetCommentsByParentIdQueryParams page(@javax.annotation.Nullable final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetCommentsByParentIdQueryParams size(@javax.annotation.Nullable final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
    public GetCommentsByParentIdQueryParams sort(@javax.annotation.Nullable final List<String> value) {
      put("sort", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * 
   * 
   * @param parentType  (required)
   * @param parentId  (required)
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return List&lt;CommentDTO&gt;
   */
  @RequestLine("GET /api/comments/parent/{parentType}/{parentId}?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  List<CommentDTO> getCommentsOptimizedByParent(@Param("parentType") @javax.annotation.Nonnull String parentType, @Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);

  /**
   * 
   * Similar to <code>getCommentsOptimizedByParent</code> but it also returns the http response headers .
   * 
   * @param parentType  (required)
   * @param parentId  (required)
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/comments/parent/{parentType}/{parentId}?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<CommentDTO>> getCommentsOptimizedByParentWithHttpInfo(@Param("parentType") @javax.annotation.Nonnull String parentType, @Param("parentId") @javax.annotation.Nonnull UUID parentId, @Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>getCommentsOptimizedByParent</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetCommentsOptimizedByParentQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param parentType  (required)
   * @param parentId  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
   *   <li>size - The size of the page to be returned (optional, default to 20)</li>
   *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
   *   </ul>
   * @return List&lt;CommentDTO&gt;
   */
  @RequestLine("GET /api/comments/parent/{parentType}/{parentId}?page={page}&size={size}&sort={sort}")
  @Headers({
  "Accept: */*",
  })
  List<CommentDTO> getCommentsOptimizedByParent(@Param("parentType") @javax.annotation.Nonnull String parentType, @Param("parentId") @javax.annotation.Nonnull UUID parentId, @QueryMap(encoded=true) GetCommentsOptimizedByParentQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>getCommentsOptimizedByParent</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param parentType  (required)
              * @param parentId  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
          *   <li>size - The size of the page to be returned (optional, default to 20)</li>
          *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
      *   </ul>
          * @return List&lt;CommentDTO&gt;
      */
      @RequestLine("GET /api/comments/parent/{parentType}/{parentId}?page={page}&size={size}&sort={sort}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<CommentDTO>> getCommentsOptimizedByParentWithHttpInfo(@Param("parentType") @javax.annotation.Nonnull String parentType, @Param("parentId") @javax.annotation.Nonnull UUID parentId, @QueryMap(encoded=true) GetCommentsOptimizedByParentQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getCommentsOptimizedByParent</code> method in a fluent style.
   */
  public static class GetCommentsOptimizedByParentQueryParams extends HashMap<String, Object> {
    public GetCommentsOptimizedByParentQueryParams page(@javax.annotation.Nullable final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetCommentsOptimizedByParentQueryParams size(@javax.annotation.Nullable final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
    public GetCommentsOptimizedByParentQueryParams sort(@javax.annotation.Nullable final List<String> value) {
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
