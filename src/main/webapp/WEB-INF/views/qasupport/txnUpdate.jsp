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
			style="margin-left: 40px; height: 40px; width: 250px;">
			<h4>Transaction Type:</h4>
			<select id="txnType" name="txnType" class="form-control"
				style="border: 0; width: 100%;" name="operation"
				onchange="getOperation();">
				<option disabled selected>-------select Transaction type-------</option>
				<option value="approved-nach">Approved - NACH</option>
				<option value="approved-neft">Approved - NEFT</option>
				<option value="failed">Failed</option>
				<option value="paid">Paid</option>
			</select>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 250px;">
			<h4>CPG Code:</h4>
			<input type="text" id="payexCpgCode" name="payexCpgCode" 
						placeholder="Please enter CPG Code" autocomplete="off"/>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 250px;">
			<h4>Retailer Code:</h4>
			<input type="text" id="payexRetailerCode" name="payexRetailerCode" 
						placeholder="Please enter Retailer Code" autocomplete="off"/>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 250px;">
			<h4>Transaction ID:</h4>
			<input type="text" id="payexPaymentId" name="payexPaymentId" 
						placeholder="Please enter Payex Payment Id" autocomplete="off"/>
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
				<tbody id="tableData">
				
				<c:forEach var="txn" items="${txns}">	
				<tr>	
					<td>${txn.payexPaymentId}</td>
					<td>${txn.payexCpgCode}</td>
					<td>${txn.payexRetailerCode}</td>
					<td>${txn.approvedOn}</td>
					<td>${txn.dateToPay}</td>
					<td>${txn.amountPresented}</td>
					<td>${txn.amountCd}</td>
					<td>${txn.amountApproved}</td>
					<td>${txn.amountCn}</td>
					<td>${txn.amountBank}</td>
					<td>${txn.paymentModeDesc}</td>
					<td>${txn.paymentStatus}</td>
					<td>
					<div class="dropdown">
				        <button class="btn btn-light btn-sm dropdown-toggle"
				                data-bs-toggle="dropdown">
				            Actions
				        </button>
				
				        <ul class="dropdown-menu">
				            <li><a class="dropdown-item" onclick="handleAction('paid', '${txn.payexPaymentId}',this)">Paid</a></li>
				            <li><a class="dropdown-item" onclick="handleAction('failed', '${txn.payexPaymentId}',this)">Failed</a></li>
				            <li><a class="dropdown-item text-danger" onclick="handleAction('pending', '${txn.payexPaymentId}',this)">Pending</a></li>
				        </ul>
				    </div>
					</td>			
					
					
				</tr>
				
			</c:forEach>
				

				</tbody>
			</table>
		</div>

		<div id="R2P"
			style="margin-left: 40px; height: 50px; width: 200px; background: red; display: none;">
			<b>Reject to Pending view</b>
		</div>

		<div id="P2R"
			style="margin-left: 40px; height: 50px; width: 200px; background: green; display: none;">
			<b>Pending to Reject view</b>
		</div>

</div>



</body>
<script type="text/javascript">

function handleAction(action, id, element) {
           
    if (confirm('Are you sure you want to '+action+'?')) {
 	   updateRecord(action,id, element);
    }    
}

function updateRecord(action,id, element) {    
    fetch(window.location.href, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: id,
            action: action.toUpperCase()
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Server error : ' + response.status + ' ' + response.statusText);
        }
        return response.json(); // { action, msg, data : { counts... }, success }
    })
    .then(data => {
    	console.log(data);
        alert(formatActionResponse(id, data));
        if (data.success) {
            removeTableRow(element);
        }
    })
    .catch(error => {
        console.error(error);
        alert(action.toUpperCase() + ' failed for Payment ID ' + id + '\n' + error.message);
    });
}

/** the action response as readable lines : message, then each value of data (update counts etc.) */
function formatActionResponse(id, res) {
    var lines = [(res.action || '') + ' : Payment ID ' + id,
                 res.msg || (res.success ? 'Action completed.' : 'Action failed on server.')];
    if (res.data && typeof res.data === 'object') {
        lines.push('');
        for (var key in res.data) {
            var value = res.data[key];
            lines.push(key + ' : ' + (value !== null && typeof value === 'object' ? JSON.stringify(value) : value));
        }
    } else if (res.data !== undefined && res.data !== null) {
        lines.push('', String(res.data));
    } else if (res.success) {
        lines.push('', 'No data returned for this action.');
    }
    return lines.join('\n');
}

function removeTableRow(element) {
    const row = element.closest('tr');
    if (row) {
        row.remove();
    }
}

function downloadRecord(id) {
    alert('Download record ' + id);
}

function getOperation(){
	
	var url="../ta/index";
	var drop1=document.getElementById("operation");
	var selectedString=drop1.options[drop1.selectedIndex].value;
	$('#doc-search').hide();
	$('#R2P').hide();
	$('#P2R').hide();
	$('#F2P').hide();
	
	
	switch (selectedString) {
	case "f2p":
		$('#doc-search').show();
		$('#ta_search_text').val("");
		$('#tableData').empty();
			/* $('#F2P').show(); */
		break;
		
	case "r2p":		
		$('#doc-search').show();
		$('#ta_search_text').val("");
		$('#tableData').empty();
		/* $('#R2P').show(); */
		break;
		
	case "p2r":
		$('#doc-search').show();
		$('#ta_search_text').val("");
		$('#tableData').empty();
		/* $('#P2R').show(); */
		break;
	case "s2p":
		$('#doc-search').show();
		$('#ta_search_text').val("");
		$('#tableData').empty();
		
		break;	

	default:
		break;
	}
}


	function getInvoiceInfo() {
		if (!$("#ta_search_text").val()) {
			alert("Please enter Invoice Number");
			return false;
		} else {
			var opera=$("#operation").val();
			var invoiceNumberr = $("#ta_search_text").val();	
			console.log(invoiceNumberr);
			data = {ta_search_text:invoiceNumberr, operat:opera };
		
			$.ajax({
				type : "POST",
				url : "search",
				dataType : "json",
				data : data,
				
				success : function (data) {
					$('#tableData').empty();
					if (data === null || data.length<1) {
						alert("no invoices found");
					} else {
						$('#F2P').show();
								showInvoicesData(data);
							
						}
					
				},
				error : function() {
					alert("No invoice data found.");
				}
			});
		}

	}

	
	function showInvoicesData(data) {
		var opera=$("#operation").val().toUpperCase();
		$.each(data, function(key, value) {
			//var counter=i++;
			var tableRow = "<tr class="+'"'+value.PAYEX_PAYMENT_ID+'"'+">"
					+ "<td>"+ value.PAYEX_PAYMENT_ID +"</td>"
					+ "<td>"+ value.RETAILER_CODE +"</td>"
					+ "<td>"+ value.transactionId +"</td>"
					+ "<td>"+ value.invoiceNo +"</td>"
					+ "<td>"+ value.CPG_INVOICE_AMOUNT +"</td>"
					+ "<td>"+ value.amountPresented +"</td>"
					+ "<td>"+ "<button class="+ '"btn btn-primary"'+ "onclick="+ '"moveToChecker(this,'+ value.PAYEX_PAYMENT_ID + ','+ value.transactionId +');">'+opera+"</button>" + "</td>"
					+ "</tr>";

			console.log(tableRow);

			$(tableRow).appendTo('#tableData');
		})
	}

	/* function moveToPending(that, payexPaymentId, payexTransactionId) {
		var operation=$("#operation").val();
		if(operation==="f2p"){
		var x = confirm("Do you want to move This transaction(Payment ID:"
				+ payexPaymentId
				+ ") to pending state.\n All transaction associated with these will go into pending state.");
		}
		else if(operation==="r2p"){
			var x = confirm("Do you want to move This transaction(Transaction ID:"
					+ payexTransactionId
					+ ") to pending state.\n All transaction associated with these will go into pending state.");
		}
		else{
			var x = confirm("Do you want to move This transaction(Transaction ID:"
					+ payexTransactionId
					+ ") to reject state.\n All transaction associated with these will go into reject state.");
		}
		if (x) {
			var url = "moveToPending";
			
			var data = {
				"payexPaymentId" : payexPaymentId,"payexTransactionId": payexTransactionId,"operation": operation
			};
			$
					.ajax({
						type : "POST",
						url : url,
						dataType : "json",
						//contentType : "application/json",
						data : data,
						success : function(response) {
							console.log("SUCCESS " + response)
							var responseFromServer = JSON.parse(JSON
									.stringify(response));
							if (responseFromServer.status) {
								if(operation==="f2p"){
								alert("Your invoice file moved to pending")
								}else if(operation==="r2p"){
									alert("Your invoice file moved to pending")
								}else{
									alert("Your invoice file moved to reject")
								}
								$(that).closest('tr').remove();
							} else {
								alert("Your Payex_credit_note data change but Payex_Transaction_core data didn't set");
								return;
							}
						},
						error : function(response) {
							console.log("FAILURE " + response)
							alert("Unable to process your request. Please try later.");
							return;
						}
					});
			return true;
		} else {
			return false;
		}

	} */
	
	function moveToChecker(that, payexPaymentId, payexTransactionId){
		
		var operation=$("#operation").val();
		if(operation==="f2p"){
		var x = confirm("Do you want to move This transaction(Payment ID:"
				+ payexPaymentId
				+ ") to pending state.\n All transaction associated with these will go into pending state.");
		}
		else if(operation==="r2p"){
			var x = confirm("Do you want to move This transaction(Transaction ID:"
					+ payexTransactionId
					+ ") to pending state.\n All transaction associated with these will go into pending state.");
		}
		else if(operation==="p2r"){
			var x = confirm("Do you want to move This transaction(Transaction ID:"
					+ payexTransactionId
					+ ") to reject state.\n All transaction associated with these will go into reject state.");
		}else{
			var x = confirm("Do you want to move This transaction(Transaction ID:"
					+ payexTransactionId
					+ ") to pending state.\n All transaction associated with these will go into pending state.");
		}
		if (x) {
			var url = "moveToPendingJson";
			
			var data = {
				"payexPaymentId" : payexPaymentId,"payexTransactionId": payexTransactionId,"operation": operation
			};
			
					$.ajax({
						type : "POST",
						url : url,
						dataType : "json",
						//contentType : "application/json",
						data : data,
						success : function(response) {
							debugger
							console.log("SUCCESS " + response)
							var responseFromServer=JSON.parse(JSON.stringify(response));
							if (responseFromServer.status) {
								if(operation==="f2p"){
								alert("Your invoice's moved to checker side for approval")
								$("."+payexPaymentId).remove();
								}else if(operation==="r2p"){
									alert("Your invoice's moved to checker side for approval")
									$(that).closest('tr').remove();
								}else{
									alert("Your invoice's moved to checker side for approval")
									$(that).closest('tr').remove();
								}
							} else {
								alert("Invoice's already in a checker side");
								if(operation==="f2p"){
								     $("."+payexPaymentId).remove();
								}else{						
								$(that).closest('tr').remove();
								}
								return;
							}
						},
						error : function(response) {
							console.log("FAILURE " + response)
							alert("Unable to process your request. Please try later.");
							return;
						}
					});
			return true;
		} else {
			return false;
		}
	}
</script>
</html>