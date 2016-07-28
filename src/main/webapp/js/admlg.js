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
	var ID_REQ_GET_IDIOMAS			= "getIdiomas.admdo";
	var ID_REQ_GET_JUEGOS			= "getJuegos.admdo";
	var ID_REQ_GET_RUTAS			= "getRutas.admdo";
	var ID_REQ_GET_USUARIOS			= "getUsuarios.admdo";
	
	
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
	 * Carga archivos de mantenimiento de la aplicacion
	 */
	my.doUpload = function() {
		$('#upload-form').ajaxForm({
			success: function (data, textStatus, jqXHR) {			
				if (data.error)
					alert(data.error.mensaje);
				else
					procesarUpload(data);
            },
            error: function (xhr) {
				console.log(xhr);
				alert("Error producido en el requerimiento AJAX");
            }
        });
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
	
	my.doGetIdiomas = function() {		
		ajaxPost(ID_REQ_GET_IDIOMAS, {}, false, procesarGetIdiomas);
	}
	
	my.doGetJuegos = function() {
		var lang = $("#idioma").val();
		ajaxPost(ID_REQ_GET_JUEGOS, {idioma: lang}, false, procesarGetJuegos);
	}
	
	my.doGetRutas = function() {		
		ajaxPost(ID_REQ_GET_RUTAS, {}, false, procesarGetRutas);
	}
	
	my.doGetUsuarios = function() {		
		ajaxPost(ID_REQ_GET_USUARIOS, {}, false, procesarGetUsuarios);
	}
	
	
	/**
	 * Mostrar/Ocultar paneles
	 */
	my.toggle = function (selectorId) {
		$(selectorId).toggle();
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
	 * Procesa el resultado del upload de un archivo
	 */
	function procesarUpload(data) {
		if (data.archivos) {
			var tblArchivos = document.getElementById("archivos"); 
			$("#archivos").empty();
			UtilLG.destroyDataTable("#archivos");
			var dataSet = [];
			
			for (i in data.archivos) {
				var arch = data.archivos[i];
				var fila = [arch];
				dataSet.push(fila); 
			}
			
			$('#archivos').DataTable({				
		        data: dataSet,
		        columns: [
		            { title: "Archivo" }
		        ]
		    });
		} else
			alert("Error inesperado al realizar la accion");
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
	
	
	
	/**
	 * Borra todas las filas de la tabla idiomas y carga la tabla con los datos de los idiomas
	 */
	function procesarGetIdiomas (data) {
		if (data.idiomas) {
			var tblIdiomas = document.getElementById("idiomas"); 
			$("#idiomas").empty();
			UtilLG.destroyDataTable("#idiomas");
			var dataSet = [];
			
			for (i in data.idiomas) {
				var idioma = data.idiomas[i];
				var fila = [idioma.id, idioma.nombre, idioma.icono];
				dataSet.push(fila); 
			}
			
			$('#idiomas').DataTable({				
		        data: dataSet,
		        columns: [
		            { title: "Id" },
		            { title: "Nombre" },
		            { title: "Icono" }
		        ]
		    });
		} else
			alert("Error inesperado al realizar la accion");
	}
	
	
	
	/**
	 * Borra todas las filas de la tabla de juegos y carga la tabla con los datos de los juegos
	 */
	function procesarGetJuegos (data) {
		if (data.juegos) {
			var tbl = document.getElementById("juegos"); 
			$("#juegos").empty();
			UtilLG.destroyDataTable("#juegos");
			var dataSet = [];
			
			for (i in data.juegos) {
				var juego = data.juegos[i];				
				var fila = [juego.id, juego.cantDims, juego.cantValores, juego.costo, juego.titulo, juego.idiomas, juego.texto, juego.solucion, juego.defJuego];
				dataSet.push(fila);
			}
			
			$('#juegos').DataTable({
		        data: dataSet,
		        columns: [
		            { title: "Id", width: "40px" },
		            { title: "Dims", width: "40px" },
		            { title: "Vals", width: "40px" },
		            { title: "Costo", width: "40px" },
		            { title: "Titulo", width: "60px" },
		            { title: "Idiomas", width: "60px" },
		            { title: "Texto", width: "100px" },
		            { title: "Solucion" },
		            { title: "Definicion" }
		        ]
		    });
		} else
			alert("Error inesperado al realizar la accion");
	}
	
	
	
	/**
	 * Borra todas las filas de la tabla y carga las rutas
	 */
	function procesarGetRutas (data) {
		if (data.rutas) {
			var tbl = document.getElementById("rutas");
			$("#rutas").empty();
			UtilLG.destroyDataTable("#rutas");
			var dataSet = [];
			
			for (i in data.rutas) {
				var ruta = data.rutas[i];
				var fila = [ruta.ruta, ruta.nivel, ruta.juego, ruta.estilo];
				dataSet.push(fila);
			}
			
			$('#rutas').DataTable({				
		        data: dataSet,
		        columns: [
		            { title: "Ruta" },
		            { title: "Nivel" },
		            { title: "Id Juego" },
		            { title: "Hoja Estilo" }
		        ]
		    });
		} else
			alert("Error inesperado al realizar la accion");
	}
	
	
	
	/**
	 * Borra todas las filas de la tabla y carga los usuarios
	 */
	function procesarGetUsuarios (data) {
		if (data.usuarios) {
			var tbl = document.getElementById("usuarios");
			$("#usuarios").empty();
			UtilLG.destroyDataTable("#usuarios");
			var dataSet = [];
			
			for (i in data.usuarios) {
				var usuario = data.usuarios[i];
				var fila = [usuario.id, usuario.alias, usuario.correo, usuario.logeado, usuario.ruta, usuario.nivel, usuario.idioma, usuario.estado];
				dataSet.push(fila);
			}
			
			$('#usuarios').DataTable({				
		        data: dataSet,
		        columns: [
		            { title: "Id" },
		            { title: "Alias" },
		            { title: "Correo" },
		            { title: "Logeado ?" },
		            { title: "Ruta" },
		            { title: "Nivel" },
		            { title: "Idioma" },
		            { title: "Estado" }
		        ]
		    });
		} else
			alert("Error inesperado al realizar la accion");
	}
	
	return my;
}());
