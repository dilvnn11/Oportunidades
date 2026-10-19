package cl.duoc.oportunidades.ui.navegacion

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.oportunidades.ui.auth.AuthViewModel
import cl.duoc.oportunidades.ui.auth.LoginScreen
import cl.duoc.oportunidades.ui.auth.RegistroScreen
import cl.duoc.oportunidades.ui.menu.MenuScreen
import cl.duoc.oportunidades.ui.oportunidad.ListaOportunidadesScreen
import cl.duoc.oportunidades.ui.oportunidad.OportunidadFormScreen
import cl.duoc.oportunidades.ui.oportunidad.OportunidadViewModel

object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val MENU = "menu"
    const val LISTA = "lista"
    const val FORM = "form/{id}"
    fun form(id: Long) = "form/$id"
}

/** Flujo: Iniciar sesión → Menú → ListaOportunidadesScreen → OportunidadFormScreen */
@Composable
fun AppNavigation() {
    val nav = rememberNavController()
    val auth: AuthViewModel = viewModel()
    val oportunidades: OportunidadViewModel = viewModel()

    NavHost(
        navController = nav, startDestination = Rutas.LOGIN,
        enterTransition = { slideInHorizontally { it } + fadeIn() },
        exitTransition = { slideOutHorizontally { -it / 3 } + fadeOut() },
        popEnterTransition = { slideInHorizontally { -it / 3 } + fadeIn() },
        popExitTransition = { slideOutHorizontally { it } + fadeOut() }
    ) {
        composable(Rutas.LOGIN) {
            LoginScreen(auth,
                alIngresar = { nav.navigate(Rutas.MENU) { popUpTo(Rutas.LOGIN) { inclusive = true } } },
                alRegistrarse = { nav.navigate(Rutas.REGISTRO) })
        }
        composable(Rutas.REGISTRO) {
            RegistroScreen(auth,
                alRegistrar = { nav.navigate(Rutas.MENU) { popUpTo(Rutas.LOGIN) { inclusive = true } } },
                alVolver = { nav.popBackStack() })
        }
        composable(Rutas.MENU) {
            MenuScreen(auth,
                alAbrirOportunidades = { nav.navigate(Rutas.LISTA) },
                alCerrarSesion = { nav.navigate(Rutas.LOGIN) { popUpTo(Rutas.MENU) { inclusive = true } } })
        }
        composable(Rutas.LISTA) {
            ListaOportunidadesScreen(oportunidades,
                alAgregar = { nav.navigate(Rutas.form(0)) },
                alEditar = { id -> nav.navigate(Rutas.form(id)) },
                alVolver = { nav.popBackStack() })
        }
        composable(Rutas.FORM, arguments = listOf(navArgument("id") { type = NavType.LongType })) { entrada ->
            OportunidadFormScreen(oportunidades, entrada.arguments?.getLong("id") ?: 0L, alVolver = { nav.popBackStack() })
        }
    }
}
