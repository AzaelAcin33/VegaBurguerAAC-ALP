package ies.sequeros.com.dam.pmdm.administrador

import androidx.compose.material3.adaptive.WindowAdaptiveInfo

import androidx.lifecycle.ViewModel
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.DependienteDTO

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdministradorViewModel: ViewModel() {
    private val _darkMode = MutableStateFlow<Boolean>(false)
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()

    private val _user= MutableStateFlow< DependienteDTO?>(null)
    val user: StateFlow<DependienteDTO?> = _user.asStateFlow()

    private val _windowsAdaptativeInfo =MutableStateFlow<WindowAdaptiveInfo?>(null);
    val windowsAdaptativeInfo=_windowsAdaptativeInfo.asStateFlow()
    fun setWindowsAdatativeInfo(wai: WindowAdaptiveInfo){
       _windowsAdaptativeInfo.value=null;
        _windowsAdaptativeInfo.value=wai;

    }
    fun hasPermission(): Boolean{
        if(user.value==null )
            return false
        user.value.let {
            return it?.isAdmin ?: false
        }
    }

    fun setDependienteDTO(value: DependienteDTO){
        this._user.value=value
    }
    fun exit(){
        this._user.value=null;
    }
    fun setDarkMode(){
        _darkMode.value=true;
    }
    fun setLighMode(){
        _darkMode.value=false;
    }
    fun swithMode(){
        _darkMode.value=!_darkMode.value;
    }

}