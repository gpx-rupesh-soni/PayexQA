
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
				<option value="all">All</option>
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
				style="border: 0; width: 100%;">
				<option disabled selected>-------select CPG Code-------</option>
				<option value="CPGVGUARD">CPGVGUARD</option>
				<option value="CPGBRGSTN">CPGBRGSTN</option>
				<option value="CPGCROM">CPGCROM</option>
				<option value="CPGMRLS">CPGMRLS</option>
			</select>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 200px;">
			<h4>Payex Retailer Codes:</h4>
			<input type="text" id="payexRetailerCode" name="payexRetailerCode" 
						placeholder="Please enter Payex Retailer Code" autocomplete="off"/>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 200px;">
			<h4>Retailer Codes:</h4>
			<input type="text" id="retailerCode" name="retailerCode" 
						placeholder="Please enter Retailer Code" autocomplete="off"/>
			</div>
	</td>
	<td>
		<div id="op-header" 
			style="margin-left: 40px; height: 40px; width: 250px;">
			<h4>Action : </h4>
			<button id="btn-others"onclick="handleAction('purge-data')">Purge Data</button>
			</div>
	</td>
	</tr>
	</table>
</div>
<script type="text/javascript">

function handleAction(action){
	alert(action);
	if("purge-data"===action){
		alert(action);
		purgeData(action);
	}
}

function handleResponse(action, response){	
	alert("handleResponse called : "+JSON.stringify(response));
	if("purge-data"===action){
		showApiLog(response);
	}
}

var response;
function purgeData(action){

	var docType = $("#docType").val();
	var payexCpgCode = $("#payexCpgCode").val();
	var retailerCode = $("#retailerCode").val();
	var payexRetailerCode = $("#payexRetailerCode").val();
	
	var postData = {
			docType:docType,
			payexCpgCode:payexCpgCode,
			retailerCode:retailerCode,
			payexRetailerCode:payexRetailerCode			
	    };
	purgeDataCall(postData,action);
}

function showApiLog(response) {
	alert("handleResponse called : "+JSON.stringify(response.apiLog));
}


function purgeDataCall(postData,action){
	
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