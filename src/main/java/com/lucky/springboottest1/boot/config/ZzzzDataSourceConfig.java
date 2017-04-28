package com.lucky.springboottest1.boot.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@MapperScan(basePackages = ZzzzDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "zzzzSqlSessionFactory")
@PropertySource(value={"classpath:config/config.properties"})
public class ZzzzDataSourceConfig {
	private Logger logger = LoggerFactory.getLogger(getClass());
	// 精确到 zzzz 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.lucky.springboottest1.api.mapper.zzzz";
    static final String MAPPER_LOCATION = "classpath:com/lucky/springboottest1/api/mapper/zzzz/*.xml";
    
    @Value("${zzzz.url}")
    private String dbUrl;
    
    @Value("${zzzz.username}")
    private String username;
    
    @Value("${zzzz.password}")
    private String password;
    
    @Value("${zzzz.driver-class-name}")
    private String driverClassName;
    
    @Value("${spring.datasource.initialSize}")
    private int initialSize;
    
    @Value("${spring.datasource.minIdle}")
    private int minIdle;
    
    @Value("${spring.datasource.maxActive}")
    private int maxActive;
    
    @Value("${spring.datasource.maxWait}")
    private int maxWait;
    
    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    
    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    
    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;
    
    @Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;
    
    @Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;
    
    @Value("${spring.datasource.testOnReturn}")
    private boolean testOnReturn;
    
    @Value("${spring.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;
    
    @Value("${spring.datasource.filters}")
    private String filters;
    
    /*@Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", "druid");
        reg.addInitParameter("loginPassword", "druid");
        return reg;
    }
	@Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
        filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");
        return filterRegistrationBean;
    }*/
	
    @Bean(name="zzzzDataSource")
    public DataSource zzzzDataSource(){  
        DruidDataSource datasource = new DruidDataSource();  
          
        datasource.setUrl(this.dbUrl);  
        datasource.setUsername(username);  
        datasource.setPassword(password);  
        datasource.setDriverClassName(driverClassName);  
        datasource.setInitialSize(initialSize);  
        datasource.setMinIdle(minIdle);  
        datasource.setMaxActive(maxActive);  
        datasource.setMaxWait(maxWait);  
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);  
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);  
        datasource.setValidationQuery(validationQuery);  
        datasource.setTestWhileIdle(testWhileIdle);  
        datasource.setTestOnBorrow(testOnBorrow);  
        datasource.setTestOnReturn(testOnReturn);  
        datasource.setPoolPreparedStatements(poolPreparedStatements);  
        try {  
            datasource.setFilters(filters);  
        } catch (SQLException e) {  
            logger.error("druid configuration initialization filter", e);  
        }  
        return datasource;  
    }
    @Bean(name = "zzzzTransactionManager")
    public DataSourceTransactionManager zzzzTransactionManager() {
        return new DataSourceTransactionManager(zzzzDataSource());
    }
 
    @Bean(name = "zzzzSqlSessionFactory")
    public SqlSessionFactory zzzzSqlSessionFactory(@Qualifier("zzzzDataSource") DataSource masterDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(ZzzzDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
