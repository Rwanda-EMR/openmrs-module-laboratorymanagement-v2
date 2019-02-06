package org.openmrs.module.laboratorymanagement.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.*;
import org.openmrs.api.context.Context;

public class GlobalPropertiesMgt {


	protected final static Log log = LogFactory.getLog(GlobalPropertiesMgt.class);


	public static Collection<Concept> getLabGlobalPropert() {
		List<Concept> labConcepts = new ArrayList<Concept>();
		GlobalProperty gp = Context
				.getAdministrationService()
				.getGlobalPropertyObject("laboratorymanagement.labtests.conceptIds");
		String[] conceptIds = gp.getPropertyValue().split(",");
		for (String s : conceptIds) {
			labConcepts.add(Context.getConceptService().getConcept(
					Integer.valueOf(s)));

		}
		return labConcepts;
	}
	
	/**
	 * gets the concepts that has multiple answers
	 * @return Map<Concept,Bolean>
	 */
	public static Map<Concept, Boolean> getConceptHasMultipleAnswers() { Map<Concept, Boolean> labConcepts = new HashMap<Concept, Boolean>();
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("laboratorymanagement.multipleAnswerConceptIds");
		String[] conceptIds = gp.getPropertyValue().split(",");
		for (String s : conceptIds) {
			labConcepts.put(Context.getConceptService().getConcept(Integer.valueOf(s)), true);
		}
		return labConcepts;
	}

	public  static String getLabOrderEncounterTypeFromGlobalProperties() {
		return Context.getAdministrationService().getGlobalProperty(
				"laboratorymanagement.encounterType.labOrderEncounterTypeId");

	}
	public static  String getLabOrderTypeId() {
		return Context.getAdministrationService().getGlobalProperty(
				"laboratorymanagement.orderType.labOrderTypeId");

	}
	/**
	 * Gets the a list of categorized grouup of lab exams
	 * 
	 * @return List<Concept>
	 */
	public static  List<Concept> getLabExamCategories() {
		List<Concept>conceptCategories=new ArrayList<Concept>();
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("laboratorymanagement.labExamCategory");
				String[] conceptIds = gp.getPropertyValue().split(",");
		for (String conceptIdstr: conceptIds) {
			Concept cpt=Context.getConceptService().getConcept(Integer.valueOf(conceptIdstr)) ;
		conceptCategories.add(cpt);	
			
		}
		return conceptCategories;
	}


	public Program getProgram(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getProgram(globalProperty);
	}

	public PatientIdentifierType getPatientIdentifier(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getPatientIdentifierType(globalProperty);
	}

	public static Concept getConcept(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getConcept(globalProperty);
	}

	public static List<Concept> getConceptList(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getConceptList(globalProperty);
	}

	public List<Concept> getConceptList(String globalPropertyName, String separator) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getConceptList(globalProperty,separator);
	}


	public List<Concept> getConceptsByConceptSet(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		Concept c = MetadataLookup.getConcept(globalProperty);
		return Context.getConceptService().getConceptsByConceptSet(c);
	}

	public Form getForm(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getForm(globalProperty);
	}

	public static EncounterType getEncounterType(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getEncounterType(globalProperty);
	}

	public List<EncounterType> getEncounterTypeList(String globalPropertyName, String separator) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getEncounterTypeList(globalProperty,separator);
	}

	public List<EncounterType> getEncounterTypeList(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getEncounterTypeList(globalProperty);
	}

	public List<Form> getFormList(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getFormList(globalProperty);
	}

	public List<Form> getFormList(String globalPropertyName, String separator) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getFormList(globalProperty,separator);
	}

	public RelationshipType getRelationshipType(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getRelationshipType(globalProperty);
	}

	public OrderType getOrderType(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getOrderType(globalProperty);
	}

	public ProgramWorkflow getProgramWorkflow(String globalPropertyName, String programName) {
		String programGp = Context.getAdministrationService().getGlobalProperty(programName);
		String workflowGp = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getProgramWorkflow(programGp, workflowGp);
	}

	public ProgramWorkflowState getProgramWorkflowState(String globalPropertyName, String workflowName, String programName) {
		String programGp = Context.getAdministrationService().getGlobalProperty(programName);
		String workflowGp = Context.getAdministrationService().getGlobalProperty(workflowName);
		String stateGp = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getProgramWorkflowState(programGp, workflowGp, stateGp);

	}

	public List<ProgramWorkflowState> getProgramWorkflowStateList(String globalPropertyName) {
		String programGp = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getProgramWorkflowstateList(programGp);

	}
	public Map<Concept, Double> getVialSizes() {
		Map<Concept, Double> vialSizes = new HashMap<Concept, Double>();
		String vialGp =  Context.getAdministrationService().getGlobalProperty("reports.vialSizes");
		String[] vials = vialGp.split(",");
		for(String vial: vials) {
			String[] v = vial.split(":");
			try {
				Concept drugConcept = MetadataLookup.getConcept(v[0]);
				Double size = Double.parseDouble(v[1]);
				vialSizes.put(drugConcept, size);
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Unable to convert " + vial + " into a vial size Concept and Double", e);
			}
		}
		return vialSizes;
	}

	public Integer getGlobalPropertyAsInt(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return Integer.parseInt(globalProperty);
	}


	public static final String LABEXAMSToORDER="laboratorymanagement.LabExamsToOrder";


	public static final String LABORATORYSERVICES="laboratorymanagement.appointmentInLaboratoryService";
	public static final String Consultationservice="laboratorymanagement.appointmentInConsultationService";


	public static final String LAB_TEST_ENCOUNTER_TYPE="laboratorymanagement.encounterType.labEncounterTypeId";





	public static final String spermConcept="laboratorymanagement.LabTestConstants.spermConcept";

	public static final String hematology="laboratorymanagement.LabTestConstants.hematology";

	public static final String PARASITOLOGY="laboratorymanagement.LabTestConstants.PARASITOLOGY";

	public static final String hemostasis="laboratorymanagement.LabTestConstants.hemostasis";

	public static final String bacteriology="laboratorymanagement.LabTestConstants.bacteriology";

	public static final String urineChemistry="laboratorymanagement.LabTestConstants.urineChemistry";

	public static final String immunoSerology="laboratorymanagement.LabTestConstants.immunoSerology";

	public static final String bloodChemistry="laboratorymanagement.LabTestConstants.bloodChemistry";

	public static final String toxicology="laboratorymanagement.LabTestConstants.toxicology";

	public static final String tumourMarker="laboratorymanagement.LabTestConstants.tumourMarker";

	public static final String thyroidFunction="laboratorymanagement.LabTestConstants.thyroidFunction";

	public static final String fertilityHormone="laboratorymanagement.LabTestConstants.fertilityHormone";





}
