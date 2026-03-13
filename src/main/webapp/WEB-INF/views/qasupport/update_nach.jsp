<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<style>
#table1 {
	border-spacing: 20px;
	border-collapse: separate;
}

#but1 {
	border-radius: 10px;
	background-color: white;
	width: 100px;
}

#but2 {
	border-radius: 10px;
	background-color: white;
	width: 100px;
}

#but3 {
	border-radius: 10px;
	background-color: white;
	width: 100px;
}

#Bulk {
	background-color: white;
	width: 500px;
	border: 2px solid black;
	padding: 20px;
	margin: 20px;
	align-items: center;
}

hr {
	background-color: black;
	color: dimgrey;
	border: solid 1px black;
}

#myFile {
	border-radius: 10px;
	background-color: white;
	width: 100px;
}

.upload-btn-wrapper {
	position: relative;
	overflow: hidden;
	display: inline-block;
}

.btn {
	border: 1px solid gray;
	color: black;
	background-color: white;
	padding: 2px 10px;
	border-radius: 6px;
	font-size: 15px;
	font-weight: bold;
}

.upload-btn-wrapper input[type=file] {
	font-size: 100px;
	position: absolute;
	left: 0;
	top: 0;
	opacity: 0;
}

#myTable {
	overflow: scroll;
	width: 100%;
}

#dvCSV {
	height: 231px;
	overflow-y: scroll;
}

#subBtn1 {
	border-radius: 10px;
	background-color: white;
	width: 100px;
	position: fixed;
}

#myTable th {
	background-color: darkgrey;
}

#myTable tr:nth-child(odd) {
	background-color: #DCDCDC;
}

#myTable th, td {
	text-align: center;
}
</style>
</head>

<body>
	<div class="container-fluid" style="margin-top: 40px;">
    <div id="Bulk" style="margin-left: 400px;">
        <div>
            <b>NACH UPDATE DATA (CSV [ppid, status{0/1}, utr]):</b><br/>
            <textarea id="nachData" cols="50" rows="5"></textarea><br/><br/>

            <button class="btn" onclick="uploadNachData()">Upload data</button>
        </div>
    </div>
</div>
</body>
<script>

function uploadNachData() {
    const textarea = document.getElementById("nachData");
    const csvText = textarea.value.trim();

    if (!csvText) {
        alert("Nach data cannot be empty");
        return;
    }
    
    const status0 = [];
    const status1 = [];
    const lines = csvText.split("\n");

    for (let i = 0; i < lines.length; i++) {
        const line = lines[i].trim();
        

        if (!line) continue;

        const cols = line.split(",");

        // Must be exactly 3 columns
        if (cols.length !== 3) {
            alert('Invalid CSV format at line ${i + 1}');
            return;
        }

        const ppid = cols[0].trim();
        const status = cols[1].trim();
        const utr = cols[2].trim();

        if (!ppid || !utr) {
            alert("PPID or UTR missing at line ${i + 1}");
            return;
        }

        if (status !== "0" && status !== "1") {
            alert("Invalid status at line ${i + 1}. Allowed: 0 or 1");
            return;
        }
        
        if (status === "0") {
            status0.push(ppid);
        } else if (status === "1") {
            status1.push(ppid);
        } else {
            alert("Invalid status at line ${i + 1}. Allowed: 0 or 1");
            return;
        }
    }

    // POST CSV TEXT (not JSON)
    fetch("update-nach", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
        	failPpids: status0.join(","),
        	passPpids: status1.join(",")
        })
    })
    .then(res => {
        if (!res.ok) {
            throw new Error("Upload failed");
        }
        return res.text();
    })
    .then(response => {
        alert("CSV uploaded successfully");
        textarea.value = "";
        console.log(response);
    })
    .catch(err => {
        alert("Error while uploading CSV");
        console.error(err);
    });
}
</script>
</html>