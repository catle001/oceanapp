package com.amazonaws.mobile;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.models.nosql.DataCollectionTableDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.example.user.fypapp.Presenter.QuizPresenter;

/**
 * Created by CAT on 23/11/2017.
 */

public class DynamoDBManager {

    public static void update(DataCollectionTableDO appClass) {

        AmazonDynamoDBClient ddb = QuizPresenter.managerClass
                .ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        mapper.save(appClass);
    }
}
