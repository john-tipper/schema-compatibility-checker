package org.johntipper;

import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import io.confluent.kafka.schemaregistry.json.JsonSchema;
import io.confluent.kafka.schemaregistry.protobuf.ProtobufSchema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultSchemaChecker implements SchemaChecker {

    @Override
    public List<String> performCompatibilityCheck(SCHEMA_PROTOCOL protocol, DIRECTION direction, Path schema1Path, Path schema2Path) {
        try {
            String newSchemaContents = Files.readString(schema1Path);
            String previousSchemaContents = Files.readString(schema2Path);
            return performCompatibilityCheck(protocol, direction, newSchemaContents, previousSchemaContents);

        } catch (IOException e) {
            return List.of("Error reading schema files");
        }
    }

    @Override
    public List<String> performCompatibilityCheck(SchemaChecker.SCHEMA_PROTOCOL protocol, DIRECTION direction, String schema1, String schema2) {
        List<String> annotatedErrors = new ArrayList<>();
        List<String> errors;

        switch (direction) {
            case BACKWARD:
                errors = isBackwardCompatible(protocol, schema1, schema2);
                break;
            case FORWARD:
                errors = isBackwardCompatible(protocol, schema2, schema1);
                break;
            default:
                // we'll process BOTH outside of this function
                throw new UnsupportedOperationException("Unrecognised direction when performing compatibility check");

        }
        if (errors.size() > 0) {
            annotatedErrors.add(String.format("Schema %s is NOT %s compatible with schema %s, errors are:", schema1, direction.toString(), schema2));
            annotatedErrors.addAll(errors);
        } else {
            System.out.printf("Schema %s is %s compatible with schema %s%n", schema1, direction.toString(), schema2);
        }

        return annotatedErrors;
    }


    List<String> isBackwardCompatible(SCHEMA_PROTOCOL protocol, String newSchemaContents, String previousSchemaContents) {

        ParsedSchema newSchema, previousSchema;

        switch (protocol) {
            case AVRO:
                newSchema = new AvroSchema(newSchemaContents);
                previousSchema = new AvroSchema(previousSchemaContents);
                break;

            case JSON:
                newSchema = new JsonSchema(newSchemaContents);
                previousSchema = new JsonSchema(previousSchemaContents);
                break;

            case PROTOBUF:
                newSchema = new ProtobufSchema(newSchemaContents);
                previousSchema = new ProtobufSchema(previousSchemaContents);
                break;

            default:
                // shouldn't see this
                throw new UnsupportedOperationException("Unrecognised protocol");
        }


        return Collections.unmodifiableList(newSchema.isBackwardCompatible(previousSchema));
    }
}
