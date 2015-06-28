package ca.appdirect.appchallenge.model.lib.appdirect;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "editionCode")
public enum EditionCode {

	FREE
	, BASIC
	, ADVANCED
	, PREMIUM
}