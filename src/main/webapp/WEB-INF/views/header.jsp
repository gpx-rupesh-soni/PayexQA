<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  


<nav class="navbar navbar-inverse navbar-fixed-top "> 
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#myNavbar">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="<c:url value="/"/>">Global PayEX Pvt. Ltd.</a>
		</div>
		<div class="collapse navbar-collapse" id="myNavbar" style="font-size: 13px;">
			<ul class="nav navbar-nav">				
				<c:choose>
				
					<c:when test="${not empty role}">	
						<c:if test="${op.contains('DEVQA')}">
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Retailer<span class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="<c:url value="/qasupport/update_retailer"/>">Update Retailer</a></li>
							</ul>
						</li>
						
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Ledger<span class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="<c:url value="/qasupport/genInvData"/>">Generate Ledger Data</a></li>
							</ul>
						</li>
						
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Transaction<span class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="<c:url value="/qasupport/txn-info"/>">Transaction Details</a></li>
								<li><a href="<c:url value="/qasupport/update-txn"/>">Update Transaction</a></li>
								<li><a href="<c:url value="/qasupport/update-doc"/>">Update Document</a></li>
							</ul>
						</li>
						
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Payments<span class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="<c:url value="/qasupport/update-nach"/>">Update NACH</a></li>
								<li><a href="<c:url value="/qasupport/neft-payment"/>">NEFT Payment</a></li>
							</ul>
						</li>
						
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Data Import<span class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="<c:url value="/qasupport/update_retailer"/>">Update Retailer</a></li>
								<li><a href="<c:url value="/qasupport/update-txn"/>">Update Transaction</a></li>
								<li><a href="<c:url value="/qasupport/update-nach"/>">Update NACH</a></li>
								<li><a href="<c:url value="/qasupport/update-doc"/>">Update Document</a></li>
								<li><a href="<c:url value="/qasupport/neft-payment"/>">NEFT Payment</a></li>
							</ul>
						</li>						
						</c:if> 			
					</c:when>
				</c:choose>
			</ul>
			<c:choose>
				<c:when test="${not empty user}">
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#"><span
								class="glyphicon glyphicon-user"></span> ${user.fname} <span
								class="caret"></span></a>
							<ul class="dropdown-menu">							
								<li><a href="<c:url value="/logout"/>"><span
										class="glyphicon glyphicon-log-out"></span> Logout</a></li>
							</ul></li>
					</ul>					
				</c:when>
				<c:otherwise>
					<ul class="nav navbar-nav navbar-right">						
						<li><a href="<c:url value="/login"/>"><span
								class="glyphicon glyphicon-log-in"></span> Login</a></li>
					</ul>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</nav>
