package org.openmrs.module.laboratorymanagement.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.laboratorymanagement.advice.LaboratoryMgt;
import org.openmrs.module.laboratorymanagement.service.LaboratoryService;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class DisplayLabExamsController extends ParameterizableViewController {

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		SimpleDateFormat df = OpenmrsUtil.getDateFormat();
		LaboratoryService laboratoryService = Context
				.getService(LaboratoryService.class);

		String posLabConceptIdStr = request.getParameter("poslabConceptId");
		String neglabConceptIdStr = request.getParameter("neglabConceptId");
		String totlabConceptIdStr = request.getParameter("totlabConceptId");

		String startDateStr = request.getParameter("startDate");
		String endDateStr = request.getParameter("endDate");
		Date startDate = df.parse(startDateStr);
		Date endDate = df.parse(endDateStr);
		if (posLabConceptIdStr != null) {
			int labConceptId = Integer.parseInt(posLabConceptIdStr);

			List<Obs> positiveLabExams = laboratoryService
					.getAllPositiveLabExams(startDate, endDate, labConceptId);

			Map<String,Obs> positiveLabExamsMap=new HashMap<String, Obs>();
			for (Obs o:positiveLabExams) {
				if(o.getVoided()==false)
				positiveLabExamsMap.put(""+o.getObsId()+"_"+o.getEncounter().getPatient().getPatientIdentifier(3),o);
			}


			//model.put("positiveLabExams", positiveLabExams);
			model.put("positiveLabExams", positiveLabExamsMap);

		}
		if (neglabConceptIdStr != null) {
			int labConceptId = Integer.parseInt(neglabConceptIdStr);

			List<Obs> negativeLabExams = laboratoryService
					.getAllNegtiveLabExams(startDate, endDate, labConceptId);
			model.put("negativeLabExams", negativeLabExams);

		}
		if (totlabConceptIdStr != null) {
			int totlabConceptId = Integer.parseInt(totlabConceptIdStr);
			List<Obs> allLabExamsByName = LaboratoryMgt
					.getAllTestWithResult(laboratoryService
							.getLabExamsByExamTypeBetweenTwoDates(startDate,
									endDate, totlabConceptId));

			model.put("labExamsByName", allLabExamsByName);

		}
         model.put("startDate", startDateStr);
         model.put("endDate", endDateStr);
         
		return new ModelAndView(getViewName(), model);
	}
}
