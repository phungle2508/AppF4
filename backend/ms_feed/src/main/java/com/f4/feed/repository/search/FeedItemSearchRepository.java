package com.f4.feed.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.f4.feed.domain.FeedItem;
import com.f4.feed.repository.FeedItemRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link FeedItem} entity.
 */
public interface FeedItemSearchRepository extends ElasticsearchRepository<FeedItem, UUID>, FeedItemSearchRepositoryInternal {}

interface FeedItemSearchRepositoryInternal {
    Page<FeedItem> search(String query, Pageable pageable);

    Page<FeedItem> search(Query query);

    @Async
    void index(FeedItem entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class FeedItemSearchRepositoryInternalImpl implements FeedItemSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FeedItemRepository repository;

    FeedItemSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FeedItemRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<FeedItem> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<FeedItem> search(Query query) {
        SearchHits<FeedItem> searchHits = elasticsearchTemplate.search(query, FeedItem.class);
        List<FeedItem> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(FeedItem entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), FeedItem.class);
    }
}
