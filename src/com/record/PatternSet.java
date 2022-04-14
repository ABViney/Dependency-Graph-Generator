package com.record;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A singular or order-preserved set of Patterns generated from a singular or array of
 * String parameters.
 * 
 * @author viney
 *
 */
public class PatternSet {
	
	Pattern[] patterns;
	
	/**
	 * PatternSet compiled of only a singular rule.
	 * 
	 * @param rule - String literal or regex pattern
	 */
	public PatternSet(String rule) {
		patterns = new Pattern[] { Pattern.compile(rule) };
	}
	
	/**
	 * PatternSet compiled of an initial grouping rule and subsequent
	 * iterative rules to be applied to the result(s) of the initial rule.
	 * 
	 * @param rules - String literal or regex patterns
	 */
	public PatternSet(String[] rules) {
		patterns = new Pattern[rules.length];
		for(int i = 0; i < patterns.length; i++) {
			patterns[i] = Pattern.compile(rules[i]);
		}
	}
	
	/**
	 * Parses the passed String for all matches of the initial rule of this instance.
	 * Additional rules stored in this PatternSet will be applied to the result(s) of the
	 * first Match sequence by order of reception until the final Match sequence is obtained.
	 * 
	 * @param data - String
	 * @return - List<String> final match state.
	 */
	public List<String> runPatternSet(String data) {
		List<String> lastMatches = new ArrayList<>();
		Matcher m = patterns[0].matcher(data);
		while(m.find()) lastMatches.add(m.group());
		
		for(int i = 1; i < patterns.length; i++) {
			List<String> nextMatches = new ArrayList<>();
			for(String prevMatch : lastMatches) {
				m = patterns[i].matcher(prevMatch);
				if(m.find()) nextMatches.add(m.group());
			} lastMatches = nextMatches;
		} return lastMatches;
	}
	
}
