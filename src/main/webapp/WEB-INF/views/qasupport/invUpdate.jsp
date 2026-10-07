<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>QA-Updaste Transaction</title>
</head>
<body>
	<div id="main-0001" style="margin-top: 60px;">
	<form action="">
	<table>
	<tr>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 200px;">
			<h4>Doc Type:</h4>
			<select id="docType" name="docType" class="form-control"
				style="border: 0; width: 100%;" >
				<option ${empty docType ? 'selected' : ''} disabled>-------select Doc type-------</option>
				<option value="inv" ${docType == 'inv' ? 'selected' : ''}>INV</option>
				<option value="dn" ${docType == 'dn' ? 'selected' : ''}>Debit Note</option>
				<option value="cn" ${docType == 'cn' ? 'selected' : ''}>Credit Note</option>
			</select>
			</div>
	</td>
	<td>
		<div id="op-header"
			style="margin-left: 40px; height: 40px; width: 200px;">
			<h4>CPG Code:</h4>
			<select id="payexCpgCode" name="payexCpgCode" class="form-control"
				style="border: 0; width: 100%;">
				<option ${empty payexCpgCode ? 'selected' : ''} disabled>-------select CPG Code-------</option>
				<option value="CPGVGUARD" ${payexCpgCode == 'CPGVGUARD' ? 'selected' : ''}>CPGVGUARD</option>
				<option value="CPGBRGSTN" ${payexCpgCode == 'CPGBRGSTN' ? 'selected' : ''}>CPGBRGSTN</option>
				<option value="CPGCROM" ${payexCpgCode == 'CPGCROM' ? 'selected' : ''}>CPGCROM</option>
				<option value="CPGMRLS" ${payexCpgCode == 'CPGMRLS' ? 'selected' : ''}>CPGMRLS</option>
			</select>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 250px;">
			<h4>Retailer Code:</h4>
			<input type="text" id="payexRetailerCode" name="payexRetailerCode" value="${payexRetailerCode}"
						placeholder="Please enter Payex Retailer Code" autocomplete="off"/>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 250px;">
			<h4>Invoice Number:</h4>
			<input type="text" id="cpgInvoiceNumber" name="cpgInvoiceNumber" value="${cpgInvoiceNumber}"
						placeholder="Please enter Invoice Number" autocomplete="off"/>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 250px;">
			<h4>Action : </h4>
			<input type="submit" id="submit_search" name="submit_search" value="Search"/>
			</div>
	</td>
	</tr>
	</table>
	</form>

		<div class=""
			style="margin-left: 40px; height: 20px; width: 80%;" align="left">
			<br>
			<br>
			<table class="table table-bordered" id="table">
				<thead>
					<tr>
						<th>Transaction ID</th>
						<th>Status</th>
						<th>Retailer Code</th>
						<th>Invoice Number</th>
						<th>Invoice Amount</th>
						<th>Due Amount</th>
						<th>Approved Amount</th>						
						<th>CN</th>
						<th>CD</th>
						<th>Bank Amount</th>
						<th>CD Details</th>
						<th>Action</th>
					</tr>
				<thead>
				<tbody id="tableData">
				
				<c:forEach var="txn" items="${txns}">	
				<tr>	
					<td>${txn.PAYEX_TRANSACTION_ID}</td>
					<td>${txn.PROCESS_STATUS}</td>
					<td>${txn.PAYEX_RETAILER_CODE}</td>
					<td>${txn.CPG_INVOICE_NUMBER}</td>
					<td>${txn.CPG_INVOICE_AMOUNT}</td>					
					<td>${txn.TOTAL_AMOUNT_DUE}</td>
					<td>${txn.AMOUNT_APPROVED}</td>					
					<td>${txn.AMOUNT_CN}</td>
					<td>${txn.AMOUNT_CD}</td>					
					<td>${txn.AMOUNT_BANK}</td>					
					<td>${txn.APPROVED_CD_DETAILS}</td>
					<td>
					<div class="dropdown">
				        <button class="btn btn-light btn-sm dropdown-toggle"
				                data-bs-toggle="dropdown">
				            Actions
				        </button>
				
				        <ul class="dropdown-menu">
				            <li><a class="dropdown-item" onclick="handleAction('add-part-outstanding', '${txn.PAYEX_TRANSACTION_ID}',this)">Add part outstanding</a></li>
				        </ul>
				    </div>
					</td>			
					
					
				</tr>
				
			</c:forEach>
				

				</tbody>
			</table>
		</div>	

</div>



</body>
<script type="text/javascript">

function handleAction(action, id, element) {
           
    if (confirm('Are you sure you want to '+action+'?')) {
    	alert("1111");
    	alert("action : "+action);
 	  	if(action=="add-part-outstanding"){
 	  		alert("2222");
 	  		var postData = {
 	  				action:action,
 	  				pti:id
 	  		    };
 	  		
 	  		addPartOutstanding(postData,action);
		}
    }    
}

function addPartOutstanding(postData,action){
	alert("3333");
	// ---- POST call ----
    $.ajax({
        url: "",
        type: "POST",
        data: postData,
        dataType: "json",
        success: function (response) {
            // Handle success response
            console.log(response);            
            //handleResponse(postData.action, response); 
            alert(data.data);
        },
        error: function (xhr, status, error) {
            alert("Server error: " + error);
            $("#retInfo").hide();
        }
    });	
	
}

</script>
</html>