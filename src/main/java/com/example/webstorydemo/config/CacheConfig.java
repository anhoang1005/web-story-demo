package com.example.webstorydemo.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "topStoriesToday", "topStoriesWeek", "topStoriesMonth", "categoryCache");
        cacheManager.setCaffeine(caffeine);
//        cacheManager.setCacheLoader(cacheLoader());
        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
//                .refreshAfterWrite(Duration.ofMinutes(30)) // Refresh ngầm sau 30 phút
                .expireAfterWrite(Duration.ofMinutes(30))  // Expire chính thức sau 1h (bảo vệ)
                .maximumSize(100);                        // tối đa 1000 entries
    }


//    public CacheLoader<Object, Object> cacheLoader() {
//        return key -> {
//            // Logic tải lại dữ liệu cho cache tương ứng (ví dụ gọi service lấy top stories)
//            // key sẽ là tên cache (String)
//            if ("topStoriesToday".equals(key)) {
//                return fetchTopStoriesToday();  // implement method gọi DB
//            } else if ("topStoriesWeek".equals(key)) {
//                return fetchTopStoriesWeek();
//            } else if ("topStoriesMonth".equals(key)) {
//                return fetchTopStoriesMonth();
//            }
//            return null;
//        };
//    }
}
