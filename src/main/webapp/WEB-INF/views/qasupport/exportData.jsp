<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>QA-Export data</title>
</head>
<body>
	<div id="main-0001" style="margin-top: 60px;">
	<form action="">
	<table>
	<tr>
	
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 250px;">
			<h4>Transaction IDs:</h4>
			<input type="text" id="ppid" name="ppid" 
						placeholder="Please enter Payex Payment Ids" autocomplete="off"/>
			</div>
	</td>
	
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 250px;">
			<h4>Inv IDs:</h4>
			<input type="text" id="invid" name="invid" 
						placeholder="Please enter Inv Ids" autocomplete="off"/>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 250px;">
			<h4>CN IDs:</h4>
			<input type="text" id="cnid" name="cnid" 
						placeholder="Please enter CN Ids" autocomplete="off"/>
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
						<th>Payment ID</th>
						<th>Retailer Code</th>
						<th>Transaction ID</th>
						<th>Invoice Number</th>
						<th>Invoice Amount</th>
						<th>Total Amount Due</th>
						<th>Action</th>
					</tr>
				<thead>
				<tbody id="tableData">
				
				<c:forEach var="txn" items="${txns}">	
				<tr>	
					<td>${txn.payexPaymentId}</td>
					<td>${txn.payexCpgCode}</td>
					<td>${txn.payexRetailerCode}</td>
					<td>${txn.amountBank}</td>
					<td>${txn.dateToPay}</td>
					<td>${txn.amountBank}</td>
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
			<button type="button" onclick="callExtractLedger();" style="clear:both; display:block; margin-top:10px;">Export Data</button>
		</div>
		
		

		<div id="R2P"
			style="margin-left: 40px; height: 50px; width: 200px; background: red; display: none;">
			<b>Reject to Pending view</b>
		</div>

		<div id="P2R"
			style="margin-left: 40px; height: 50px; width: 200px; background: green; display: none;">
			<b>Pending to Reject view</b>
		</div>
<form id="exportForm" action="" method="post">
				<input type="hidden" id="action" name="action" value=""/>
				<input type="hidden" id="data" name="data" value=""/>
			</form>	
</div>



</body>
<script type="text/javascript">

function callExtractLedger(){	
	
	try {
	    const request =  JSON.stringify(getRequestJson());
	    alert(request);

	    // Output:
	    // {
	    //   ppid: [12345],
	    //   invid: [],
	    //   cnid: [2434, 56756]
	    // }

	    //postForm("export", request);
	    
	    $('#action').val("");
		$('#data').val(request);
	    $("#exportForm").submit();

	} catch (e) {
	    alert(e.message);
	}
}

function getRequestJson() {

    function parseInput(id) {
        let value = $("#" + id).val().trim();

        // Empty input -> empty array
        if (value === "") {
            return [];
        }

        // Validate: comma separated numbers
        const regex = /^\d+(\s*,\s*\d+)*$/;
        if (!regex.test(value)) {
            throw new Error("Invalid input in '" + id + "'. Please enter only comma-separated numbers.");
        }

        // Convert to array of numbers
        return value.split(",")
                    .map(v => Number(v.trim()));
    }

    return {
    	"T-IDS": parseInput("ppid"),
    	"I-IDS": parseInput("invid"),
    	"C-IDS": parseInput("cnid")
    };
}

</script>
</html>