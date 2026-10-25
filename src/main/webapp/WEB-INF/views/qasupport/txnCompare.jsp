<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
	#txn-compare-main { margin: 70px 40px 40px 40px; }
	#txn-compare-main .input-panel { min-height: 120px; }
	#txn-compare-main .panel-body .form-inline .form-group { margin-right: 15px; margin-bottom: 5px; }
	#txn-compare-main .panel-body .form-inline input[type=number] { width: 170px; }
	#txn-compare-main .result-summary { font-size: 15px; margin-bottom: 10px; }
	#txn-compare-main .result-summary .label { font-size: 13px; }
	#txn-compare-main .result-toggles a { margin-right: 20px; }
	#txn-compare-main pre.raw-json { overflow-x: auto; font-size: 12px; margin-top: 10px; }
	#txn-compare-main .table-scroll { overflow-x: auto; max-height: 500px; margin-bottom: 15px; }
	#txn-compare-main table.data-table { margin-bottom: 0; }
	#txn-compare-main table.data-table td, #txn-compare-main table.data-table th { font-size: 12px; white-space: nowrap; }
	#txn-compare-main table.data-table td.val { white-space: normal; word-break: break-all; min-width: 120px; max-width: 400px; }
	#resultTables { margin-top: 10px; }
	#txn-compare-main .log-link { margin-left: 10px; }
	#compareLogPanel .table-scroll { max-height: 300px; overflow-y: auto; }
	#compareLogPanel table td, #compareLogPanel table th { font-size: 12px; vertical-align: middle; }
</style>

<div id="txn-compare-main">
	<div class="row">
		<div class="col-sm-6">
			<div class="panel panel-default input-panel">
				<div class="panel-heading"><b>1. Save Old Flow Snapshot</b><br/>
					<small class="text-muted">paid txn: saves the txn, invoices and CNs to a file; otherwise shows the saved snapshot</small>
				</div>
				<div class="panel-body">
					<div class="form-inline">
						<div class="form-group">
							<label for="snapshotPpi">PPID :</label>
							<input type="number" id="snapshotPpi" class="form-control" placeholder="Old flow PPID" autocomplete="off"/>
						</div>
						<button class="btn btn-primary" onclick="takeSnapshot()">Snapshot</button>
					</div>
				</div>
			</div>
		</div>

		<div class="col-sm-6">
			<div class="panel panel-default input-panel">
				<div class="panel-heading"><b>2. Compare Old vs New Flow</b><br/>
					<small class="text-muted">both PPIDs are compared from their snapshot files; a missing snapshot is created from the database first</small>
				</div>
				<div class="panel-body">
					<div class="form-inline">
						<div class="form-group">
							<label for="oldPpi">Old PPID :</label>
							<input type="number" id="oldPpi" class="form-control" placeholder="Old flow PPID" autocomplete="off"/>
						</div>
						<div class="form-group">
							<label for="newPpi">New PPID :</label>
							<input type="number" id="newPpi" class="form-control" placeholder="New flow PPID" autocomplete="off"/>
						</div>
						<button class="btn btn-primary" onclick="compareTxns()">Compare</button>
						<a href="#" class="log-link" onclick="toggleCompareLog(); return false;">Previous compares</a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="panel panel-default" id="compareLogPanel" style="display: none;">
		<div class="panel-heading"><b>Previous Compares</b>
			<small class="text-muted">&nbsp;(latest first, one row per old / new pair)</small>
			<a href="#" class="pull-right" onclick="loadCompareLog(); return false;">Refresh</a>
		</div>
		<div class="panel-body" id="compareLogView"></div>
	</div>

	<div class="panel panel-default" id="resultPanel" style="display: none;">
		<div class="panel-heading"><b id="resultTitle">Result</b></div>
		<div class="panel-body">
			<div id="resultSummary" class="result-summary"></div>
			<div class="result-toggles">
				<a href="#" id="tablesToggle" onclick="$('#resultTables').toggle(); return false;" style="display: none;">Show / hide tables</a>
				<a href="#" onclick="$('#rawJson').toggle(); return false;">Show / hide raw JSON</a>
			</div>
			<div id="resultTables"></div>
			<pre id="rawJson" class="raw-json" style="display: none;"></pre>
		</div>
	</div>
</div>

<script type="text/javascript">

var baseUrl = '<c:url value="/qasupport/"/>';

function takeSnapshot(){
	var ppi = $.trim($("#snapshotPpi").val());
	if(!ppi){
		alert("Please enter PPID");
		return;
	}
	callApi(baseUrl + "txn-snapshot/" + ppi, "Snapshot of PPID " + ppi, showSnapshot);
}

function compareTxns(){
	var oldPpi = $.trim($("#oldPpi").val());
	var newPpi = $.trim($("#newPpi").val());
	if(!oldPpi || !newPpi){
		alert("Please enter old and new PPID");
		return;
	}
	callApi(baseUrl + "txn-compare/" + oldPpi + "/" + newPpi, "Compare old PPID " + oldPpi + " vs new PPID " + newPpi, function (response) {
		showCompare(response);
		// the compare just ran is now in the log
		if($("#compareLogPanel").is(":visible"))
			loadCompareLog();
	});
}

/* ---------- previous compares ---------- */

function toggleCompareLog(){
	if($("#compareLogPanel").is(":visible")){
		$("#compareLogPanel").hide();
		return;
	}
	$("#compareLogPanel").show();
	loadCompareLog();
}

function loadCompareLog(){
	$("#compareLogView").html('<span class="text-muted">Loading...</span>');
	$.ajax({
		url: baseUrl + "txn-compare-log",
		type: "GET",
		dataType: "json",
		success: function (response) {
			if(response.Status === false){
				$("#compareLogView").html(statusLabel(false, "FAILED") + ' ' + esc(response.msg));
				return;
			}
			showCompareLog(response.list || []);
		},
		error: function (xhr, status, error) {
			$("#compareLogView").html(statusLabel(false, "ERROR") + ' Server error : ' + esc(error));
		}
	});
}

function showCompareLog(list){
	if(!list.length){
		$("#compareLogView").html('<p class="text-muted">No compare done yet</p>');
		return;
	}
	var html = '<div class="table-scroll"><table class="table table-bordered table-condensed table-striped table-hover">'
		+ '<thead><tr><th>Last Compared</th><th>Old PPID</th><th>New PPID</th><th>Result</th>'
		+ '<th>Transaction</th><th>Invoices</th><th>Credit Notes</th><th>Times</th><th></th></tr></thead><tbody>';
	for(var i = 0; i < list.length; i++){
		var entry = list[i];
		html += '<tr><td>' + esc(entry.time) + '</td>'
			+ '<td>' + esc(entry.oldPpi) + '</td>'
			+ '<td>' + esc(entry.newPpi) + '</td>'
			+ '<td>' + statusLabel(entry.matched, entry.matched ? "MATCHED" : "NOT MATCHED") + '</td>'
			+ '<td>' + matchText(entry.txnMatched === undefined ? null : { matched: entry.txnMatched }) + '</td>'
			+ '<td>' + matchText(entry.invoicesMatched === undefined ? null : { matched: entry.invoicesMatched }) + '</td>'
			+ '<td>' + matchText(entry.creditNotesMatched === undefined ? null : { matched: entry.creditNotesMatched }) + '</td>'
			+ '<td>' + esc(entry.count) + '</td>'
			+ '<td><button class="btn btn-primary btn-xs" onclick="compareFromLog(' + Number(entry.oldPpi) + ', ' + Number(entry.newPpi) + ')">Compare</button></td></tr>';
	}
	$("#compareLogView").html(html + '</tbody></table></div>');
}

function compareFromLog(oldPpi, newPpi){
	$("#oldPpi").val(oldPpi);
	$("#newPpi").val(newPpi);
	compareTxns();
	$("html, body").animate({ scrollTop: $("#resultPanel").offset().top - 60 }, 300);
}

function callApi(url, title, render){
	$("#resultPanel").show();
	$("#resultTitle").text(title);
	$("#resultSummary").html('<span class="text-muted">Loading...</span>');
	$("#resultTables").html("");
	$("#tablesToggle").hide();
	$("#rawJson").text("");
	$.ajax({
		url: url,
		type: "GET",
		dataType: "json",
		success: function (response) {
			$("#rawJson").text(JSON.stringify(response, null, 2));
			if(response.Status === false){
				$("#resultSummary").html(statusLabel(false, "FAILED") + ' ' + esc(response.msg));
				return;
			}
			render(response);
			$("#tablesToggle").show();
		},
		error: function (xhr, status, error) {
			$("#resultSummary").html(statusLabel(false, "ERROR") + ' Server error : ' + esc(error));
			$("#rawJson").text(xhr.responseText || "");
		}
	});
}

/* ---------- snapshot ---------- */

function showSnapshot(response){
	var snapshot = response.snapshot || {};
	var txn = snapshot.transaction || {};
	$("#resultSummary").html(statusLabel(true, "SAVED") + ' File : <code>' + esc(response.file) + '</code>'
		+ '<br/><small>PPID : ' + esc(snapshot.ppi) + ' | Snapshot time : ' + esc(snapshot.snapshotTime)
		+ ' | Invoices : ' + count(snapshot.invoices) + ' | Credit notes : ' + count(snapshot.creditNotes) + '</small>');

	var html = '<h4>Transaction</h4>' + columnValueTable(txn);
	html += '<h4>Invoices <small>' + count(snapshot.invoices) + '</small></h4>' + recordsTable(snapshot.invoices);
	html += '<h4>Credit Notes <small>' + count(snapshot.creditNotes) + '</small></h4>' + recordsTable(snapshot.creditNotes);
	$("#resultTables").html(html);
}

/** a single row : column names in the header, values in the cells below */
function columnValueTable(row){
	var columns = Object.keys(row || {}).sort();
	if(!columns.length)
		return '<p class="text-muted">No data</p>';
	var html = '<div class="table-scroll"><table class="table table-bordered table-condensed table-striped data-table"><thead><tr>';
	for(var c = 0; c < columns.length; c++)
		html += '<th>' + esc(columns[c]) + '</th>';
	html += '</tr></thead><tbody><tr>';
	for(var i = 0; i < columns.length; i++)
		html += '<td>' + esc(row[columns[i]]) + '</td>';
	return html + '</tr></tbody></table></div>';
}

/** rows keyed by id shown one record per line, columns from all records */
function recordsTable(rows){
	var ids = Object.keys(rows || {}).sort();
	if(!ids.length)
		return '<p class="text-muted">No data</p>';
	var columnSet = {};
	for(var i = 0; i < ids.length; i++)
		for(var column in rows[ids[i]])
			columnSet[column] = true;
	var columns = Object.keys(columnSet).sort();

	var html = '<div class="table-scroll"><table class="table table-bordered table-condensed table-striped data-table"><thead><tr>';
	for(var c = 0; c < columns.length; c++)
		html += '<th>' + esc(columns[c]) + '</th>';
	html += '</tr></thead><tbody>';
	for(var r = 0; r < ids.length; r++){
		html += '<tr>';
		for(var k = 0; k < columns.length; k++)
			html += '<td>' + esc(rows[ids[r]][columns[k]]) + '</td>';
		html += '</tr>';
	}
	return html + '</tbody></table></div>';
}

/* ---------- compare ---------- */

function showCompare(response){
	var inv = response.invoices || {};
	var cn = response.creditNotes || {};
	$("#resultSummary").html(statusLabel(response.matched, response.matched ? "MATCHED" : "NOT MATCHED")
		+ '<br/><small>Old snapshot : <code>' + esc(response.oldSource) + '</code>' + createdText(response.oldSnapshotCreated)
		+ ' | New snapshot : <code>' + esc(response.newSource) + '</code>' + createdText(response.newSnapshotCreated) + '</small>'
		+ '<br/><small>Transaction : ' + matchText(response.transaction)
		+ ' | Invoices : ' + matchText(inv) + ' (old ' + esc(inv.oldCount) + ' / new ' + esc(inv.newCount) + ')'
		+ ' | Credit notes : ' + matchText(cn) + ' (old ' + esc(cn.oldCount) + ' / new ' + esc(cn.newCount) + ')</small>');

	var html = sectionRow("Transaction", response.transaction);
	html += sectionRows("Invoices", inv);
	html += sectionRows("Credit Notes", cn);
	$("#resultTables").html(html);
}

/** transaction section : a single row compare */
function sectionRow(title, result){
	result = result || {};
	var html = '<h4>' + title + ' ' + statusLabel(result.matched, result.matched ? "MATCHED" : "NOT MATCHED") + '</h4>';
	if(result.msg)
		html += '<p class="text-danger">' + esc(result.msg) + '</p>';
	html += compareTable([result], false);
	return html;
}

/** invoices / credit notes section : rows keyed by id */
function sectionRows(title, result){
	var html = '<h4>' + title + ' ' + statusLabel(result.matched, result.matched ? "MATCHED" : "NOT MATCHED")
		+ ' <small>old count : ' + esc(result.oldCount) + ', new count : ' + esc(result.newCount) + '</small></h4>';
	if(result.onlyInOld && result.onlyInOld.length)
		html += '<p><b>Only in old :</b> ' + esc(result.onlyInOld.join(", ")) + '</p>';
	if(result.onlyInNew && result.onlyInNew.length)
		html += '<p><b>Only in new :</b> ' + esc(result.onlyInNew.join(", ")) + '</p>';
	html += compareTable(result.rows, true);
	return html;
}

/**
 * All columns of the compared rows : keys in the header, per row its old values then its new values.
 * Cells that differ are marked red, rows found on one side only are marked yellow.
 */
function compareTable(rows, withId){
	rows = $.grep(rows || [], function (row) { return row && (count(row.old) || count(row["new"])); });
	if(!rows.length)
		return '<p class="text-muted">No data</p>';
	var columnSet = {};
	var diffColumns = [];
	for(var i = 0; i < rows.length; i++){
		for(var oldColumn in rows[i].old)
			columnSet[oldColumn] = true;
		for(var newColumn in rows[i]["new"])
			columnSet[newColumn] = true;
		var diffSet = {};
		var diffs = rows[i].diffs || [];
		for(var d = 0; d < diffs.length; d++)
			diffSet[diffs[d].column] = true;
		diffColumns.push(diffSet);
	}
	var columns = Object.keys(columnSet).sort();

	var html = '<div class="table-scroll"><table class="table table-bordered table-condensed data-table"><thead><tr>'
		+ (withId ? '<th>Id</th>' : '') + '<th></th>';
	for(var c = 0; c < columns.length; c++)
		html += '<th>' + esc(columns[c]) + '</th>';
	html += '</tr></thead><tbody>';
	for(var r = 0; r < rows.length; r++){
		var oneSide = !count(rows[r].old) || !count(rows[r]["new"]);
		var rowClass = oneSide ? ' class="warning"' : '';
		html += '<tr' + rowClass + '>'
			+ (withId ? '<th rowspan="2" style="vertical-align: middle;">' + esc(rows[r].id)
				+ (oneSide ? '<br/><small class="text-danger">' + esc(rows[r].msg) + '</small>' : '') + '</th>' : '')
			+ '<th>Old</th>' + valueCells(rows[r].old, columns, diffColumns[r]) + '</tr>';
		html += '<tr' + rowClass + '><th>New</th>' + valueCells(rows[r]["new"], columns, diffColumns[r]) + '</tr>';
	}
	return html + '</tbody></table></div>';
}

/** cells of one side of a row, red where the column differs */
function valueCells(row, columns, diffSet){
	var html = '';
	for(var c = 0; c < columns.length; c++){
		var value = row && row.hasOwnProperty(columns[c]) ? row[columns[c]] : null;
		html += '<td class="val' + (diffSet[columns[c]] ? ' danger' : '') + '">' + esc(value) + '</td>';
	}
	return html;
}

/* ---------- helpers ---------- */

function createdText(created){
	return created ? ' <span class="label label-info">created now</span>' : ' <span class="text-muted">(existing)</span>';
}

function matchText(result){
	return result && result.matched ? '<span class="text-success">matched</span>' : '<span class="text-danger">not matched</span>';
}

function statusLabel(ok, text){
	return '<span class="label ' + (ok ? 'label-success' : 'label-danger') + '">' + text + '</span>';
}

function count(obj){
	return obj ? Object.keys(obj).length : 0;
}

function esc(value){
	if(value === null || value === undefined)
		return '<i class="text-muted">null</i>';
	return $('<div/>').text(String(value)).html();
}
</script>
