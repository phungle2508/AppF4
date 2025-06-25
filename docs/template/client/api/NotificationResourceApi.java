package com.f4.reel.client.api;

import com.f4.reel.client.ApiClient;
import com.f4.reel.client.EncodingUtils;
import com.f4.reel.client.model.ApiResponse;

import com.f4.reel.client.model.NotificationDTO;
import java.util.UUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-06-25T20:17:20.430689056+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.13.0")
public interface NotificationResourceApi extends ApiClient.Api {


  /**
   * 
   * 
   * @param notificationDTO  (required)
   * @return NotificationDTO
   */
  @RequestLine("POST /api/notifications")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  NotificationDTO createNotification(@javax.annotation.Nonnull NotificationDTO notificationDTO);

  /**
   * 
   * Similar to <code>createNotification</code> but it also returns the http response headers .
   * 
   * @param notificationDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/notifications")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<NotificationDTO> createNotificationWithHttpInfo(@javax.annotation.Nonnull NotificationDTO notificationDTO);



  /**
   * 
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/notifications/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteNotification(@Param("id") @javax.annotation.Nonnull UUID id);

  /**
   * 
   * Similar to <code>deleteNotification</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/notifications/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteNotificationWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id);



  /**
   * 
   * 
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return List&lt;NotificationDTO&gt;
   */
  @RequestLine("GET /api/notifications?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  List<NotificationDTO> getAllNotifications(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);

  /**
   * 
   * Similar to <code>getAllNotifications</code> but it also returns the http response headers .
   * 
   * @param page Zero-based page index (0..N) (optional, default to 0)
   * @param size The size of the page to be returned (optional, default to 20)
   * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/notifications?page={page}&size={size}&sort={sort}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<NotificationDTO>> getAllNotificationsWithHttpInfo(@Param("page") @javax.annotation.Nullable Integer page, @Param("size") @javax.annotation.Nullable Integer size, @Param("sort") @javax.annotation.Nullable List<String> sort);


  /**
   * 
   * 
   * Note, this is equivalent to the other <code>getAllNotifications</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllNotificationsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
   *   <li>size - The size of the page to be returned (optional, default to 20)</li>
   *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
   *   </ul>
   * @return List&lt;NotificationDTO&gt;
   */
  @RequestLine("GET /api/notifications?page={page}&size={size}&sort={sort}")
  @Headers({
  "Accept: */*",
  })
  List<NotificationDTO> getAllNotifications(@QueryMap(encoded=true) GetAllNotificationsQueryParams queryParams);

  /**
  * 
  * 
  * Note, this is equivalent to the other <code>getAllNotifications</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>page - Zero-based page index (0..N) (optional, default to 0)</li>
          *   <li>size - The size of the page to be returned (optional, default to 20)</li>
          *   <li>sort - Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)</li>
      *   </ul>
          * @return List&lt;NotificationDTO&gt;
      */
      @RequestLine("GET /api/notifications?page={page}&size={size}&sort={sort}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<NotificationDTO>> getAllNotificationsWithHttpInfo(@QueryMap(encoded=true) GetAllNotificationsQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllNotifications</code> method in a fluent style.
   */
  public static class GetAllNotificationsQueryParams extends HashMap<String, Object> {
    public GetAllNotificationsQueryParams page(@javax.annotation.Nullable final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAllNotificationsQueryParams size(@javax.annotation.Nullable final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
    public GetAllNotificationsQueryParams sort(@javax.annotation.Nullable final List<String> value) {
      put("sort", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * 
   * 
   * @param id  (required)
   * @return NotificationDTO
   */
  @RequestLine("GET /api/notifications/{id}")
  @Headers({
    "Accept: */*",
  })
  NotificationDTO getNotification(@Param("id") @javax.annotation.Nonnull UUID id);

  /**
   * 
   * Similar to <code>getNotification</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/notifications/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<NotificationDTO> getNotificationWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id);



  /**
   * 
   * 
   * @param id  (required)
   * @param notificationDTO  (required)
   * @return NotificationDTO
   */
  @RequestLine("PATCH /api/notifications/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  NotificationDTO partialUpdateNotification(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull NotificationDTO notificationDTO);

  /**
   * 
   * Similar to <code>partialUpdateNotification</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @param notificationDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PATCH /api/notifications/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<NotificationDTO> partialUpdateNotificationWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull NotificationDTO notificationDTO);



  /**
   * 
   * 
   * @param id  (required)
   * @param notificationDTO  (required)
   * @return NotificationDTO
   */
  @RequestLine("PUT /api/notifications/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  NotificationDTO updateNotification(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull NotificationDTO notificationDTO);

  /**
   * 
   * Similar to <code>updateNotification</code> but it also returns the http response headers .
   * 
   * @param id  (required)
   * @param notificationDTO  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /api/notifications/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<NotificationDTO> updateNotificationWithHttpInfo(@Param("id") @javax.annotation.Nonnull UUID id, @javax.annotation.Nonnull NotificationDTO notificationDTO);


}
