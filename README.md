# Schema Compatibility Checker

Simple command line tool to perform compatibility checking of schemas written in Avro, Protobuf and JSON schema.

## Usage

Run from Docker:

```shell script
# bind mount a directory or files and then refer to the schemas within
docker run -it -v $(pwd)/path/to/schemas:/schemas johnlondon/schema-compatibility-checker -p json -d both /schemas/new.json /schemas/existing.json 
```

If running the Java application directly, specify the protocol and paths to the schemas to compare.

```shell script
usage: csc ARGS PATH_EXISTING_SCHEMA PATH_NEW_SCHEMA
Where ARGS are:
 -d,--direction <direction>   Compatibility direction [forward, backward,
                              both] default=backward
 -p,--protocol <protocol>     Schema protocol [a, avro, j, json, p,
                              protobuf]
```

The code will return a return code of 0 if the schemas are compatible, otherwise it will return a non-zero code and print out the errors it found.

## Development

Build the Java code, test and then build the Docker images.
```shell script
./gradlew dockerBuildImage
```

## Limitations

- Only the JSON Schema comparisons have been tested. Use other protocols at own risk.
- The comparisons are done using Schema Registry tooling and is subject to limitations of thta code. For instance, `patternProperties` appears to be unsupported and ignored when performing comparisons.

## Licence

This code is released under an Apache 2 licence. It makes use of code from [Confluent Schema Registry](https://github.com/confluentinc/schema-registry) which is licenced under the Confluent Community licence.
