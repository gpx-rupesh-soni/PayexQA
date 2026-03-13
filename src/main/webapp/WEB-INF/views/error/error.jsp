
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Global PayEX</title>
    <style>
    
    .oopsContainer{
        display: flex;
        width: 100%;
        height: 100%;
		padding-top:10%;
        justify-content: center;
    }
    .oppTxtContainer{
        display: flex;
        flex-direction: row;
        align-items: center;
        text-align: center;
    }
    .headerContainer{
        display: flex; 
        flex-direction: column; 
        text-align: right;
        width: 100%;
    }
    .h_txt{
        font-size: 5em;
        color: red;
    }
    .oop_img{
        width: 50%;
        height: 100%;
    }
    p{
        margin: 0px;        
    }

    .p_txt{
        display: flex;
        flex-direction: column;
        font-size: 4vh;
    }
        
    </style>
</head>
<body>
    <div class="oopsContainer">
        <div class="oppTxtContainer">
            <div class="headerContainer">
                <div class="h_txt">
                    OOPS!
                </div>
                <div class="p_txt">
                    <p>There is an internal error!</p>
                    <p>Please try after some time.</p>
                    <p>${error}</p>
                </div>  
            </div>      
            <div>
                <img class="oop_img" src="resources/images/oops_icon.jpg">
            </div>  
        </div>        
    </div>
</body>
</html>
