<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="container"  style="margin-top: 100px;">

	<h2>Login</h2>
	<br />
	<div class="well well-lg col-md-6 col-md-offset-3" align="center">
	<span>${msg}</span>
	<form action="" method="post">
		<table>

			
			<tr height="80px">
				<td><label>Login Id * : </label></td>
				<td><input type="text" name="userid" ></td>
			</tr>
			<tr>
				<td><label>Password * : </label></td>
				<td><input type="password" name="password" required="required" /></td>
				
			</tr>

			<tr height="80px">
				<td><input type="submit" id="submit_login"
					name="submit_login" value="Login" /></td>
			</tr>


			
		</table>
	</form>	
	</div>
</div>




