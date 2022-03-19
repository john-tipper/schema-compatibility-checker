package org.johntipper;

import java.nio.file.Path;
import java.util.List;

public interface SchemaChecker {
    enum SCHEMA_PROTOCOL {
        AVRO,
        JSON,
        PROTOBUF
    }

    enum DIRECTION {
        FORWARD,
        BACKWARD,
        BOTH
    }

    List<String> performCompatibilityCheck(SchemaChecker.SCHEMA_PROTOCOL protocol, DIRECTION direction, Path schema1Path, Path schema2Path);

    List<String> performCompatibilityCheck(SchemaChecker.SCHEMA_PROTOCOL protocol, DIRECTION direction, String schema1, String schema2);

}
