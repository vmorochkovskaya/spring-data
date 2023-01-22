package org.example.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.List;

@Configuration
@EnableCassandraRepositories
public class CassandraConfig extends AbstractCassandraConfiguration {
    private static final String KEY_SPACE_NAME = "spring_cassandra";

    @Override
    public String getContactPoints() {
        return "localhost";
    }

    @Override
    public String getKeyspaceName() {
        return KEY_SPACE_NAME;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(KEY_SPACE_NAME)
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .withSimpleReplication(1L)
                .ifNotExists();
        return List.of(specification);
    }

    @Override
    protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
        return List.of(DropKeyspaceSpecification.dropKeyspace(KEY_SPACE_NAME));
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] { "org.example.app.entity" };
    }
}
