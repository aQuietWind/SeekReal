package seekreal.social.Config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfig {
    //初始化EsClient连接
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // 1. 创建底层 HTTP 客户端
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http")
        ).build();

        // 2. 创建传输层
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper()
        );
        // 3. 返回 ES客户端
        return new ElasticsearchClient(transport);
    }
}