package org.openmrs.module.laboratorymanagement.web.controller;

import org.openmrs.*;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.laboratorymanagement.LabOrderParent;
import org.openmrs.module.laboratorymanagement.advice.LabTestConstants;
import org.openmrs.module.laboratorymanagement.utils.GlobalPropertiesMgt;
import org.openmrs.module.laboratorymanagement.web.LabUtils;
import org.openmrs.web.controller.PortletController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
@Controller
@RequestMapping("**/labOrderSetupPortlet.portlet")
public class LabOrderSetupPortletController extends PortletController {

	@SuppressWarnings("unchecked")
	protected void populateModel(HttpServletRequest request,
			Map<String, Object> model) {

		/*long t1= System.currentTimeMillis();
		System.out.println("Starttttttttttttttttttttttttttttttt");*/
		//categories of lab tests on lab request form
		int hematologyId =LabTestConstants.hematologyId;
		int parasitologyId =LabTestConstants.PARASITOLOGYID;
		int hemostasisId = LabTestConstants.hemostasisId;
		int bacteriologyId = LabTestConstants.bacteriologyId;
		int spermConceptId =LabTestConstants.spermConceptId;
		int urinaryChemistryId= LabTestConstants.urineChemistryId ;
		int immunoSerologyId = LabTestConstants.immunoSerologyId ;
		int bloodChemistryId = LabTestConstants.bloodChemistryId ;
		int toxicologyId = LabTestConstants.toxicologyId ;
		int tumourMarkerId = LabTestConstants.tumourMarkerId;
		int thyroidFunctionId = LabTestConstants.thyroidFunctionId;
		int fertilityHormoneId = LabTestConstants.fertilityHormoneId;





		List<Concept> conceptCategories = GlobalPropertiesMgt.getLabExamCategories();
		List<LabOrderParent> lopList = LabUtils.getsLabOrdersByCategories(conceptCategories);

		// get all selected Lab tests and save them as Order
		Map<String, String[]> parameterMap = request.getParameterMap();

	/*	String patientIdstr = request.getParameter("patientId");
		Patient patient = Context.getPatientService().getPatient(Integer.parseInt(patientIdstr));
*/

		/**
		 * <<<<<<< Appointment Consultation waiting list management >>>>>>>
		 */
		/*Appointment appointment = null;
		// This is from the Provider's Appointment dashboard
		if (request.getParameter("appointmentId") != null) {
			appointment = AppointmentUtil.getWaitingAppointmentById(Integer.parseInt(request
							.getParameter("appointmentId")));
			LabUtils.setConsultationAppointmentAsAttended(appointment);
		}
		
		if(request.getParameter("orderId")!=null){			
			
			LabUtils.cancelLabOrder(request.getParameter("orderId"));
		}
		*/
		/**
		 * <<<<<<<<< APPOINTMENTS STUFF ENDS HERE >>>>>>>>
		 */

		if(request.getParameter("saveLabOrders")!=null){
			// Saving selected lab orders:	

			//LabUtils.saveSelectedLabOrders(parameterMap, patient);
			model.put("msg", "The Lab request form is successfully created");


			//LabUtils.createWaitingLabAppointment(patient, null);
		}


		// request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR,
		// "Orders created");

		Location dftLoc = Context.getLocationService().getDefaultLocation();

		User user = Context.getAuthenticatedUser();
		String providerName = user.getFamilyName() + " " + user.getGivenName();
		/*String patientName = patient.getFamilyName() + " "
				+ patient.getMiddleName() + " " + patient.getGivenName();
*/



		// String orderTypeIdStr =
		// Context.getAdministrationService().getGlobalProperty("orderType.labOrderTypeId");

		Map<ConceptName, Collection<Concept>> mappedLabOrder = new HashMap<ConceptName, Collection<Concept>>();

/*

		mappedLabOrder = LabUtils.getLabExamsToOrder(Integer
				.parseInt(patientIdstr));
*/




		OrderService orderService = Context.getOrderService();



		// get observations by Person

		/*Map<Date, List<OrderObs>> orderObsMap = LabUtils.getMappedOrderToObs(orders, patient);
		List<Order> orders = orderService.getOrdersByPatient(patient);
		*///Map<String, Object> orderObsMapOrdered = new TreeMap<String, Object>((Comparator<? super String>) orderObsMap);
		
		//int spermConceptId = LabTestConstants.SPERMCONCEPTID;
/*
		long t2= System.currentTimeMillis();
		long t= t2-t1;
		System.out.println("Enddddddddddddddddddddddddddddddddddddd1: "+t);

		System.out.println("Starttttttttttttttttttttttttttttttt");*/

		String stringLabOrderToDisplay=Context.getAdministrationService().getGlobalProperty("laboratorymanagement.currentLabRequestFormConceptIDs");

		String[] stringLabOrderToDisplayList=null;
		Set<Integer> labOrderToDisplay=new HashSet<Integer>();

		if(stringLabOrderToDisplay!=null && stringLabOrderToDisplay.trim().length()!=0) {
			stringLabOrderToDisplayList = stringLabOrderToDisplay.split(",");
			for (String strO : stringLabOrderToDisplayList) {
				labOrderToDisplay.add(Integer.parseInt(strO));
			}
		}


		model.put("dftLoc", dftLoc);
		// model.put("locations", locations);
		//model.put("patientId", patientIdstr);
		model.put("mappedLabOrder", mappedLabOrder);
		//model.put("obsMap", orderObsMap);
		model.put("providerName", providerName);
		//model.put("patienName", patientName);
		model.put("labOrderparList", lopList);
		model.put("labOrderCurrentlyDisplayed", labOrderToDisplay);
		model.put("hematology",Context.getConceptService().getConcept(hematologyId).getName().getName() );
		model.put("parasitology",Context.getConceptService().getConcept(parasitologyId).getName());
		model.put("hemostasis",Context.getConceptService().getConcept(hemostasisId).getName());
		model.put("bacteriology",Context.getConceptService().getConcept(bacteriologyId).getName());
		model.put("spermogram", Context.getConceptService().getConcept(spermConceptId).getName());
		model.put("urinaryChemistry", Context.getConceptService().getConcept(urinaryChemistryId).getName());
		model.put("immunoSerology", Context.getConceptService().getConcept(immunoSerologyId).getName());
		model.put("bloodChemistry", Context.getConceptService().getConcept(bloodChemistryId).getName());
		model.put("toxicology", Context.getConceptService().getConcept(toxicologyId).getName());
		model.put("tumourMarker",Context.getConceptService().getConcept(tumourMarkerId).getName());
		model.put("thyroidFunction",Context.getConceptService().getConcept(thyroidFunctionId).getName());
		model.put("fertilityHormone",Context.getConceptService().getConcept(fertilityHormoneId).getName());


		/*long t3= System.currentTimeMillis();
		long t4= t3-t1;
		System.out.println("Enddddddddddddddddddddddddddddddddddddd2: "+t4);*/



		super.populateModel(request, model);

	}

}
