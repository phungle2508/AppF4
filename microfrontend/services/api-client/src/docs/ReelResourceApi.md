# ReelResourceApi

All URIs are relative to */services/msreels*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createReel**](#createreel) | **POST** /api/reels | |
|[**deleteReel**](#deletereel) | **DELETE** /api/reels/{id} | |
|[**getAllReels**](#getallreels) | **GET** /api/reels | |
|[**getReel**](#getreel) | **GET** /api/reels/{id} | |
|[**partialUpdateReel**](#partialupdatereel) | **PATCH** /api/reels/{id} | |
|[**searchReels**](#searchreels) | **GET** /api/reels/_search | |
|[**updateReel**](#updatereel) | **PUT** /api/reels/{id} | |

# **createReel**
> ReelDTO createReel(reelDTO)


### Example

```typescript
import {
    ReelResourceApi,
    Configuration,
    ReelDTO
} from './api';

const configuration = new Configuration();
const apiInstance = new ReelResourceApi(configuration);

let reelDTO: ReelDTO; //

const { status, data } = await apiInstance.createReel(
    reelDTO
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **reelDTO** | **ReelDTO**|  | |


### Return type

**ReelDTO**

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

# **deleteReel**
> deleteReel()


### Example

```typescript
import {
    ReelResourceApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ReelResourceApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.deleteReel(
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

# **getAllReels**
> Array<ReelDTO> getAllReels()


### Example

```typescript
import {
    ReelResourceApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ReelResourceApi(configuration);

let page: number; //Zero-based page index (0..N) (optional) (default to 0)
let size: number; //The size of the page to be returned (optional) (default to 20)
let sort: Array<string>; //Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional) (default to undefined)

const { status, data } = await apiInstance.getAllReels(
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

**Array<ReelDTO>**

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

# **getReel**
> ReelDTO getReel()


### Example

```typescript
import {
    ReelResourceApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ReelResourceApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.getReel(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**ReelDTO**

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

# **partialUpdateReel**
> ReelDTO partialUpdateReel(reelDTO)


### Example

```typescript
import {
    ReelResourceApi,
    Configuration,
    ReelDTO
} from './api';

const configuration = new Configuration();
const apiInstance = new ReelResourceApi(configuration);

let id: string; // (default to undefined)
let reelDTO: ReelDTO; //

const { status, data } = await apiInstance.partialUpdateReel(
    id,
    reelDTO
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **reelDTO** | **ReelDTO**|  | |
| **id** | [**string**] |  | defaults to undefined|


### Return type

**ReelDTO**

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

# **searchReels**
> Array<ReelDTO> searchReels()


### Example

```typescript
import {
    ReelResourceApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ReelResourceApi(configuration);

let query: string; // (default to undefined)
let page: number; //Zero-based page index (0..N) (optional) (default to 0)
let size: number; //The size of the page to be returned (optional) (default to 20)
let sort: Array<string>; //Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional) (default to undefined)

const { status, data } = await apiInstance.searchReels(
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

**Array<ReelDTO>**

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

# **updateReel**
> ReelDTO updateReel(reelDTO)


### Example

```typescript
import {
    ReelResourceApi,
    Configuration,
    ReelDTO
} from './api';

const configuration = new Configuration();
const apiInstance = new ReelResourceApi(configuration);

let id: string; // (default to undefined)
let reelDTO: ReelDTO; //

const { status, data } = await apiInstance.updateReel(
    id,
    reelDTO
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **reelDTO** | **ReelDTO**|  | |
| **id** | [**string**] |  | defaults to undefined|


### Return type

**ReelDTO**

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

