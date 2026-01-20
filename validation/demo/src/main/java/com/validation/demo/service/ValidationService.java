
package com.validation.demo.service;

import com.validation.demo.model.Drug;
import com.validation.demo.model.DrugInteraction;
import com.validation.demo.repository.DrugRepository;
import com.validation.demo.repository.DrugInteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ValidationService {

	@Autowired
	private DrugRepository drugRepository;

	@Autowired
	private DrugInteractionRepository drugInteractionRepository;

	public List<Drug> getAllDrugs() {
		return drugRepository.findAll();
	}

	public ValidationResult validateDrugs(List<String> drugNames) {
		// 1. Map names to Drug objects
		List<Drug> drugs = drugRepository.findByNameIn(drugNames);
		Map<String, String> nameToId = drugs.stream()
				.collect(Collectors.toMap(Drug::getName, Drug::getId));

		// 2. Generate all unique pairs (unordered)
		List<String> ids = new ArrayList<>(nameToId.values());
		Set<Pair> pairs = new HashSet<>();
		for (int i = 0; i < ids.size(); i++) {
			for (int j = i + 1; j < ids.size(); j++) {
				String a = ids.get(i);
				String b = ids.get(j);
				pairs.add(new Pair(a, b));
			}
		}

		// 3. Check interactions
		List<Issue> issues = new ArrayList<>();
		boolean block = false;
		for (Pair pair : pairs) {
			Optional<DrugInteraction> interaction = drugInteractionRepository.findByDrugAAndDrugB(pair.a, pair.b);
			if (!interaction.isPresent()) {
 		   interaction = drugInteractionRepository.findByDrugAAndDrugB(pair.b, pair.a);
			}
			if (interaction.isPresent()) {
				String level = interaction.get().getLevel();
				if ("MAJOR".equalsIgnoreCase(level) || "CONTRAINDICATED".equalsIgnoreCase(level)) {
					block = true;
				}
				issues.add(new Issue(pair.a, pair.b, level));
			}
		}

		ValidationResult result = new ValidationResult();
		result.setOk(!block);
		result.setIssues(issues);
		result.setMappedItems(drugs);
		return result;
	}

	// Helper classes
	public static class Pair {
		public String a;
		public String b;
		public Pair(String id1, String id2) {
			if (id1.compareTo(id2) < 0) {
				this.a = id1;
				this.b = id2;
			} else {
				this.a = id2;
				this.b = id1;
			}
		}
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Pair pair = (Pair) o;
			return Objects.equals(a, pair.a) && Objects.equals(b, pair.b);
		}
		@Override
		public int hashCode() {
			return Objects.hash(a, b);
		}
	}

	public static class Issue {
		public String drugA;
		public String drugB;
		public String level;
		public Issue(String drugA, String drugB, String level) {
			this.drugA = drugA;
			this.drugB = drugB;
			this.level = level;
		}
		// getters/setters if needed
	}

	public static class ValidationResult {
		private boolean ok;
		private List<Issue> issues;
		private List<Drug> mappedItems;

		public boolean isOk() { return ok; }
		public void setOk(boolean ok) { this.ok = ok; }
		public List<Issue> getIssues() { return issues; }
		public void setIssues(List<Issue> issues) { this.issues = issues; }
		public List<Drug> getMappedItems() { return mappedItems; }
		public void setMappedItems(List<Drug> mappedItems) { this.mappedItems = mappedItems; }
	}
}

