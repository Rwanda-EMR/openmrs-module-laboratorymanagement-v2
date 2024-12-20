package org.openmrs.module.laboratorymanagement.advice;

import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.ConceptSet;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.laboratorymanagement.service.LaboratoryService;
import org.openmrs.module.laboratorymanagement.utils.GlobalPropertiesMgt;
import org.openmrs.module.laboratorymanagement.utils.LabUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappedLabExamManagement {
	/**
	 * Group the the lab tests according to the group they belong to get
	 * Map<X,y> where X stands for the conceptName(the groupConcept)and Y list
	 * of object for a patient but between 2 dates *
	 * 
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @return Map<ConceptName, List<Object[]>>
	 */
	public static Map<ConceptName, List<Object[]>> getMappedExamsByLabType(	int patientId, Date startDate, Date endDate) {
		ConceptService cptService = Context.getConceptService();		
		Map<ConceptName, List<Object[]>> mappedLabExam = new HashMap<ConceptName, List<Object[]>>();
		
		LaboratoryService laboratoryService = Context.getService(LaboratoryService.class);
		// Initilializes an integer array where by each element is a group
		// concepts of Lab tests
		//int intLabSetIds[]={7836, 7217, 7192, 7243, 7244, 7265, 7222,7193,7918, 7835,8046};
		List<Obs> obsWithValues = null;
		@SuppressWarnings("unused")
		Object testStatus[] = null;
		// Run through each group and get members that are Lab tests


		List<Integer> intLabSetIds=new ArrayList<Integer>();
		String conceptLabSetToOrderString=Context.getAdministrationService().getGlobalProperty(GlobalPropertiesMgt.LABEXAMSToORDER);
		for (String s:conceptLabSetToOrderString.split(",")){
			//conceptLabSetToOrder.add(Context.getConceptService().getConcept(Integer.parseInt(s)));
			intLabSetIds.add(Integer.parseInt(s));
		}




		for (int labSetid : intLabSetIds){
			//
			List<Object[]> labExamHistory = new ArrayList<Object[]>();
			Concept groupConcept = cptService.getConcept(labSetid);
			Collection<ConceptSet> setMembers = groupConcept.getConceptSets();

			Collection<Integer>cptsLst = new ArrayList<Integer>();
			for (ConceptSet setMember : setMembers) {
				// if the datatype of member is text or numeric or coded add it to cptList
				if(setMember.getConcept().isSet()){
					for (ConceptSet childCpt : setMember.getConcept().getConceptSets()) {
						cptsLst.add(childCpt.getConcept().getConceptId());
					}
				}
				else{
					cptsLst.add(setMember.getConcept().getConceptId());
				}			
				
				
			}
			// run through all patient Lab observations and take only the Lab 
			// obs whose values either coded,numeric or text is not null
			obsWithValues = LaboratoryMgt.getAllTestWithResult(laboratoryService.getLabExamsByExamType(patientId, cptsLst,startDate, endDate));
           
			for (Obs oneLabObs : obsWithValues) {
				// at index 0,put the obs as one Lab obs and then at index 1 the   normal range	
				if (oneLabObs != null && oneLabObs.getOrder()!=null) {
					testStatus = new Object[]{oneLabObs, LabUtils.getNormalRanges(oneLabObs.getConcept()) };
					labExamHistory.add(testStatus);
				}
			}

			if (labExamHistory.size() > 0) {
				// map the group concept name to Lab exam history
				mappedLabExam.put(groupConcept.getName(), labExamHistory);
			}

		}		

		/*List<Object[]> labObsHistory = getLabObsWithResults(patientId,parasitConceptId, startDate, endDate);
		if (labObsHistory.size()> 0) {
			mappedLabExam.put(parasitConcept.getName(), labObsHistory);
		}*/
		
		
		return mappedLabExam;

	}

	public static Map<ConceptName, List<Object[]>> getMappedExamsByLabTypeBetweenTwoDates(
			HttpServletRequest request, Date startDate, Date endDate) {
		ConceptService cptService = Context.getConceptService();
		Map<ConceptName, List<Object[]>> mappedLabExam = new HashMap<ConceptName, List<Object[]>>();
		List<Obs> positiveLabExams = null;
		List<Obs> negativelabExams = null;

		LaboratoryService laboratoryService = Context
				.getService(LaboratoryService.class);

		// Initilializes an integer array of length 2 where the first element is
		// 7005, second element is 7006 and so on.
		//int intLabSetIds[] = { 7836, 7217, 7192, 7243, 7244, 7265, 7835, 7222 };
		List<Obs> testWithResult = null;
		@SuppressWarnings("unused")
		Object testStatus[] = null;


		List<Integer> intLabSetIds=new ArrayList<Integer>();
		String conceptLabSetToOrderString=Context.getAdministrationService().getGlobalProperty(GlobalPropertiesMgt.LABEXAMSToORDER);
		for (String s:conceptLabSetToOrderString.split(",")){
			//conceptLabSetToOrder.add(Context.getConceptService().getConcept(Integer.parseInt(s)));
			intLabSetIds.add(Integer.parseInt(s));
		}



		for (int labSetid : intLabSetIds) {
			//
			List<Object[]> labExamHistory = new ArrayList<Object[]>();
			Concept cpt = cptService.getConcept(labSetid);
			Collection<ConceptSet> setMembers = cpt.getConceptSets();
			Collection<Integer> cptsLst = new ArrayList<Integer>();
			for (ConceptSet setMember : setMembers) {
				cptsLst.add(setMember.getConcept().getConceptId());
			}

			// testWithResult =
			// laboratoryService.getLabExamsByExamTypeBetweenTwoDates(startDate,endDate,
			// cptsLst);

			int i = 0;
			for (Integer oneConceptId : cptsLst) {
				System.out.println(">>>>>>>>lab concept ids" + labSetid + ":"
						+ oneConceptId);

				testWithResult = LaboratoryMgt
						.getAllTestWithResult(laboratoryService
								.getLabExamsByExamTypeBetweenTwoDates(
										startDate, endDate, oneConceptId));
				if (Context.getConceptService().getConcept(oneConceptId)
						.getDatatype().isCoded()) {

					negativelabExams = laboratoryService.getAllNegtiveLabExams(
							startDate, endDate, oneConceptId);
					positiveLabExams = laboratoryService
							.getAllPositiveLabExams(startDate, endDate,
									oneConceptId);

					testStatus = new Object[] {
							cptService.getConcept(oneConceptId),
							positiveLabExams.size(), negativelabExams.size(),
							testWithResult.size(), oneConceptId };
					labExamHistory.add(testStatus);
				}
				if (Context.getConceptService().getConcept(oneConceptId)
						.getDatatype().isNumeric()) {
					testStatus = new Object[] {
							cptService.getConcept(oneConceptId).getName(),
							" - ", " - ", testWithResult.size(), oneConceptId };
					labExamHistory.add(testStatus);

				}

				if (testStatus != null) {
					mappedLabExam.put(cpt.getName(), labExamHistory);

				}

			}

		}

		return mappedLabExam;

	}

	public static Map<ConceptName, Collection<Concept>> getMappedLabOrder(
			int patientId) {

		ConceptService cptService = Context.getConceptService();

		Map<ConceptName, Collection<Concept>> mappedLabOrder = new HashMap<ConceptName, Collection<Concept>>();

		// Initilializes an integer array of length 2 where the first element is
		// 7005, second element is 7006 and so on.
	//	int intLabSetIds[] = { 7836, 7217, 7192, 7243, 7244, 7265, 7222, 7193,8046, 7991, 7193 };
		@SuppressWarnings("unused")
		Object testStatus[] = null;


		List<Integer> intLabSetIds=new ArrayList<Integer>();
		String conceptLabSetToOrderString=Context.getAdministrationService().getGlobalProperty(GlobalPropertiesMgt.LABEXAMSToORDER);
		for (String s:conceptLabSetToOrderString.split(",")){
			//conceptLabSetToOrder.add(Context.getConceptService().getConcept(Integer.parseInt(s)));
			intLabSetIds.add(Integer.parseInt(s));
		}

		for (int labSetid : intLabSetIds) { //
			Concept cpt = cptService.getConcept(labSetid);
			Collection<ConceptSet> setMembers = cpt.getConceptSets();
			Collection<Concept> cptsLst = new ArrayList<Concept>();
			for (ConceptSet setMember : setMembers) {
				cptsLst.add(setMember.getConcept());
			}
			mappedLabOrder.put(cpt.getName(), cptsLst);

		}
		return mappedLabOrder;
	}

	/**
	 * get Lab orders by category Auto generated method comment
	 * 
	 * @param patientId
	 * @param startDate
	 * @param enddate
	 * @return
	 */

	public static Map<Concept, Collection<Order>> getMappedLabOrder(int patientId,Date startDate, Date enddate) {

		LaboratoryService laboratoryService = Context.getService(LaboratoryService.class);
		Map<Concept, Collection<Order>> mappedLabOrders = new HashMap<Concept, Collection<Order>>();
		ConceptService cptService = Context.getConceptService();
		//to do
		//Collection of all patient lab orders orderded betwetwo dates	
		Collection<Order>labOrders=laboratoryService.getPatientLabordersBetweendates(patientId, startDate, enddate);
       // Collection<Order>labOrderslist=laboratoryService.getPatientLabordersBetweendates(patientId, startDate, enddate);
        List<Order>labOrderslist=null;
		//int intLabSetIds[] = {7836,7217, 7192, 7243, 7244, 7265, 7222, 7193 };


		List<Integer> intLabSetIds=new ArrayList<Integer>();
		String conceptLabSetToOrderString=Context.getAdministrationService().getGlobalProperty(GlobalPropertiesMgt.LABEXAMSToORDER);
		for (String s:conceptLabSetToOrderString.split(",")){
			//conceptLabSetToOrder.add(Context.getConceptService().getConcept(Integer.parseInt(s)));
			intLabSetIds.add(Integer.parseInt(s));
		}


		for (int labSetid : intLabSetIds) { 
			labOrderslist=new ArrayList<Order>();
			Concept parentConcept = cptService.getConcept(labSetid);
			//check if  the conceptId from patientLaborders already exists in cptLst
			//collection of childConceptIds whose parent is parentConcept
			Collection<Integer>childConceptIds=getListOfChildConceptIds(parentConcept);
			/*List<Order> labOrderslist = laboratoryService.getLabOrders(
					patientId, childConceptIds, startDate, enddate);*/			
			for (Order laborder : labOrders) {
				if (childConceptIds.contains(laborder.getConcept().getConceptId())) {
					labOrderslist.add(laborder);
					
				}
				
			}
			
			
			
			if (labOrderslist.size() > 0) {
				mappedLabOrders.put(parentConcept, labOrderslist);

			}

		}
		// System.out.println(">>>>>>>>mapped Lab odrer" + mappedLabOrders);
		

		return mappedLabOrders;
	}

	/*get all child of conceptparent
	 * param Collection<ConceptSet>setMember
	 * return the Collect<Integer>childConce;ptid
	 */
	
	public  static Collection<Integer> getListOfChildConceptIds(Concept cpt ){
		

		Collection<ConceptSet> setMembers = cpt.getConceptSets();			
		//Collection<Integer> cptsLst =getListOfChildConceptIds(labSetid);
		Collection<Integer> cptsLst = new ArrayList<Integer>();
		for (ConceptSet setMember : setMembers) {			
			Concept childConcept=setMember.getConcept();
			
			if (childConcept.isSet()){					
				Collection<ConceptSet> setMemebrChildren = childConcept.getConceptSets();					
				for (ConceptSet mbrCpt : setMemebrChildren) {						
					cptsLst.add(mbrCpt.getConcept().getConceptId());
				}			
			}

			cptsLst.add(setMember.getConcept().getConceptId());
			//the set member is also a set of Lab tests go through each memeber			

		}


		return cptsLst;
		
	}
}
