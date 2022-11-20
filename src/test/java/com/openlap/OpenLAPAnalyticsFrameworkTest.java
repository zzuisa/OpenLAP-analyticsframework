package com.openlap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.openlap.AnalyticsEngine.dto.LrsObjects;
import com.openlap.AnalyticsEngine.dto.QueryParameters;
import com.openlap.AnalyticsEngine.model.Statement;
import com.openlap.AnalyticsMethods.services.AnalyticsMethodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;


/**
 Created By Ao on 2022/10/30
 */

//@SpringBootTest(classes = OpenLAPAnalyticsFramework.class)
//@RunWith(SpringJUnit4ClassRunner.class)
public class OpenLAPAnalyticsFrameworkTest {

    public static void main(String[] args) {

        List<LrsObjects> lrsObjects = Arrays.asList(new LrsObjects("1", "324"), new LrsObjects("1", "34"));
        lrsObjects.forEach(System.out::println);
        List<Object> collect = lrsObjects.stream().map(LrsObjects::getStatement).collect(Collectors.toList());
        System.out.println(collect);
        Gson gson = new Gson();
        ObjectMapper objectMapper = new ObjectMapper();
        // Converting Java Object to Json
        String temp = "{\n" +
                "            \"agg\": [\n" +
                "                {\n" +
                "                    \"$match\": {\n" +
                "                        \"statement.verb.id\": \"http://id.tincanapi.com/verb/viewed\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"$group\": {\n" +
                "                        \"_id\": \"$statement.actor.account.name\",\n" +
                "                        \"statement\": {\n" +
                "                            \"$sum\": 1\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            ],\n" +
                "            \"parametersToBeReturnedInResult\": {\n" +
                "                \"view_total\": 1\n" +
                "            },\n" +
                "            \"statementDuration\": [\n" +
                "                {\n" +
                "                    \"statement.stored\": {\n" +
                "                        \"$gte\": \"2022-07-07T12:10:11.221Z\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"statement.stored\": {\n" +
                "                        \"$lte\": \"2022-07-14T12:10:11.221Z\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        }";
        QueryParameters queryParameters = gson.fromJson(temp, QueryParameters.class);
        String statementDuration = gson.toJson(queryParameters.getStatementDuration());
        String parametersToReceive = gson.toJson(queryParameters.getParametersToBeReturnedInResult());
        // Converting Json to DBObject for MongoDB
        System.out.println("queryParameters = " + queryParameters);
        DBObject queryObject = (DBObject) JSON.parse(temp);
        @SuppressWarnings("deprecation")
        DBObject statementDurationObject = (DBObject) JSON.parse(statementDuration);
        @SuppressWarnings("deprecation")
        DBObject parametersToReceiveObject = (DBObject) JSON.parse(parametersToReceive);
    }
}
