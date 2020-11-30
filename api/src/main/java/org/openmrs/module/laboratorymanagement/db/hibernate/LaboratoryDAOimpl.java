/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.laboratorymanagement.db.hibernate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.SQLGrammarException;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.api.APIException;
import org.openmrs.api.EncounterService;
import org.openmrs.api.ObsService;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.laboratorymanagement.db.LaboratoryDAO;

/**
 *
 */
public class LaboratoryDAOimpl implements LaboratoryDAO {

	protected final Log log = LogFactory.getLog(getClass());

	private SessionFactory sessionFactory;

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void saveOrderEncounter(Encounter encounter) {
		EncounterService encounterService = Context.getEncounterService();
		encounterService.saveEncounter(encounter);

	}

	public List<Encounter> getAllEncountersByProvider(int providerId) {
		// TODO Auto-generated method stub

		return null;
	}

	/**
	 * gives all observations by location from the database
	 * 
	 * @return list of all observation
	 */

	public List<Obs> getAllObsByLocation(int locationId) {
		List<Obs> patientObservations = new ArrayList<Obs>();

		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session
				.createSQLQuery("SELECT  o.obs_id  FROM obs o where o.location_id ="
						+ locationId);

		List<Integer> testObsIds = query.list();
		for (Integer oneTestObsId : testObsIds) {
			patientObservations.add(Context.getObsService()
					.getObs(oneTestObsId));

		}

		return patientObservations;

	}

	public void getSampleCodeByPatient(int testObsId, int newobsId) {

		String sb = new String();
		sb = "update trac_sample_test  set  test_obs_id=" + newobsId
				+ " where test_obs_id=" + testObsId;
		Session session = sessionFactory.getCurrentSession();

		try {
			session.beginTransaction();
			@SuppressWarnings("unused")
			int query = session.createSQLQuery(sb).executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			log.error(e);
		}

	}
	
	public void addLabCodeToOrders(Order order, String labCode) {
		String sb = new String();
		sb = "update orders  set  accession_number=" + labCode
				+ " where order_id=" + order.getOrderId();
		int updated = sessionFactory.getCurrentSession()
				.createSQLQuery(sb)
				.executeUpdate();
		if (updated != 1) {
			throw new APIException("Error setting accession numer on order");
		}
	}

	public List<EncounterType> getAllEncounterType() {
		List<EncounterType> encounterTypeList = new ArrayList<EncounterType>();
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session
				.createSQLQuery("SELECT encounter_type_id FROM encounter_type e");
		List<Integer> encounterTypeIds = query.list();
		for (Integer encounterTypeId : encounterTypeIds) {
			encounterTypeList.add(Context.getEncounterService()
					.getEncounterType(encounterTypeId));

		}

		return encounterTypeList;
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public List<Obs> getPatientTestBeforeDate(Date startDate, Date endDate) {
		// Date testEndDate=LaboratoryMgt.getDateParameter(endDate);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		List<Obs> patientObservations = new ArrayList<Obs>();
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session
				.createSQLQuery("SELECT  o.obs_id  FROM obs o where  o.obs_datetime  between  "

						+ "'"
						+ df.format(startDate)
						+ "'and "
						+ "'"
						+ df.format(endDate) + "'");

		List<Integer> testObsIds = query.list();

		for (Integer testObsId : testObsIds) {
			if (testObsId != null) {
				// System.out.println(">>>>>test obsId" + testObsId);
				patientObservations.add(Context.getObsService().getObs(
						testObsId));

			}

		}

		return patientObservations;
	}

	@SuppressWarnings("unchecked")
	public List<Obs> getTestOfPatientBetweenTwoDate(int patientId,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Obs> patientObservations = new ArrayList<Obs>();
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session
				.createSQLQuery("SELECT obs_id FROM obs o where cast(o.obs_datetime as date) "
						+ "between "
						+ "'"
						+ df.format(startDate)
						+ "'"
						+ " and "
						+ "'"
						+ df.format(endDate)
						+ "'"
						+ " and person_id= " + patientId);

		/*
		 * 
		 * .createSQLQuery("SELECT obs_id FROM obs o  " +
		 * "where o.obs_datetime  between  " + "'" + df.format(startDate) +
		 * "'and " + "'" + df.format(endDate) + "' and o.person_id =" +
		 * patientId );
		 */System.out.println(">>>>>>>>>tests between two dates patient query"
				+ query.toString());

		List<Integer> testObsIds = query.list();

		for (Integer testObsId : testObsIds) {
			if (testObsId != null) {
				// System.out.println(">>>>>test obsId" + testObsId);
				patientObservations.add(Context.getObsService().getObs(
						testObsId));

			}

		}

		return patientObservations;
	}

	public List<Obs> getTestsBetweenTwoDateByLocation(int locationId,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Obs> patientObservations = new ArrayList<Obs>();
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session
				.createSQLQuery("SELECT  distinct ob.obs_id FROM obs ob "
						+ "where  ob.location_id ="
						+ locationId
						+ " and  ob.obs_datetime  between  "
						+ "'"
						+ df.format(startDate)
						+ "'and "
						+ "'"
						+ df.format(endDate)
						+ "' and  ob.obs_id  in (select test_obs_id  from  trac_sample_test) ");

		List<Integer> testObsIds = query.list();

		for (Integer testObsId : testObsIds) {
			if (testObsId != null) {
				// System.out.println(">>>>>test obsId" + testObsId);
				patientObservations.add(Context.getObsService().getObs(
						testObsId));

			}

		}

		return patientObservations;
	}

	public List<Obs> getAllPatientLabtestByLocation(int patientId,
			int locationId, int conceptId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Obs> patientObservations = new ArrayList<Obs>();
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session
				.createSQLQuery("select o.obs_id from obs o where "
						+ "  o.person_id=" + patientId + " and o.location_id="
						+ locationId + " and o.concept_id=" + conceptId + " "
						+ " and o.obs_datetime between '"
						+ df.format(startDate) + "' and '" + df.format(endDate)
						+ "'");
		log.info("ZZZZlab test by locato and patioend between two dates"
				+ query.toString());
		List<Integer> testObsIds = query.list();

		for (Integer testObsId : testObsIds) {
			if (testObsId != null) {
				// System.out.println(">>>>>test obsId" + testObsId);
				patientObservations.add(Context.getObsService().getObs(
						testObsId));

			}

		}

		return patientObservations;
	}

	public List<Obs> getPatientLabtestBetweenTwoDate(int patientId,
			int conceptId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Obs> patientObservations = new ArrayList<Obs>();
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session
				.createSQLQuery("select o.obs_id from obs o where "
						+ "  o.person_id="
						+ patientId
						+ "  and o.concept_id="
						+ conceptId
						+ " "
						+ " and o.obs_datetime between '"
						+ df.format(startDate)
						+ "' and '"
						+ df.format(endDate)
						+ "' and o.obs_id in (select t.test_obs_id from trac_sample_test t) ");

		System.out
				.println("ZZZZlab test by locato and patioend between two dates"
						+ query.toString());
		List<Integer> testObsIds = query.list();
		for (Integer testObsId : testObsIds) {
			if (testObsId != null) {
				// System.out.println(">>>>>test obsId" + testObsId);
				patientObservations.add(Context.getObsService().getObs(
						testObsId));

			}

		}

		return patientObservations;
	}

	public List<Obs> getPatientLabtestByLocation(int patientId, int locationId,
			int conceptId) {
		// TODO Auto-generated method stub

		List<Obs> patientObservations = new ArrayList<Obs>();
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session
				.createSQLQuery("select o.obs_id from obs o where "
						+ "  o.person_id=" + patientId + " and o.location_id="
						+ locationId + " and o.concept_id=" + conceptId + " ");
		log.info("ZZZZlab test by locato and patioend between two dates"
				+ query.toString());
		List<Integer> testObsIds = query.list();
		for (Integer testObsId : testObsIds) {
			if (testObsId != null) {
				// System.out.println(">>>>>test obsId" + testObsId);
				patientObservations.add(Context.getObsService().getObs(
						testObsId));

			}

		}

		return patientObservations;
	}

	public List<Obs> getAllLabTest(Integer labConceptId) {
		// TODO Auto-generated method stub

		// List to store all lab test by lab test type
		List<Obs> allLabtest = new ArrayList<Obs>();
		ObsService obsServ = Context.getObsService();
		Session session = getSessionFactory().getCurrentSession();

		SQLQuery query = session
				.createSQLQuery("SELECT o.obs_id FROM obs o where o.concept_id ="
						+ labConceptId
						+ " and o.obs_datetime between '2011-08-01'and '2011-11-11' ");

		// System.out.println("ZZZZlab test by locato and patioend between two dates"
		// + query.toString());
		List<Integer> labTestObsIds = query.list();
		for (Integer labTestObsId : labTestObsIds) {
			allLabtest.add(obsServ.getObs(labTestObsId));

		}

		return allLabtest;
	}

	/**
	 * get all Lab obs
	 * 
	 * @see LaboratoryDAO#getLabExamsByExamType(int,
	 *      java.util.Collection, java.util.Date, java.util.Date)
	 */
	public List<Obs> getLabExamsByExamType(int patientId,
			Collection<Integer> cptIds, Date startDate, Date endDate) {
		// TODO Auto-generated method stu
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Obs> labExamsByCategory = new ArrayList<Obs>();
		ObsService labObbService = Context.getObsService();
		SQLQuery query = null;
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("SELECT o.obs_id FROM obs o where o.concept_id in ( ");

		int i = 1;
		for (Integer onecptId : cptIds) {

			if (i < cptIds.size()) {
				strbuf.append(" " + onecptId + ",");
			}
			if (i == cptIds.size()) {
				strbuf.append(" " + onecptId);
			}
			i = i + 1;

		}
		strbuf.append(" ) and o.voided=0 and   o.person_id=" + patientId);
		strbuf.append("  " + " and  cast(o.obs_datetime as date) between  '"
				+ df.format(startDate) + "' and '" + df.format(endDate) + "'");
		query = sessionFactory.getCurrentSession().createSQLQuery(
				strbuf.toString());

		List<Integer> obsIdFromQuery = query.list();

		for (Integer integer : obsIdFromQuery) {
			labExamsByCategory.add(labObbService.getObs(integer));
		}

		return labExamsByCategory;

	}

	public List<Obs> getLabExamsByExamTypeBetweenTwoDates(Date startDate,
			Date endDate, Integer conceptId) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Obs> labExamsByCategory = new ArrayList<Obs>();
		ObsService labObbService = Context.getObsService();
		SQLQuery query = null;
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("SELECT o.obs_id FROM obs o where o.concept_id ="
				+ conceptId + "");
		strbuf.append("  " + " and o.obs_datetime between  '"
				+ df.format(startDate) + "' and '" + df.format(endDate) + "'");

		query = sessionFactory.getCurrentSession().createSQLQuery(
				strbuf.toString());

		List<Integer> obsIdFromQuery = query.list();

		for (Integer integer : obsIdFromQuery) {
			labExamsByCategory.add(labObbService.getObs(integer));
		}

		return labExamsByCategory;
	}

	@Override
	public List<Obs> getAllNegtiveLabExams(Date startDate, Date endDate,
			int conceptId) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Obs> labExamsByCategory = new ArrayList<Obs>();
		ObsService labObbService = Context.getObsService();
		SQLQuery query = null;
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("SELECT o.obs_id FROM obs o where o.concept_id ="
				+ conceptId + "");
		strbuf.append("  "
				+ " and o.value_coded in (664) and  o.obs_datetime between  '"
				+ df.format(startDate) + "' and '" + df.format(endDate) + "' and o.voided=0");

		query = sessionFactory.getCurrentSession().createSQLQuery(
				strbuf.toString());

		List<Integer> obsIdFromQuery = query.list();

		for (Integer integer : obsIdFromQuery) {
			labExamsByCategory.add(labObbService.getObs(integer));
		}

		return labExamsByCategory;
	}

	@Override
	public List<Obs> getAllPositiveLabExams(Date startDate, Date endDate,
			int conceptId) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Obs> labExamsByCategory = new ArrayList<Obs>();
		ObsService labObbService = Context.getObsService();
		SQLQuery query = null;
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("SELECT o.obs_id FROM obs o where o.concept_id ="
				+ conceptId + "");
		strbuf
				.append("  "
						+ " and o.value_coded not in(664) and o.obs_datetime between  '"
						+ df.format(startDate) + "' and '" + df.format(endDate)
						+ "' and o.voided=0");

		query = sessionFactory.getCurrentSession().createSQLQuery(
				strbuf.toString());

		List<Integer> obsIdFromQuery = query.list();

		for (Integer integer : obsIdFromQuery) {
			labExamsByCategory.add(labObbService.getObs(integer));
		}

		return labExamsByCategory;
	}

	@Override
	public List<Order> getLabOrders(int patientId, Collection<Integer> cptIds,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub

		OrderService orderServc = Context.getOrderService();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Order> labOrdeByCategory = new ArrayList<Order>();
		ObsService labObbService = Context.getObsService();
		SQLQuery query = null;
		StringBuffer strbuf = new StringBuffer();
		strbuf
				.append("SELECT o.order_id FROM orders o where o.concept_id in ( ");

		int i = 1;
		for (Integer onecptId : cptIds) {
			if (i < cptIds.size()) {
				strbuf.append(" " + onecptId + ",");
			}
			if (i == cptIds.size()) {
				strbuf.append(" " + onecptId);

			}
			i = i + 1;

		}
		strbuf.append(" ) and  o.patient_id=" + patientId);
		strbuf.append("  and  o.voided= 0 ");
		strbuf.append("  " + " and  cast(o.date_activated as date) between  '"
				+ df.format(startDate) + "' and '" + df.format(endDate) + "'");
		query = sessionFactory.getCurrentSession().createSQLQuery(
				strbuf.toString());
		System.out.println("ZZZZlaborder query" + query.toString());
		List<Integer> oderIdsIdFromQuery = new ArrayList<Integer>();
		try {
		oderIdsIdFromQuery = query.list();
		}catch (SQLGrammarException e){
			System.out.println("Something went wrong in a Query");
		}



		for (Integer orderId : oderIdsIdFromQuery) {
			labOrdeByCategory.add(orderServc.getOrder(orderId));
		}

		System.out.println(">>>>laborders size" + labOrdeByCategory.size());

		return labOrdeByCategory;
	}

	@Override
	public List<Order> getLabOrdersBetweentwoDate(int patientId,
			Date startDate, Date enddate) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SQLQuery query = null;
		List<Order> ordersList = new ArrayList<Order>();
		OrderService orderServic = Context.getOrderService();
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("SELECT  o.order_id  FROM orders o where o.voided=0 and o.patient_Id ="
				+ patientId + "");
		strbuf.append("  " + " and  cast(o.date_activated as date) between  '"
				+ df.format(startDate) + "' and '" + df.format(enddate) + "'");

		query = sessionFactory.getCurrentSession().createSQLQuery(
				strbuf.toString());
		System.out.println(">>>>>order query" + query.toString());

		List<Integer> orderIdsFromQuery = query.list();
		for (Integer orderId : orderIdsFromQuery) {
			ordersList.add(orderServic.getOrder(orderId));

		}
		System.out.println("OrderListBetween two dates" + ordersList);

		return ordersList;

	}

	@Override
	public List<Order> getLabOrdersBetweentwoDate(Date startDate, Date enddate) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SQLQuery query = null;
		List<Order> ordersList = new ArrayList<Order>();
		OrderService orderServic = Context.getOrderService();
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("SELECT  o.order_id  FROM orders o where  o.voided=0 and " + "");
		strbuf.append("  " + "  cast(o.date_activated as date) between  '"
				+ df.format(startDate) + "' and '" + df.format(enddate) + "'");

		query = sessionFactory.getCurrentSession().createSQLQuery(
				strbuf.toString());
		System.out.println(">>>>>order query" + query.toString());

		List<Integer> orderIdsFromQuery = query.list();
		for (Integer orderId : orderIdsFromQuery) {
			ordersList.add(orderServic.getOrder(orderId));

		}
		System.out.println("OrderListBetween two dates" + ordersList);

		return ordersList;
	}

	@Override
	public List<Order> getLabOrdersByLabCode(String labCode) {
		// TODO Auto-generated method stub

		SQLQuery query = null;
		List<Order> ordersList = new ArrayList<Order>();
		//OrderService orderServic = Context.getOrderService();
		StringBuffer strbuf = new StringBuffer();
		strbuf
				.append("SELECT  o.order_id  FROM orders o where  o.voided = 0 and  o.accession_number ='"
						+ labCode + "'");

		query = sessionFactory.getCurrentSession().createSQLQuery(
				strbuf.toString());
		System.out.println(">>>>>order query" + query.toString());

		List<Integer> orderIdsFromQuery = query.list();
		for (Integer orderId : orderIdsFromQuery) {
			ordersList.add(Context.getOrderService().getOrder(orderId));

		}
		return ordersList;
	}

	@Override
	public List<Obs> getAllpatientObs(int patientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFoundLabCode(String labCode) {
		// TODO Auto-generated method stub

		boolean isFound = false;
		SQLQuery query = null;

		StringBuffer strbuf = new StringBuffer();
		strbuf.append("SELECT DISTINCT accession_number FROM orders o");
		query = sessionFactory.getCurrentSession().createSQLQuery(
				strbuf.toString());

		List<String> accessionNumbers = query.list();
		if (accessionNumbers.contains(labCode)) {
			isFound = true;
		}
		return isFound;
	}

	/**
	 * @see LaboratoryDAO#getPatientLabordersBetweendates(int,
	 *      java.util.Date, java.util.Date)
	 */
	@Override
	public Collection<Order> getPatientLabordersBetweendates(int patientId,
			Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SQLQuery query = null;
		Collection<Order> ordersList = new ArrayList<Order>();
		OrderService orderServic = Context.getOrderService();
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("SELECT  o.order_id  FROM orders o where o.patient_Id ="
				+ patientId + "");
		strbuf.append("  " + " and   cast(o.date_activated as date) between  '"
				+ df.format(startDate) + "' and '" + df.format(endDate) + "'");

		query = sessionFactory.getCurrentSession().createSQLQuery(
				strbuf.toString());

		List<Integer> orderIdsFromQuery = query.list();
		for (Integer orderId : orderIdsFromQuery) {
			ordersList.add(orderServic.getOrder(orderId));

		}

		return ordersList;
	}

	/**
	 * @see LaboratoryDAO#getObsByLabCode(java.lang.String)
	 */
	@Override
	public List<Obs> getObsByLabCode(String labCode) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(
				Obs.class).add(Restrictions.eq("accessionNumber", labCode))
				.add(Restrictions.eq("voided", false));
		return query.list();
	}

	/**
	 * @see LaboratoryDAO#getObsByLabOrder(org.openmrs.Order,
	 *      org.openmrs.Concept)
	 */
	@Override
	public List<Obs> getObsByLabOrder(Order order, Concept cpt) {
		log
				.info(">>>>>>>>>>>log info> within the existingorderObs>>>has concept>>"
						+ order.getConcept().getConceptId());
		Criteria query = sessionFactory.getCurrentSession().createCriteria(
				Obs.class).add(Restrictions.eq("order", order)).add(
				Restrictions.eq("voided", false));

		if (cpt != null)
			query.add(Restrictions.eq("concept", cpt));

		return query.list();
	}

	@Override
	public Collection<Encounter> getPatientEncountersByDate(int patientId,
			Date startDate, EncounterType encounterType) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//Encounter labEncounter = null;
		Collection<Encounter> encountersByDate = new ArrayList<Encounter>();
		//String formattedDate = df.format(startDate);
		/*Criteria query = sessionFactory.getCurrentSession().createCriteria(
				Encounter.class).add(Restrictions.eq("patientId", patientId))
				.add(Restrictions.eq("encounterType", encounterType));*/
		StringBuffer strencout = new StringBuffer();

		strencout.append("select * from encounter where patient_id ="+patientId+" and encounter_type="+encounterType.getEncounterTypeId()+"");

		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(strencout.toString()).addEntity(Encounter.class);
		Collection<Encounter> encounterslist = query.list();

		if (encounterslist.size() > 0) {
			for (Encounter encounter : encounterslist) {
				String encounterDateStr = df.format(encounter.getEncounterDatetime());
				if (encounterDateStr.equals(df.format(startDate))) {
					encountersByDate.add(encounter);
				}
			}
		}
	
		return encountersByDate;
	}
}
