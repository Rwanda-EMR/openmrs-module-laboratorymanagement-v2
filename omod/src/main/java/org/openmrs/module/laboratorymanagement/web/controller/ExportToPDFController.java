package org.openmrs.module.laboratorymanagement.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.laboratorymanagement.advice.HeaderFooterMgt;
import org.openmrs.module.laboratorymanagement.service.LaboratoryService;
import org.openmrs.module.laboratorymanagement.web.LabUtils;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


public class ExportToPDFController extends AbstractController {
	protected final Log log = LogFactory.getLog(getClass());

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SimpleDateFormat df = OpenmrsUtil.getDateFormat(Context.getLocale());

		LaboratoryService laboratoryService = Context.getService(LaboratoryService.class);
		String conceptIdStr = request.getParameter("testType");
		String patientIdstr=request.getParameter("patientId");
		
		
		String startD = request.getParameter("startDate");
		String endD = request.getParameter("endDate");
		
		
		
		// Date declaration
		Date startDate = null;
		Date endDate = null;
		/*
		 * Data validation
		 */

		if (startD != null && startD.length() != 0) {
			startDate = df.parse(startD);
		}
		if (endD != null && endD.length() != 0) {
			endDate = df.parse(endD);
		}
		

		if (patientIdstr!=null){
			int patientId=Integer.parseInt(patientIdstr);
			
			//Map<ConceptName, List<Object[]>> mappedLabExam= MappedLabExamManagement	.getMappedExamsByLabType(patientId,startDate,endDate);
			//Object patientIdentifElement [] = LabUtils.getPatientIdentificationFromLab(patientId, startDate, endDate);
			Map<ConceptName, List<Object[]>>	mappedLabExam = LabUtils.getPatientLabresults(patientId, startDate, endDate);
			//Map<ConceptName, List<Object[]>> mappedLabExam  = (Map<ConceptName, List<Object[]>>) request.getSession().getAttribute("patientMappedLabExam");
			exportPatientReportToPDF(request, response, mappedLabExam, "patientReport.pdf", "test",patientId);
					
			
		}
		
		return null;
	}

	public void exportToPDF(HttpServletRequest request,
			HttpServletResponse response, List<Object[]> listOflabtest,
			String filename, String title, int conceptId)
			throws DocumentException, IOException {
		// TODO Auto-generated method stub
		Concept cpt = Context.getConceptService().getConcept(conceptId);
		System.out.println(">>>>>>list to export" + listOflabtest);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		response.setContentType("application/pdf");
		Document document = new Document();
		try {

			response.setHeader("Content-Disposition",
					"attachment; filename=\"laboratory.pdf\"");
			PdfWriter writer = PdfWriter.getInstance(document, response
					.getOutputStream());
			document.open();
			writer.setBoxSize("art", PageSize.A4);

			float[] colsWidth = { 5f, 20f, 17f, 17f, 17f, 16f, 20f };
			PdfPTable table = new PdfPTable(colsWidth);
			// add a title
			PdfPCell cell = new PdfPCell(new Paragraph("CD4 lab tests"));
			cell.setColspan(2);

			document.add(new Paragraph(
					"                                Laboratory tests:"
							+ cpt.getName()));

			document.add(new Paragraph("  "));
			document.add(new Paragraph("  "));

			table.addCell("#");
			table.addCell("GIVEN NAME");
			table.addCell("FAMILY NAME");
			table.addCell("TEST NAME");
			table.addCell("TESTED ON");
			table.addCell("LOCATION");
			table.addCell("TEST RESULT");

			for (int i = 0; i < listOflabtest.size(); i++) {

				Object[] labtest = listOflabtest.get(i);
				Person p = (Person) labtest[0];
				Obs ob = (Obs) labtest[1];

				table.addCell(i + 1 + "");
				table.addCell(p.getGivenName() + "");
				table.addCell(p.getFamilyName() + "");
				table.addCell(ob.getConcept().getName() + "");
				table.addCell(df.format(ob.getObsDatetime()) + "");
				table.addCell(ob.getLocation() + "");
				if (cpt.getDatatype().isNumeric()) {
					table.addCell(ob.getValueNumeric() + "");
				}
				if (cpt.getDatatype().isCoded()) {
					table.addCell(ob.getValueCoded().getName() + "");

				}
				if (cpt.getDatatype().isText()) {
					table.addCell(ob.getValueText() + "");

				}
				if (cpt.getDatatype().isDate()) {
					table.addCell(df.format((ob.getValueDatetime())) + "");

				}

			}

			document.add(table);
			document.getJavaScript_onLoad();
			document.close();
			// document.add(new Paragraph("Hello world"));
			// document.close();
		} catch (DocumentException e) {
		}
	}

	public void exportPatientReportToPDF(HttpServletRequest request,
			HttpServletResponse response,
			Map<ConceptName, List<Object[]>> mappedLabExam, String filename,
			String title, int patientId ) throws DocumentException, IOException {

		Document document = new Document();
		document.setPageSize(PageSize.A4_LANDSCAPE.rotate());

		Patient patient = Context.getPatientService().getPatient(patientId);
		// List<PatientBill> patientBills =
		// (List<PatientBill>)request.getAttribute("reportedPatientBillsPrint");

		/*
		 * PatientBill pb = null;
		 *
		 * pb = Context.getService(BillingService.class).getPatientBill(
		 * Integer.parseInt(request.getParameter("patientBills")));
		 */
		/*
		 * String filename = pb.getBeneficiary().getPatient().getPersonName()
		 * .toString().replace(" ", "_"); filename =
		 * pb.getBeneficiary().getPolicyIdNumber().replace(" ", "_") + "_" +
		 * filename + ".pdf";
		 */
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "report"); // file name

		PdfWriter writer = PdfWriter.getInstance(document, response
				.getOutputStream());
		writer.setBoxSize("art", new Rectangle(0, 0, 2382, 3369));
		writer.setBoxSize("art", PageSize.A4_LANDSCAPE.rotate());

		HeaderFooterMgt event = new HeaderFooterMgt();
		writer.setPageEvent(event);

		document.open();
		// document.setPageSize(new Rectangle(0, 0, 2382, 3369));

		document.addAuthor(Context.getAuthenticatedUser().getPersonName()
				.toString());// the name of the author

		FontSelector fontTitle = new FontSelector();
		fontTitle.addFont(new Font(Font.FontFamily.COURIER, 10.0f, Font.ITALIC));

		// Report title
		Chunk chk = new Chunk("Printed on : "
				+ (new SimpleDateFormat("dd-MMM-yyyy").format(new Date())));
		chk.setFont(new Font(Font.FontFamily.COURIER, 10.0f, Font.BOLD));
		Paragraph todayDate = new Paragraph();
		todayDate.setAlignment(Element.ALIGN_RIGHT);
		todayDate.add(chk);
		document.add(todayDate);
		/*document.add(fontTitle.process("REPUBLIQUE DU RWANDA\n"));
		document.add(fontTitle.process("POLICE NATIONALE\n"));
		document.add(fontTitle.process("KACYIRU POLICE HOSPITAL\n"));
		document.add(fontTitle.process("B.P. 6183 KIGALI\n"));
		document.add(fontTitle.process("T�l : 584897\n"));
		document.add(fontTitle.process("E-mail : medical@police.gov.rw"));*/
		document.add(fontTitle.process(Context.getAdministrationService().getGlobalProperty("laboratorymodule.healthfacility.name")));
		document.add(fontTitle.process("\n"));
		document.add(fontTitle.process(Context.getAdministrationService().getGlobalProperty("laboratorymodule.healthfacility.POBOX")));
		document.add(fontTitle.process("\n"));
		document.add(fontTitle.process(Context.getAdministrationService().getGlobalProperty("laboratorymodule.healthfacility.telephone")));
		document.add(fontTitle.process("\n"));
		document.add(fontTitle.process("E-mail : "+Context.getAdministrationService().getGlobalProperty("laboratorymodule.healthfacility.email")));
		// End Report title

		document.add(new Paragraph("\n"));
		chk = new Chunk("Laboratory results");
		chk.setFont(new Font(Font.FontFamily.COURIER, 10.0f, Font.BOLD));
		chk.setUnderline(0.2f, -2f);
		Paragraph pa = new Paragraph();
		pa.add(chk);
		pa.setAlignment(Element.ALIGN_CENTER);
		document.add(pa);
		document.add(new Paragraph("\n"));

		document.add(fontTitle.process("Family Name: "
				+ patient.getFamilyName() + "\n"));
		document.add(fontTitle.process("Given name: " + patient.getGivenName()
				+ "\n"));
		document.add(fontTitle.process("Age: " + patient.getAge() + "\n"));


		// title row
		FontSelector fontTitleSelector = new FontSelector();
		fontTitleSelector.addFont(new Font(Font.FontFamily.UNDEFINED, 9, Font.BOLD));

		FontSelector fontContentSelector = new FontSelector();
		fontContentSelector.addFont(new Font(Font.FontFamily.UNDEFINED, 8, Font.NORMAL));
		// Table of identification;
		PdfPTable table = null;
		table = new PdfPTable(2);
		table.setWidthPercentage(100f);

		// tableHeader.addCell(table);

		// document.add(tableHeader);

		document.add(new Paragraph("\n"));

		// Table of lab report items;
		float[] colsWidth = { 15f, 6f, 10f, 10f ,6f, 8f, 6f, 8f, 6f, 8f, 9f};
		table = new PdfPTable(colsWidth);
		table.setWidthPercentage(100f);
		BaseColor bckGroundTitle = new BaseColor(170, 170, 170);
		BaseColor bckGroundTitl = new BaseColor(Color.yellow);

		// table Header
		PdfPCell cell = new PdfPCell(fontTitleSelector.process("Exam"));

		cell.setBackgroundColor(bckGroundTitle);

		table.addCell(cell);

		cell = new PdfPCell(fontTitleSelector.process("Result"));
		cell.setBackgroundColor(bckGroundTitle);

		table.addCell(cell);

		cell = new PdfPCell(fontTitleSelector.process("Comment"));
		cell.setBackgroundColor(bckGroundTitle);

		table.addCell(cell);

		cell = new PdfPCell(fontTitleSelector.process("Normal Range"));
		cell.setBackgroundColor(bckGroundTitle);
		table.addCell(cell);

		cell = new PdfPCell(fontTitleSelector.process("Lab Code"));
		cell.setBackgroundColor(bckGroundTitle);
		table.addCell(cell);

		cell = new PdfPCell(fontTitleSelector.process("Orderer"));
		cell.setBackgroundColor(bckGroundTitle);
		table.addCell(cell);

		cell = new PdfPCell(fontTitleSelector.process("Ordered On"));
		cell.setBackgroundColor(bckGroundTitle);
		table.addCell(cell);

		cell = new PdfPCell(fontTitleSelector.process("Code Enterer"));
		cell.setBackgroundColor(bckGroundTitle);
		table.addCell(cell);

		cell = new PdfPCell(fontTitleSelector.process("Coded On"));
		cell.setBackgroundColor(bckGroundTitle);
		table.addCell(cell);

		cell = new PdfPCell(fontTitleSelector.process("Result Enterer"));
		cell.setBackgroundColor(bckGroundTitle);
		table.addCell(cell);

		cell = new PdfPCell(fontTitleSelector.process("Result Entererd On"));
		cell.setBackgroundColor(bckGroundTitle);
		table.addCell(cell);
		/*
		 * cell = new PdfPCell(fontTitleSelector.process("Date "));
		 * cell.setBackgroundColor(bckGroundTitle); table.addCell(cell);
		 */

		// normal row
		FontSelector fontselector = new FontSelector();
		fontselector.addFont(new Font(Font.FontFamily.COURIER, 8, Font.NORMAL));

		// empty row
		FontSelector fontTotals = new FontSelector();
		fontTotals.addFont(new Font(Font.FontFamily.COURIER, 9, Font.BOLD));

		// ===========================================================
		for (ConceptName cptName : mappedLabExam.keySet()) {

			cell = new PdfPCell(fontTitleSelector.process("" + cptName));
			cell.setBackgroundColor(bckGroundTitl);
			table.addCell(cell);
			cell = new PdfPCell(fontTitleSelector.process(""));
			table.addCell(cell);
			cell = new PdfPCell(fontTitleSelector.process(""));
			table.addCell(cell);
			cell = new PdfPCell(fontTitleSelector.process(""));
			table.addCell(cell);
			cell = new PdfPCell(fontTitleSelector.process(""));
			table.addCell(cell);
			cell = new PdfPCell(fontTitleSelector.process(""));
			table.addCell(cell);
			cell = new PdfPCell(fontTitleSelector.process(""));
			table.addCell(cell);
			cell = new PdfPCell(fontTitleSelector.process(""));
			table.addCell(cell);
			cell = new PdfPCell(fontTitleSelector.process(""));
			table.addCell(cell);
			cell = new PdfPCell(fontTitleSelector.process(""));
			table.addCell(cell);
			cell = new PdfPCell(fontTitleSelector.process(""));
			table.addCell(cell);

			List<Object[]> labExamHistory = mappedLabExam.get(cptName);
			for (Object[] labExam : labExamHistory) {
				// table Header
				// Object[] labe = listOflabtest.get(i);
				Obs ob = (Obs) labExam[0];
				cell = new PdfPCell(fontContentSelector.process(""
						+ ob.getConcept().getName()));

				table.addCell(cell);
				if (ob.getConcept().getDatatype().isNumeric()) {
					cell = new PdfPCell(fontContentSelector.process(""
							+ ob.getValueNumeric()));
					table.addCell(cell);

				}

				if (ob.getConcept().getDatatype().isCoded()) {
					cell = new PdfPCell(fontContentSelector.process(""
							+ ob.getValueCoded().getName()));
					table.addCell(cell);

				}

				if (ob.getConcept().getDatatype().isText()) {
					cell = new PdfPCell(fontContentSelector.process(""
							+ ob.getValueText()));
					table.addCell(cell);

				}

				cell = new PdfPCell(fontContentSelector.process(""+ob.getComment()));
				table.addCell(cell);

				cell = new PdfPCell(fontContentSelector.process(""
						+ (labExam[1] != null ? labExam[1] : "-")));
				table.addCell(cell);


				cell = new PdfPCell(fontContentSelector.process(""+ob.getAccessionNumber()));
				table.addCell(cell);

				cell = new PdfPCell(fontContentSelector.process(""+ob.getOrder().getOrderer().getPerson().getFamilyName()+" "+ob.getOrder().getOrderer().getPerson().getGivenName()));
				table.addCell(cell);

				cell = new PdfPCell(fontContentSelector.process(""+ob.getOrder().getDateCreated()));
				table.addCell(cell);

				cell = new PdfPCell(fontContentSelector.process(""+ob.getOrder().getCreator().getFamilyName()+" "+ob.getOrder().getCreator().getGivenName()));
				table.addCell(cell);

				cell = new PdfPCell(fontContentSelector.process(""+ob.getOrder().getDateActivated()));
				table.addCell(cell);

				cell = new PdfPCell(fontContentSelector.process(""+ob.getCreator().getFamilyName()+" "+ob.getCreator().getGivenName()));
				table.addCell(cell);

				cell = new PdfPCell(fontContentSelector.process(""+ob.getDateCreated()));
				table.addCell(cell);

				fontselector.addFont(new Font(Font.FontFamily.COURIER, 8,
						Font.NORMAL));

				// empty row
				// FontSelector fontTotals = new FontSelector();
				fontTotals.addFont(new Font(Font.FontFamily.COURIER, 9, Font.BOLD));

			}

		}

		/*cell = new PdfPCell(fontTitleSelector
				.process("Names, Signature et Stamp of Lab Chief\n"
						//+ Context.getAuthenticatedUser().getPersonName()));
						+ Context.getUserService().getUser(140).getPersonName()));
		*/
		cell = new PdfPCell(fontTitleSelector
				.process("Names, Signature et Stamp of Lab Chief\n"
						//+ Context.getAuthenticatedUser().getPersonName()));
						+ Context.getAuthenticatedUser().getPersonName().getFullName()));




		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		// ================================================================
		table.addCell(cell);

		document.add(table);

		// Table of signatures;
		table = new PdfPTable(2);
		table.setWidthPercentage(100f);

		cell = new PdfPCell(fontTitleSelector.process(" "));

		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		cell = new PdfPCell(fontTitleSelector
				.process("Names, Signature and  Stamp of Provider\n"
						+ Context.getAuthenticatedUser().getPersonName()));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);
		document.close();



		document.close();

	}
}
