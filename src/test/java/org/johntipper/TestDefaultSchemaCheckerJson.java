package org.johntipper;

import org.junit.Test;

import java.util.Collections;

import static org.johntipper.SchemaChecker.SCHEMA_PROTOCOL.JSON;
import static org.junit.Assert.assertEquals;

/**
 * Tests taken from https://yokota.blog/2021/03/29/understanding-json-schema-compatibility/
 * Pattern properties appear to be unsupported and ignored by schema registry
 */
public class TestDefaultSchemaCheckerJson {

    static String schema1 = "{\n" +
        "  \"type\": \"object\",\n" +
        "  \"properties\": {\n" +
        "    \"foo\": { \"type\": \"string\" },\n" +
        "    \"bar\": { \"type\": \"string\" }\n" +
        "  }\n" +
        "}";

    static String schema2 = "{\n" +
        "  \"type\": \"object\",\n" +
        "  \"properties\": {\n" +
        "    \"foo\": { \"type\": \"string\" },\n" +
        "    \"bar\": { \"type\": \"string\" }\n" +
        "  },\n" +
        "  \"additionalProperties\": false\n" +
        "}";

    static String schema3 = "{\n" +
        "  \"type\": \"object\",\n" +
        "  \"properties\": {\n" +
        "    \"foo\": { \"type\": \"string\" },\n" +
        "    \"bar\": { \"type\": \"string\" }\n" +
        "  },\n" +
        "  \"additionalProperties\": true\n" +
        "}";

    static String schema4 = "{\n" +
        "  \"type\": \"object\",\n" +
        "  \"properties\": {\n" +
        "    \"foo\": { \"type\": \"string\" },\n" +
        "    \"bar\": { \"type\": \"string\" }\n" +
        "  },\n" +
        "  \"additionalProperties\": { \"type\": \"string\" }\n" +
        "}";

    static String schema5 = "{\n" +
        "  \"type\": \"object\",\n" +
        "  \"properties\": {\n" +
        "    \"foo\": { \"type\": \"string\" },\n" +
        "    \"bar\": { \"type\": \"string\" },\n" +
        "    \"zap\": { \"type\": \"string\" }\n" +
        "  },\n" +
        "  \"additionalProperties\": { \"type\": \"string\" }\n" +
        "}";

    static String schema6 = "{\n" +
        "  \"type\": \"object\",\n" +
        "  \"properties\": {\n" +
        "    \"foo\": { \"type\": \"string\" },\n" +
        "    \"bar\": { \"type\": \"string\" },\n" +
        "    \"zap\": { \n" +
        "      \"oneOf\": [ { \"type\": \"string\" }, { \"type\": \"integer\" } ] \n" +
        "    }\n" +
        "  },\n" +
        "  \"additionalProperties\": { \"type\": \"string\" }\n" +
        "}";

    static String schema7 = "{\n" +
        "  \"type\": \"object\",\n" +
        "  \"properties\": {\n" +
        "    \"foo\": { \"type\": \"string\" },\n" +
        "    \"bar\": { \"type\": \"string\" }\n" +
        "  },\n" +
        "  \"patternProperties\": {\n" +
        "    \"^sxxx_\": { \"type\": \"string\" }\n" +
        "  },\n" +
        "  \"additionalProperties\": false\n" +
        "}";

    static String schema8 = "{\n" +
        "  \"type\": \"object\",\n" +
        "  \"properties\": {\n" +
        "    \"foo\": { \"type\": \"string\" },\n" +
        "    \"bar\": { \"type\": \"string\" },\n" +
        "    \"zap\": true\n" +
        "  },\n" +
        "  \"additionalProperties\": true\n" +
        "}";

    static String schema9 = "{\n" +
        "  \"type\": \"object\",\n" +
        "  \"properties\": {\n" +
        "    \"foo\": { \"type\": \"string\" },\n" +
        "    \"bar\": { \"type\": \"string\" },\n" +
        "    \"zap\": false\n" +
        "  },\n" +
        "  \"additionalProperties\": false\n" +
        "}";

    static String schema10 = "{\n" +
        "  \"type\": \"object\",\n" +
        "  \"properties\": {\n" +
        "    \"foo\": { \"type\": \"string\" },\n" +
        "    \"bar\": { \"type\": \"string\" }\n" +
        "  },\n" +
        "  \"additionalProperties\": { \n" +
        "    \"oneOf\": [ { \"type\": \"string\" }, { \"type\": \"integer\" } ] \n" +
        "  }\n" +
        "}";

    static String schema11 = "{\n" +
        "  \"type\": \"object\",\n" +
        "  \"properties\": {\n" +
        "    \"foo\": { \"type\": \"string\" },\n" +
        "    \"bar\": { \"type\": \"string\" },\n" +
        "    \"zap\": { \n" +
        "      \"oneOf\": [ { \"type\": \"string\" }, { \"type\": \"integer\" } ] \n" +
        "    }\n" +
        "  },\n" +
        "  \"additionalProperties\": { \n" +
        "    \"oneOf\": [ { \"type\": \"string\" }, { \"type\": \"integer\" } ] \n" +
        "  }\n" +
        "}";

    static String schema12 = "{\n" +
        "  \"type\": \"object\",\n" +
        "  \"properties\": {\n" +
        "    \"foo\": { \"type\": \"string\" },\n" +
        "    \"bar\": { \"type\": \"string\" }\n" +
        "  },\n" +
        "  \"patternProperties\": {\n" +
        "    \"^s_\": { \"type\": \"string\" },\n" +
        "    \"^i_\": { \"type\": \"integer\" }\n" +
        "  },\n" +
        "  \"additionalProperties\": false\n" +
        "}";

    static String schema13 = "{\n" +
        "  \"type\": \"object\",\n" +
        "  \"properties\": {\n" +
        "    \"foo\": { \"type\": \"string\" },\n" +
        "    \"bar\": { \"type\": \"string\" },\n" +
        "    \"s_zap\": { \"type\": \"string\" },\n" +
        "    \"i_zap\": { \"type\": \"integer\" }\n" +
        "  },\n" +
        "  patternProperties\": {\n" +
        "    \"^s_\": { \"type\": \"string\" },\n" +
        "    \"^i_\": { \"type\": \"integer\" }\n" +
        "  },\n" +
        "  \"additionalProperties\": false\n" +
        "}";

    @Test
    public void check_s1BackwardsS2() {

        DefaultSchemaChecker schemaChecker = new DefaultSchemaChecker();

        assertEquals(Collections.emptyList(), schemaChecker.performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.BACKWARD, schema1, schema2));
    }

    @Test
    public void check_s2ForwardsS1() {
        DefaultSchemaChecker schemaChecker = new DefaultSchemaChecker();

        assertEquals(Collections.emptyList(), schemaChecker.performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.FORWARD, schema2, schema1));
    }

    @Test
    public void check_s1EquivalentS3() {

        DefaultSchemaChecker schemaChecker = new DefaultSchemaChecker();

        assertEquals(Collections.emptyList(), schemaChecker.performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.BACKWARD, schema1, schema3));
        assertEquals(Collections.emptyList(), schemaChecker.performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.FORWARD, schema1, schema3));
    }

    @Test
    public void check_s5BackwardsS4() {

        DefaultSchemaChecker schemaChecker = new DefaultSchemaChecker();

        assertEquals(Collections.emptyList(), schemaChecker.performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.BACKWARD, schema5, schema4));
    }

    @Test
    public void check_s6BackwardsS4() {

        DefaultSchemaChecker schemaChecker = new DefaultSchemaChecker();

        assertEquals(Collections.emptyList(), schemaChecker.performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.BACKWARD, schema6, schema4));
    }

    @Test
    public void check_s8BothS3() {

        DefaultSchemaChecker schemaChecker = new DefaultSchemaChecker();

        assertEquals(Collections.emptyList(), schemaChecker.performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.BACKWARD, schema8, schema3));
        assertEquals(Collections.emptyList(), schemaChecker.performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.FORWARD, schema8, schema3));
    }

    @Test
    public void check_s9BackwardsS2() {

        DefaultSchemaChecker schemaChecker = new DefaultSchemaChecker();

        assertEquals(Collections.emptyList(), schemaChecker.performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.BACKWARD, schema9, schema2));
    }

    @Test
    public void check_s10BothS11() {

        DefaultSchemaChecker schemaChecker = new DefaultSchemaChecker();

        assertEquals(Collections.emptyList(), schemaChecker.performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.BACKWARD, schema10, schema11));
        assertEquals(Collections.emptyList(), schemaChecker.performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.FORWARD, schema10, schema11));
    }


}
