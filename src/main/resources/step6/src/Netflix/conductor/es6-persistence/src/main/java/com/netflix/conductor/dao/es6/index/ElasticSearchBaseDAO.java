/* (rank 181) copied from https://github.com/Netflix/conductor/blob/d6b5ca91ab0e0bcc30772b9371a98286ddf87c9b/es6-persistence/src/main/java/com/netflix/conductor/dao/es6/index/ElasticSearchBaseDAO.java
 * Copyright 2020 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.netflix.conductor.dao.es6.index;

import com.netflix.conductor.dao.IndexDAO;
import com.netflix.conductor.dao.es6.index.query.parser.Expression;
import com.netflix.conductor.elasticsearch.query.parser.ParserException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

abstract class ElasticSearchBaseDAO implements IndexDAO {

    String indexPrefix;

    String loadTypeMappingSource(String path) throws IOException {
        return applyIndexPrefixToTemplate(IOUtils.toString(ElasticSearchBaseDAO.class.getResourceAsStream(path)));
    }

    private String applyIndexPrefixToTemplate(String text) {
        String pattern = "\"template\": \"\\*(.*)\\*\"";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, m.group(0).replaceFirst(Pattern.quote(m.group(1)), indexPrefix + "_" + m.group(1)));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    BoolQueryBuilder boolQueryBuilder(String expression, String queryString) throws ParserException {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        if (StringUtils.isNotEmpty(expression)) {
            Expression exp = Expression.fromString(expression);
            queryBuilder = exp.getFilterBuilder();
        }
        BoolQueryBuilder filterQuery = QueryBuilders.boolQuery().must(queryBuilder);
        QueryStringQueryBuilder stringQuery = QueryBuilders.queryStringQuery(queryString);
        return QueryBuilders.boolQuery().must(stringQuery).must(filterQuery);
    }

    protected String getIndexName(String documentType) {
        return indexPrefix + "_" + documentType;
    }
}
