package com.amazon.ata.graphs.cli;

/**
 * All operations supported by the ATA Discussion CLI
 */
public enum DiscussionCliOperation {
    LOGIN("Login"),
    VIEW_TOPICS("View topics"),
    CHANGE_TOPIC("Select topic"),
    VIEW_TOPIC("View topic"),
    CREATE_TOPIC("Create new topic"),
    VIEW_TOPIC_MESSAGES("View messages in topic"),
    CREATE_TOPIC_MESSAGE("Post message to topic"),
    VIEW_YOUR_FOLLOWS("View all members you follow"),
    CREATE_FOLLOW("Follow a member"),
    VIEW_RECOMMENDATIONS("View recommended members to follow"),
    UNFOLLOW("unfollow a member"),
    EXIT("Exit");

    private final String userVisibleRepresentation;

    DiscussionCliOperation(final String userVisibleRepresentation) {
        this.userVisibleRepresentation = userVisibleRepresentation;
    }

    /**
     * Returns a string representation of the DiscussionCliOperation appropriate
     * for display to a CLI user.
     *
     * @return the display string for this operation
     */
    public String getUserVisibleRepresentation() {
        return userVisibleRepresentation;
    }
}
