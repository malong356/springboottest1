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
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
@MapperScan(basePackages = YyyyDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "yyyySqlSessionFactory")
@PropertySource(value={"classpath:config/config.properties"})
public class YyyyDataSourceConfig {
	private Logger logger = LoggerFactory.getLogger(getClass());
	// 精确到 yyyy 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.lucky.springboottest1.api.mapper.yyyy";
    static final String MAPPER_LOCATION = "classpath:com/lucky/springboottest1/api/mapper/yyyy/*.xml";
    
    @Value("${yyyy.url}")
    private String dbUrl;
    
    @Value("${yyyy.username}")
    private String username;
    
    @Value("${yyyy.password}")
    private String password;
    
    @Value("${yyyy.driver-class-name}")
    private String driverClassName;
    
    @Value("${spring.datasource.filters}")
    private String filters;
    
    @Bean
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
    }
	
    @Primary
    @Bean(name="yyyyDataSource")
    public DataSource yyyyDataSource(){  
        DruidDataSource datasource = new DruidDataSource();  
          
        datasource.setUrl(this.dbUrl);  
        datasource.setUsername(username);  
        datasource.setPassword(password);  
        datasource.setDriverClassName(driverClassName);  
        datasource.setMinIdle(2);                          //配置初始化大小、最小、最大
        datasource.setMaxActive(10);                       //配置初始化大小、最小、最大
        datasource.setInitialSize(2);                      //配置初始化大小、最小、最大
        datasource.setMaxWait(60000);                       //配置获取连接等待超时的时间
        datasource.setMinEvictableIdleTimeMillis(300000);   //配置一个连接在池中最小生存的时间,单位是毫秒
        datasource.setTimeBetweenEvictionRunsMillis(60000); //配置间隔多久才进行一次检测,检测需要关闭的空闲连接,单位是毫秒
        //默认的testWhileIdle=true,testOnBorrow=false,testOnReturn=false
        datasource.setValidationQuery("SELECT 1");//用来检测连接是否有效的sql
        datasource.setTestOnBorrow(false);//申请连接时执行validationQuery检测连接是否有效
        datasource.setTestWhileIdle(true);//建议配置为true，不影响性能，并且保证安全性。
        datasource.setPoolPreparedStatements(false);//是否缓存preparedStatement，也就是PSCache
        try {  
            datasource.setFilters(filters);  
        } catch (SQLException e) {  
            logger.error("druid configuration initialization filter", e);  
        }  
        return datasource;  
    }
    @Primary
    @Bean(name = "yyyyTransactionManager")
    public DataSourceTransactionManager yyyyTransactionManager() {
        return new DataSourceTransactionManager(yyyyDataSource());
    }
 
    @Primary
    @Bean(name = "yyyySqlSessionFactory")
    public SqlSessionFactory yyyySqlSessionFactory(@Qualifier("yyyyDataSource") DataSource dataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(YyyyDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
