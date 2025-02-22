package com.amazon.ata.graphs.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides access to FollowEdge items.
 */
public class FollowEdgeDao {
    private DynamoDBMapper mapper;

    /**
     * Creates a FollowEdgeDao with the given DynamoDBMapper.
     * @param mapper The DynamoDBMapper
     */
    @Inject
    public FollowEdgeDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Retrieves a list of follows from the given username, if one exists.
     * @param username The username to look for
     * @return A list of all follows for the given user
     */
    public PaginatedQueryList<FollowEdge> getAllFollows(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is not provided");
        }
        FollowEdge followEdge = new FollowEdge(username, null);

        DynamoDBQueryExpression<FollowEdge> dynamoDBQueryExpression = new DynamoDBQueryExpression<FollowEdge>()
                .withHashKeyValues(followEdge);
        return mapper.query(FollowEdge.class, dynamoDBQueryExpression);
    }

    /**
     * Retrieves a list of followers for the given username, if one exists.
     * @param username The username to scope followers to
     * @return A list of all followers for the given user
     */
    public PaginatedQueryList<FollowEdge> getAllFollowers(String username) {

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is not provided");
        }
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":toUsername", new AttributeValue().withS(username));

        DynamoDBQueryExpression<FollowEdge> dynamoDBQueryExpression = new DynamoDBQueryExpression<FollowEdge>()
                .withKeyConditionExpression("toUsername = :toUsername")
                .withExpressionAttributeValues(valueMap);
        return mapper.query(FollowEdge.class, dynamoDBQueryExpression);
    }

    /**
     * Saves new follow.
     * @param fromUsername The Member that is following
     * @param toUsername The Member that is followed
     * @return The FollowEdge that was created
     */
    public FollowEdge createFollowEdge(String fromUsername, String toUsername) {
        if (null == fromUsername || null == toUsername) {
            throw new IllegalArgumentException("One of the passed in usernames was null: " + fromUsername + " was trying to follow " + toUsername);
        }

        FollowEdge edge = new FollowEdge(fromUsername, toUsername);
        mapper.save(edge);
        return edge;
    }
    public void unfollowEdge(String fromUsername, String toUsername) {
        FollowEdge followEdge = new FollowEdge(fromUsername, toUsername);
        mapper.delete(followEdge);

    }

}
