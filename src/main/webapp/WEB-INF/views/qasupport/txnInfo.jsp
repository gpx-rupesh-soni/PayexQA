<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id="main-0001" style="margin-top: 60px;">
	<form action="">
	<table>
	<tr>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 250px;">
			<h4>Transaction ID:</h4>
			<input type="text" id="payexPaymentId" name="payexPaymentId" 
						placeholder="Please enter Payex Payment Id" autocomplete="off" value="${ppid}"/>
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
		<c:if test="${not empty txnInfo}">
		<div class="" id="txn-info-1"
			style="margin-left: 40px; height: 20px; width: 80%;" align="left">
			<br>
			<br>
			<table class="table table-bordered" id="txn-table">
				<thead>
					<tr>
						<th>PPID</th>
						<th>CPG Code</th>
						<th>Retailer</th>
						<th>App. Date</th>
						<th>Pay Date</th>						
						<th>Txn Amt</th>						
						<th>CD Amt</th>
						<th>App. Amt</th>
						<th>CN Amt</th>
						<th>Bank Amt</th>
						<th>Mode</th>
						<th>Status</th>
						<th>Action</th>
					</tr>
				<thead>
				<tbody id="tableData">				
					
				<tr>	
					<td>${txnInfo.txn.payexPaymentId}</td>
					<td>${txnInfo.txn.payexCpgCode}</td>
					<td>${txnInfo.txn.payexRetailerCode}</td>
					<td>${txnInfo.txn.approvedOn}</td>
					<td>${txnInfo.txn.dateToPay}</td>					
					<td>${txnInfo.txn.amountPresented}</td>					
					<td>${txnInfo.txn.amountCd}</td>
					<td>${txnInfo.txn.amountApproved}</td>
					<td>${txnInfo.txn.amountCn}</td>
					<td>${txnInfo.txn.amountBank}</td>
					<td>${txnInfo.txn.paymentModeDesc}</td>	
					<td>${txnInfo.txn.paymentStatus}</td>
					<td><button id="btn-api-log" onclick="validateInvoiceTable();">validate</button></td>
				</tr>
				

				</tbody>
			</table>
			
			<table class="table table-bordered" id="inv-table">
				<thead>
					<tr>
						<th>Inv ID</th>
						<th>Inv No.</th>
						<th>Inv Date</th>
						<th>Due Date</th>
						<th>Inv Amt</th>
						<th>Due Amt</th>
						<th>Tax Amt</th>
						<th>App. Amt</th>						
						<th>CD Amt</th>
						<th>CD Tds</th>
						<th>Pay. Amt</th>
						<th>CN Amt</th>
						<th>Bank Amt</th>
						<th>Status</th>
					</tr>
				<thead>
				<tbody id="tableData">				
				<c:forEach var="inv" items="${txnInfo.inv}">	
				<tr>	
					<td>${inv.PAYEX_TRANSACTION_ID}</td>
					<td>${inv.CPG_INVOICE_NUMBER}</td>					
					<td>${inv.CPG_INCVOICE_DATE}</td>
					<td>${inv.DATE_TO_DUE}</td>					
					<td>${inv.CPG_INVOICE_AMOUNT}</td>
					<td>${inv.TOTAL_AMOUNT_DUE}</td>
					<td>${inv.TAXABLE_AMOUNT}</td>
					<td>${inv.AMOUNT_APPROVED}</td>					
					<td>${inv.AMOUNT_CD}</td>
					<td>${inv.AMOUNT_TDS}</td>
					<td>${inv.PAYABLE_AMOUNT}</td>
					<td>${inv.AMOUNT_CN}</td>
					<td>${inv.AMOUNT_BANK}</td>
					<td>${inv.PROCESS_STATUS}/${inv.ACTIVE_FLAG}</td>					
				</tr>
				</c:forEach>

				</tbody>
			</table>
			
			<c:if test="${not empty txnInfo.cn}">
				<table class="table table-bordered" id="inv-table">
				<thead>
					<tr>
						<th>CN ID</th>
						<th>CN No.</th>
						<th>CN Type</th>
						<th>CN Date</th>
						<th>Due Date</th>
						<th>CN Amt</th>
						<th>Due Amt</th>
						<th>Used Amt</th>
						<th>Part Pay</th>
						<th>Status</th>
					</tr>
				<thead>
				<tbody id="tableData">				
				<c:forEach var="cn" items="${txnInfo.cn}">	
				<tr>	
					<td>${cn.PAYEX_CN_ID}</td>
					<td>${cn.CPG_CN_ID}</td>
					<td>${cn.CN_TYPE}</td>					
					<td>${cn.CPG_STATRT_DATE}</td>
					<td>${cn.CPG_END_DATE}</td>					
					<td>${cn.CN_FIRST_BALANCE}</td>
					<td>${cn.CN_LAST_BALANCE}</td>
					<td>${cn.AMOUNT_USED}</td>					
					<td>${cn.IS_PARTIALLY_USED}</td>
					<td>${cn.CN_STATUS}</td>					
				</tr>
				</c:forEach>

				</tbody>
				</table>				
			</c:if>
			
			<button id="btn-api-log" onclick="handleAction('api-log', ${ppid})">apiLog</button>
			<button id="btn-others"onclick="handleAction('other-info', ${ppid})">Other Info</button>	
			<table class="table table-bordered" id="log-table" style="display: none;">
				<thead><tr><th>Log id</th><th>Request</th><th>Response</th></tr></thead>
				<tbody id="apiLog-tableData"></tbody>
		</table>		
		</div>		
		</c:if>
</div>


<script type="text/javascript">

function handleAction(action, ppid){
	alert(action);
	if("api-log"===action){
		alert(action);
		getApiLog(action,ppid);
	}
}

function handleResponse(action, response){	
	alert("handleResponse called : "+JSON.stringify(response));
	if("api-log"===action){
		showApiLog(response);
	}
}

var response;
function getApiLog(action,ppid){
	var postData = {
			action:action,
			ppid:ppid
	    };	
	getInvoiceInfo(postData);
}

function showApiLog(response) {
	alert("handleResponse called : "+JSON.stringify(response.apiLog));
	var tableRow = "<tr class="+'"'+response.apiLog.id+'"'+">"			
			+ "<td>"+ JSON.stringify(response.apiLog.req) +"</td>"
			+ "<td>"+ JSON.stringify(response.apiLog.res) +"</td>"
			+ "</tr>";
	console.log(tableRow);
	$(tableRow).appendTo('#apiLog-tableData');
	$("#log-table").show();
}


function getInvoiceInfo(postData,action){
	
	// ---- POST call ----
    $.ajax({
        url: "",
        type: "POST",
        data: postData,
        dataType: "json",
        success: function (response) {
            // Handle success response
            console.log(response);            
            handleResponse(postData.action, response);           
        },
        error: function (xhr, status, error) {
            alert("Server error: " + error);
            $("#retInfo").hide();
        }
    });	
	
	}
	
function validateInvoiceTable() {

    const table = document.getElementById("inv-table");
    const rows = table.querySelectorAll("tbody tr");

    rows.forEach(row => {

        // column indexes based on your table
        const appAmt  = parseFloat(row.cells[7].innerText)  || 0;
        const cdAmt   = parseFloat(row.cells[8].innerText)  || 0;
        const tdsAmt  = parseFloat(row.cells[9].innerText)  || 0;
        const payAmt  = parseFloat(row.cells[10].innerText) || 0;
        const cnAmt   = parseFloat(row.cells[11].innerText) || 0;
        const bankAmt = parseFloat(row.cells[12].innerText) || 0;

        const expectedPayAmt  = appAmt - cdAmt - tdsAmt;
        const expectedBankAmt = appAmt - cdAmt - tdsAmt - cnAmt;

        const payCell = row.cells[10];
        const bankCell = row.cells[12];

        // validate Pay Amount
        if (Math.abs(payAmt - expectedPayAmt) < 0.01) {
            payCell.style.backgroundColor = "#90EE90"; // green
        } else {
            payCell.style.backgroundColor = "#FF7F7F"; // red
        }

        // validate Bank Amount
        if (Math.abs(bankAmt - expectedBankAmt) < 0.01) {
            bankCell.style.backgroundColor = "#90EE90"; // green
        } else {
            bankCell.style.backgroundColor = "#FF7F7F"; // red
        }

    });
}
</script>