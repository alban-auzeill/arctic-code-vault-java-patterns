package com.ctrip.framework.apollo.common.dto; // (rank 50) copied from https://github.com/ctripcorp/apollo/blob/849ad711058b0a553e6192bae31e3fb0fb8daec8/apollo-common/src/main/java/com/ctrip/framework/apollo/common/dto/NamespaceDTO.java

import com.ctrip.framework.apollo.common.utils.InputValidator;
import javax.validation.constraints.Pattern;

public class NamespaceDTO extends BaseDTO{
  private long id;

  private String appId;

  private String clusterName;

  @Pattern(
      regexp = InputValidator.CLUSTER_NAMESPACE_VALIDATOR,
      message = "Invalid Namespace format: " + InputValidator.INVALID_CLUSTER_NAMESPACE_MESSAGE
  )
  private String namespaceName;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAppId() {
    return appId;
  }

  public String getClusterName() {
    return clusterName;
  }

  public String getNamespaceName() {
    return namespaceName;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  public void setNamespaceName(String namespaceName) {
    this.namespaceName = namespaceName;
  }
}
