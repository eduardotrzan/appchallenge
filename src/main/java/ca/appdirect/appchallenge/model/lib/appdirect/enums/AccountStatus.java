package ca.appdirect.appchallenge.model.lib.appdirect.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "status")
public enum AccountStatus {
	FREE_TRIAL
	, FREE_TRIAL_EXPIRED
	, ACTIVE
	, SUSPENDED
	, CANCELLED
}