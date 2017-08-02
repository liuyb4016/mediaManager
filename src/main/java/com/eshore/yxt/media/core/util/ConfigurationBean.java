package com.eshore.yxt.media.core.util;

import com.eshore.yxt.media.core.util.cache.MemcacheCaller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationBean {

    public @Bean
    MemcacheCaller memcacheCaller() {
        return MemcacheCaller.INSTANCE;
    }
}
