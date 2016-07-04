<html>
<head>
<title>Carga de datos al sistema LogicGame</title>
<%! java.lang.String idiomaDef = java.util.Locale.getDefault().getLanguage(); %>
</head>

<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script> -->
<script src="../js/jquery-1.11.3.min.js"></script>
<script src="../js/UtilLG.js"></script>
<script src="../js/admlg.js"></script>

<body>
<form action="upload" method="post" enctype="multipart/form-data">

<table id="lg.adm.menu.estructura">
	<tr>
		<td><input type="button" id="lg_btt_crear_tablas" value="Crear Tablas" onclick="AdmLG.doCrearTablas()"/></td>
	</tr>
</table>
<hr>
<table id="panel_adm">
	<tr><td>Idioma:</td><td><input type="text" name="idioma" value="<%=idiomaDef %>" /></td></tr>
	<tr><td colspan="2"><input type="file" name="file" size="50" /></td></tr>
	<tr><td colspan="2"><input type="submit" value="Cargar" /></td></tr>
</table>
<hr>
<table>
	<tr>
		<td><input type="button" id="lg_btt_crear_ruta_x_defecto" value="Crear Ruta por Defecto" onclick="AdmLG.doCrearRutaXDefecto()"/></td>
	</tr>
</table>
<hr>
<table>
	<tr>
		<td><input type="button" id="lg_btt_borrar_datos" value="Borrar Datos" onclick="AdmLG.doBorrarDatos()"/></td>
		<td><input type="button" id="lg_btt_borrar_tablas" value="Borrar Tablas" onclick="AdmLG.doBorrarTablas()"/></td>
	</tr>
</table>
</form>
</body>
</html>