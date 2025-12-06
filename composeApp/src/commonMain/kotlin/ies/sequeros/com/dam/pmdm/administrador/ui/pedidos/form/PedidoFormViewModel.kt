package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form
import androidx.lifecycle.ViewModel
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form.PedidoFormState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class PedidoFormViewModel (private val item: PedidoDTO?, onSuccess: (PedidoFormState) -> Unit): ViewModel(){

    private val _uiState = MutableStateFlow(PedidoFormState(
        clienteName = item?.clienteName ?: "",
        estado = item?.estado?:"",
        fecha = item?.fecha?:"",
        dependienteId = item?.dependienteId?:""
    ))

    val uiState: StateFlow<PedidoFormState> = _uiState.asStateFlow()

    val isFormValid: StateFlow<Boolean> = uiState.map { state ->
        if (item == null)
            state.clienteNameError == null &&
                    state.estadoError == null &&
                    state.fechaError ==null &&
                    state.dependienteIdError ==null &&
                    !state.clienteName.isBlank() &&
                    !state.estado.isBlank() &&
                    state.fecha.isNotBlank() &&
                    state.dependienteId.isNotBlank()
        else{
            state.clienteNameError == null &&
                    state.estadoError == null &&
                    state.fechaError ==null &&
                    state.dependienteIdError ==null &&
                    !state.clienteName.isBlank() &&
                    !state.estado.isBlank() &&
                    state.fecha.isNotBlank() &&
                    state.dependienteId.isNotBlank()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    fun onClienteNameChange(v:String){
        _uiState.value = _uiState.value.copy(clienteName = v, clienteNameError = validateClienteName(v))
    }

    fun onFechaChange(v:String){
        _uiState.value = _uiState.value.copy(fecha =  v, fechaError =  validateFecha(v))
    }

    fun onDependienteIdChange(v: String){
        _uiState.value = _uiState.value.copy(dependienteId = v, dependienteIdError = validateDependienteId(v))
    }

    fun onEstadoChange(v:String){
        _uiState.value = _uiState.value.copy(estado = v, estadoError = validateEstado(v))
    }

    fun clear(){
        _uiState.value = PedidoFormState()
    }

    private fun validateClienteName(name:String): String?{
        if (name.isBlank()) return "El nombre es obligatorio"
        if (name.length < 2) return "El nombre es muy corto"
        if (name.length > 100) return "El nombre es muy largo"
        return null
    }

    private fun validateFecha(fecha: String): String? {
        if (fecha.isBlank()) return "La fecha es obligatoria"

        val regex = Regex("\\d{4}-\\d{2}-\\d{2}")
        if (!regex.matches(fecha)) return "Formato de fecha inválido (YYYY-MM-DD)"

        return null
    }

    private fun validateDependienteId(id: String): String? {
        if (id.isBlank()) return "Debe seleccionar un dependiente"
        return null
    }

    private fun validateEstado(estado: String): String? {
        if (estado.isBlank()) return "El estado es obligatorio"
        return null
    }

    fun validateAll(): Boolean {
        val s = _uiState.value
        val clienteErr = validateClienteName(s.clienteName)
        val fechaErr = validateFecha(s.fecha)
        val depErr = validateDependienteId(s.dependienteId)
        val estadoErr = validateEstado(s.estado)
        val newState = s.copy(
            clienteNameError = clienteErr,
            fechaError = fechaErr,
            dependienteIdError = depErr,
            estadoError = estadoErr,
            submitted = true
        )
        _uiState.value = newState

        return listOf(clienteErr, fechaErr, depErr, estadoErr).all { it == null }
    }


    fun submit(
        onSuccess: ( PedidoFormState) -> Unit,
        onFailure: ((PedidoFormState) -> Unit)? = null
    ) {
        //se ejecuta en una corrutina, evitando que se bloque la interfaz gráficas
        viewModelScope.launch {
            val ok = validateAll()
            if (ok) {
                onSuccess(_uiState.value)
            } else {
                onFailure?.invoke(_uiState.value)
            }
        }
    }


}