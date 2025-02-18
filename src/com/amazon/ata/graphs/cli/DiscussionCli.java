package com.amazon.ata.graphs.cli;

import com.amazon.ata.graphs.handler.*;
import com.amazon.ata.input.console.ATAUserHandler;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.amazon.ata.graphs.cli.DiscussionCliOperation.LOGIN;

/**
 * The CLI app to interact with user to fetch Discussion app data for them.
 */
public class DiscussionCli {
    // Mappings from user-entered options to DiscussionCliOperation enums (see handleRequests())
    private final Map<String, DiscussionCliOperation> userOperationOptions = new HashMap<>();
    private final Map<DiscussionCliOperation, DiscussionCliOperationHandler> operationHandlers = new HashMap<>();

    private final ATAUserHandler userHandler;
    private final LoginHandler loginHandler;
    private final ViewTopicsHandler viewTopicsHandler;
    private final CreateTopicHandler createTopicHandler;
    private final ViewTopicMessagesHandler viewTopicMessagesHandler;
    private final ChangeTopicHandler changeTopicHandler;
    private final CreateTopicMessageHandler createTopicMessageHandler;
    private final ViewRecommendationsHandler viewRecommendationsHandler;
    private final ViewYourFollowsHandler viewYourFollowsHandler;
    private final CreateFollowHandler createFollowHandler;
    private final ExitHandler exitHandler;
    private final DiscussionCliState state;
    private final UnfollowHandler unfollowHandler;

    /**
     * Creates a new DiscussionCli to interact with user.
     */
    @Inject
    public DiscussionCli(final ATAUserHandler userHandler,
                         final LoginHandler loginHandler,
                         final ViewTopicsHandler viewTopicsHandler,
                         final CreateTopicHandler createTopicHandler,
                         final ViewTopicMessagesHandler viewTopicMessagesHandler,
                         final ChangeTopicHandler changeTopicHandler,
                         final CreateTopicMessageHandler createTopicMessageHandler,
                         final ViewRecommendationsHandler viewRecommendationsHandler,
                         final ViewYourFollowsHandler viewYourFollowsHandler,
                         final CreateFollowHandler createFollowHandler,
                         final ExitHandler exitHandler,
                         final DiscussionCliState state,
                         final UnfollowHandler unfollowHandler) {
        this.userHandler = userHandler;
        this.loginHandler = loginHandler;
        this.viewTopicsHandler = viewTopicsHandler;
        this.createTopicHandler = createTopicHandler;
        this.viewTopicMessagesHandler = viewTopicMessagesHandler;
        this.changeTopicHandler = changeTopicHandler;
        this.createTopicMessageHandler = createTopicMessageHandler;
        this.viewRecommendationsHandler = viewRecommendationsHandler;
        this.viewYourFollowsHandler = viewYourFollowsHandler;
        this.createFollowHandler = createFollowHandler;
        this.exitHandler = exitHandler;
        this.state = state;
        this.unfollowHandler = unfollowHandler;

        registerHandlers();
    }

    /**
     * For as long as the user wants to keep using the CLI app, keeps handling their requests,
     * e.g. to fetch a member, fetch a message.
     */
    public void handleRequests() {
        displayWelcomeMessage();

        state.setNextOperation(LOGIN);
        DiscussionCliOperation nextOperation;
        do {
            nextOperation = getNextRequestedCliOperation();
            if (operationHandlers.containsKey(nextOperation)) {
                System.out.println(operationHandlers.get(nextOperation).handleRequest(state));
            } else {
                System.out.println("Hm, got a valid DiscussionCliOperation that the DiscussionCli doesn't " +
                                   "know how to handle, please fix this!: " + nextOperation + ". Exiting");
                state.setNextOperation(DiscussionCliOperation.EXIT);
            }
        } while (state.getNextOperation() != DiscussionCliOperation.EXIT);
    }

    private void registerHandlers() {
        userOperationOptions.put("1", DiscussionCliOperation.VIEW_TOPICS);
        userOperationOptions.put("2", DiscussionCliOperation.CHANGE_TOPIC);
        userOperationOptions.put("3", DiscussionCliOperation.VIEW_TOPIC_MESSAGES);
        userOperationOptions.put("4", DiscussionCliOperation.CREATE_TOPIC);
        userOperationOptions.put("5", DiscussionCliOperation.CREATE_TOPIC_MESSAGE);
        userOperationOptions.put("6", DiscussionCliOperation.VIEW_YOUR_FOLLOWS);
        userOperationOptions.put("7", DiscussionCliOperation.CREATE_FOLLOW);
        userOperationOptions.put("8", DiscussionCliOperation.VIEW_RECOMMENDATIONS);
        userOperationOptions.put("9", DiscussionCliOperation.EXIT);
        userOperationOptions.put("10", DiscussionCliOperation.UNFOLLOW);

        operationHandlers.put(LOGIN, loginHandler);
        operationHandlers.put(DiscussionCliOperation.VIEW_TOPICS, viewTopicsHandler);
        operationHandlers.put(DiscussionCliOperation.CHANGE_TOPIC, changeTopicHandler);
        operationHandlers.put(DiscussionCliOperation.CREATE_TOPIC, createTopicHandler);
        operationHandlers.put(DiscussionCliOperation.VIEW_TOPIC_MESSAGES, viewTopicMessagesHandler);
        operationHandlers.put(DiscussionCliOperation.CREATE_TOPIC_MESSAGE, createTopicMessageHandler);
        operationHandlers.put(DiscussionCliOperation.VIEW_YOUR_FOLLOWS, viewYourFollowsHandler);
        operationHandlers.put(DiscussionCliOperation.CREATE_FOLLOW, createFollowHandler);
        operationHandlers.put(DiscussionCliOperation.VIEW_RECOMMENDATIONS, viewRecommendationsHandler);
        operationHandlers.put(DiscussionCliOperation.UNFOLLOW, unfollowHandler);
        operationHandlers.put(DiscussionCliOperation.EXIT, exitHandler);
    }

    private DiscussionCliOperation getNextRequestedCliOperation() {
        if (null != state.getNextOperation()) {
            DiscussionCliOperation nextOperation = state.getNextOperation();
            state.setNextOperation(null);
            return nextOperation;
        }

        String prompt = "\nWhat would you like to do?\n" +
                        userOperationOptions.entrySet().stream()
                            .map((entry) -> entry.getKey() + ": " + entry.getValue().getUserVisibleRepresentation())
                            .collect(Collectors.joining("\n"));

        String nextCliOperationOption;

        do {
            nextCliOperationOption = userHandler.getString(userOperationOptions.keySet(), prompt, "> ")
                                                .trim();
        } while ("".equals(nextCliOperationOption));

        return userOperationOptions.get(nextCliOperationOption);
    }

    private void displayWelcomeMessage() {
        System.out.println("Welcome to the Discussion app CLI.");
    }
}
