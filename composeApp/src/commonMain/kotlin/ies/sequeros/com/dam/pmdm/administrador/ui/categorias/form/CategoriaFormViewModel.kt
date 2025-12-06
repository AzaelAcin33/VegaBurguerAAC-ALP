package ies.sequeros.com.dam.pmdm.administrador.ui.categorias.form

import androidx.lifecycle.ViewModel
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.ui.dependientes.form.DependienteFormState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class CategoriaFormViewModel (private val item: CategoriaDTO?, onSuccess: (CategoriaFormState) -> Unit): ViewModel(){

    private val _uiState = MutableStateFlow(CategoriaFormState(
        nombre = item?.name ?: "",
        description = item?.description?:"",
        imagePath = item?.imagePath?:"",
        enabled = item?.enabled?:false
    ))

    val uiState: StateFlow<CategoriaFormState> = _uiState.asStateFlow()

    val isFormValid: StateFlow<Boolean> = uiState.map { state ->
        if (item == null)
        state.nombreError == null &&
                state.descriptionError == null &&
                state.imagePathError ==null &&
                !state.nombre.isBlank() &&
                !state.description.isBlank() &&
                state.imagePath.isNotBlank()
        else{
            state.nombreError == null &&
                    state.descriptionError == null &&
                    state.imagePathError ==null &&
                    !state.nombre.isBlank() &&
                    !state.description.isBlank() &&
                    state.imagePath.isNotBlank()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    fun onNameChange(v:String){
        _uiState.value = _uiState.value.copy(nombre = v, nombreError = validateName(v))
    }

    fun onDescriptionChange(v:String){
        _uiState.value = _uiState.value.copy(description = v)
    }

    fun onImagePathChange(v:String){
        _uiState.value = _uiState.value.copy(imagePath =  v, imagePathError =  validateImagePath(v))
    }

    fun onEnabledChange(v:Boolean){
        _uiState.value = _uiState.value.copy(enabled =  v)
    }

    fun clear(){
        _uiState.value = CategoriaFormState()
    }

    private fun validateName(name:String): String?{
        if (name.isBlank()) return "El nombre es obligatorio"
        if (name.length < 2) return "El nombre es muy corto"
        if (name.length > 100) return "El nombre es muy largo"
        return null
    }

    private fun validateDescription(description:String): String?{
        if (description.length > 250) return "La descripción es muy larga"
        return null
    }

    private fun validateImagePath(path: String): String? {
        if (path.isBlank()) return "La imagen es obligatoria"
        return null
    }

    fun validateAll(): Boolean {
        val s = _uiState.value
        val nombreErr = validateName(s.nombre)
        val descriptionErr = validateDescription(s.description)
        val imageErr=validateImagePath(s.imagePath)
        val newState = s.copy(
            nombreError = nombreErr,
            descriptionError = descriptionErr,
            imagePathError = imageErr,
            submitted = true
        )
        _uiState.value = newState
        return listOf(nombreErr, descriptionErr, imageErr).all { it == null }
    }

    fun submit(
        onSuccess: ( CategoriaFormState) -> Unit,
        onFailure: ((CategoriaFormState) -> Unit)? = null
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