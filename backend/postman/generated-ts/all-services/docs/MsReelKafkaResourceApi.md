# MsReelKafkaResourceApi

All URIs are relative to */services/msreels*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**publish**](#publish) | **POST** /api/ms-reel-kafka/publish | |
|[**register**](#register) | **GET** /api/ms-reel-kafka/register | |
|[**unregister**](#unregister) | **GET** /api/ms-reel-kafka/unregister | |

# **publish**
> publish()


### Example

```typescript
import {
    MsReelKafkaResourceApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MsReelKafkaResourceApi(configuration);

let message: string; // (default to undefined)

const { status, data } = await apiInstance.publish(
    message
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **message** | [**string**] |  | defaults to undefined|


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

# **register**
> ResponseBodyEmitter register()


### Example

```typescript
import {
    MsReelKafkaResourceApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MsReelKafkaResourceApi(configuration);

const { status, data } = await apiInstance.register();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**ResponseBodyEmitter**

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

# **unregister**
> unregister()


### Example

```typescript
import {
    MsReelKafkaResourceApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MsReelKafkaResourceApi(configuration);

const { status, data } = await apiInstance.unregister();
```

### Parameters
This endpoint does not have any parameters.


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

