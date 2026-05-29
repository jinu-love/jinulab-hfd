package dev.jinulab.isetdx.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import dev.jinulab.dbist.config.AbstractTransactionConfig;



@Configuration
@EnableTransactionManagement
//public class TransactionConfig extends AbstractTransactionConfig {
//	
//}

public class TransactionConfig extends AbstractTransactionConfig {
	
//	@Autowired
//	@Qualifier("dataSource")
//	private DataSource dataSource;
//
//	@Value("${jinulab.transactionManager.defaultTimeout}")
//	protected int defaultTimeout;
//	
//	@Bean
//	@Primary
//	//public PlatformTransactionManager transactionManager(DataSource dataSource) {
//	public PlatformTransactionManager transactionManager() {
//		DataSourceTransactionManager txManager = new DataSourceTransactionManager(dataSource);
//		txManager.setDefaultTimeout(this.defaultTimeout);
//		return txManager;
//	}
}
