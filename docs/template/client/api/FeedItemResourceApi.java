package com.f4.reel.client.api;

import com.f4.reel.client.ApiClient;
import com.f4.reel.client.EncodingUtils;
import com.f4.reel.client.model.ApiResponse;

import com.f4.reel.client.model.FeedItemDTO;
import com.f4.reel.client.model.FeedWithOtherDTO;
import java.util.UUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-06-27T09:24:56.232976487+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.13.0")
public interface FeedItemResourceApi extends ApiClient.Api {


  /**
   * 
   * 
   * @param feedItemDTO  (required)
   * @return FeedItemDTO
   */
  @RequestLine("POST /api/feed-items")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  FeedItemDTO createFeedItem(@javax.annotation.Nonnull FeedItemDTO feedItemDTO);

  /**
   * 
   * Similar to <code>createFeedItem</code> but it also returns the http response headers .
   * 
   * @param feedItemDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/feed-items")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<FeedItemDTO> createFeedItemWithHttpInfo(@javax.annotation.Nonnull FeedItemDTO feedItemDTO);



  /**
   * 
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/feed-items/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteFeedItem(@Param("id") @javax.annotation.Nonnull UUID id);

  /**
   * 
   * Similar to <code>deleteFeedItem</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/feed-items/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteFeedItemWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id);



  /**
   * 
   * 
   * @param query  (required)
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return List&lt;FeedWithOtherDTO&gt;
   */
  @RequestLine("GET /api/feed-items/search-for-main-page?query={query}&page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  List<FeedWithOtherDTO> findFeedWithOther(@Param("query") @javax.annotation.Nonnull String query, @Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);

  /**
   * 
   * Similar to <code>findFeedWithOther</code> but it also returns the http response headers .
   * 
   * @param query  (required)
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/feed-items/search-for-main-page?query={query}&page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<FeedWithOtherDTO>> findFeedWithOtherWithHttpInfo(@Param("query") @javax.annotation.Nonnull String query, @Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>findFeedWithOther</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link FindFeedWithOtherQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>query -  (required)</li>
   *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
   *   <li>size - The size of the page to be returned (optional, default to 20)</li>
   *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
   *   </ul>
   * @return List&lt;FeedWithOtherDTO&gt;
   */
  @RequestLine("GET /api/feed-items/search-for-main-page?query={query}&page={page}&size={size}&sort={sort}")
  @Headers({
  "Accept: */*",
  })
  List<FeedWithOtherDTO> findFeedWithOther(@QueryMap(encoded=true) FindFeedWithOtherQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>findFeedWithOther</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>query -  (required)</li>
          *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
          *   <li>size - The size of the page to be returned (optional, default to 20)</li>
          *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
      *   </ul>
          * @return List&lt;FeedWithOtherDTO&gt;
      */
      @RequestLine("GET /api/feed-items/search-for-main-page?query={query}&page={page}&size={size}&sort={sort}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<FeedWithOtherDTO>> findFeedWithOtherWithHttpInfo(@QueryMap(encoded=true) FindFeedWithOtherQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>findFeedWithOther</code> method in a fluent style.
   */
  public static class FindFeedWithOtherQueryParams extends HashMap<String, Object> {
    public FindFeedWithOtherQueryParams query(@javax.annotation.Nonnull final String value) {
      put("query", EncodingUtils.encode(value));
      return this;
    }
    public FindFeedWithOtherQueryParams page(@javax.annotation.Nullable final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public FindFeedWithOtherQueryParams size(@javax.annotation.Nullable final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
    public FindFeedWithOtherQueryParams sort(@javax.annotation.Nullable final List<String> value) {
      put("sort", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * 
   * 
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return List&lt;FeedItemDTO&gt;
   */
  @RequestLine("GET /api/feed-items?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  List<FeedItemDTO> getAllFeedItems(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);

  /**
   * 
   * Similar to <code>getAllFeedItems</code> but it also returns the http response headers .
   * 
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/feed-items?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<FeedItemDTO>> getAllFeedItemsWithHttpInfo(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>getAllFeedItems</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllFeedItemsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
   *   <li>size - The size of the page to be returned (optional, default to 20)</li>
   *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
   *   </ul>
   * @return List&lt;FeedItemDTO&gt;
   */
  @RequestLine("GET /api/feed-items?page={page}&size={size}&sort={sort}")
  @Headers({
  "Accept: */*",
  })
  List<FeedItemDTO> getAllFeedItems(@QueryMap(encoded=true) GetAllFeedItemsQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>getAllFeedItems</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
          *   <li>size - The size of the page to be returned (optional, default to 20)</li>
          *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
      *   </ul>
          * @return List&lt;FeedItemDTO&gt;
      */
      @RequestLine("GET /api/feed-items?page={page}&size={size}&sort={sort}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<FeedItemDTO>> getAllFeedItemsWithHttpInfo(@QueryMap(encoded=true) GetAllFeedItemsQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllFeedItems</code> method in a fluent style.
   */
  public static class GetAllFeedItemsQueryParams extends HashMap<String, Object> {
    public GetAllFeedItemsQueryParams page(@javax.annotation.Nullable final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAllFeedItemsQueryParams size(@javax.annotation.Nullable final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
    public GetAllFeedItemsQueryParams sort(@javax.annotation.Nullable final List<String> value) {
      put("sort", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * 
   * 
   * @param id  (required)
   * @return FeedItemDTO
   */
  @RequestLine("GET /api/feed-items/{id}")
  @Headers({
    "Accept: */*",
  })
  FeedItemDTO getFeedItem(@Param("id") @javax.annotation.Nonnull UUID id);

  /**
   * 
   * Similar to <code>getFeedItem</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/feed-items/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<FeedItemDTO> getFeedItemWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id);



  /**
   * 
   * 
   * @param id  (required)
   * @param feedItemDTO  (required)
   * @return FeedItemDTO
   */
  @RequestLine("PATCH /api/feed-items/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  FeedItemDTO partialUpdateFeedItem(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull FeedItemDTO feedItemDTO);

  /**
   * 
   * Similar to <code>partialUpdateFeedItem</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @param feedItemDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PATCH /api/feed-items/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<FeedItemDTO> partialUpdateFeedItemWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull FeedItemDTO feedItemDTO);



  /**
   * 
   * 
   * @param query  (required)
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return List&lt;FeedItemDTO&gt;
   */
  @RequestLine("GET /api/feed-items/_search?query={query}&page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  List<FeedItemDTO> searchFeedItems(@Param("query") @javax.annotation.Nonnull String query, @Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);

  /**
   * 
   * Similar to <code>searchFeedItems</code> but it also returns the http response headers .
   * 
   * @param query  (required)
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/feed-items/_search?query={query}&page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<FeedItemDTO>> searchFeedItemsWithHttpInfo(@Param("query") @javax.annotation.Nonnull String query, @Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>searchFeedItems</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SearchFeedItemsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>query -  (required)</li>
   *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
   *   <li>size - The size of the page to be returned (optional, default to 20)</li>
   *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
   *   </ul>
   * @return List&lt;FeedItemDTO&gt;
   */
  @RequestLine("GET /api/feed-items/_search?query={query}&page={page}&size={size}&sort={sort}")
  @Headers({
  "Accept: */*",
  })
  List<FeedItemDTO> searchFeedItems(@QueryMap(encoded=true) SearchFeedItemsQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>searchFeedItems</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>query -  (required)</li>
          *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
          *   <li>size - The size of the page to be returned (optional, default to 20)</li>
          *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
      *   </ul>
          * @return List&lt;FeedItemDTO&gt;
      */
      @RequestLine("GET /api/feed-items/_search?query={query}&page={page}&size={size}&sort={sort}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<FeedItemDTO>> searchFeedItemsWithHttpInfo(@QueryMap(encoded=true) SearchFeedItemsQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>searchFeedItems</code> method in a fluent style.
   */
  public static class SearchFeedItemsQueryParams extends HashMap<String, Object> {
    public SearchFeedItemsQueryParams query(@javax.annotation.Nonnull final String value) {
      put("query", EncodingUtils.encode(value));
      return this;
    }
    public SearchFeedItemsQueryParams page(@javax.annotation.Nullable final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public SearchFeedItemsQueryParams size(@javax.annotation.Nullable final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
    public SearchFeedItemsQueryParams sort(@javax.annotation.Nullable final List<String> value) {
      put("sort", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * 
   * 
   * @param id  (required)
   * @param feedItemDTO  (required)
   * @return FeedItemDTO
   */
  @RequestLine("PUT /api/feed-items/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  FeedItemDTO updateFeedItem(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull FeedItemDTO feedItemDTO);

  /**
   * 
   * Similar to <code>updateFeedItem</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @param feedItemDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /api/feed-items/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<FeedItemDTO> updateFeedItemWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull FeedItemDTO feedItemDTO);


}
