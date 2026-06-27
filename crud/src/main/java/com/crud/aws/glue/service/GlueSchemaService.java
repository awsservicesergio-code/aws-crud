package com.crud.aws.glue.service;

import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.glue.GlueClient;
import software.amazon.awssdk.services.glue.model.GetSchemaVersionResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class GlueSchemaService {

    private final GlueClient glueClient;
    private final Map<String, Schema> cache = new ConcurrentHashMap<>();

    /**
     * Método responsável por obter o schema registry do AWS GLUE Schema Registry da aws.
     * @param versionId
     * @return Schema
     */
    public Schema buscarSchema(String versionId) {
        return cache.computeIfAbsent(versionId, this::buscar);
    }

    /**
     * Método responsável por auxiliar a obter o schema registry do AWS GLUE Schema Registry da aws.
     * @param versionId
     * @return
     */
    private Schema buscar(String versionId) {
        GetSchemaVersionResponse response = glueClient.getSchemaVersion(builder ->
                builder.schemaVersionId(versionId)
        );
        return new Schema.Parser().parse(response.schemaDefinition());
    }

}
