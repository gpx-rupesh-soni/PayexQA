<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container" style="margin-top: 50px;">
	<div>		
	<h4>Search By: </h4> 
	Payex Retailer Code<input  type="text" name="payex_retailer_code" value="">
	<b>OR</b>		
			
			
	CPG Retailer Code<select id="payex_cpg_code" name="payex_cpg_code">
		<option value="" disabled selected hidden>
	        -- Select CPG --
	    </option>				
		<option value="CPGVGUARD">VGUARD</option>
		<option value="CPGBRGSTN">BRGSTN</option>
		<option value="CPGMTLS">MTLS</option>
	</select> <input type="text" id="cpg_retailer_code" name="cpg_retailer_code" />
	<button id="retSearchBtn">Search</button>		
	</div>	
	
	<div id="retInfo" style="display: none;">
		<h3>Retailer Info</h3>
		<input type="hidden" id="payexRetailerCode" name="payexRetailerCode" />
		<input type="hidden" id="cpgTag" name="cpgTag" />
		<button id="delRetData">Purge Data</button>
		<button id="delRet">Purge Retailer</button>
		<table id="retailerTable" style="margin-top: 5px;">
		    <tr>
		        <td style="text-align: right;">Retailer Code : </td>
		        <td id="retailerCode" style="padding-left: 5px;"></td>
		    </tr>
		    <tr>
		        <td style="text-align: right;">Retailer Name : </td>
		        <td id="retailerName" style="padding-left: 5px;"></td>
		    </tr>
		    <tr>
		        <td style="text-align: right;">Mobile : </td>
		        <td id="retailerMobile" style="padding-left: 5px;"></td>
		    </tr>
		    <tr>
		        <td style="text-align: right;">Email : </td>
		        <td id="retailerEmail" style="padding-left: 5px;"></td>
		    </tr>
		    <tr>
		        <td style="text-align: right;">CPG : </td>
		        <td id="cpg" style="padding-left: 5px;"></td>
		    </tr>
		    <tr>
		        <td style="text-align: right;">Status : </td>
		        <td id="retailerstatus" style="padding-left: 5px;"></td>
		    </tr>
		</table>
		<br/>
		<table id="callResponse" style="display: none;">
		    <tr>
		        <td style="text-align: right;">Message : </td>
		        <td id="resMsg" style="padding-left: 5px;"></td>
		    </tr>
		    <tr>
		        <td style="text-align: right;">Data : </td>
		        <td id="resData" style="padding-left: 5px;"></td>
		    </tr>		    
		</table>
	</div>	
</div>

<script>
$("#retSearchBtn").click(function () {    
    searchRet();
});

$("#delRetData").click(function () {    
	delOperation("del-data");
});

$("#delRet").click(function () {    
	delOperation("del-ret");
});

function searchRet(){
	//alert("searchRet called...");	 
	var payexCode = $.trim($("input[name='payex_retailer_code']").val());
   	var cpgCode   = $("#payex_cpg_code").val();
    var cpgRetail = $.trim($("#cpg_retailer_code").val());

    // ---- Validation ----
    if (payexCode === "" && !(cpgCode && cpgRetail !== "")) {
        alert("Please enter Payex Retailer Code OR select CPG and enter CPG Retailer Code.");
        return;
    }

    // ---- Build POST data ----
    var postData = {
        payex_retailer_code: payexCode,
        payex_cpg_code: cpgCode,
        cpg_retailer_code: cpgRetail
    };

    callsearchRet(postData);
}

function delOperation(action){
	var payexRetailerCode = $.trim($("input[name='payexRetailerCode']").val());
	var cpgTag = $.trim($("input[name='cpgTag']").val());
	var postData = {
			action:action,
			cpgTag:cpgTag,
	        payex_retailer_code: payexRetailerCode
	    };
	
	callDelRet(postData);
}

function callsearchRet(postData){
	
	// ---- POST call ----
    $.ajax({
        url: "./searchRetailer",
        type: "POST",
        data: postData,
        dataType: "json",
        success: function (response) {
            // Handle success response
            console.log(response);

            if (response.status === "SUCCESS") {
                //alert("Retailer found");
                displayResult(response.ret);
                // fill text fields / table / labels here
            } else {
                alert(response.message || "No retailer found");
                $("#retInfo").hide();
            }
        },
        error: function (xhr, status, error) {
            alert("Server error: " + error);
            $("#retInfo").hide();
        }
    });	
	
	function displayResult(ret){		
		$("#payexRetailerCode").val(ret.payex_RETAILER_CODE);
		$("#retailerCode").text(ret.payex_RETAILER_CODE);
        $("#retailerName").text(ret.shop_NAME);
        $("#retailerMobile").text(ret.mobile_NUMBER);
        $("#retailerEmail").text(ret.email_ID);
        $("#cpg").text(ret.cpgTag);
        $("#cpgTag").val(ret.cpgTag);
        $("#retailerstatus").text(ret.active_FLAG);
		$("#retInfo").show();		
	}
	
}

function callDelRet(postData){
	alert("postData: " + JSON.stringify(postData));
	// ---- POST call ----
    $.ajax({
        url: "",
        type: "POST",
        data: postData,
        dataType: "json",
        success: function (response) {
            // Handle success response
            console.log(response);

            if (response.status === "SUCCESS") {
                //alert("postData: " + JSON.stringify(response.data));
                // fill text fields / table / labels here
                displayResult(response);
            } else {
                alert(response.message || "No retailer found");
                $("#retInfo").hide();
            }
        },
        error: function (xhr, status, error) {
            alert("Server error: " + error);
            $("#retInfo").hide();
        }
    });
	
    function displayResult(response){		
    	$("#resMsg").text(response.msg);
		$("#resData").text(JSON.stringify(response.data));        
		$("#callResponse").show();		
	}
}

</script>



