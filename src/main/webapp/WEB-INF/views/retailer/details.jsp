<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div class="container" style="margin-top: 40px;">
	<h2>Retailer Info</h2>
	<form id="search" name="search" action="search_retailer" method="post">
		<div>
			<label>Search By: </label> <select id="search_by" name="search_by">				
				<option value="mobile">Mobile No.</option>
				<option value="code">Retailer Code</option>
				<option value="name">Retailer Name</option>
			</select> <input type="text" id="search_text" name="search_text" />
			<input type="submit" id="submit_search" name="submit_search" value="Search" />
			<!-- <input type="button" id="submit_search" name="submit_search"
				value="Search" onclick="document.getElementById('search').submit();" /> -->
		</div>
		<br />
	</form>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th>#</th>
				<th>Retailer Code</th>
				<th>Shop Name</th>
				<th>Contact Person</th>
				<th>Address</th>								
			</tr>
		<thead>
			<c:forEach var="retailer" items="${retailers}" varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${retailer}</td>
					<td>${retailer}</td>
					<td>${retailer}</td>
					<td>${retailer}</td>
				</tr>				
			</c:forEach>
	</table>
</div>

<script type="text/javascript">
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
</script>