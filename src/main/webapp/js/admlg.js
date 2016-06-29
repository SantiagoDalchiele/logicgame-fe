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
	var ID_REQ_LOGIN_ADM = "login.admdo";
	var ID_REQ_CREAR_ROLES = "crear_roles.admdo";
	var ID_REQ_CREAR_TABLAS = "crear_tablas.admdo";
	var ID_REQ_ASIGNAR_PERMISOS = "asignar_permisos.admdo";
	var ID_REQ_CREAR_RUTA_X_DEFECTO = "crear_ruta_x_defecto.admdo"
	
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
	 * Envia el requerimiento ajax de crear roles
	 */
	my.doCrearRoles = function() {
		ajaxPost(ID_REQ_CREAR_ROLES, {}, false, procesarAdmBaseDatos);
	}
	
	
	
	/**
	 * Envia el requerimiento ajax de crear las tablas
	 */
	my.doCrearTablas = function() {
		ajaxPost(ID_REQ_CREAR_TABLAS, {}, false, procesarAdmBaseDatos);
	}
	
	
	
	/**
	 * Envia el requerimiento ajax de asignar los permisos para los roles en la base de datos
	 */
	my.doAsignarPermisos = function() {
		ajaxPost(ID_REQ_ASIGNAR_PERMISOS, {}, false, procesarAdmBaseDatos);
	}
	
	
	
	/**
	 * Envia el requerimiento ajax de crear la ruta por defecto en la base de datos para asignar
	 * a los usuarios nuevos que se crean
	 */
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