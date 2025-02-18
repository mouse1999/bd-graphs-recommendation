package com.amazon.ata.graphs.dynamodb;

//import com.amazon.ata.graphs.dependency.DaggerDiscussionCliComponent;
import com.amazon.ata.graphs.dependency.DaggerDiscussionCliComponent;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides access to Member items.
 */
public class MemberDao {
    private DynamoDBMapper mapper;

    /**
     * Creates a MemberDao with the given DynamoDBMapper
     * @param mapper The DynamoDBMapper
     */
    @Inject
    public MemberDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Retrieves a Member with the given username, if one exists.
     * @param username The username to look for
     * @return The Member with the given username, if one exists. {@code null} otherwise.
     */
    public Member getMember(String username) {
        if (username.equals("")) {
            throw new IllegalArgumentException("username cannot be empty");
        }

        return mapper.load(Member.class, username);
    }

    /**
     * Saves new member.
     * @param member The Member to create
     * @return The Member that was created
     */
    public Member createMember(Member member) {
        if (null == member.getUsername() || null == member.isActive() || null == member.getKarmaPointsAvailable()) {
            throw new IllegalArgumentException("Member has null field(s): " + member);
        }

        mapper.save(member);
        return member;
    }

    public List<String> getCommonFollowers(String un1, String un2) {
        FollowEdgeDao followEdgeDao = DaggerDiscussionCliComponent.create().provideFollowEdgeDao();
        PaginatedQueryList<FollowEdge> un1FollowerEdges = followEdgeDao.getAllFollowers(un1);
        PaginatedQueryList<FollowEdge> un2FollowerEdges = followEdgeDao.getAllFollowers(un2);
        Set<String> un1FollowerUsernames = un1FollowerEdges.stream().map(FollowEdge::getFromUsername).collect(Collectors.toSet());
        Set<String> un2FollowerUsernames = un2FollowerEdges.stream().map(FollowEdge::getFromUsername).collect(Collectors.toSet());
        return un1FollowerUsernames.stream().filter(un2FollowerUsernames::contains).collect(Collectors.toList());
    }
}
