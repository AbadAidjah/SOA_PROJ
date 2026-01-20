
package com.validation.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;

import com.pharmacie.demo.ReserveItem;
import com.pharmacie.demo.ReserveMedicinesRequest;
import com.pharmacie.demo.ReserveMedicinesResponse;
import com.validation.demo.controller.DrugController;
import com.validation.demo.model.Drug;
import com.validation.demo.model.DrugInteraction;
import com.validation.demo.repository.DrugInteractionRepository;
import com.validation.demo.repository.DrugRepository;
// import com.validation.validation.ReserveItem;
// import com.validation.validation.ReserveMedicinesRequest;
// import com.validation.validation.ReserveMedicinesResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ValidationService {

	@Autowired
	private DrugRepository drugRepository;

	@Autowired
	private DrugInteractionRepository drugInteractionRepository;

	@Autowired
	private WebServiceTemplate webServiceTemplate;

	// private final com.validation.validation.ObjectFactory pharmacieObjectFactory = new com.validation.validation.ObjectFactory();

	@Value("${pharmacie.soap.url}")
	private String pharmacieSoapUrl;

	public List<Drug> getAllDrugs() {
		return drugRepository.findAll();
	}

	public ValidationResult validateDrugs(List<DrugController.DrugRequest> drugRequests) {
    List<String> drugNames = drugRequests.stream().map(d -> d.name).toList();
    Map<String, Integer> nameToQty = drugRequests.stream()
            .collect(Collectors.toMap(d -> d.name, d -> d.quantity));
    List<Drug> drugs = drugRepository.findByNameIn(drugNames);
    Map<String, String> nameToId = drugs.stream()
            .collect(Collectors.toMap(Drug::getName, Drug::getId));

    List<String> ids = new ArrayList<>(nameToId.values());
    Set<Pair> pairs = new HashSet<>();
    for (int i = 0; i < ids.size(); i++) {
        for (int j = i + 1; j < ids.size(); j++) {
            String a = ids.get(i);
            String b = ids.get(j);
            pairs.add(new Pair(a, b));
        }
    }

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

    if (result.isOk() && !drugs.isEmpty()) {
		System.out.println("the block is entereddddd");
        try {
            // ReserveMedicinesRequest soapRequest = pharmacieObjectFactory.createReserveMedicinesRequest();
			ReserveMedicinesRequest soapRequest = new ReserveMedicinesRequest();
			System.out.println("the block is entereddddd22222222");
            for (Drug drug : drugs) {
                ReserveItem item = new ReserveItem();
                item.setDrugCode(drug.getId());
                item.setQtyReserved(nameToQty.getOrDefault(drug.getName(), 1));
                soapRequest.getItems().add(item);
				System.out.println("the block is entereddddd333333333");
            }
            WebServiceMessageCallback messageCallback = message -> {
                if (message instanceof SoapMessage soapMessage) {
                    soapMessage.setSoapAction("");
                }
            };
            ReserveMedicinesResponse soapResponse = (ReserveMedicinesResponse)
                    webServiceTemplate.marshalSendAndReceive(pharmacieSoapUrl, soapRequest, messageCallback);
							System.out.println("the block is entereddddd444444444444");
					System.out.println("SOAP response: " + soapResponse.getStatus() + " - " + soapResponse.getMessage());
        } catch (Exception e) {
            System.err.println("SOAP request failed: " + e.getMessage());
        }
    }
    return result;
}

	
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

