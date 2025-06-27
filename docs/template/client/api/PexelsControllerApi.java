package com.f4.reel.client.api;

import com.f4.reel.client.ApiClient;
import com.f4.reel.client.EncodingUtils;
import com.f4.reel.client.model.ApiResponse;

import com.f4.reel.client.model.UploadVideoRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-06-27T08:11:01.300407367+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.13.0")
public interface PexelsControllerApi extends ApiClient.Api {


  /**
   * 
   * 
   * @param imageUrl  (required)
   * @return String
   */
  @RequestLine("POST /api/pexels/download?imageUrl={imageUrl}")
  @Headers({
    "Accept: */*",
  })
  String downloadAndStoreImage(@Param("imageUrl") @javax.annotation.Nonnull String imageUrl);

  /**
   * 
   * Similar to <code>downloadAndStoreImage</code> but it also returns the http response headers .
   * 
   * @param imageUrl  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/pexels/download?imageUrl={imageUrl}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<String> downloadAndStoreImageWithHttpInfo(@Param("imageUrl") @javax.annotation.Nonnull String imageUrl);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>downloadAndStoreImage</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link DownloadAndStoreImageQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>imageUrl -  (required)</li>
   *   </ul>
   * @return String
   */
  @RequestLine("POST /api/pexels/download?imageUrl={imageUrl}")
  @Headers({
  "Accept: */*",
  })
  String downloadAndStoreImage(@QueryMap(encoded=true) DownloadAndStoreImageQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>downloadAndStoreImage</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>imageUrl -  (required)</li>
      *   </ul>
          * @return String
      */
      @RequestLine("POST /api/pexels/download?imageUrl={imageUrl}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<String> downloadAndStoreImageWithHttpInfo(@QueryMap(encoded=true) DownloadAndStoreImageQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>downloadAndStoreImage</code> method in a fluent style.
   */
  public static class DownloadAndStoreImageQueryParams extends HashMap<String, Object> {
    public DownloadAndStoreImageQueryParams imageUrl(@javax.annotation.Nonnull final String value) {
      put("imageUrl", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * 
   * 
   * @param videoUrl  (required)
   * @return String
   */
  @RequestLine("POST /api/pexels/videos/download?videoUrl={videoUrl}")
  @Headers({
    "Accept: */*",
  })
  String downloadAndStoreVideo(@Param("videoUrl") @javax.annotation.Nonnull String videoUrl);

  /**
   * 
   * Similar to <code>downloadAndStoreVideo</code> but it also returns the http response headers .
   * 
   * @param videoUrl  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/pexels/videos/download?videoUrl={videoUrl}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<String> downloadAndStoreVideoWithHttpInfo(@Param("videoUrl") @javax.annotation.Nonnull String videoUrl);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>downloadAndStoreVideo</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link DownloadAndStoreVideoQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>videoUrl -  (required)</li>
   *   </ul>
   * @return String
   */
  @RequestLine("POST /api/pexels/videos/download?videoUrl={videoUrl}")
  @Headers({
  "Accept: */*",
  })
  String downloadAndStoreVideo(@QueryMap(encoded=true) DownloadAndStoreVideoQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>downloadAndStoreVideo</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>videoUrl -  (required)</li>
      *   </ul>
          * @return String
      */
      @RequestLine("POST /api/pexels/videos/download?videoUrl={videoUrl}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<String> downloadAndStoreVideoWithHttpInfo(@QueryMap(encoded=true) DownloadAndStoreVideoQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>downloadAndStoreVideo</code> method in a fluent style.
   */
  public static class DownloadAndStoreVideoQueryParams extends HashMap<String, Object> {
    public DownloadAndStoreVideoQueryParams videoUrl(@javax.annotation.Nonnull final String value) {
      put("videoUrl", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * 
   * 
   * @param query  (required)
   * @param perPage  (optional, default to 10)
   * @return List&lt;String&gt;
   */
  @RequestLine("GET /api/pexels/search?query={query}&perPage={perPage}")
  @Headers({
    "Accept: */*",
  })
  List<String> searchImages(@Param("query") @javax.annotation.Nonnull String query, @Param("perPage") @javax.annotation.Nullable Integer perPage);

  /**
   * 
   * Similar to <code>searchImages</code> but it also returns the http response headers .
   * 
   * @param query  (required)
   * @param perPage  (optional, default to 10)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/pexels/search?query={query}&perPage={perPage}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<String>> searchImagesWithHttpInfo(@Param("query") @javax.annotation.Nonnull String query, @Param("perPage") @javax.annotation.Nullable Integer perPage);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>searchImages</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SearchImagesQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>query -  (required)</li>
   *   <li>perPage -  (optional, default to 10)</li>
   *   </ul>
   * @return List&lt;String&gt;
   */
  @RequestLine("GET /api/pexels/search?query={query}&perPage={perPage}")
  @Headers({
  "Accept: */*",
  })
  List<String> searchImages(@QueryMap(encoded=true) SearchImagesQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>searchImages</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>query -  (required)</li>
          *   <li>perPage -  (optional, default to 10)</li>
      *   </ul>
          * @return List&lt;String&gt;
      */
      @RequestLine("GET /api/pexels/search?query={query}&perPage={perPage}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<String>> searchImagesWithHttpInfo(@QueryMap(encoded=true) SearchImagesQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>searchImages</code> method in a fluent style.
   */
  public static class SearchImagesQueryParams extends HashMap<String, Object> {
    public SearchImagesQueryParams query(@javax.annotation.Nonnull final String value) {
      put("query", EncodingUtils.encode(value));
      return this;
    }
    public SearchImagesQueryParams perPage(@javax.annotation.Nullable final Integer value) {
      put("perPage", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * 
   * 
   * @param query  (required)
   * @param perPage  (optional, default to 10)
   * @return List&lt;Map&lt;String, Object&gt;&gt;
   */
  @RequestLine("GET /api/pexels/videos/search?query={query}&perPage={perPage}")
  @Headers({
    "Accept: */*",
  })
  List<Map<String, Object>> searchVideos(@Param("query") @javax.annotation.Nonnull String query, @Param("perPage") @javax.annotation.Nullable Integer perPage);

  /**
   * 
   * Similar to <code>searchVideos</code> but it also returns the http response headers .
   * 
   * @param query  (required)
   * @param perPage  (optional, default to 10)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/pexels/videos/search?query={query}&perPage={perPage}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<Map<String, Object>>> searchVideosWithHttpInfo(@Param("query") @javax.annotation.Nonnull String query, @Param("perPage") @javax.annotation.Nullable Integer perPage);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>searchVideos</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SearchVideosQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>query -  (required)</li>
   *   <li>perPage -  (optional, default to 10)</li>
   *   </ul>
   * @return List&lt;Map&lt;String, Object&gt;&gt;
   */
  @RequestLine("GET /api/pexels/videos/search?query={query}&perPage={perPage}")
  @Headers({
  "Accept: */*",
  })
  List<Map<String, Object>> searchVideos(@QueryMap(encoded=true) SearchVideosQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>searchVideos</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>query -  (required)</li>
          *   <li>perPage -  (optional, default to 10)</li>
      *   </ul>
          * @return List&lt;Map&lt;String, Object&gt;&gt;
      */
      @RequestLine("GET /api/pexels/videos/search?query={query}&perPage={perPage}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<Map<String, Object>>> searchVideosWithHttpInfo(@QueryMap(encoded=true) SearchVideosQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>searchVideos</code> method in a fluent style.
   */
  public static class SearchVideosQueryParams extends HashMap<String, Object> {
    public SearchVideosQueryParams query(@javax.annotation.Nonnull final String value) {
      put("query", EncodingUtils.encode(value));
      return this;
    }
    public SearchVideosQueryParams perPage(@javax.annotation.Nullable final Integer value) {
      put("perPage", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * 
   * 
   * @param pexelsImageUrl  (required)
   * @return String
   */
  @RequestLine("POST /api/pexels/store?pexelsImageUrl={pexelsImageUrl}")
  @Headers({
    "Accept: */*",
  })
  String storePexelsImage(@Param("pexelsImageUrl") @javax.annotation.Nonnull String pexelsImageUrl);

  /**
   * 
   * Similar to <code>storePexelsImage</code> but it also returns the http response headers .
   * 
   * @param pexelsImageUrl  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/pexels/store?pexelsImageUrl={pexelsImageUrl}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<String> storePexelsImageWithHttpInfo(@Param("pexelsImageUrl") @javax.annotation.Nonnull String pexelsImageUrl);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>storePexelsImage</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link StorePexelsImageQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>pexelsImageUrl -  (required)</li>
   *   </ul>
   * @return String
   */
  @RequestLine("POST /api/pexels/store?pexelsImageUrl={pexelsImageUrl}")
  @Headers({
  "Accept: */*",
  })
  String storePexelsImage(@QueryMap(encoded=true) StorePexelsImageQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>storePexelsImage</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>pexelsImageUrl -  (required)</li>
      *   </ul>
          * @return String
      */
      @RequestLine("POST /api/pexels/store?pexelsImageUrl={pexelsImageUrl}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<String> storePexelsImageWithHttpInfo(@QueryMap(encoded=true) StorePexelsImageQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>storePexelsImage</code> method in a fluent style.
   */
  public static class StorePexelsImageQueryParams extends HashMap<String, Object> {
    public StorePexelsImageQueryParams pexelsImageUrl(@javax.annotation.Nonnull final String value) {
      put("pexelsImageUrl", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * 
   * 
   * @param pexelsVideoUrl  (required)
   * @return String
   */
  @RequestLine("POST /api/pexels/videos/store?pexelsVideoUrl={pexelsVideoUrl}")
  @Headers({
    "Accept: */*",
  })
  String storePexelsVideo(@Param("pexelsVideoUrl") @javax.annotation.Nonnull String pexelsVideoUrl);

  /**
   * 
   * Similar to <code>storePexelsVideo</code> but it also returns the http response headers .
   * 
   * @param pexelsVideoUrl  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/pexels/videos/store?pexelsVideoUrl={pexelsVideoUrl}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<String> storePexelsVideoWithHttpInfo(@Param("pexelsVideoUrl") @javax.annotation.Nonnull String pexelsVideoUrl);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>storePexelsVideo</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link StorePexelsVideoQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>pexelsVideoUrl -  (required)</li>
   *   </ul>
   * @return String
   */
  @RequestLine("POST /api/pexels/videos/store?pexelsVideoUrl={pexelsVideoUrl}")
  @Headers({
  "Accept: */*",
  })
  String storePexelsVideo(@QueryMap(encoded=true) StorePexelsVideoQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>storePexelsVideo</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>pexelsVideoUrl -  (required)</li>
      *   </ul>
          * @return String
      */
      @RequestLine("POST /api/pexels/videos/store?pexelsVideoUrl={pexelsVideoUrl}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<String> storePexelsVideoWithHttpInfo(@QueryMap(encoded=true) StorePexelsVideoQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>storePexelsVideo</code> method in a fluent style.
   */
  public static class StorePexelsVideoQueryParams extends HashMap<String, Object> {
    public StorePexelsVideoQueryParams pexelsVideoUrl(@javax.annotation.Nonnull final String value) {
      put("pexelsVideoUrl", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * 
   * 
   * @param uploadVideoRequest  (optional)
   * @return String
   */
  @RequestLine("POST /api/pexels/upload")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  String uploadImage(@javax.annotation.Nullable UploadVideoRequest uploadVideoRequest);

  /**
   * 
   * Similar to <code>uploadImage</code> but it also returns the http response headers .
   * 
   * @param uploadVideoRequest  (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/pexels/upload")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<String> uploadImageWithHttpInfo(@javax.annotation.Nullable UploadVideoRequest uploadVideoRequest);



  /**
   * 
   * 
   * @param uploadVideoRequest  (optional)
   * @return String
   */
  @RequestLine("POST /api/pexels/videos/upload")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  String uploadVideo(@javax.annotation.Nullable UploadVideoRequest uploadVideoRequest);

  /**
   * 
   * Similar to <code>uploadVideo</code> but it also returns the http response headers .
   * 
   * @param uploadVideoRequest  (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/pexels/videos/upload")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<String> uploadVideoWithHttpInfo(@javax.annotation.Nullable UploadVideoRequest uploadVideoRequest);


}
