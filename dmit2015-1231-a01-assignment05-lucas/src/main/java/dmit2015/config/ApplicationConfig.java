package dmit2015.config;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.annotation.sql.DataSourceDefinitions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.annotation.FacesConfig;

// TODO: Set the Jakarta EE Authentication Mechanism

// TODO: Set the Jakarta EE Identity Store

@DataSourceDefinitions({

	@DataSourceDefinition(
			name="java:app/datasources/h2databaseDS",
			className="org.h2.jdbcx.JdbcDataSource",
			// url="jdbc:h2:file:~/jdk/databases/h2/DMIT2015CourseDB;MODE=LEGACY;",
			url="jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=LEGACY;",
			user="user2015",
			password="Password2015"),
//
//	@DataSourceDefinition(
//		name="java:app/datasources/LocalMssqlDMIT2015DS",
//		className="com.microsoft.sqlserver.jdbc.SQLServerDataSource",
//		url="jdbc:sqlserver://localhost;databaseName=DMIT2015CourseDB;TrustServerCertificate=true",
//		user="user2015",
//		password="Password2015"),

//	@DataSourceDefinition(
//		name="java:app/datasources/oracleUser2015DS",
//		className="oracle.jdbc.xa.client.OracleXADataSource",
//		url="jdbc:oracle:thin:@localhost:1521/FREEPDB1",
//		user="user2015",
//		password="Password2015"),

})

@FacesConfig
@ApplicationScoped
public class ApplicationConfig {

}