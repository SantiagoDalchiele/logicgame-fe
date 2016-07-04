/**
 * Controller de la consola de administracion de LogicGame
 */
var AdmLG = (function() {
	
	/** Instancia a retornar */
	var my = {};

	
	
	/**
	 * Requerimientos ajax
	 * @see uy.com.uma.logicgame.fe.web.adm.actions.AdmAbstractAction
	 */
	var ID_REQ_BORRAR_TABLAS		= "borrar_tablas.admdo";
	var ID_REQ_BORRAR_DATOS			= "borrar_datos.admdo";
	var ID_REQ_CREAR_TABLAS			= "crear_tablas.admdo";
	var ID_REQ_CREAR_RUTA_X_DEFECTO = "crear_ruta_x_defecto.admdo"
	var ID_REQ_LOGIN_ADM			= "login.admdo";
	
	
	/** Ruta de la página principal de administración del sistema */
	var ADM_LG_PAGE_PATH = "jsp/admlg.jsp"; 



	/**
	 * Realiza el login y en caso de exito redirige a la pagina de upload (uploadlg.jsp)
	 */
	my.doLoginAdm = function() {
		var pass = $("#lg_password").val();
		ajaxPost(ID_REQ_LOGIN_ADM, { clave: pass }, false, procesarLogin);
	}
	
	
	
	/**
	 * Envio de requerimientos AJAX
	 */
	my.doBorrarTablas = function() {
		ajaxPost(ID_REQ_BORRAR_TABLAS, {}, false, procesarAdmBaseDatos);
	}
	
	my.doBorrarDatos = function() {
		ajaxPost(ID_REQ_BORRAR_DATOS, {}, false, procesarAdmBaseDatos);
	}
	
	my.doCrearTablas = function() {
		ajaxPost(ID_REQ_CREAR_TABLAS, {}, false, procesarAdmBaseDatos);
	}
	
	my.doCrearRutaXDefecto = function() {
		ajaxPost(ID_REQ_CREAR_RUTA_X_DEFECTO, {}, false, procesarAdmBaseDatos);
	}
	
	
	
	/**
	 * Envia el requerimiento al servidor via AJAX
	 */
	function ajaxPost (requerimiento, params, asincrono, procesaResponse) {
		$.ajax({type: "POST",
			url: UtilLG.getPageUrl() + requerimiento,
			data: params,
			async: asincrono,
			contentType: "application/x-www-form-urlencoded; charset=ISO-8859-1",
			dataType: "json",
			success: function (data, textStatus, jqXHR) {			
				if (data.error)
					alert(data.error.mensaje);
				else if (procesaResponse != null)
					procesaResponse (data);
			},
			error: function (xhr) {
				console.log(xhr);
				alert("Error producido en el requerimiento AJAX");
			}
		});
	}
	
	
	
	/**
	 * Procesa el resultado del login del usuario
	 */
	function procesarLogin(data) {
		if (data.resultado == 1)
			window.location.href = UtilLG.getPageUrl() + ADM_LG_PAGE_PATH;
		else
			alert("Error al realizar el login o clave incorrecta");
	}
	
	
	
	/**
	 * Procesa el resultado de administracion de la base de datos
	 */
	function procesarAdmBaseDatos (data) {
		if (data.resultado)
			alert(data.resultado);
		else
			alert("Error inesperado al realizar la accion");
	}
	
	
	
	return my;
}());