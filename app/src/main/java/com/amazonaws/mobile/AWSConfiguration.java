//
// Copyright 2016 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.13
//
package com.amazonaws.mobile;

import com.amazonaws.regions.Regions;

/**
 * This class defines constants for the developer's resource
 * identifiers and API keys. This configuration should not
 * be shared or posted to any public source code repository.
 */
public class AWSConfiguration {
    // AWS MobileHub user agent string
    public static final String AWS_MOBILEHUB_USER_AGENT =
        "MobileHub 9a058248-4d26-4eaf-ab61-1e80bd2ea38f aws-my-sample-app-android-v0.13";
    // AMAZON COGNITO
    public static final Regions AMAZON_COGNITO_REGION =
      Regions.fromName("ap-southeast-1");
    public static final String  AMAZON_COGNITO_IDENTITY_POOL_ID =
        "ap-southeast-1:6290bb55-2265-4126-adbe-02e8a02c662e";
    public static final Regions AMAZON_DYNAMODB_REGION =
       Regions.AP_SOUTHEAST_1;
}