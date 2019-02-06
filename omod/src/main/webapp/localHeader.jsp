<h2><spring:message code="laboratorymanagement.title" /></h2>

<ul id="menu">
	<!-- 
	<li
		class="first<c:if test='<%= request.getRequestURI().contains("laboratorymanagementForm") %>'>active</c:if>">
	<a
		href="${pageContext.request.contextPath}/module/laboratorymanagement/laboratorymanagementLink.form"><spring:message
		code="laboratorymanagement.addOrder" /></a></li> -->


	<li
		class="first<c:if test='<%= request.getRequestURI().contains("viewLabOrder") %>'>active</c:if>">
	<a
		href="${pageContext.request.contextPath}/module/laboratorymanagement/viewLabOrder.form"><spring:message
		code="laboratorymanagement.viewLabOrder" /></a></li>

	<li
		class="<c:if test='<%= request.getRequestURI().contains("addResult") %>'>active</c:if>"><a
		href="${pageContext.request.contextPath}/module/laboratorymanagement/addResult.form"><spring:message
		code="laboratorymanagement.addResult" /></a></li>
	<li
		class="<c:if test='<%= request.getRequestURI().contains("testStatistics") %>'>active</c:if>"><a
		href="${pageContext.request.contextPath}/module/laboratorymanagement/testStatistics.form"><spring:message
		code="laboratorymanagement.viewStatistics" /></a></li>
	<li
		class="<c:if test='<%= request.getRequestURI().contains("patientReport") %>'>active</c:if>"><a
		href="${pageContext.request.contextPath}/module/laboratorymanagement/patientReport.form"><spring:message
		code="laboratorymanagement.patientReport" /></a></li>
	<li
		class="<c:if test='<%= request.getRequestURI().contains("monthlyReport") %>'>active</c:if>"><a
		href="${pageContext.request.contextPath}/module/laboratorymanagement/monthlyReport.form"><spring:message
		code="laboratorymanagement.ViewMonthlyReport" /></a></li>
	<openmrs:hasPrivilege privilege="View Lab configuration">
		<li
			class="<c:if test='<%= request.getRequestURI().contains("labConfiguration") %>'>active</c:if>"><a
			href="${pageContext.request.contextPath}/module/laboratorymanagement/labConfiguration.form"><spring:message
			code="laboratorymanagement.labConfiguration" /></a></li>
	</openmrs:hasPrivilege>

</ul>