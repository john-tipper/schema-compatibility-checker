package org.johntipper;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.nio.file.Path;

import static org.johntipper.SchemaChecker.SCHEMA_PROTOCOL.*;
import static org.mockito.Mockito.*;

public class TestMain {

    @Test
    public void check_findsAvro() throws ParseException {
        SchemaChecker mockChecker = mock(SchemaChecker.class);
        Main main = new Main(mockChecker);

        String[] args = new String[] {"-p", "a", "new", "existing"};
        main.check(Main.programOptions(), args);
        verify(mockChecker, times(1)).performCompatibilityCheck(AVRO, SchemaChecker.DIRECTION.BACKWARD, Path.of("new"), Path.of("existing"));
        clearInvocations(mockChecker);

        args = new String[] {"-p", "avro", "new", "existing"};
        main.check(Main.programOptions(), args);
        verify(mockChecker, times(1)).performCompatibilityCheck(AVRO, SchemaChecker.DIRECTION.BACKWARD, Path.of("new"), Path.of("existing"));
        clearInvocations(mockChecker);

        args = new String[] {"--protocol", "A", "new", "existing"};
        main.check(Main.programOptions(), args);
        verify(mockChecker, times(1)).performCompatibilityCheck(AVRO, SchemaChecker.DIRECTION.BACKWARD, Path.of("new"), Path.of("existing"));
        clearInvocations(mockChecker);

        args = new String[] {"--protocol", "AVRO", "new", "existing"};
        main.check(Main.programOptions(), args);
        verify(mockChecker, times(1)).performCompatibilityCheck(AVRO, SchemaChecker.DIRECTION.BACKWARD, Path.of("new"), Path.of("existing"));

    }

    @Test
    public void check_findsJson() throws ParseException {
        SchemaChecker mockChecker = mock(SchemaChecker.class);
        Main main = new Main(mockChecker);

        String[] args = new String[] {"-p", "j", "new", "existing"};
        main.check(Main.programOptions(), args);
        verify(mockChecker, times(1)).performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.BACKWARD, Path.of("new"), Path.of("existing"));
        clearInvocations(mockChecker);

        args = new String[] {"-p", "json", "new", "existing"};
        main.check(Main.programOptions(), args);
        verify(mockChecker, times(1)).performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.BACKWARD, Path.of("new"), Path.of("existing"));
        clearInvocations(mockChecker);

        args = new String[] {"--protocol", "J", "new", "existing"};
        main.check(Main.programOptions(), args);
        verify(mockChecker, times(1)).performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.BACKWARD, Path.of("new"), Path.of("existing"));
        clearInvocations(mockChecker);

        args = new String[] {"--protocol", "JSON", "new", "existing"};
        main.check(Main.programOptions(), args);
        verify(mockChecker, times(1)).performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.BACKWARD, Path.of("new"), Path.of("existing"));

    }

    @Test
    public void check_findsDirection() throws ParseException {
        SchemaChecker mockChecker = mock(SchemaChecker.class);
        Main main = new Main(mockChecker);

        String[] args = new String[] {"-p", "j", "-d", "backward", "new", "existing"};
        main.check(Main.programOptions(), args);
        verify(mockChecker, times(1)).performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.BACKWARD, Path.of("new"), Path.of("existing"));
        clearInvocations(mockChecker);

        args = new String[] {"-p", "j", "-d", "forward", "new", "existing"};
        main.check(Main.programOptions(), args);
        verify(mockChecker, times(1)).performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.FORWARD, Path.of("new"), Path.of("existing"));
        clearInvocations(mockChecker);

        args = new String[] {"-p", "j", "-d", "both", "new", "existing"};
        main.check(Main.programOptions(), args);
        verify(mockChecker, times(1)).performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.FORWARD, Path.of("new"), Path.of("existing"));
        verify(mockChecker, times(1)).performCompatibilityCheck(JSON, SchemaChecker.DIRECTION.BACKWARD, Path.of("new"), Path.of("existing"));

    }

    @Test
    public void check_findsProtobuf() throws ParseException {
        SchemaChecker mockChecker = mock(SchemaChecker.class);
        Main main = new Main(mockChecker);

        String[] args = new String[] {"-p", "p", "new", "existing"};
        main.check(Main.programOptions(), args);
        verify(mockChecker, times(1)).performCompatibilityCheck(PROTOBUF, SchemaChecker.DIRECTION.BACKWARD, Path.of("new"), Path.of("existing"));
        clearInvocations(mockChecker);

        args = new String[] {"-p", "protobuf", "new", "existing"};
        main.check(Main.programOptions(), args);
        verify(mockChecker, times(1)).performCompatibilityCheck(PROTOBUF, SchemaChecker.DIRECTION.BACKWARD, Path.of("new"), Path.of("existing"));
        clearInvocations(mockChecker);

        args = new String[] {"--protocol", "P", "new", "existing"};
        main.check(Main.programOptions(), args);
        verify(mockChecker, times(1)).performCompatibilityCheck(PROTOBUF, SchemaChecker.DIRECTION.BACKWARD, Path.of("new"), Path.of("existing"));
        clearInvocations(mockChecker);

        args = new String[] {"--protocol", "PROTOBUF", "new", "existing"};
        main.check(Main.programOptions(), args);
        verify(mockChecker, times(1)).performCompatibilityCheck(PROTOBUF, SchemaChecker.DIRECTION.BACKWARD, Path.of("new"), Path.of("existing"));

    }

    @Test(expected = ParseException.class)
    public void check_throwsUnknownProtocol() throws ParseException {
        SchemaChecker mockChecker = mock(SchemaChecker.class);
        Main main = new Main(mockChecker);

        String[] args = new String[] {"-p", "x", "new", "existing"};
        main.check(Main.programOptions(), args);

    }
}