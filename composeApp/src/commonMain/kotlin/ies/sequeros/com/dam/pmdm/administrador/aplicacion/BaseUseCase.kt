package ies.sequeros.com.dam.pmdm.administrador.aplicacion



class PermisoDenegadoException(message: String = "Acción no autorizada") : Exception(message)
abstract class BaseUseCase( protected val requiredAdmin:Boolean) { // Ya no necesita UserRepository en el constructor

    protected fun validateUser(currentUser: Any?){
        /*if (requiredAdmin) {
            val isAdmin = currentUser?.isAdmin ?: false
            val isEnabled= currentUser?.enabled?:false
            if (!isAdmin ) {
                throw PermisoDenegadoException("Se requieren permisos de administrador.")
            }
            if(!isEnabled)
                throw PermisoDenegadoException("El usuario está desactivado.")

        }*/
    }



}