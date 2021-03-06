package org.whispersystems.textsecuregcm.storage; // (rank 325) copied from https://github.com/signalapp/Signal-Server/blob/3432529f9c018d75774ce89f3207b18051c26fe7/service/src/main/java/org/whispersystems/textsecuregcm/storage/RemoteConfig.java

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RemoteConfig {

  @JsonProperty
  @Pattern(regexp = "[A-Za-z0-9\\.]+")
  private String name;

  @JsonProperty
  @NotNull
  @Min(0)
  @Max(100)
  private int percentage;

  @JsonProperty
  @NotNull
  private Set<UUID> uuids = new HashSet<>();

  public RemoteConfig() {}

  public RemoteConfig(String name, int percentage, Set<UUID> uuids) {
    this.name       = name;
    this.percentage = percentage;
    this.uuids      = uuids;
  }

  public int getPercentage() {
    return percentage;
  }

  public String getName() {
    return name;
  }

  public Set<UUID> getUuids() {
    return uuids;
  }
}
