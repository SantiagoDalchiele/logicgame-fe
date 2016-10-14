<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Cambio de clave - LogicGame</title>

<!-- <link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/redmond/jquery-ui.min.css" /> -->  
<link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css" />
<!-- <link rel="stylesheet" type="text/css" href="css/lg.min.css">  -->
<link rel="stylesheet" type="text/css" href="css/lg-juego.css">
<link rel="stylesheet" type="text/css" href="css/lg-ui.css">

<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>  -->  
<script src="js/jquery-1.11.3.min.js"></script>
<script src="js/jquery-ui.min.js"></script>

<script src="js/i18next-1.11.0.min.js"></script>
<script src="js/UtilLG.js"></script>

<!-- <script src="js/lg.min.js"></script>  -->
<script src="js/UILG-core.js"></script>
<script src="js/UILG-dialogos.js"></script>
<script src="js/UILG-ajax.js"></script>
<script src="js/UILG-configuracion.js"></script>
<script src="js/UILG-login.js"></script>
<script src="js/UILG-registro.js"></script>
<script src="js/UILG-juego.js"></script>
<script src="js/UILG-pista.js"></script>
<script src="js/UILG-main.js"></script>

</head>

<body>
<table class="lg_main"><tr><td class="lg_noselect">
	<table id="lg_panel_resetClave">
		<tr><td><input id="lg_reset_password" type="password" required="required" onkeypress="UtilLG.defaultButton(event, 'lg_btt_resetClave')" pattern="[_a-zA-Z0-9-\.]{5,32}"/></td></tr>
		<tr><td><input id="lg_reset_password2" type="password" required="required" onkeypress="UtilLG.defaultButton(event, 'lg_btt_resetClave')" pattern="[_a-zA-Z0-9-\.]{5,32}"/></td></tr>
		<tr><td style="text-align: center"><input type="button" id="lg_btt_doResetClave" onclick="MainLG.doResetClave()"/></td></tr>
	</table>
</td></tr></table>

<!-- Dialogo de Error -->
<div id="lg_dialogoError">
  	<table><tr><td><img src="img/error.png" class="lgui-icono"/></td><td><p id="lg_error_msg"></p></td></tr></table>
	<div id="lg_error_detalle"><p id="lg_error_detalle_texto"></p></div>
</div>
	
</body>
</html>
