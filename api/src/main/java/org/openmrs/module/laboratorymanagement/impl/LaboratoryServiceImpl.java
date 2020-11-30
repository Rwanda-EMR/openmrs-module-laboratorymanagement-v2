package org.openmrs.module.laboratorymanagement.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.api.context.Context;
import org.openmrs.module.laboratorymanagement.db.LaboratoryDAO;
import org.openmrs.module.laboratorymanagement.service.LaboratoryService;


public class LaboratoryServiceImpl implements LaboratoryService {
	protected final Log log = LogFactory.getLog(getClass());

	private LaboratoryDAO laboratoryDAO;

	public LaboratoryDAO getLaboratoryDAO() {
		return laboratoryDAO;
	}

	public void setLaboratoryDAO(LaboratoryDAO laboratoryDAO) {
		this.laboratoryDAO = laboratoryDAO;
	}

	public void saveOrderEncounter(Encounter encounter) {

		laboratoryDAO.saveOrderEncounter(encounter);
		// TODO Auto-generated method stub

	}

	public List<Obs> getAllpatientObs(int patientId) {
		// TODO Auto-generated method stub
		return laboratoryDAO.getAllpatientObs(patientId);
	}

	public List<Obs> getAllObsByLocation(int locationId) {
		// TODO Auto-generated method stub
		return laboratoryDAO.getAllObsByLocation(locationId);
	}

	public void getSampleCodeByPatient(int testobsId, int newObsId) {
		laboratoryDAO.getSampleCodeByPatient(testobsId, newObsId);
	}
	
	public void addLabCodeToOrders(Order order, String labCode) {
		laboratoryDAO.addLabCodeToOrders(order, labCode);
	}

	public List<EncounterType> getAllEncounterType() {
		// TODO Auto-generated method stub
		return laboratoryDAO.getAllEncounterType();
	}

	public List<Obs> getPatientTestBeforeDate(Date startDate,Date endDate) {
		return laboratoryDAO.getPatientTestBeforeDate(startDate,endDate);
	}

	public List<Obs> getTestOfPatientBetweenTwoDate(int patientId,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return laboratoryDAO.getTestOfPatientBetweenTwoDate(patientId, startDate, endDate);
	}

	public List<Obs> getTestsBetweenTwoDateByLocation(int locationId,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return laboratoryDAO.getTestsBetweenTwoDateByLocation(locationId,startDate,endDate);
	}

	public List<Obs> getAllPatientLabtestByLocation(int patientId,
			int locationId, int conceptId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return laboratoryDAO.getAllPatientLabtestByLocation(patientId, locationId, conceptId, startDate, endDate);
	}

	public List<Obs> getPatientLabtestBetweenTwoDate(int patientId,
			int conceptId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return laboratoryDAO.getPatientLabtestBetweenTwoDate( patientId,
			 conceptId,  startDate,  endDate);
	}

	public List<Obs> getPatientLabtestByLocation(int patientId, int locationId,
			int conceptId) {
		// TODO Auto-generated method stub
		return laboratoryDAO. getPatientLabtestByLocation(patientId,locationId,	conceptId);
	}

	public List<Obs> getAllLabTest(Integer labConceptId) {
		// TODO Auto-generated method stub
		return laboratoryDAO.getAllLabTest(labConceptId);
	}

	public List<Obs> getLabExamsByExamType(int patientId,
			Collection<Integer> cptIds,Date startDate,Date endDate) {
		// TODO Auto-generated method stub
		return laboratoryDAO.getLabExamsByExamType(patientId,cptIds,startDate,endDate);
	}

	public List<Obs> getLabExamsByExamTypeBetweenTwoDates(Date startDate,Date endDate,Integer conceptId) {
		// TODO Auto-generated method stub
		return  laboratoryDAO.getLabExamsByExamTypeBetweenTwoDates(startDate,endDate,conceptId);
	}

	@Override
	public List<Obs> getAllNegtiveLabExams(Date startDate, Date endDate,
			int conceptId) {
		// TODO Auto-generated method stub
		return laboratoryDAO.getAllNegtiveLabExams(startDate, endDate,
				 conceptId);
	}

	@Override
	public List<Obs> getAllPositiveLabExams(Date startDate, Date endDate,
			int conceptId) {
		// TODO Auto-generated method stub
		return  laboratoryDAO. getAllPositiveLabExams(startDate, endDate,
				conceptId);
	}

	@Override
	public List<Order> getLabOrders(int patientId, Collection<Integer> cptIds,
			Date startDate, Date enddate) {
//		// TODO Auto-generated method stub
		return laboratoryDAO.getLabOrders(patientId,  cptIds,
				startDate, enddate);
	}

	@Override
	public List<Order> getLabOrdersBetweentwoDate(int patientId,
			Date startDate, Date enddate) {
		// TODO Auto-generated method stub
		return laboratoryDAO.getLabOrdersBetweentwoDate(patientId,	startDate, enddate);
	}
	public List<Order> getLabOrdersBetweentwoDate(Date startDate, Date enddate){
		// TODO Auto-generated method stub
		return laboratoryDAO.getLabOrdersBetweentwoDate(startDate, enddate);
	}
	public List<Order> getLabOrdersByLabCode(String  labCode){
		return laboratoryDAO.getLabOrdersByLabCode(labCode);
	}

	@Override
	public List<Obs> getObsByLabOrder(int orderId) {
		Order order = Context.getOrderService().getOrder(orderId);
		return laboratoryDAO.getObsByLabOrder(order, null);	
	}

	@Override
	public boolean isFoundLabCode(String labCode) {
		return laboratoryDAO.isFoundLabCode(labCode);
	}

	@Override
	public Collection<Order> getPatientLabordersBetweendates(int patientId, Date startDate, Date endDate) {
		return laboratoryDAO. getPatientLabordersBetweendates(patientId,startDate,endDate);
	}

	@Override
	public Collection<Obs> getObsByLabCode(String labCode) {
		// TODO Auto-generated method stub
		return laboratoryDAO.getObsByLabCode(labCode);
	}
	
	@Override
	public List<Obs> getExistingOrderObs(Order order, Concept cpt){
		return laboratoryDAO.getObsByLabOrder(order, cpt);
	}
	
	@Override
	public void deleteExistingOrderObs(Order order, Concept cpt){
		List<Obs> obss = laboratoryDAO.getObsByLabOrder(order, cpt);
		log.info("List of all existing obs for the order "+order+"and concept"+cpt+" are"+obss.size());
		
		for (Obs obs : obss) {			
			log.info("Purging existing obs" + obs.getObsId()+" has lab order"+obs.getOrder().getOrderId()+"and concept "+obs.getConcept());
			//Context.getObsService().purgeObs(obs);
			Context.getObsService().voidObs(obs, "updated obs");
			
		}
	}

	@Override
	public Collection<Encounter> getPatientEncountersByDate(int patientId, Date startDate,EncounterType encounterType) {
		
		return laboratoryDAO.getPatientEncountersByDate(patientId,startDate,encounterType);
	}
	

}
