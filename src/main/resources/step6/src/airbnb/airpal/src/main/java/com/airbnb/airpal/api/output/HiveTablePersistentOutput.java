package com.airbnb.airpal.api.output; // (rank 574) copied from https://github.com/airbnb/airpal/blob/e3e65283d66d866c3321fd921e02428c6aed1747/src/main/java/com/airbnb/airpal/api/output/HiveTablePersistentOutput.java

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.regex.Pattern;

import static java.lang.String.format;

@Slf4j
@JsonTypeName("hive")
public class HiveTablePersistentOutput implements PersistentJobOutput
{
    private final static Pattern INVALID_TABLE_CHARS = Pattern.compile("\\s");

    private final UUID jobUUID;
    @Getter
    private final String tmpTableName;
    @Getter
    private final String destinationSchema;

    @Getter
    @Setter
    private URI location;

    public HiveTablePersistentOutput(UUID jobUUID,
                                     String tmpTableName,
                                     String destinationSchema)
    {
        this.jobUUID = jobUUID;
        this.tmpTableName = tmpTableName;
        try {
            this.location = new URI(format("%s.%s", destinationSchema, tmpTableName));
        }
        catch (URISyntaxException e) {
            this.location = null;
            log.error("Couldn't create hive output", e);
        }
        this.destinationSchema = destinationSchema;
    }

    @JsonCreator
    public HiveTablePersistentOutput(@JsonProperty("location") URI location,
                                     @JsonProperty("type") String type,
                                     @JsonProperty("description") String description)
    {
        this((UUID) null, null, null);
        this.location = location;
    }

    @Override
    public String getType()
    {
        return "hive";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String processQuery(String query)
    {
        String tableFqn = format("%s.%s", destinationSchema, tmpTableName);

        return format("CREATE TABLE %s AS %s", tableFqn, query);
    }
}
