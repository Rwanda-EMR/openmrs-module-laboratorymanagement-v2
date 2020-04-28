<%@ include file="/WEB-INF/template/include.jsp"%>
<!--
<openmrs:htmlInclude
	file="/moduleResources/laboratorymanagement/scripts/jquery1.js" /> -->
<openmrs:htmlInclude
	file="/moduleResources/laboratorymanagement/scripts/demo_page1.css" />
<openmrs:htmlInclude
	file="/moduleResources/laboratorymanagement/scripts/demo_table1.css" />

<openmrs:htmlInclude
	file="/moduleResources/laboratorymanagement/scripts/jquery.dataTables1.js" />
<openmrs:htmlInclude
	file="/moduleResources/laboratorymanagement/scripts/jquery.simplemodal.js" />
<openmrs:htmlInclude	file="/moduleResources/laboratorymanagement/scripts/basic.js" />

<openmrs:htmlInclude
	file="/moduleResources/laboratorymanagement/scripts/basic.css" />

<openmrs:htmlInclude
	file="/moduleResources/laboratorymanagement/scripts/jquery.PrintArea1.js" />


<style type="text/css">
@media print {
	.non-printable {
		display: none;
	}
}
</style>
<script>
	jQuery(document)
			.ready(
					function() {
						jQuery("#labOrderForm").show();
						jQuery("#toggleForm").click( function() {
							jQuery("#labOrderForm").toggle();
						});
						oTable = jQuery('#example_table')
								.dataTable(
										{
											"fnDrawCallback" : function(
													oSettings) {
												if (oSettings.aiDisplay.length == 0) {
													jQuery('table#example_table')
															.css( {
																'width' :'100%'
															});
													return;
												}
												var nTrs = jQuery('tbody tr',
														oSettings.nTable);
												jQuery('table#example_table').css( {
													'width' :'100%'
												});
												var iColspan = nTrs[0]
														.getElementsByTagName('td').length;
												var sLastGroup = "";
												for ( var i = 0; i < nTrs.length; i++) {
													var iDisplayIndex = oSettings._iDisplayStart
															+ i;
													var sGroup = oSettings.aoData[oSettings.aiDisplay[iDisplayIndex]]._aData[0];
													if (sGroup != sLastGroup) {
														var nGroup = document
																.createElement('tr');
														var nCell = document
																.createElement('td');
														nCell.colSpan = iColspan;
														nCell.className = "group";
														nCell.innerHTML = sGroup;
														nGroup
																.appendChild(nCell);
														nTrs[i].parentNode
																.insertBefore(
																		nGroup,
																		nTrs[i]);
														sLastGroup = sGroup;
													}
												}
											},
											"aoColumnDefs" : [ {
												"bVisible" :false,
												"aTargets" : [ 0 ]
											} ],
											"aaSortingFixed" : [ [ 0, 'asc' ] ],
											"aaSorting" : [ [ 1, 'asc' ] ],
											"sDom" :'lfr<"giveHeight"t>ip'
										});
						jQuery('#print_lab_ordonance')
								.click(
										function(e) {
											var row = null;
											var s = '';
											var columns = jQuery(
													'#example_table thead th')
													.map( function() {
														return jQuery(this).text();
													});
											var tableObject = jQuery(
													'#example_table tbody tr')
													.map(
															function(i) {
																row = {};
																jQuery(this)
																		.find(
																				'td')
																		.each(
																				function(
																						i) {
																					var rowName = columns[i];
																					row[rowName] = jQuery(
																							this)
																							.text();
																					s += jQuery(
																							this)
																							.text() + ',';
																				});
																s += ';';
															});
											var res = s.split(';');
											var tmpArr = null;
											var leftStr = '';
											var rightStr = '';
											var count = 1;
											var date = '';
											var frequency = '';
											var patientName = jQuery(
													'#patientHeaderPatientName')
													.text();
											for ( var k = 0; k < res.length; k++) {
												tmpArr = res[k].split(',');
												for ( var j = 0; j < tmpArr.length; j++) {
													if (j == 1) {
														var num = parseInt(tmpArr[0]);
														if (count % 2 != 0) {
															if (tmpArr[1]) {
																leftStr += count
																		+ '. '
																		+ tmpArr[1]
																		+ '<br />';
																date = tmpArr[3];
																count++;
															}
														} else {
															if (tmpArr[1]) {
																rightStr += count
																		+ '. '
																		+ tmpArr[1]
																		+ '<br />';
																date = tmpArr[3];
																count++;
															}
														}
													}
												}
											}
											jQuery('#firstTd').html(leftStr);
											jQuery('#secondTd').html(rightStr);
											jQuery('#dateId').html(date);
										});
						jQuery("#print_btn").click( function() {
							jQuery(".printable").printArea();
						});
						jQuery('.parent')
								.click(
										function() {
											var parentId = this.id;
											var parentConceptId = parentId
													.split('_')[1];
											if (jQuery('#' + parentId)
													.is(':checked')) {
												jQuery('.child_' + parentConceptId)
														.attr('checked',
																'checked');
											} else {
												jQuery('.child_' + parentConceptId)
														.removeAttr('checked');
											}
										});
					});
</script>

<b><Oderable Exams></b>
<div align="center"><b>${model.msg}</b></div>
<fieldset><legend style="cursor: pointer;">Edit Lab
Request Form</legend>
<div id="labOrderForm">
<form action="labTechSetup.form" method="post">
<div>
<table>
	<tr>
		<!-- first column -->
		<td valign="top" width="20%"><c:forEach var="labOrderParent"
			items="${model.labOrderparList}">
			<c:set var="gdParentConcept"
				value="${labOrderParent.grandFatherConcept}" />
			<c:set var="parentConcepts" value="${labOrderParent.labTests}" />
			<c:set var="hematology" value="${model.hematology}" />
			<c:set var="parasitology" value="${model.parasitology}" />
			<c:set var="hemostasis" value="${model.hemostasis}" />
			<c:set var="bacteriology" value="${model.bacteriology}" />
			<c:set var="urinaryChemistry" value="${model.urinaryChemistry}" />
			<c:set var="immunoSerology" value="${model.immunoSerology}" />
			<c:set var="bloodChemistry" value="${model.bloodChemistry}" />
			<c:set var="toxicology" value="${model.toxicology}" />
			<c:set var="tumourMarker" value="${model.tumourMarker}" />
			<c:set var="thyroidFunction" value="${model.thyroidFunction}" />
			<c:set var="fertilityHormone" value="${model.fertilityHormone}" />





			<c:if test="${gdParentConcept.name.name eq  hematology }">
				<table>
					<tr>
						<td><b>${gdParentConcept.name.name}</b></td>
					</tr>
					<c:forEach var="labOrder" items="${parentConcepts}"
						varStatus="status">
						<c:set var="childrenConcepts" value="${labOrder.childrenConcept}" />
						<c:set var="parentConcept" value="${labOrder.parentConcept}" />
						<c:set var="fieldNameP"
							value="lab-${gdParentConcept}-${labOrder.parentConcept.conceptId}" />
						<tr>

							<c:set var="checkDisp" value="0" />
                            						<c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                                    	<c:if test="${currDis == parentConcept.conceptId}">
                                                    		<c:set var="checkDisp" value="1" />
                                                    	</c:if>
                                                    </c:forEach>

                   <c:choose>
                       	<c:when test="${checkDisp == 1}">
							<td><input name="${parentConcept.conceptId}"
								value="${parentConcept.conceptId}" type="checkbox"
								id="parent_${labOrder.parentConcept.conceptId}" class="parent" checked="true"><c:out
								value="${labOrder.parentConcept.name}" /></td>
                        </c:when>
                        <c:otherwise>
							<td><input name="${parentConcept.conceptId}"
								value="${parentConcept.conceptId}" type="checkbox"
								id="parent_${labOrder.parentConcept.conceptId}" class="parent"><c:out
								value="${labOrder.parentConcept.name}" /></td>
                        </c:otherwise>

                   </c:choose>


						</tr>
						<c:forEach var="childConcept" items="${childrenConcepts}"
							varStatus="status">
							<c:set var="fieldNameC"
								value="${fieldNameP}-${childConcept.conceptId}" />
							<tr>

							<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == childConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
                   <c:choose>
                       	<c:when test="${checkDisp == 1}">
								<td style="text-indent: 20px"><input name="${fieldNameC}"
									value="${childConcept.conceptId}"
									class="child_${labOrder.parentConcept.conceptId}"
									id="child_${childConcept.conceptId}" type="checkbox" checked="true"> <c:out
									value="${childConcept.name}" /></td>
                        </c:when>
                        <c:otherwise>
								<td style="text-indent: 20px"><input name="${fieldNameC}"
									value="${childConcept.conceptId}"
									class="child_${labOrder.parentConcept.conceptId}"
									id="child_${childConcept.conceptId}" type="checkbox"> <c:out
									value="${childConcept.name}" /></td>
                        </c:otherwise>

                   </c:choose>

							</tr>
						</c:forEach>
					</c:forEach>
				</table>
			</c:if>

			<c:if test="${gdParentConcept.name.name eq parasitology }">
				<table>
					<tr>
						<td><b>${gdParentConcept.name.name}</b></td>
					</tr>
					<c:forEach var="labOrder" items="${parentConcepts}"
						varStatus="status">
						<c:set var="childrenConcepts" value="${labOrder.childrenConcept}" />
						<c:set var="parentConcept" value="${labOrder.parentConcept}" />
						<c:set var="fieldNameP"
							value="lab-${gdParentConcept}-${labOrder.parentConcept.conceptId}" />
						<tr>

<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == parentConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>

						<c:choose>
                           	<c:when test="${checkDisp == 1}">
							<td><input name="${fieldNameP}"
								value="${parentConcept.conceptId}" type="checkbox"
								id="parent_${labOrder.parentConcept.conceptId}" class="parent" checked="true"><c:out
								value="${labOrder.parentConcept.name}" /></td>
							</c:when>
							<c:otherwise>
							<td><input name="${fieldNameP}"
								value="${parentConcept.conceptId}" type="checkbox"
								id="parent_${labOrder.parentConcept.conceptId}" class="parent"><c:out
								value="${labOrder.parentConcept.name}" /></td>
                            </c:otherwise>
                         </c:choose>


						</tr>
						<!--
						<c:forEach var="childConcept" items="${childrenConcepts}" varStatus="status">
							<c:set var="fieldNameC" value="${fieldNameP}-${childConcept.conceptId}" />
							<tr>
								<td style="text-indent: 20px"><input name="${fieldNameC}" value="${childConcept.conceptId}" class="child_${labOrder.parentConcept.conceptId}" id="child_${childConcept.conceptId}" type="checkbox">
									<c:out value="${childConcept.name}" /></td>
							</tr>
						</c:forEach> -->
					</c:forEach>
				</table>
			</c:if>

			<c:if test="${gdParentConcept.name.name eq  urinaryChemistry}">
            				<table>
            					<tr>
            						<td><b>${gdParentConcept.name.name}</b></td>
            					</tr>
            					<c:forEach var="labOrder" items="${parentConcepts}"
            						varStatus="status">

            						<c:set var="childrenConcepts" value="${labOrder.childrenConcept}" />
            						<c:set var="parentConcept" value="${labOrder.parentConcept}" />
            						<c:set var="fieldNameP"
            							value="lab-${gdParentConcept}-${labOrder.parentConcept.conceptId}" />

            						<tr>
<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == labOrder.parentConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">
            							<td><input name="${fieldNameP}"
            								value="${labOrder.parentConcept.conceptId}" type="checkbox"
            								id="parent_${labOrder.parentConcept.conceptId}" class="parent" checked="true"><c:out
            								value="${labOrder.parentConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>
            							<td><input name="${fieldNameP}"
            								value="${labOrder.parentConcept.conceptId}" type="checkbox"
            								id="parent_${labOrder.parentConcept.conceptId}" class="parent"><c:out
            								value="${labOrder.parentConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>


            						</tr>
            						<c:forEach var="childConcept" items="${childrenConcepts}"
            							varStatus="status">
            							<c:set var="fieldNameC"
            								value="${fieldNameP}-${childConcept.conceptId}" />
            							<tr>

<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == childConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">

            								<td style="text-indent: 20px"><input name="${fieldNameC}"
            									value="${childConcept.conceptId}"
            									class="child_${labOrder.parentConcept.conceptId}"
            									id="child_${childConcept.conceptId}" type="checkbox" checked="true"> <c:out
            									value="${childConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>

            								<td style="text-indent: 20px"><input name="${fieldNameC}"
            									value="${childConcept.conceptId}"
            									class="child_${labOrder.parentConcept.conceptId}"
            									id="child_${childConcept.conceptId}" type="checkbox"> <c:out
            									value="${childConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>
            							</tr>
            						</c:forEach>
            					</c:forEach>
            				</table>
            			</c:if>

		</c:forEach></td>
		<!-- /First Column -->

		<!-- Second Column -->
		<td valign="top" width="20%"><c:forEach var="labOrderParent"
			items="${model.labOrderparList}">
			<c:set var="gdParentConcept"
				value="${labOrderParent.grandFatherConcept}" />
			<c:set var="parentConcepts" value="${labOrderParent.labTests}" />

			<!-- first column -->

			<c:if test="${gdParentConcept.name.name eq bacteriology }">
				<table>
					<tr>
						<td><b>${gdParentConcept.name.name}</b></td>
					</tr>
					<c:forEach var="labOrder" items="${parentConcepts}"
						varStatus="status">
						<c:set var="childrenConcepts" value="${labOrder.childrenConcept}" />

						<c:set var="parentConcept" value="${labOrder.parentConcept}" />
						<c:set var="fieldNameP"
							value="lab-${gdParentConcept}-${labOrder.parentConcept.conceptId}" />

						<c:if test="${parentConcept.name.name eq 'Spermogram'}">
							<tr>
<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == labOrder.parentConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">

								<td><input name="${fieldNameP}"
									value="${labOrder.parentConcept.conceptId}" type="checkbox"
									id="parent_${labOrder.parentConcept.conceptId}" class="parent" checked="true"><c:out
									value="${labOrder.parentConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>

								<td><input name="${fieldNameP}"
									value="${labOrder.parentConcept.conceptId}" type="checkbox"
									id="parent_${labOrder.parentConcept.conceptId}" class="parent"><c:out
									value="${labOrder.parentConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>
							</tr>

						</c:if>
						<c:if test="${parentConcept.name.name != 'Spermogram'}">
							<tr>

<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == labOrder.parentConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">

								<td><input name="${parentConcept.conceptId}"
									value="${labOrder.parentConcept.conceptId}" type="checkbox"
									id="parent_${labOrder.parentConcept.conceptId}" class="parent" checked="true"><c:out
									value="${labOrder.parentConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>

								<td><input name="${parentConcept.conceptId}"
									value="${labOrder.parentConcept.conceptId}" type="checkbox"
									id="parent_${labOrder.parentConcept.conceptId}" class="parent"><c:out
									value="${labOrder.parentConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>

							</tr>
							<c:forEach var="childConcept" items="${childrenConcepts}"
								varStatus="status">
								<c:set var="fieldNameC"
									value="${fieldNameP}-${childConcept.conceptId}" />
								<tr>

<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == childConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">

									<td style="text-indent: 20px"><input name="${fieldNameC}"
										value="${childConcept.conceptId}"
										class="child_${labOrder.parentConcept.conceptId}"
										id="child_${childConcept.conceptId}" type="checkbox" checked="true">
									<c:out value="${childConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>

									<td style="text-indent: 20px"><input name="${fieldNameC}"
										value="${childConcept.conceptId}"
										class="child_${labOrder.parentConcept.conceptId}"
										id="child_${childConcept.conceptId}" type="checkbox">
									<c:out value="${childConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>
								</tr>
							</c:forEach>
						</c:if>
					</c:forEach>

				</table>
			</c:if>



			<c:if test="${gdParentConcept.name.name eq hemostasis}">
				<table>
					<tr>
						<td><b>${gdParentConcept.name.name}</b></td>
					</tr>
					<c:forEach var="labOrder" items="${parentConcepts}"
						varStatus="status">
						<c:set var="childrenConcepts" value="${labOrder.childrenConcept}" />
						<c:set var="parentConcept" value="${labOrder.parentConcept}" />
						<c:set var="fieldNameP"
							value="lab-${gdParentConcept}-${labOrder.parentConcept.conceptId}" />
						<tr>


<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == parentConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">

							<td><input name="${fieldNameP}"
								value="${parentConcept.conceptId}" type="checkbox"
								id="parent_${labOrder.parentConcept.conceptId}" class="parent" checked="true"><c:out
								value="${labOrder.parentConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>

							<td><input name="${fieldNameP}"
								value="${parentConcept.conceptId}" type="checkbox"
								id="parent_${labOrder.parentConcept.conceptId}" class="parent"><c:out
								value="${labOrder.parentConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>


						</tr>

					</c:forEach>
				</table>
			</c:if>





		</c:forEach></td>
		<!-- Second Column -->

		<!-- Third Column -->
		<td valign="top" width="20%"><c:forEach var="labOrderParent"
			items="${model.labOrderparList}">
			<c:set var="gdParentConcept"
				value="${labOrderParent.grandFatherConcept}" />
			<c:set var="parentConcepts" value="${labOrderParent.labTests}" />


			<!-- first column -->
			<c:if test="${gdParentConcept.name.name eq immunoSerology}">
				<table>
					<tr>
						<td><b>${gdParentConcept.name.name}</b></td>
					</tr>
					<c:forEach var="labOrder" items="${parentConcepts}"
						varStatus="status">
						<c:set var="childrenConcepts" value="${labOrder.childrenConcept}" />

						<c:set var="parentConcept" value="${labOrder.parentConcept}" />
						<c:set var="fieldNameP"
							value="lab-${gdParentConcept}-${labOrder.parentConcept.conceptId}" />
						<tr>

<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == parentConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">

							<td><input name="${fieldNameP}"
								value="${parentConcept.conceptId}" type="checkbox"
								id="parent_${labOrder.parentConcept.conceptId}" class="parent" checked="true">
							<c:out value="${labOrder.parentConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>

							<td><input name="${fieldNameP}"
								value="${parentConcept.conceptId}" type="checkbox"
								id="parent_${labOrder.parentConcept.conceptId}" class="parent">
							<c:out value="${labOrder.parentConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>


						</tr>
						<c:forEach var="childConcept" items="${childrenConcepts}"
							varStatus="status">
							<c:set var="fieldNameC"
								value="${fieldNameP}-${childConcept.conceptId}" />
							<tr>


<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == childConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">
								<td style="text-indent: 20px"><input name="${fieldNameC}"
									value="${childConcept.conceptId}"
									class="child_${labOrder.parentConcept.conceptId}"
									id="child_${childConcept.name}" type="checkbox" checked="true"> <c:out
									value="${childConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>
								<td style="text-indent: 20px"><input name="${fieldNameC}"
									value="${childConcept.conceptId}"
									class="child_${labOrder.parentConcept.conceptId}"
									id="child_${childConcept.name}" type="checkbox"> <c:out
									value="${childConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>

							</tr>
						</c:forEach>
					</c:forEach>
				</table>
			</c:if>
             		<!-- tumourMarker Section -->

             <c:if test="${gdParentConcept.name.name eq  tumourMarker}">
             				<table>
             					<tr>
             						<td><b>${gdParentConcept.name.name}</b></td>
             					</tr>
             					<c:forEach var="labOrder" items="${parentConcepts}"
             						varStatus="status">

             						<c:set var="childrenConcepts" value="${labOrder.childrenConcept}" />
             						<c:set var="parentConcept" value="${labOrder.parentConcept}" />
             						<c:set var="fieldNameP"
             							value="lab-${gdParentConcept}-${labOrder.parentConcept.conceptId}" />

             						<tr>

<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == labOrder.parentConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">
             							<td><input name="${fieldNameP}"
             								value="${labOrder.parentConcept.conceptId}" type="checkbox"
             								id="parent_${labOrder.parentConcept.conceptId}" class="parent" checked="true"><c:out
             								value="${labOrder.parentConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>
             							<td><input name="${fieldNameP}"
             								value="${labOrder.parentConcept.conceptId}" type="checkbox"
             								id="parent_${labOrder.parentConcept.conceptId}" class="parent"><c:out
             								value="${labOrder.parentConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>
             						</tr>
             						<c:forEach var="childConcept" items="${childrenConcepts}"
             							varStatus="status">
             							<c:set var="fieldNameC"
             								value="${fieldNameP}-${childConcept.conceptId}" />
             							<tr>


<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == childConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">
             								<td style="text-indent: 20px"><input name="${fieldNameC}"
             									value="${childConcept.conceptId}"
             									class="child_${labOrder.parentConcept.conceptId}"
             									id="child_${childConcept.conceptId}" type="checkbox" checked="true"> <c:out
             									value="${childConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>
             								<td style="text-indent: 20px"><input name="${fieldNameC}"
             									value="${childConcept.conceptId}"
             									class="child_${labOrder.parentConcept.conceptId}"
             									id="child_${childConcept.conceptId}" type="checkbox"> <c:out
             									value="${childConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>
             							</tr>
             						</c:forEach>
             					</c:forEach>
             				</table>
             			</c:if>



		</c:forEach></td>



		<!-- third Column -->

		<!-- fourth column -->

		<td valign="top" width="20%"><c:forEach var="labOrderParent"
			items="${model.labOrderparList}">
			<c:set var="gdParentConcept"
				value="${labOrderParent.grandFatherConcept}" />
			<c:set var="parentConcepts" value="${labOrderParent.labTests}" />

			<c:if test="${gdParentConcept.name.name eq bloodChemistry}">
				<table>
					<tr>
						<td><b>${gdParentConcept.name.name}</b></td>
					</tr>
					<c:forEach var="labOrder" items="${parentConcepts}"
						varStatus="status">
						<c:set var="childrenConcepts" value="${labOrder.childrenConcept}" />

						<c:set var="parentConcept" value="${labOrder.parentConcept}" />
						<c:set var="fieldNameP"
							value="lab-${gdParentConcept}-${labOrder.parentConcept.conceptId}" />
						<tr>


<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == parentConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">
							<td><input name="${fieldNameP}"
								value="${parentConcept.conceptId}" type="checkbox"
								id="${labOrder.parentConcept.name}_${labOrder.parentConcept.conceptId}"
								class="parent" checked="true"><c:out
								value="${labOrder.parentConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>
							<td><input name="${fieldNameP}"
								value="${parentConcept.conceptId}" type="checkbox"
								id="${labOrder.parentConcept.name}_${labOrder.parentConcept.conceptId}"
								class="parent"><c:out
								value="${labOrder.parentConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>

						</tr>
						<c:forEach var="childConcept" items="${childrenConcepts}"
							varStatus="status">
							<c:set var="fieldNameC"
								value="${fieldNameP}-${childConcept.conceptId}" />
							<tr>


<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == childConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">
								<td style="text-indent: 20px"><input name="${fieldNameC}"
									value="${childConcept.conceptId}"
									class="parent_${labOrder.parentConcept.conceptId}"
									id="${childConcept.name}_${status.count}" type="checkbox" checked="true">
								<c:out value="${childConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>
								<td style="text-indent: 20px"><input name="${fieldNameC}"
									value="${childConcept.conceptId}"
									class="parent_${labOrder.parentConcept.conceptId}"
									id="${childConcept.name}_${status.count}" type="checkbox">
								<c:out value="${childConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>
							</tr>
						</c:forEach>
					</c:forEach>
				</table>
			</c:if>
		</c:forEach></td>

		<!-- fifth column -->

		<td valign="top" width="20%"><c:forEach var="labOrderParent"
			items="${model.labOrderparList}">
			<c:set var="gdParentConcept"
				value="${labOrderParent.grandFatherConcept}" />
			<c:set var="parentConcepts" value="${labOrderParent.labTests}" />

			<c:if test="${gdParentConcept.name.name eq toxicology }">
				<table>
					<tr>
						<td><b>${gdParentConcept.name.name}</b></td>
					</tr>
					<c:forEach var="labOrder" items="${parentConcepts}"
						varStatus="status">
						<c:set var="childrenConcepts" value="${labOrder.childrenConcept}" />

						<c:set var="parentConcept" value="${labOrder.parentConcept}" />
						<c:set var="fieldNameP"
							value="lab-${gdParentConcept}-${labOrder.parentConcept.conceptId}" />
						<tr>



<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == parentConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">
							<td><input name="${fieldNameP}"
								value="${parentConcept.conceptId}" type="checkbox"
								id="${labOrder.parentConcept.name}_${labOrder.parentConcept.conceptId}"
								class="parent" checked="true"><c:out
								value="${labOrder.parentConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>
							<td><input name="${fieldNameP}"
								value="${parentConcept.conceptId}" type="checkbox"
								id="${labOrder.parentConcept.name}_${labOrder.parentConcept.conceptId}"
								class="parent"><c:out
								value="${labOrder.parentConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>
						</tr>
						<c:forEach var="childConcept" items="${childrenConcepts}"
							varStatus="status">
							<c:set var="fieldNameC"
								value="${fieldNameP}-${childConcept.conceptId}" />
							<tr>


<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == childConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">
								<td style="text-indent: 20px"><input name="${fieldNameC}"
									value="${childConcept.conceptId}"
									class="parent_${labOrder.parentConcept.conceptId}"
									id="${childConcept.name}_${status.count}" type="checkbox" checked="true">
								<c:out value="${childConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>
								<td style="text-indent: 20px"><input name="${fieldNameC}"
									value="${childConcept.conceptId}"
									class="parent_${labOrder.parentConcept.conceptId}"
									id="${childConcept.name}_${status.count}" type="checkbox">
								<c:out value="${childConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>
							</tr>
						</c:forEach>
					</c:forEach>
				</table>
			</c:if>
            <c:if test="${gdParentConcept.name.name eq thyroidFunction }">
            				<table>
            					<tr>
            						<td><b>${gdParentConcept.name.name}</b></td>
            					</tr>
            					<c:forEach var="labOrder" items="${parentConcepts}"
            						varStatus="status">
            						<c:set var="childrenConcepts" value="${labOrder.childrenConcept}" />

            						<c:set var="parentConcept" value="${labOrder.parentConcept}" />
            						<c:set var="fieldNameP"
            							value="lab-${gdParentConcept}-${labOrder.parentConcept.conceptId}" />
            						<tr>


<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == parentConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">
            							<td><input name="${fieldNameP}"
            								value="${parentConcept.conceptId}" type="checkbox"
            								id="${labOrder.parentConcept.name}_${labOrder.parentConcept.conceptId}"
            								class="parent" checked="true"><c:out
            								value="${labOrder.parentConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>
            							<td><input name="${fieldNameP}"
            								value="${parentConcept.conceptId}" type="checkbox"
            								id="${labOrder.parentConcept.name}_${labOrder.parentConcept.conceptId}"
            								class="parent"><c:out
            								value="${labOrder.parentConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>
            						</tr>
            						<c:forEach var="childConcept" items="${childrenConcepts}"
            							varStatus="status">
            							<c:set var="fieldNameC"
            								value="${fieldNameP}-${childConcept.conceptId}" />
            							<tr>

<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == childConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">
            								<td style="text-indent: 20px"><input name="${fieldNameC}"
            									value="${childConcept.conceptId}"
            									class="parent_${labOrder.parentConcept.conceptId}"
            									id="${childConcept.name}_${status.count}" type="checkbox" checked="true">
            								<c:out value="${childConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>
            								<td style="text-indent: 20px"><input name="${fieldNameC}"
            									value="${childConcept.conceptId}"
            									class="parent_${labOrder.parentConcept.conceptId}"
            									id="${childConcept.name}_${status.count}" type="checkbox">
            								<c:out value="${childConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>
            							</tr>
            						</c:forEach>
            					</c:forEach>
            				</table>
            			</c:if>


            			<c:if test="${gdParentConcept.name.name eq fertilityHormone }">
            				<table>
            					<tr>
            						<td><b>${gdParentConcept.name.name}</b></td>
            					</tr>
            					<c:forEach var="labOrder" items="${parentConcepts}"
            						varStatus="status">
            						<c:set var="childrenConcepts" value="${labOrder.childrenConcept}" />

            						<c:set var="parentConcept" value="${labOrder.parentConcept}" />
            						<c:set var="fieldNameP"
            							value="lab-${gdParentConcept}-${labOrder.parentConcept.conceptId}" />
            						<tr>

<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == parentConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">
            							<td><input name="${fieldNameP}"
            								value="${parentConcept.conceptId}" type="checkbox"
            								id="${labOrder.parentConcept.name}_${labOrder.parentConcept.conceptId}"
            								class="parent" checked="true"><c:out
            								value="${labOrder.parentConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>
            							<td><input name="${fieldNameP}"
            								value="${parentConcept.conceptId}" type="checkbox"
            								id="${labOrder.parentConcept.name}_${labOrder.parentConcept.conceptId}"
            								class="parent"><c:out
            								value="${labOrder.parentConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>
            						</tr>
            						<c:forEach var="childConcept" items="${childrenConcepts}"
            							varStatus="status">
            							<c:set var="fieldNameC"
            								value="${fieldNameP}-${childConcept.conceptId}" />
            							<tr>

<c:set var="checkDisp" value="0" />
                            <c:forEach var="currDis" items="${model.labOrderCurrentlyDisplayed}" varStatus="status">
                                <c:if test="${currDis == childConcept.conceptId}">
                                    <c:set var="checkDisp" value="1" />
                                </c:if>
                            </c:forEach>
						<c:choose>
                           	<c:when test="${checkDisp == 1}">
            								<td style="text-indent: 20px"><input name="${fieldNameC}"
            									value="${childConcept.conceptId}"
            									class="parent_${labOrder.parentConcept.conceptId}"
            									id="${childConcept.name}_${status.count}" type="checkbox" checked="tue">
            								<c:out value="${childConcept.name}" /></td>
                            </c:when>
                            <c:otherwise>
            								<td style="text-indent: 20px"><input name="${fieldNameC}"
            									value="${childConcept.conceptId}"
            									class="parent_${labOrder.parentConcept.conceptId}"
            									id="${childConcept.name}_${status.count}" type="checkbox">
            								<c:out value="${childConcept.name}" /></td>
                            </c:otherwise>
						</c:choose>
            							</tr>
            						</c:forEach>
            					</c:forEach>
            				</table>
            			</c:if>

		</c:forEach></td>

	</tr>

</table>
<input type="submit" name="saveLabOrders" value="Change Lab Request Form"></div>

</form>
</div>
</fieldset>


