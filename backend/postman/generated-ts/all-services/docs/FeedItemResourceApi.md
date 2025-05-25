# FeedItemResourceApi

All URIs are relative to */services/msreels*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createFeedItem**](#createfeeditem) | **POST** /api/feed-items | |
|[**deleteFeedItem**](#deletefeeditem) | **DELETE** /api/feed-items/{id} | |
|[**getAllFeedItems**](#getallfeeditems) | **GET** /api/feed-items | |
|[**getFeedItem**](#getfeeditem) | **GET** /api/feed-items/{id} | |
|[**partialUpdateFeedItem**](#partialupdatefeeditem) | **PATCH** /api/feed-items/{id} | |
|[**searchFeedItems**](#searchfeeditems) | **GET** /api/feed-items/_search | |
|[**updateFeedItem**](#updatefeeditem) | **PUT** /api/feed-items/{id} | |

# **createFeedItem**
> FeedItemDTO createFeedItem(feedItemDTO)


### Example

```typescript
import {
    FeedItemResourceApi,
    Configuration,
    FeedItemDTO
} from './api';

const configuration = new Configuration();
const apiInstance = new FeedItemResourceApi(configuration);

let feedItemDTO: FeedItemDTO; //

const { status, data } = await apiInstance.createFeedItem(
    feedItemDTO
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **feedItemDTO** | **FeedItemDTO**|  | |


### Return type

**FeedItemDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deleteFeedItem**
> deleteFeedItem()


### Example

```typescript
import {
    FeedItemResourceApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FeedItemResourceApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.deleteFeedItem(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getAllFeedItems**
> Array<FeedItemDTO> getAllFeedItems()


### Example

```typescript
import {
    FeedItemResourceApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FeedItemResourceApi(configuration);

let page: number; //Zero-based page index (0..N) (optional) (default to 0)
let size: number; //The size of the page to be returned (optional) (default to 20)
let sort: Array<string>; //Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional) (default to undefined)

const { status, data } = await apiInstance.getAllFeedItems(
    page,
    size,
    sort
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **page** | [**number**] | Zero-based page index (0..N) | (optional) defaults to 0|
| **size** | [**number**] | The size of the page to be returned | (optional) defaults to 20|
| **sort** | **Array&lt;string&gt;** | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | (optional) defaults to undefined|


### Return type

**Array<FeedItemDTO>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getFeedItem**
> FeedItemDTO getFeedItem()


### Example

```typescript
import {
    FeedItemResourceApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FeedItemResourceApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.getFeedItem(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**FeedItemDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **partialUpdateFeedItem**
> FeedItemDTO partialUpdateFeedItem(feedItemDTO)


### Example

```typescript
import {
    FeedItemResourceApi,
    Configuration,
    FeedItemDTO
} from './api';

const configuration = new Configuration();
const apiInstance = new FeedItemResourceApi(configuration);

let id: string; // (default to undefined)
let feedItemDTO: FeedItemDTO; //

const { status, data } = await apiInstance.partialUpdateFeedItem(
    id,
    feedItemDTO
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **feedItemDTO** | **FeedItemDTO**|  | |
| **id** | [**string**] |  | defaults to undefined|


### Return type

**FeedItemDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json, application/merge-patch+json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **searchFeedItems**
> Array<FeedItemDTO> searchFeedItems()


### Example

```typescript
import {
    FeedItemResourceApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FeedItemResourceApi(configuration);

let query: string; // (default to undefined)
let page: number; //Zero-based page index (0..N) (optional) (default to 0)
let size: number; //The size of the page to be returned (optional) (default to 20)
let sort: Array<string>; //Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional) (default to undefined)

const { status, data } = await apiInstance.searchFeedItems(
    query,
    page,
    size,
    sort
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **query** | [**string**] |  | defaults to undefined|
| **page** | [**number**] | Zero-based page index (0..N) | (optional) defaults to 0|
| **size** | [**number**] | The size of the page to be returned | (optional) defaults to 20|
| **sort** | **Array&lt;string&gt;** | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | (optional) defaults to undefined|


### Return type

**Array<FeedItemDTO>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateFeedItem**
> FeedItemDTO updateFeedItem(feedItemDTO)


### Example

```typescript
import {
    FeedItemResourceApi,
    Configuration,
    FeedItemDTO
} from './api';

const configuration = new Configuration();
const apiInstance = new FeedItemResourceApi(configuration);

let id: string; // (default to undefined)
let feedItemDTO: FeedItemDTO; //

const { status, data } = await apiInstance.updateFeedItem(
    id,
    feedItemDTO
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **feedItemDTO** | **FeedItemDTO**|  | |
| **id** | [**string**] |  | defaults to undefined|


### Return type

**FeedItemDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

