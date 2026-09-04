package com.c2.lc.ms.master.repos.elastic;

import com.c2.lc.ms.master.entities.elastic.ElLcItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LcItemElRepository extends ElasticsearchRepository<ElLcItem, String> {

    //@Query("{ 'c_item_name' : { $regex: ?0 } }")
    @Query("{\"bool\": {\"must\": [{\"match\": {\"itemName\": \"?0\"}}]}}")
    // @Query("{\"bool\": {\"must\":{\"regexp\": {\"itemName\": \"?0\"}}}}")
    //@Query("{\"_source.itemName\":{\"$regex\":\"?0\"}}")
    List<ElLcItem> findByName(String regexp, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"itemName\": \"?0\"}}]}}")
    List<ElLcItem> elCount(String regexp);

    List <ElLcItem> findByItemName(String regexp, Pageable pageable);
}
