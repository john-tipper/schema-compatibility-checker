package org.johntipper;

import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.util.List;

public class Main {
    static String PROTOCOL_ARG = "p";
    static String DIRECTION_ARG = "d";

    final SchemaChecker schemaChecker;

    public Main(SchemaChecker schemaChecker) {
        this.schemaChecker = schemaChecker;
    }

    List<String> check(Options options, String[] args) throws ParseException {

        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        String protocolStr = line.getOptionValue(PROTOCOL_ARG);

        SchemaChecker.DIRECTION direction = SchemaChecker.DIRECTION.BACKWARD;
        if (line.hasOption(DIRECTION_ARG)) {
            switch (line.getOptionValue(DIRECTION_ARG).toLowerCase()) {
                case "forward":
                    direction = SchemaChecker.DIRECTION.FORWARD;
                    break;

                case "backward":
                    direction = SchemaChecker.DIRECTION.BACKWARD;
                    break;

                case "both":
                    direction = SchemaChecker.DIRECTION.BOTH;
                    break;
            }

        }


        SchemaChecker.SCHEMA_PROTOCOL protocol;

        switch (protocolStr.toLowerCase()) {
            case "a":
            case "avro":
                protocol = SchemaChecker.SCHEMA_PROTOCOL.AVRO;
                break;

            case "j":
            case "json":
                protocol = SchemaChecker.SCHEMA_PROTOCOL.JSON;
                break;

            case "p":
            case "protobuf":
                protocol = SchemaChecker.SCHEMA_PROTOCOL.PROTOBUF;
                break;

            default:
                throw new ParseException("Protocol not recognised");

        }

        String[] schemaList = line.getArgs();

        if (schemaList.length != 2) {
            throw new ParseException("Paths to existing and new schema must be specified");
        }
        List<String> errors;

        switch (direction) {
            case BACKWARD:
            case FORWARD:
                errors = schemaChecker.performCompatibilityCheck(protocol, direction, Path.of(schemaList[0]), Path.of(schemaList[1]));
                break;

            case BOTH:
            default:
                errors = schemaChecker.performCompatibilityCheck(protocol, SchemaChecker.DIRECTION.FORWARD, Path.of(schemaList[0]), Path.of(schemaList[1]));
                errors.addAll(schemaChecker.performCompatibilityCheck(protocol, SchemaChecker.DIRECTION.BACKWARD, Path.of(schemaList[0]), Path.of(schemaList[1])));
                break;

        }

        return errors;
    }

    static Options programOptions() {
        Options options = new Options();
        options.addOption(Option.builder(PROTOCOL_ARG)
                                .longOpt("protocol")
                                .argName("protocol")
                                .desc("Schema protocol [a, avro, j, json, p, protobuf]")
                                .hasArg()
                                .required()
                                .build());

        options.addOption(Option.builder(DIRECTION_ARG)
                                .longOpt("direction")
                                .argName("direction")
                                .desc("Compatibility direction [forward, backward, both] default=backward")
                                .hasArg()
                                .build());
        return options;
    }

    public static void main(String[] args) {
        Main main = new Main(new DefaultSchemaChecker());
        Options options = programOptions();
        try {
            List<String> errors = main.check(options, args);
            if (errors.size() > 0) {
                for (String err : errors) {
                    System.err.println(err);
                }
                System.exit(1);
            }
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("csc ARGS PATH_EXISTING_SCHEMA PATH_NEW_SCHEMA", "Where ARGS are:", options, null);
            System.exit(-1);
        }

    }
}


