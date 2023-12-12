package common.config;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.annotation.sql.DataSourceDefinitions;
import jakarta.enterprise.context.ApplicationScoped;

@DataSourceDefinitions({

        @DataSourceDefinition(
                name="java:app/datasources/mssqlDS",
                className="com.microsoft.sqlserver.jdbc.SQLServerDataSource",
                url="jdbc:sqlserver://localhost;databaseName=DMIT2015_1223_CourseDB;TrustServerCertificate=true",
                user="user2015",
                password="Password2015"),

        // @DataSourceDefinition(
        // 	name="java:app/datasources/oracleUser2015DS",
        // 	className="oracle.jdbc.xa.client.OracleXADataSource",
        // 	url="jdbc:oracle:thin:@localhost:1521/FREEPDB1",
        // 	user="user2015",
        // 	password="Password2015"),

        // @DataSourceDefinition(
        // 	name="java:app/datasources/mariadbDS",
        // 	className="org.mariadb.jdbc.MariaDbDataSource",
        // 	url="jdbc:mariadb://127.0.0.1:3306/DMIT2015CourseDB",
        // 	user="user2015",
        // 	password="Password2015"),

})

@ApplicationScoped
public class ApplicationConfig {

}