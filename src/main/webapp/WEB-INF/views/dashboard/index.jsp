<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container-fluid" style="margin-top: 40px;">	
	<br>
	<br>
	<div class="clearfix"></div>
	
	<ul class="nav nav-tabs">
		<li <c:if test="${tab == 'd'}">class="active"</c:if>><a href="?t=d">Invoices Due</a></li>		
		<li <c:if test="${tab == 'c'}">class="active"</c:if>><a href="?t=c">Invoices of CPG</a></li>
		<li <c:if test="${tab == 'r'}">class="active"</c:if>><a href="?t=r">Invoices of Retailer</a></li>
	</ul>
	<div class="clearfix"></div>
	
	<c:if test="${tab == 'd'}">	
	<%-- <jsp:include page="dues_list.jsp"></jsp:include> --%>
	</c:if>
		
	<c:if test="${tab.equals('c')}">	
	<%-- <jsp:include page="cpg_list.jsp"></jsp:include> --%>
	</c:if>
	
	<c:if test="${tab == 'r'}">	
	<%-- <jsp:include page="retailer_list.jsp"></jsp:include> --%>
	</c:if>


</div>
