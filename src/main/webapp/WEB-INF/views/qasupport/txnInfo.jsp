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
						placeholder="Comma separated Payex Payment Ids" autocomplete="off" value="${ppid}"/>
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
		<c:forEach var="txnInfo" items="${txnInfos}">
		<div class="txn-block"
			style="margin-left: 40px; width: 80%;" align="left">
			<br>
			<h4>PPID : ${txnInfo.ppid}</h4>
			<c:choose>
			<c:when test="${empty txnInfo.txn}">
				<p class="text-danger">No transaction found for PPID ${txnInfo.ppid}</p>
			</c:when>
			<c:otherwise>
			<table class="table table-bordered txn-table">
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
				</thead>
				<tbody>
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
					<td><button onclick="validateInvoiceTable(this);">validate</button></td>
				</tr>
				</tbody>
			</table>
			
			<table class="table table-bordered inv-table">
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
				</thead>
				<tbody>				
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
				<table class="table table-bordered cn-table">
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
				</thead>
				<tbody>				
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
			
			<button onclick="handleAction('api-log', ${txnInfo.ppid}, this)">apiLog</button>
			<button onclick="handleAction('other-info', ${txnInfo.ppid}, this)">Other Info</button>	
			<table class="table table-bordered log-table" style="display: none;">
				<thead><tr><th>Log id</th><th>Request</th><th>Response</th></tr></thead>
				<tbody class="apiLog-tableData"></tbody>
			</table>
			</c:otherwise>
			</c:choose>
			<hr/>
		</div>
		</c:forEach>
</div>


<script type="text/javascript">

function handleAction(action, ppid, element){
	if("api-log"===action){
		getApiLog(action, ppid, $(element).closest(".txn-block"));
	}
}

function getApiLog(action, ppid, block){
	var postData = {
			action:action,
			payexPaymentId:ppid
	    };
	getTxnOtherInfo(postData, function (response) {
		showApiLog(response, block);
	});
}

function showApiLog(response, block) {
	if(!response.apiLog){
		alert("No API log found");
		return;
	}
	var tableRow = $("<tr/>")
			.append($("<td/>").text(response.apiLog.id))
			.append($("<td/>").text(JSON.stringify(response.apiLog.req)))
			.append($("<td/>").text(JSON.stringify(response.apiLog.res)));
	block.find(".apiLog-tableData").empty().append(tableRow);
	block.find(".log-table").show();
}

function getTxnOtherInfo(postData, onSuccess){
	
	// ---- POST call ----
    $.ajax({
        url: "",
        type: "POST",
        data: postData,
        dataType: "json",
        success: function (response) {
            console.log(response);            
            onSuccess(response);
        },
        error: function (xhr, status, error) {
            alert("Server error: " + error);
        }
    });	
	
	}
	
function validateInvoiceTable(element) {

    // invoices of the transaction whose validate was clicked
    const table = element.closest(".txn-block").querySelector(".inv-table");
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