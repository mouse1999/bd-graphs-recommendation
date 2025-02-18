package com.amazon.ata.graphs.dependency;

import com.amazon.ata.graphs.cli.DiscussionCli;
import com.amazon.ata.graphs.cli.DiscussionCliState;
import com.amazon.ata.graphs.dynamodb.FollowEdgeDao;
import com.amazon.ata.graphs.dynamodb.MemberDao;
import com.amazon.ata.graphs.dynamodb.RecommendationDao;
import com.amazon.ata.graphs.handler.*;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = { DiscussionCliModule.class })
@Singleton
public interface DiscussionCliComponent {
    DiscussionCli provideDiscussionCli();
    LoginHandler provideLoginHandler();
    MemberDao provideMemberDao();
    ViewTopicsHandler provideViewTopicsHandler();
    CreateTopicHandler provideCreateTopicHandler();
    ViewTopicMessagesHandler provideViewTopicMessagesHandler();
    ChangeTopicHandler provideChangeTopicHandler();
    CreateTopicMessageHandler provideCreateTopicMessageHandler();
    ExitHandler provideExitHandler();
    DiscussionCliState provideDiscussionCliState();
    FollowEdgeDao provideFollowEdgeDao();
    RecommendationDao provideRecommendationDao();
    ViewRecommendationsHandler provideRecommendationsHandler();
    ViewYourFollowsHandler provideViewYourFollowsHandler();
    CreateFollowHandler provideCreateFollowHandler();
    UnfollowHandler provideUnfollowHandler();
}
