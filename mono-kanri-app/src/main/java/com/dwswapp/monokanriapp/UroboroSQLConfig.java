package com.dwswapp.monokanriapp;

import jp.co.future.uroborosql.SqlAgent;
import jp.co.future.uroborosql.UroboroSQL;
import jp.co.future.uroborosql.config.SqlConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class UroboroSQLConfig {

    @Bean
    public SqlAgent sqlAgent(DataSource dataSource) {
        // v1.0.9 互換の初期化方法を使用
        // Spring Bootが提供するDataSourceを使ってSqlConfigを初期化します
        SqlConfig config = UroboroSQL.builder(dataSource).build();

        // 設定済みのSqlConfigからSqlAgentを取得
        return config.agent();
    }
}
