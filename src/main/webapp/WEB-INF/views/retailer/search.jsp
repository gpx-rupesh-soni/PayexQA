<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="container" style="margin-top: 40px;">
	<h2>Retailer Info</h2>
	<form id="search" name="search" action="search" method="post">
		<div>
			<label>Search By: </label> <select id="search_by" name="search_by">
				<option value="shop">Shop Name</option>
				<option value="code">Retailer Code</option>
				<option value="mobile">Mobile No.</option>
			</select> <input type="text" id="search_text" name="search_text" /> <input
				type="submit" id="submit_search" name="submit_search" value="Search" />
			<!-- <input type="button" id="submit_search" name="submit_search"
				value="Search" onclick="document.getElementById('search').submit();" /> -->
		</div>
		<br />
	</form>
	<table class="table table-bordered">
		<thead>
			<tr style="white-space: pre; text-align: center">
				<th>#</th>
				<th>Retailer Code</th>
				<th>Mobile</th>
				<th>Shop Name</th>
				<th>Contact Person</th>
				<th>Address</th>
				<th>Options</th>
				<th style="text-align: center;">Metrics</th>
				
			</tr>
		<thead>
			<c:forEach var="retailer" items="${retailers}" varStatus="status">
				<tr id='${retailer}'>
					<td>${status.count}</td>
					<td>${retailer}</td>
					<td>${retailer.mobile_NUMBER}</td>
					<td>${retailer.shop_NAME}</td>
					<td>${retailer.key_PERSON}</td>
					<td>${retailer.address}</td>
					<td><c:if test="${role =='supsal' || role =='admin'}">
							<button style="width: 100px;height: 22px;" onclick="getDetils('${retailer}');" value="Details">Details</button>
							<button style="width: 100px;height: 22px;"  onclick="getInfo('${retailer}','pin');" value="Pin">Pin</button>
							<button style="width: 100px;height: 22px;" onclick="getInfo('${retailer}','otp');" value="Pin">OTP</button>
						</c:if> <%-- <button onclick="call_retailer('${retailer.payex_RETAILER_CODE}','${retailer.shop_NAME}');" value="Call">Call</button> --%>
						<%-- <button onclick="passdata('${retailer}','${retailer.mobile_NUMBER}','${retailer.shop_NAME}','${retailer.key_PERSON}');" value="callbyoption">Call</button> --%> 
						<button  style="width: 100px;height: 22px;" class="call_btn">Call</button> 
					</td>
					<td>					
					<c:forEach var="inv" items="${invoices}">
					<c:if test="${inv.key==retailer}">
					<table class="table table-bordered" style="margin-top: -10px;margin-bottom: -10px;margin-left: -9px;"> 
				<tr>
					<th>CPG name</th>
					<th>Last payment approved on </th>
					<th>Total approved no. of Invoices</th>
					<th>Total approved Invoices Amt.</th>
				</tr>
					<c:forEach var="cpg" items="${inv.value}">
					<tr>
							<td>${cpg.key}</td>
							<fmt:parseDate value="${cpg.value.dt}" pattern="yyyy-MM-dd HH:mm" var="myDate"/>							
							<td><fmt:formatDate value="${myDate}" type="both"/></td>
							<td>${cpg.value.aic}</td>
							<td><fmt:formatNumber type = "number" maxFractionDigits = "2" value = "${cpg.value.aia}" /></td>
					</tr>	
					</c:forEach>
					</table>
					</c:if>
					</c:forEach>
				
					</td>
 				</tr>
				
			</c:forEach>
	</table>
</div>
<div class="modal fade" id="mycall" aria-hidden="true" onload="resetform();">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="myModalLabel">Global PayEx Retailer
					Status</h4>

			</div>
			<div style="margin-left : 10px ; margin-top : 10px;">
				<label id="retailer_code" style="display: none;" />
				</div>
				<div style="margin-left : 10px ;" >
				<label id="shopname"/>
				</div>
				<div style="margin-left : 10px ;">
				<label id="keyperson"/>
				</div>
				<div style="margin-left : 10px ;">
				<label id="retcontact"/>
				</div>
				
				<div class="modal-body">
				<input type="radio" id="noanswer"name="call" value="no_answer"  onclick="javascript:followdate();"checked="checked"> NoAnswer 
				<input type="radio" id="follow" name="call" value="follw" onclick="javascript:followdate();" > Follow Up 
				<input type="radio" id="schedule" name="call" value="schedule"  onclick="javascript:followdate();"> Scheduled 
				<input type="radio" id="interest" name="call" value="intersert" onclick="javascript:followdate();"> Interested 
				
				<br> <br>
				<div id="iptdate_div" style="display: none;">
				<p>Date: <input type="text"  id="iptdate" name="iptdate" readonly/></p>
				</div>
				<textarea name="comment_txt" cols="40" rows="3" style="margin-top : 10px;" id="comment_txt"></textarea>
			</div>
			<div class="modal-footer">
				<button type="button" data-dismiss="modal" onclick="resetform();">Close</button>
				<button id="Call" value="Call" class="Call">Submit</button>
			</div>
		</div>
	</div>
</div>


 <script>
$(document).ready(function () {
    
    $(".call_btn").click(function(){
    	
    	$('#mycall').modal({
  		  backdrop: 'static',
  		  keyboard: false
  		});
    	
    	    var currentRow=$(this).closest("tr"); 
    	    
    	    var retailer_code=currentRow.find("td:eq(1)").text(); // get current row 1st TD value
    	    var shopname=currentRow.find("td:eq(3)").text(); // get current row 2nd TD
    	    var keyperson=currentRow.find("td:eq(4)").text(); // get current row 3rd TD
    	    var retcontact=currentRow.find("td:eq(2)").text(); // get current row 3rd TD
   			
    	    /* console.log(retailer_code+shopname+keyperson+retcontact); */
    	   
    	     $("#retailer_code").text(retailer_code);
    	    $("#shopname").text(shopname);
    	    $("#keyperson").text(keyperson);
    	    $("#retcontact").text(retcontact);
    	    
         $('#mycall').modal('show');
    });
});
</script> 

<script>
$(document).ready(function () {
    
    $(".Call").click(function(){
    	
    	    var retailer_code=$("#retailer_code").text();
    	    var shopname=$("#shopname").text();
    	    var keyperson=$("#keyperson").text();
    	    var retcontact=$("#retcontact").text();
    	    var date=$("#iptdate").val();
    	    var comment=$('textarea#comment_txt').val();
    	    
    	    
    	    if($('#noanswer').is(':checked'))
    	    	{
    	    	$('#iptdate').val("");
    	    	call_retailer(retailer_code,shopname,"","noanswer",comment);
    	    	console.log(retailer_code+shopname+""+"noanswer"+comment);
    	    	resetform();
    	    	}
    	    else if($('#follow').is(':checked'))
    	    	{
    	    	call_retailer(retailer_code,shopname,date,"followup",comment);
    	    	console.log(retailer_code+shopname+date+"followup"+comment);
    	    	resetform();
    	    	}
    	    else if($('#schedule').is(':checked'))
    	    	{
    	    	call_retailer(retailer_code,shopname,date,"schedule",comment);
    	    	console.log(retailer_code+shopname+date+"schedule"+comment);
    	    	resetform();
    	    	}
    	    else if($('#interest').is(':checked'))
    	    	{
    	    	$('#iptdate').val("");
    	    	call_retailer(retailer_code,shopname,"","interested",comment);
    	    	console.log(retailer_code+shopname+""+"interest"+comment);  	
    	    	resetform();
    	    	}
    });
});
</script>
<script>
function resetform() {
	document.getElementById("iptdate").value = "";
	document.getElementById("comment_txt").value = "";
	$("#iptdate_div").hide();
	if($('#follow').is(':checked'))
	{
    	 $('#noanswer').attr('checked',true);
    	 $('#follow').attr('checked',false);
	}
     else if($('#schedule').is(':checked'))
	{
    	 $('#noanswer').attr('checked',true);
    	 $('#schedule').attr('checked',false);
	}
     else if($('#interest').is(':checked'))
	{
    	 $('#noanswer').attr('checked',true);
    	 $('#interest').attr('checked',false);
	}
	console.log("Reset Call");

	}
</script>
<script type="text/javascript">
function followdate() {
	
	 if($('#noanswer').is(':checked'))
 	{
		 document.getElementById("iptdate").disabled = true;
		 $("#iptdate_div").hide();
 	}
 else if($('#follow').is(':checked'))
 	{
	 document.getElementById("iptdate").disabled = false;
	 $("#iptdate_div").show();
 	}
 else if($('#schedule').is(':checked'))
 	{
	 document.getElementById("iptdate").disabled = false;
	 $("#iptdate_div").show();
 	}
 else if($('#interest').is(':checked'))
 	{
	 document.getElementById("iptdate").disabled = true;
	 $("#iptdate_div").hide();
 	}
}


	function getDetils(id){
		alert("Finding details of retailer : "+id);
	}
	
	function getInfo(id,info_type){
		//alert("Finding pin of retailer : "+id);
		var $form = $("#search_retailaer_form");
		var url = "info";
		var data = {"id":id,"info_type":info_type};
		$.ajax({
			type : "POST",
			url : url,
			dataType : "json",
			data : data,
			success : function(response) {
				alert(response.data);								
			}
		});
	}


	function search() {
		var $form = $("#search_retailaer_form");
		var url = $form.attr("action");
		var data = $form.serialize();
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			data : data,
			success : function(response) {
				alert(response.data);
				setData(response);						
			}
		});
	}

	function setData(response) {		
		$("#mandateNO").val(response.data.bank_instruction_id);
		$("#bank_instruction_id").val(response.data.bank_instruction_id);
		
	}
	
	function call_retailer(retailer_id,retailer_code,date,call_type,comment){
		//alert("Finding pin of retailer : "+id);
		var $form = $("#search_retailaer_form");
		var url = "call_retailer";
		var data = {"retailer_id":retailer_id,"retailer_code":retailer_code,"date":date,"call_type":call_type,"comment":comment};
		console.log("data presnt -"+retailer_id+retailer_code+date+call_type+comment)
		$.ajax({
			type : "POST",
			url : url,
			dataType : "json",
			data : data,
			success : function(response) {
				alert(response.msg);	
				$('#mycall').modal('hide');
			}
		});
	}
</script> 

 <script>
  $( function() {
    $( "#iptdate" ).datepicker({dateFormat: 'dd/mm/yy'});
  } );
  </script>
 <!-- <script type="text/javascript">
	$(".call_btn").click(function(){
      navigator.geolocation.getCurrentPosition(function(position) {
      var lat = position.coords.latitude;
      var lng = position.coords.longitude;
      alert(lat.toFixed(3)+'/'+lng.toFixed(3));
    });

  });
 
</script> -->
  <%--  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script> --%>
  
