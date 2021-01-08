/* (rank 383) copied from https://github.com/Meituan-Dianping/Zebra/blob/33d74b831abe7e8e2d29f8c4e145e46ba17432dc/zebra-client/src/main/java/com/dianping/zebra/shard/router/rule/dimension/DimensionRule.java
 * Copyright (c) 2011-2018, Meituan Dianping. All Rights Reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dianping.zebra.shard.router.rule.dimension;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.dianping.zebra.shard.router.rule.ShardEvalContext;
import com.dianping.zebra.shard.router.rule.ShardEvalResult;

/**
 * @author hao.zhu
 *
 */
public interface DimensionRule {

	public static final Pattern RULE_COLUMN_PATTERN = Pattern.compile("#(.+?)#");

	ShardEvalResult eval(ShardEvalContext evalContext);

	ShardEvalResult eval(ShardEvalContext evalContext, boolean isBatchInsert);

	Map<String, Set<String>> getAllDBAndTables();

	Set<String> getShardColumns();

	boolean isMaster();

	boolean isRange();
}
