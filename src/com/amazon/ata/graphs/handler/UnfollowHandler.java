package com.amazon.ata.graphs.handler;

import com.amazon.ata.graphs.cli.DiscussionCliState;
import com.amazon.ata.graphs.dynamodb.FollowEdge;
import com.amazon.ata.graphs.dynamodb.FollowEdgeDao;
import com.amazon.ata.input.console.ATAUserHandler;

import javax.inject.Inject;
import java.util.List;

public class UnfollowHandler implements DiscussionCliOperationHandler{
    private final FollowEdgeDao followEdgeDao;
    private final ATAUserHandler userHandler;

    @Inject
    public UnfollowHandler(FollowEdgeDao followEdgeDao, ATAUserHandler userHandler) {
        this.followEdgeDao = followEdgeDao;
        this.userHandler = userHandler;

    }

    @Override
    public String handleRequest(DiscussionCliState state) {
        String username = userHandler.getString("", "username you want to unfollow");

        followEdgeDao.unfollowEdge(state.getCurrentMember().getUsername(), username);
        return "";
    }
}
