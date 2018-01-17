package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

/**
 * Created by CAT on 23/11/2017.
 */

@DynamoDBTable(tableName = "Survey")

public class DataCollectionTableDO {
    private String _userMatric;
    private String _mextraversion;
    private String _magreeableness;
    private String _mconscientiousness;
    private String _mneuroticism;
    private String _mopenness;
    private String _qextraversion;
    private String _qagreeableness;
    private String _qconscientiousness;
    private String _qneuroticism;
    private String _qopenness;

    @DynamoDBHashKey(attributeName = "matric")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserMatric() {
        return _userMatric;
    }

    public void setUserMatric(final String _userMatric) {
        this._userMatric = _userMatric;
    }

    @DynamoDBAttribute(attributeName = "mExtraversion")

    public String getMExtraversion() {
        return _mextraversion;
    }

    public void setMExtraversion(final String _mextraversion) {
        this._mextraversion = _mextraversion;
    }

    @DynamoDBAttribute(attributeName = "mAgreeableness")

    public String getMAgreeableness() {
        return _magreeableness;
    }

    public void setMAgreeableness(final String _magreeableness) {
        this._magreeableness = _magreeableness;
    }

    @DynamoDBAttribute(attributeName = "mConscientiousness")
    public String getMConscientiousness() {
        return _mconscientiousness;
    }

    public void setMConscientiousness(final String _mconscientiousness) {
        this._mconscientiousness = _mconscientiousness;
    }

    @DynamoDBAttribute(attributeName = "mNeuroticism")
    public String getMNeuroticism() {
        return _mneuroticism;
    }

    public void setMNeuroticism(final String _mneuroticism) {
        this._mneuroticism = _mneuroticism;
    }

    @DynamoDBAttribute(attributeName = "mOpenness")
    public String getMOpenness() {
        return _mopenness;
    }

    public void setMOpenness(final String _mopenness) {
        this._mopenness = _mopenness;
    }

    @DynamoDBAttribute(attributeName = "qExtraversion")
    public String getQExtraversion() {
        return _qextraversion;
    }

    public void setQExtraversion(final String _qextraversion) {
        this._qextraversion = _qextraversion;
    }

    @DynamoDBAttribute(attributeName = "qAgreeableness")
    public String getQAgreeableness() {
        return _qagreeableness;
    }

    public void setQAgreeableness(final String _qagreeableness) {
        this._qagreeableness = _qagreeableness;
    }

    @DynamoDBAttribute(attributeName = "qConscientiousness")
    public String getQConscientiousness() {
        return _qconscientiousness;
    }

    public void setQConscientiousness(final String _qconscientiousness) {
        this._qconscientiousness = _qconscientiousness;
    }

    @DynamoDBAttribute(attributeName = "qNeuroticism")
    public String getQNeuroticism() {
        return _qneuroticism;
    }

    public void setQNeuroticism(final String _qneuroticism) {
        this._qneuroticism = _qneuroticism;
    }

    @DynamoDBAttribute(attributeName = "qOpenness")
    public String getQOpenness() {
        return _qopenness;
    }

    public void setQOpenness(final String _qopenness) {
        this._qopenness = _qopenness;
    }

}
