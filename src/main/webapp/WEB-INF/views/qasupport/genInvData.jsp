
	<div id="main-0001" style="margin-top: 60px;">
	<table>
	<tr>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 200px;">
			<h4>Doc Type:</h4>
			<select id="docType" name="docType" class="form-control"
				style="border: 0; width: 100%;" >
				<option disabled selected>-------select Doc type-------</option>
				<option value="inv">INV</option>
				<option value="dn">Debit Note</option>
				<option value="cn">Credit Note</option>
			</select>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 200px;">
			<h4>CPG Code:</h4>
			<input type="text" id="payexCpgCode" name="payexCpgCode" 
						placeholder="Please enter CPG Code" autocomplete="off"/>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 200px;">
			<h4>Retailer Code:</h4>
			<input type="text" id="payexRetailerCode" name="payexRetailerCode" 
						placeholder="Please enter Retailer Code" autocomplete="off"/>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 60px; width: 200px;">
			<h4>Amount Range:</h4>
			<input type="number" id="maxAmt" name="maxAmt" 
						placeholder="Enter Max Amount" autocomplete="off"/>
			<input type="number" id="minAmt" name="minAmt" 
						placeholder="Enter Min Amount" autocomplete="off"/>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 200px;">
			<h4>Count to be Add:</h4>
			<input type="number" id="docCount" name="docCount" 
						placeholder="Enter Doc Count to be Add" autocomplete="off"/>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 250px;">
			<h4>Action : </h4>
			<button id="btn-others"onclick="handleAction('gen-ledger')">Generate Ladger</button>
			</div>
	</td>
	</tr>
	</table>
</div>
<script type="text/javascript">

function handleAction(action){
	alert(action);
	if("gen-ledger"===action){
		alert(action);
		genLedger(action);
	}
}

function handleResponse(action, response){	
	alert("handleResponse called : "+JSON.stringify(response));
	if("gen-ledger"===action){
		showApiLog(response);
	}
}

var response;
function genLedger(action){

	var docType = $("#docType").val();
	var payexCpgCode = $("#payexCpgCode").val();
	var payexRetailerCode = $("#payexRetailerCode").val();
	var maxAmt = $("#maxAmt").val();
	var minAmt = $("#minAmt").val();
	var docCount = $("#docCount").val();
	
	var postData = {
			docType:docType,
			payexCpgCode:payexCpgCode,
			payexRetailerCode:payexRetailerCode,
			maxAmt:maxAmt,
			minAmt:minAmt,
			docCount:docCount
	    };	
	genLedgerCall(postData,action);
}

function showApiLog(response) {
	alert("handleResponse called : "+JSON.stringify(response.apiLog));
}


function genLedgerCall(postData,action){
	
	// ---- POST call ----
    $.ajax({
        url: "",
        type: "POST",
        data: postData,
        dataType: "json",
        success: function (response) {
            // Handle success response
            console.log(response);            
            handleResponse(action, response);           
        },
        error: function (xhr, status, error) {
            alert("Server error: " + error);
            $("#retInfo").hide();
        }
    });	
	
	}
</script>