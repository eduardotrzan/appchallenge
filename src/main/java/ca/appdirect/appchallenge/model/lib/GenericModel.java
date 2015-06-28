package ca.appdirect.appchallenge.model.lib;

import java.io.Serializable;

public class GenericModel<ID> implements Serializable {

	private static final long serialVersionUID = 7718834633309817219L;

	private ID id;

	public ID getId() {
		return this.id;
	}

	public void setId(final ID id) {
		this.id = id;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final boolean equals(final Object object) {
		if (object == null) {
			return Boolean.FALSE;
		}

		if (this == object) {
			return Boolean.TRUE;
		}

		if (!(object instanceof GenericModel)) {
			return Boolean.FALSE;
		}

		try {
			GenericModel<ID> otherObject = (GenericModel<ID>) object;
			ID thisId 					= this.getId();
			ID oId 						= otherObject.getId();

			if (thisId == null) {
				return Boolean.FALSE;
			}

			if (oId == null) {
				return Boolean.FALSE;
			}

			if (!thisId.equals(oId)) {
				return Boolean.FALSE;
			}
		} catch (Exception e) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result 		= 1;
		ID thisId 		= this.getId();

		if ((thisId == null)) {
			result = (prime * result) + 0;
		} else {
			result = (prime * result) + thisId.hashCode();
		}
		return result;
	}
}