<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div class="container" style="margin-top: 80px;">
	<h2>Generate transaction Pin.</h2>
</div>

Retailers IDs
<textarea id="ta_retailers_id"></textarea> 
<input type="button" onclick="generatePin()" value="Generate Pin"/>

<br>
<br>

<input type="button" onclick="generatePinAll()" value="Generate Pin For all Activaed Retailere that not have pin"/>


<form id="form_add_pin" action="" method="post">
	<input id="retailers_ids" name="retailers_ids" type="hidden" value="">
	<input id="action" name="action" type="hidden" value="">
</form>


<script type="text/javascript">

	function generatePinAll(){		
		$("#action").val("all");
		
	}

	function generatePin(){
		var ids = $("#ta_retailers_id").val();
		ids = ids.replace(',','","');
		ids= '["'+ids+'"]';
		alert(ids);
		$("#retailers_ids").val(ids);
		$("#action").val("some");
		$("#form_add_pin").submit();
	}


   function approveMandate(button,mandateId){	   
	   var url = "approve_copmlete_mandate";	  
	   var data = {"mandateId":mandateId};
	 
	$.ajax({
	    type: "POST",
	    url: url,
	    dataType: "json",
	    data: data,
	    success: function(response){
	    	
	    	setData(response,button);
	       
	    }
	});
   }
   
function setData(response,button){
	
		alert(response.msg);
	   	if(response.status){
	   		alert($(button).html());
	   		$(button).parent().html("Approved");
	   		$(button).remove();
	   	}
	   
   }
   
   </script>

