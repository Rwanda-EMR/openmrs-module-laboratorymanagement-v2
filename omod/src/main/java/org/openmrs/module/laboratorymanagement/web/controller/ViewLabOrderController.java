package org.openmrs.module.laboratorymanagement.web.controller;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Order;

import org.openmrs.api.context.Context;

import org.openmrs.module.laboratorymanagement.advice.LaboratoryMgt;
import org.openmrs.module.laboratorymanagement.service.LaboratoryService;

import org.openmrs.module.laboratorymanagement.utils.GlobalPropertiesMgt;
import org.openmrs.module.laboratorymanagement.utils.LabUtils;
import org.openmrs.module.mohappointment.model.Appointment;
import org.openmrs.module.mohappointment.model.Services;
import org.openmrs.module.mohappointment.utils.AppointmentUtil;
import org.openmrs.web.WebConstants;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class ViewLabOrderController extends ParameterizableViewController {
	protected final Log log = LogFactory.getLog(getClass());
	@RequestMapping(value = "module/laboratorymanagement/viewLabOrder.form", method = RequestMethod.POST)
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {		
		Date lastMidnight = LaboratoryMgt.getPreviousMidnight(null);
		Date zeroDay = LaboratoryMgt.addDaysToDate(lastMidnight, 0);
		
		String patientIdStr = request.getParameter("patientId");
		Date startDate = LaboratoryMgt.getDateParameter(request, "startDate", lastMidnight);
		
		
		Date endDate = LaboratoryMgt.getDateParameter(request, "endDate",zeroDay);
		// String locationIdStr=request.getParameter("locationId");	

		LaboratoryService laboratoryService = Context.getService(LaboratoryService.class);
		Map<Concept, Collection<Order>> mappedLabOrders = null;

		Map<String, Object> model = new HashMap<String, Object>();
		if (patientIdStr != null && patientIdStr.length() > 0) {
			int patientId = Integer.parseInt(request.getParameter("patientId"));
			if (request.getParameter("save") != null) {
				String labCodeStr = request.getParameter("labCode");
				//if the labCode  does not exist ,add lab code to Lab  oders  
				if (!laboratoryService.isFoundLabCode(labCodeStr)) {
					String labCode = request.getParameter("labCode");
					LabUtils.addLabCodeToOrders(patientId, labCode, startDate,endDate);
					model.put("msg", "Lab code is successfully added");
					
					/**
					 *  ___________ >> Lab Appointment ends here:
					 *  Found using: Patient, Lab Service, Appointment Date ,and AppointmentState
					 */
					//Services clinicalService = AppointmentUtil.getServiceByConcept(GlobalPropertiesMgt.getConcept(GlobalPropertiesMgt.LABORATORYSERVICES));

					Services clinicalService = AppointmentUtil.getServiceByConcept(Context.getConceptService().getConcept(Context.getAdministrationService().getGlobalProperty(GlobalPropertiesMgt.LABORATORYSERVICES)));
					List<Appointment> appointments = AppointmentUtil.getAppointmentsByPatientAndDate(
							Context.getPatientService().getPatient(patientId), clinicalService, new Date());
					
					for(Appointment app: appointments){
						if(app.getAppointmentState().getDescription().equalsIgnoreCase("waiting"))
							AppointmentUtil.saveAttendedAppointment(app);
					}
					/**
					 * _______________________END of APPOINTMENT Stuff_______________________
					 */
					
				} else
					request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
							"The Lab code " + labCodeStr+ "  already exists");
					//model.put("msg", "The Lab code " + labCodeStr+ "  already exists");
				model.put("labCode", labCodeStr);
			}
			if(request.getParameter("edit") != null){
				String labCode =request.getParameter("labCode");
				int orderId = ServletRequestUtils.getIntParameter(request, "orderId");
				Order labOrder =Context.getOrderService().getOrder(orderId);
				laboratoryService.addLabCodeToOrders(labOrder, labCode);
				labOrder.setAccessionNumber(labCode); //Jut for the UI
			}


			long startTime = System.currentTimeMillis();
			long endTime = 0;

			mappedLabOrders = LabUtils.findPatientLabOrders(patientId,	startDate, endDate, null);


			endTime = System.currentTimeMillis();
			long timeneeded =  ((endTime-startTime) /1000);

			System.out.println("Cotroller Timeeeeeeeeeeee: "+timeneeded);

			model.put("patient", Context.getPatientService().getPatient(
					patientId));
			
		}
		model.put("mappedLabOrders", mappedLabOrders);
		model.put("startDate", startDate);
		model.put("startDat", request.getParameter("startDate"));
		model.put("enddate", endDate);
		return new ModelAndView(getViewName(), model);
	}
}
