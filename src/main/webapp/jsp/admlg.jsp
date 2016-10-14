<html>
<head>
<title>Carga de datos al sistema LogicGame</title>
<%! java.lang.String idiomaDef = java.util.Locale.getDefault().getLanguage(); %>

<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css"/>

<style>
	table {	table-layout: fixed; }
	td { word-wrap: break-word; }
</style>

<script src="https://code.jquery.com/jquery-1.12.3.min.js"></script>
<script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<script src="../js/jquery.form.min.js"></script>

<!-- <script src="../js/jquery-1.11.3.min.js"></script>  -->
<script src="../js/UtilLG.js"></script>
<script src="../js/admlg.js"></script>

<script>
    $(function() {
    	AdmLG.doUpload();
    });
</script>

</head>

<body>

<!-- Botones de manejo de la estructura de la base de datos -->
<table id="lg.adm.menu.estructura">
	<tr>
		<td><input type="button" id="lg_btt_crear_tablas" value="Crear Tablas" onclick="AdmLG.doCrearTablas()"/></td>		
		<td width="200"/>
		<td><input type="button" id="lg_btt_crear_ruta_x_defecto" value="Crear Ruta por Defecto" onclick="AdmLG.doCrearRutaXDefecto()"/></td>
		<td width="200"/>
		<td><input type="button" id="lg_btt_borrar_datos" value="Borrar Datos" onclick="AdmLG.doBorrarDatos()"/></td>
		<td width="200"/>
		<td><input type="button" id="lg_btt_borrar_tablas" value="Borrar Tablas" onclick="AdmLG.doBorrarTablas()"/></td>
		<td width="200"/>
		<td><input type="button" id="lg_btt_parche01" value="Parche atributos token" onclick="AdmLG.doParche01()"/></td>
	</tr>
</table>
<!-- Upload de archivo de recursos del sistema -->
<hr>
<form id="upload-form" action="upload" method="post" enctype="multipart/form-data">
<table id="panel_adm">
	<tr><td>Idioma:</td><td align="left"><input type="text" name="idioma" id="idioma" value="<%=idiomaDef %>" /></td></tr>
	<tr><td colspan="2"><input type="file" id="file" name="file" size="50" /></td></tr>
	<tr><td colspan="2"><input type="submit" id="upload-button" value="Cargar" /></td></tr>
</table>
</form>	
<!-- Archivos procesados -->
<div>
	<img src="../img/onoff.png" onclick="AdmLG.toggle('#panel_archivos')"/>
	<div id="panel_archivos">
		<div>Archivos procesados</div>
		<table id="archivos" class="display"></table>
	</div>
</div>
<!-- Idiomas -->
<hr>
<div>
	<img src="../img/onoff.png" onclick="AdmLG.toggle('#panel_idiomas')"/>
	<div id="panel_idiomas">
		<table><tr><td>Idiomas</td><td><input type="button" id="lg_btt_get_idiomas" value="Obtener" onclick="AdmLG.doGetIdiomas()"/></td></tr></table>
		<table id="idiomas" class="display"></table>
	</div>
</div>
<!-- Rutas -->
<hr>
<div>
	<img src="../img/onoff.png" onclick="AdmLG.toggle('#panel_rutas')"/>
	<div id="panel_rutas">
		<table><tr><td>Rutas</td><td><input type="button" id="lg_btt_get_rutas" value="Obtener" onclick="AdmLG.doGetRutas()"/></td></tr></table>
		<table id="rutas" class="display"></table>
	</div>
</div>
<!-- Usuarios -->
<hr>
<div>
	<img src="../img/onoff.png" onclick="AdmLG.toggle('#panel_usuarios')"/>
	<div id="panel_usuarios">
		<table><tr><td>Usuarios</td><td><input type="button" id="lg_btt_get_usuarios" value="Obtener" onclick="AdmLG.doGetUsuarios()"/></td></tr></table>
		<table id="usuarios" class="display"></table>
	</div>
</div>
<!-- Juegos -->
<hr>
<div>
	<img src="../img/onoff.png" onclick="AdmLG.toggle('#panel_juegos')"/>
	<div id="panel_juegos">
		<table><tr><td>Juegos</td><td><input type="button" id="lg_btt_get_juegos" value="Obtener" onclick="AdmLG.doGetJuegos()"/></td></tr></table>
		<table id="juegos" class="display"></table>
	</div>
</div>
</body>
</html>