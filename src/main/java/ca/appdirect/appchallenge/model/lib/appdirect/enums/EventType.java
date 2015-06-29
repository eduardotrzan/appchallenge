package ca.appdirect.appchallenge.model.lib.appdirect.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "type")
public enum EventType {
	SUBSCRIPTION_ORDER
	, SUBSCRIPTION_CANCEL
	, SUBSCRIPTION_CHANGE
	, SUBSCRIPTION_NOTICE
	, USER_ASSIGNMENT
	, USER_UNASSIGNMENT
}