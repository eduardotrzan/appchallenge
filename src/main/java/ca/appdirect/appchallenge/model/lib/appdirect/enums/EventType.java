package ca.appdirect.appchallenge.model.lib.appdirect.enums;

public enum EventType {
	SUBSCRIPTION_ORDER
	, SUBSCRIPTION_CANCEL
	, SUBSCRIPTION_CHANGE
	, SUBSCRIPTION_NOTICE
	, USER_ASSIGNMENT
	, USER_UNASSIGNMENT
	;

	public static EventType getEventType(final String type) {
		if (SUBSCRIPTION_ORDER.name().equals(type)) {
			return SUBSCRIPTION_ORDER;
		} else if (SUBSCRIPTION_CANCEL.name().equals(type)) {
			return SUBSCRIPTION_CANCEL;
		} else if (SUBSCRIPTION_CHANGE.name().equals(type)) {
			return SUBSCRIPTION_CHANGE;
		} else if (SUBSCRIPTION_NOTICE.name().equals(type)) {
			return SUBSCRIPTION_NOTICE;
		} else if (USER_ASSIGNMENT.name().equals(type)) {
			return USER_ASSIGNMENT;
		} else if (USER_UNASSIGNMENT.name().equals(type)) {
			return USER_UNASSIGNMENT;
		}
		return null;
	}
}