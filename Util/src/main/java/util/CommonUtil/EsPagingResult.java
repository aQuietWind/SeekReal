package util.CommonUtil;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.util.ArrayList;
import java.util.List;

public class EsPagingResult<E> {
    private List<E> list;
    private long esTotal;
    private Long sort;

    public EsPagingResult() {
    }

    public EsPagingResult(List<E> list, long esTotal, Long sort) {
        this.list = list;
        this.esTotal = esTotal;
        this.sort = sort;
    }

    //辅助构建请求
    public static SearchRequest getSearchRequestById(String index,String searchField
            ,String orederIdField, long id, int number, Long sort) {
        SearchRequest request;
        //先判断这是第一次请求还是第n次请求，及有没有带上次请求的末尾参数sort
        if (sort==null){
            request = new SearchRequest.Builder()
                    .index(index)
                    //查询的关联字段和值
                    .query(q -> q.term(t -> t.field(searchField).value(id)))
                    //分页需求数
                    .size(number)
                    //排序
                    .sort(s -> s.field(f -> f.field(orederIdField).order(SortOrder.Desc)))
                    .build();
        }
        //带了就转换成List<FieldValue>这一sortValue进行searchAfter
        else {
            List<FieldValue> sortValue = List.of(FieldValue.of(sort));
            request = new SearchRequest.Builder()
                    .index(index)
                    //查询的关联字段和值
                    .query(q -> q.term(t -> t.field(searchField).value(id)))
                    //分页需求数
                    .size(number)
                    //排序
                    .sort(s -> s.field(f -> f.field(orederIdField).order(SortOrder.Desc)))
                    //起始点，在本业务下为上一个数据的特征id
                    .searchAfter(sortValue)
                    .build();
        }
        return request;
    }




    //相应结果自动封装
    public EsPagingResult(SearchResponse response) {
        //将结果封装进List
        List<E> result=new ArrayList<>();
        //获取实际数据
        List<Hit<E>> hits = response.hits().hits();
        //封装
        for (Hit<E> hit:hits) {
            result.add(hit.source());
        }
        this.list = result;
        //获得总数额
        this.esTotal = response.hits().total().value();
        //从实际数据中获取并且封装分页值
        this.sort=hits.getLast().sort().getFirst().longValue();
    }


    /**
     * 获取
     * @return list
     */
    public List<E> getList() {
        return list;
    }

    /**
     * 设置
     * @param list
     */
    public void setList(List<E> list) {
        this.list = list;
    }

    /**
     * 获取
     * @return esTotal
     */
    public long getEsTotal() {
        return esTotal;
    }

    /**
     * 设置
     * @param esTotal
     */
    public void setEsTotal(long esTotal) {
        this.esTotal = esTotal;
    }

    /**
     * 获取
     * @return sort
     */
    public Long getSort() {
        return sort;
    }

    /**
     * 设置
     * @param sort
     */
    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String toString() {
        return "EsPagingResult{list = " + list + ", esTotal = " + esTotal + ", sort = " + sort + "}";
    }
}
