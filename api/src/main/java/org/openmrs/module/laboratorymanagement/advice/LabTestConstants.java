package org.openmrs.module.laboratorymanagement.advice;

import org.openmrs.module.laboratorymanagement.utils.GlobalPropertiesMgt;

public class LabTestConstants {
	public static final int LABORATORY_EXAMINATIONS_CONSTRUCT = 1337;
	public static final int HEMOSTASIS=7006;
	public static final int FBC=7005;	
	public static final int URINECONCEPTID = 7193;
	public static final int WBCDIFFID= 10088;
	//public static final int hematologyId=8004;

	/*public static final int spermConceptId = 7202;
	public static final int hematologyId=6167;

	public static final int PARASITOLOGYID=7222;
	public static final int hemostasisId=7217;
	public static final int bacteriologyId=7192;
	public static final int urineChemistryId=7243;
	public static final int immunoSerologyId=7265;
	public static final int bloodChemistryId =7244;
	public static final int toxicologyId =7835;
	//added new constants
	public static final   int tumourMarkerId = 105406;
	public static final int  thyroidFunctionId = 105411;
	public static final int fertilityHormoneId = 105417;*/


	public static final int spermConceptId = GlobalPropertiesMgt.getConcept(GlobalPropertiesMgt.spermConcept).getConceptId();
	public static final int hematologyId=GlobalPropertiesMgt.getConcept(GlobalPropertiesMgt.hematology).getConceptId();

	public static final int PARASITOLOGYID=GlobalPropertiesMgt.getConcept(GlobalPropertiesMgt.PARASITOLOGY).getConceptId();
	public static final int hemostasisId=GlobalPropertiesMgt.getConcept(GlobalPropertiesMgt.hemostasis).getConceptId();
	public static final int bacteriologyId=GlobalPropertiesMgt.getConcept(GlobalPropertiesMgt.bacteriology).getConceptId();
	public static final int urineChemistryId=GlobalPropertiesMgt.getConcept(GlobalPropertiesMgt.urineChemistry).getConceptId();
	public static final int immunoSerologyId=GlobalPropertiesMgt.getConcept(GlobalPropertiesMgt.immunoSerology).getConceptId();
	public static final int bloodChemistryId =GlobalPropertiesMgt.getConcept(GlobalPropertiesMgt.bloodChemistry).getConceptId();
	public static final int toxicologyId =GlobalPropertiesMgt.getConcept(GlobalPropertiesMgt.toxicology).getConceptId();
	//added new constants
	public static final   int tumourMarkerId = GlobalPropertiesMgt.getConcept(GlobalPropertiesMgt.tumourMarker).getConceptId();
	public static final int  thyroidFunctionId = GlobalPropertiesMgt.getConcept(GlobalPropertiesMgt.thyroidFunction).getConceptId();
	public static final int fertilityHormoneId = GlobalPropertiesMgt.getConcept(GlobalPropertiesMgt.fertilityHormone).getConceptId();
		
	
	
	
	
}
