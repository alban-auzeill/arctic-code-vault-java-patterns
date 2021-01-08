package com.ctrip.framework.apollo.common.dto; // (rank 50) copied from https://github.com/ctripcorp/apollo/blob/849ad711058b0a553e6192bae31e3fb0fb8daec8/apollo-common/src/main/java/com/ctrip/framework/apollo/common/dto/ClusterDTO.java

import com.ctrip.framework.apollo.common.utils.InputValidator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ClusterDTO extends BaseDTO{

  private long id;

  @NotBlank(message = "cluster name cannot be blank")
  @Pattern(
      regexp = InputValidator.CLUSTER_NAMESPACE_VALIDATOR,
      message = "Invalid Cluster format: " + InputValidator.INVALID_CLUSTER_NAMESPACE_MESSAGE
  )
  private String name;

  @NotBlank(message = "appId cannot be blank")
  private String appId;

  private long parentClusterId;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public long getParentClusterId() {
    return parentClusterId;
  }

  public void setParentClusterId(long parentClusterId) {
    this.parentClusterId = parentClusterId;
  }
}
