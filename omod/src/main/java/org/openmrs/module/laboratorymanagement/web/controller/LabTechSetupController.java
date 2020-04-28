package org.openmrs.module.laboratorymanagement.web.controller;

import org.openmrs.GlobalProperty;
import org.openmrs.Order;
import org.openmrs.api.context.Context;
import org.openmrs.module.laboratorymanagement.utils.LabUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LabTechSetupController extends ParameterizableViewController {
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		StringBuilder newLabExams= new StringBuilder();

		Map<String, String[]> parameterMap = request.getParameterMap();


int i=0;
		for (String parameterName : parameterMap.keySet()) {
			System.out.println("Parameterrrrrrrrrrr Before:"+parameterName);

		if (!(parameterName.startsWith("lab-") ||  isNumeric(parameterName))) {
				continue;
			}
			String[] parameterValues = parameterMap.get(parameterName);

			String SingleLabConceptIdstr = parameterValues[0];
			System.out.println("Parameterrrrrrrrrrr After:"+parameterName);
			System.out.println("Concept Nameeeeee:"+Context.getConceptService().getConcept(
					Integer.parseInt(SingleLabConceptIdstr)).getName().getName());

			if(i==0) {
				newLabExams.append(SingleLabConceptIdstr);
			}else{
				newLabExams.append(",");
				newLabExams.append(SingleLabConceptIdstr);
			}
			i++;

		}
		//String availableLabExams= Context.getAdministrationService().getGlobalProperty("laboratorymanagement.currentLabRequestFormConceptIDs");

		if(request.getParameter("saveLabOrders")!=null){

			GlobalProperty globalProperty=new GlobalProperty();
			globalProperty.setProperty("laboratorymanagement.currentLabRequestFormConceptIDs");
			globalProperty.setPropertyValue(newLabExams.toString());
			Context.getAdministrationService().saveGlobalProperty(globalProperty);

		}


		/*int  patientId=Integer.parseInt(request.getParameter("patientId"));
        String startDate = request.getParameter("startDate");
        String labCode =request.getParameter("labcode");
       
        
		
        //model.put("endDate", endDate);
	    model.put("startDate",startDate);
		model.put("patientId",patientId);
		model.put("labCode",labCode);
		
		
		
		model.put("appointmentId", request.getAttribute("appointmentId"));*/
		return new ModelAndView(getViewName(), model);
		


	}
	public static boolean isNumeric(String str)
	{
		for (char c : str.toCharArray())
		{
			if (!Character.isDigit(c)) return false;
		}
		return true;
	}

}