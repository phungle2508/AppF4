package com.f4.reel.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.f4.reel.domain.Reel;
import com.f4.reel.repository.ReelRepository;
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
 * Spring Data Elasticsearch repository for the {@link Reel} entity.
 */
public interface ReelSearchRepository extends ElasticsearchRepository<Reel, UUID>, ReelSearchRepositoryInternal {}

interface ReelSearchRepositoryInternal {
    Page<Reel> search(String query, Pageable pageable);

    Page<Reel> search(Query query);

    @Async
    void index(Reel entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class ReelSearchRepositoryInternalImpl implements ReelSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ReelRepository repository;

    ReelSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ReelRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Reel> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Reel> search(Query query) {
        SearchHits<Reel> searchHits = elasticsearchTemplate.search(query, Reel.class);
        List<Reel> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Reel entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), Reel.class);
    }
}
