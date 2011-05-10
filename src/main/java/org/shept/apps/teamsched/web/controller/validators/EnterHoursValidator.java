package org.shept.apps.teamsched.web.controller.validators;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.shept.apps.teamsched.orm.Timesheet;
import org.shept.org.springframework.beans.support.PageableList;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentUtils;
import org.shept.org.springframework.web.servlet.mvc.delegation.ComponentValidator;
import org.springframework.validation.Errors;

/** 
 * @version $$Id: EnterHoursValidator.java,v 1.1 2009/11/27 18:53:44 oops.oops Exp $$
 *
 * @author Andi
 *
 */
public class EnterHoursValidator implements ComponentValidator {

	@SuppressWarnings("unchecked")
	public void validate(Object target, Errors errors, String path) {
		if (errors.hasErrors()) return;	// stop if there are already data type mismatches
		
		PageableList pageHolder = (PageableList) ComponentUtils.getComponent(target, path);
		List<Timesheet> timesheets = (List<Timesheet>)pageHolder.getSource();
		
		// resort the timesheets in case of new entry changed the sort order
		Collections.sort(timesheets);
		
		validatePeriods(timesheets, errors);
		if (!errors.hasErrors())
			validateOverlaps(timesheets, errors);
	}

	private void validatePeriods(List<Timesheet> timesheets, Errors errors) {
		for (int idx = 0; idx < timesheets.size(); idx++) {
			Timesheet t = timesheets.get(idx);
			if (null == t.getDatetimefrom() || null == t.getDatetimetill())
				continue;
			if (null == t.getIssueId()) {
				errors.rejectValue("source[" + idx + "].timefrom", "enterhours.selectionInvalid");
				errors.rejectValue("source[" + idx + "].timetill", "enterhours.selectionInvalid");
			}
			if (t.getDatetimefrom().compareTo(t.getDatetimetill()) >= 0) {
				errors.rejectValue("source[" + idx + "].timefrom", "enterhours.timeValidation");
				errors.rejectValue("source[" + idx + "].timetill", "enterhours.timeValidation");
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void validateOverlaps(List<Timesheet> timesheets, Errors errors) {
		SortedMap<Date, Set<Integer>> dups = new TreeMap<Date, Set<Integer>>();
		SortedMap<Date, Set<Integer>> headDups = new TreeMap<Date, Set<Integer>>();
		Set<Integer> marks;
		Date time = new Date(0);
		Date last = new Date(0);
		for (Integer i = 0; i < timesheets.size(); i++) {
			Timesheet t = (Timesheet) timesheets.get(i);
			if (t.getDatetimetill() != null) {
				time = t.getDatetimefrom().getTime();
				marks = dups.get(time);
				if (marks == null) { // copy from previous entry
					marks = new HashSet<Integer>();
					headDups = (SortedMap<Date, Set<Integer>>) dups
							.headMap(time);
					if (headDups != null && !headDups.isEmpty()) {
						last = headDups.lastKey();
						marks.addAll(headDups.get(last));
					}
				}
				marks.add(i);
				dups.put(time, marks);
				time = t.getDatetimetill().getTime();
				marks = dups.get(time);
				if (marks == null) { // copy from prevous entry
					marks = new HashSet<Integer>();
					headDups = (SortedMap<Date, Set<Integer>>) dups
							.headMap(time);
					if (headDups != null && !headDups.isEmpty()) {
						last = headDups.lastKey();
						marks.addAll(headDups.get(last));
					}
				}
				marks.remove(i);
				dups.put(time, marks);
			}
		}
		Entry<Date, Set<Integer>> es;
		for (Iterator iter = dups.entrySet().iterator(); iter.hasNext();) {
			es = (Entry<Date, Set<Integer>>) iter.next();
			if (es.getValue().size() > 1) {
				for (Iterator it2 = es.getValue().iterator(); it2.hasNext();) {
					Integer idx = (Integer) it2.next();
					if (((Timesheet) timesheets.get(idx)).getDatetimefrom()
							.equals(es.getKey())) {
						errors.rejectValue("source[" + idx + "].timefrom", "enterhours.timeOverlap");
					}
					if (es.getValue().contains(idx - 1)) {
						errors.rejectValue("source[" + (idx - 1) + "].timetill","enterhours.timeOverlap");
					}
				}
			}
		}
	}

}
