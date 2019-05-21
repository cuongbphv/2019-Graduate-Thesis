package com.graduate.thesis.backend.repository.aggregation.operation;

import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.bson.Document;
import org.springframework.util.Assert;

/**
 * @author cuongbphv created on 21/05/2019
 */
public class AddFieldOperation implements AggregationOperation {

    private final Document document;

    public AddFieldOperation(final Document document) {
        Assert.notNull(document, "Criteria must not be null!");
        this.document = document;
    }

    @Override
    public Document toDocument(final AggregationOperationContext context) {
        return new Document("$addFields", this.document);
    }
}