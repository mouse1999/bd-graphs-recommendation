package com.amazon.ata.graphs.lambda;

import com.amazon.ata.graphs.dynamodb.FollowEdge;
import com.amazon.ata.graphs.dynamodb.FollowEdgeDao;
import com.amazon.ata.graphs.dynamodb.Recommendation;
import com.amazon.ata.graphs.dynamodb.RecommendationDao;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.lambda.runtime.Context;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class CreateRecommendationsActivity {

    private FollowEdgeDao followEdgeDao;
    private RecommendationDao recommendationDao;

    public CreateRecommendationsActivity(RecommendationDao recommendationDao, FollowEdgeDao followEdgeDao) {
        this.followEdgeDao = followEdgeDao;
        this.recommendationDao = recommendationDao;
    }

    public List<Recommendation> handleRequest(CreateRecommendationsRequest input, Context context) {
        if (input == null || input.getUsername() == null) {
            throw new InvalidParameterException("missing input");
        }
        List<Recommendation> recommendations = new ArrayList<>();

        PaginatedQueryList<FollowEdge> allFollows = followEdgeDao.getAllFollows(input.getUsername());

        List<String> stringFollows = allFollows.stream()
                .map(FollowEdge::getToUsername)
                .collect(Collectors.toList());

        stringFollows.stream().forEach(stringFollow -> {
            followEdgeDao.getAllFollows(stringFollow).stream()
                    .map(FollowEdge::getToUsername)
                    .forEach(string ->{

                        if (recommendations.size() <= input.getLimit()) {
                            recommendations.add(new Recommendation(input.getUsername(), string, "Active"));
                        }
                    });
        });

        return recommendations;
    }
}
