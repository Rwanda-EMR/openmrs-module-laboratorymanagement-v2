package org.openmrs.module.laboratorymanagement.utils;

import org.openmrs.TestOrder;

public class LabOrderWrapper implements Comparable<LabOrderWrapper> {

	private final TestOrder laOrder;


	public LabOrderWrapper(TestOrder l) {
		laOrder = l;
	}

	@Override
	public int compareTo(LabOrderWrapper that) {

			// If not ordered by position in the set, try to order by start date
		int ret = this.laOrder.getEffectiveStartDate().compareTo(that.laOrder.getEffectiveStartDate());
		if (ret != 0) {
			return ret;
		}

		// If this still does not result in a difference, sort by primary key id
		return this.laOrder.getId().compareTo(that.laOrder.getId());
	}

	public TestOrder getLaOrder() {
		return laOrder;
	}

}
