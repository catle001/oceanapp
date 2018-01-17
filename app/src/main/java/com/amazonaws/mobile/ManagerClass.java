package com.amazonaws.mobile;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * Created by CAT on 22/11/2017.
 */

public class ManagerClass {

    private static final String LOG_TAG = "AmazonClientManager";

    private AmazonDynamoDBClient ddb = null;
    private Context context;

    public ManagerClass (Context context) {
        this.context = context;
    }

    public AmazonDynamoDBClient ddb() {
        validateCredentials();
        return ddb;
    }


    public void validateCredentials() {

        if (ddb == null) {
            initClients();
        }
    }

    private void initClients() {
        CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(
                context,
                AWSConfiguration.AMAZON_COGNITO_IDENTITY_POOL_ID,
                AWSConfiguration.AMAZON_COGNITO_REGION);

        ddb = new AmazonDynamoDBClient(credentials);
        ddb.setRegion(Region.getRegion(AWSConfiguration.AMAZON_DYNAMODB_REGION));
    }
}
