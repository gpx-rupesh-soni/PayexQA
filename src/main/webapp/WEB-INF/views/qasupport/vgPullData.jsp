<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
	#vg-pull-main { margin: 70px 40px 40px 40px; }
	#vg-pull-main .panel-body .form-inline .form-group { margin-right: 15px; }
	#vg-pull-main pre.raw-response { max-height: 400px; overflow: auto; font-size: 12px; }
</style>

<div id="vg-pull-main">
	<div class="panel panel-default">
		<div class="panel-heading"><b>VG CN Pull Data Export</b>
			<small class="text-muted">&nbsp;(downloads the open CNs of the buyers as a CSV file)</small>
		</div>
		<div class="panel-body">
			<div class="form-inline">
				<div class="form-group">
					<label for="payexRetailercode">Payex Retailer Codes :</label>
					<input type="text" id="payexRetailercode" class="form-control" style="width: 350px;"
						placeholder="Comma separated Payex Retailer Codes" autocomplete="off"/>
				</div>
				<div class="checkbox" style="margin-right: 15px;">
					<label><input type="checkbox" id="withMaster"/> With master data</label>
				</div>
				<button class="btn btn-primary" id="btn-export" onclick="exportVgPullData()">Export</button>
			</div>
		</div>
	</div>

	<div class="panel panel-default" id="resultPanel" style="display: none;">
		<div class="panel-heading"><b id="resultTitle">Result</b></div>
		<div class="panel-body">
			<div id="resultView"></div>
			<pre id="rawResponse" class="raw-response" style="display: none;"></pre>
		</div>
	</div>
</div>

<script type="text/javascript">

var baseUrl = '<c:url value="/qasupport/"/>';

function exportVgPullData(){
	var codes = $.map($("#payexRetailercode").val().split(","), function (code) {
		code = $.trim(code);
		return code ? code : null;
	});
	if(!codes.length){
		alert("Please enter Payex Retailer Code");
		return;
	}
	var withMaster = $("#withMaster").is(":checked");

	$("#resultPanel").show();
	$("#resultTitle").text("Export of " + codes.join(", ") + (withMaster ? " (with master)" : ""));
	$("#resultView").html('<span class="text-muted">Exporting, this may take a while...</span>');
	$("#rawResponse").hide().text("");
	$("#btn-export").prop("disabled", true);

	// fetch instead of $.ajax : jQuery 1.9 cannot receive the file as binary
	var url = baseUrl + "VG-cnpull-ledger?" + $.param({ payexRetailercode: codes.join(","), withMaster: withMaster });
	fetch(url, { credentials: "same-origin" })
		.then(function (response) {
			if(!response.ok){
				return response.text().then(function (text) {
					$("#resultView").html(statusLabel(false, "ERROR") + ' Server error : ' + esc(response.status + " " + response.statusText));
					if(text)
						$("#rawResponse").show().text($('<div/>').html(text).text());
				});
			}
			var fileName = fileNameOf(response) || (codes[0] + "-pullData.csv");
			return response.blob().then(function (blob) {
				downloadBlob(blob, fileName);
				$("#resultView").html(statusLabel(true, "DOWNLOADED") + ' File : <code>' + esc(fileName) + '</code>');
			});
		})
		.catch(function (error) {
			$("#resultView").html(statusLabel(false, "ERROR") + ' Request failed : ' + esc(error));
		})
		.then(function () {
			$("#btn-export").prop("disabled", false);
		});
}

/** file name from the Content-Disposition header set by the server */
function fileNameOf(response){
	var disposition = response.headers.get("Content-Disposition") || "";
	var match = /filename="?([^";]+)"?/.exec(disposition);
	return match ? match[1] : null;
}

function downloadBlob(blob, fileName){
	var link = document.createElement("a");
	link.href = URL.createObjectURL(blob);
	link.download = fileName;
	document.body.appendChild(link);
	link.click();
	document.body.removeChild(link);
	setTimeout(function () { URL.revokeObjectURL(link.href); }, 1000);
}

function statusLabel(ok, text){
	return '<span class="label ' + (ok ? 'label-success' : 'label-danger') + '">' + text + '</span>';
}

function esc(value){
	return $('<div/>').text(String(value)).html();
}
</script>
