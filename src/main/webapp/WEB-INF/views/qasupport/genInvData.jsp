
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
			<select id="payexCpgCode" name="payexCpgCode" class="form-control"
				style="border: 0; width: 100%;" onchange="handleCpgCodeChange()">
				<option disabled selected>-------select CPG Code-------</option>
				<option value="CPGVGUARD">CPGVGUARD</option>
				<option value="CPGBRGSTN">CPGBRGSTN</option>
				<option value="CPGCROM">CPGCROM</option>
				<option value="CPGMRLS">CPGMRLS</option>
			</select>
			</div>
	</td>
	<td id="divCodeContainer" style="display: none;">
		<div id="op-header"
			style="margin-left: 40px; height: 40px; width: 200px;">
			<h4>Div Code:</h4>
			<input type="text" id="divCode" name="divCode"
						placeholder="Please enter Div Code" autocomplete="off"/>
			</div>
	</td>
	<td id="docCodeContainer" style="display: none;">
		<div id="op-header"
			style="margin-left: 40px; height: 40px; width: 200px;">
			<h4>Doc Code:</h4>
			<input type="text" id="docCode" name="docCode"
						placeholder="Please enter Doc Code" autocomplete="off"/>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 200px;">
			<h4>Retailer Code:</h4>
			<input type="text" id="retailerCode" name="retailerCode" 
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

function handleCpgCodeChange(){
	var payexCpgCode = $("#payexCpgCode").val();
	if("CPGVGUARD"===payexCpgCode){
		$("#divCodeContainer").show();
		$("#docCodeContainer").show();
	}else{
		$("#divCodeContainer").hide();
		$("#docCodeContainer").hide();
		$("#divCode").val("");
		$("#docCode").val("");
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
	var retailerCod = $("#retailerCode").val();
	var divCode = $("#divCode").val();
	var docCode = $("#docCode").val();
	var maxAmt = $("#maxAmt").val();
	var minAmt = $("#minAmt").val();
	var docCount = $("#docCount").val();

	var postData = {
			docType:docType,
			payexCpgCode:payexCpgCode,
			retailerCode:retailerCod,
			divCode:divCode,
			docCode:docCode,
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