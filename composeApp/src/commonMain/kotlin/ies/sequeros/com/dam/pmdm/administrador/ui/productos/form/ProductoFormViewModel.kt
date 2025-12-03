package ies.sequeros.com.dam.pmdm.administrador.ui.productos.form

import androidx.lifecycle.ViewModel
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.ui.productos.form.ProductoFormState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class ProductoFormViewModel (private val item: ProductoDTO?, onSuccess: (ProductoFormState) -> Unit): ViewModel(){

    private val _uiState = MutableStateFlow(ProductoFormState(
        name = item?.name ?: "",
        description = item?.description?:"",
        imagePath = item?.imagePath?:"",
        price = item?.price?:"",
        enabled = item?.enabled?:false,
        categoriaId = item?.categoriaId?: ""
    ))

    val uiState: StateFlow<ProductoFormState> = _uiState.asStateFlow()

    val isFormValid: StateFlow<Boolean> = uiState.map { state ->
        if (item == null)
            state.nameError == null &&
                    //state.descriptionError == null &&
                    state.imagePathError ==null &&
                    state.priceError ==null &&
                    state.categoriaIdError ==null &&
                    !state.name.isBlank() &&
                    !state.description.isBlank() &&
                    state.imagePath.isNotBlank() &&
                    state.price.isNotBlank() &&
                    state.categoriaId.isNotBlank()
        else{
            state.nameError == null &&
                    //state.descriptionError == null &&
                    state.imagePathError ==null &&
                    state.priceError ==null &&
                    state.categoriaIdError ==null &&
                    !state.name.isBlank() &&
                    !state.description.isBlank() &&
                    state.imagePath.isNotBlank() &&
                    state.price.isNotBlank() &&
                    state.categoriaId.isNotBlank()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    fun onNameChange(v:String){
        _uiState.value = _uiState.value.copy(name = v, nameError = validateName(v))
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

    fun onPriceChange(v:String){
        _uiState.value = _uiState.value.copy(price =  v, priceError =  validatePrice(v))
    }

    fun onCategoriaIdChange(v:String){
        _uiState.value = _uiState.value.copy(categoriaId =  v, categoriaIdError =  validateCategoriaId(v))
    }

    fun clear(){
        _uiState.value = ProductoFormState()
    }

    private fun validateName(name:String): String?{
        if (name.isBlank()) return "El nombre es obligatorio"
        if (name.length < 2) return "El nombre es muy corto"
        return null
    }

    //private fun validateDescription(){}

    private fun validateImagePath(path: String): String? {
        if (path.isBlank()) return "La imagen es obligatoria"
        return null
    }

    private fun validatePrice(price: String): String? {
        if (price.isBlank()) return "El precio es obligatorio"

        val value = price.toDoubleOrNull()
        if (value == null) return "El precio debe ser un número"
        if (value <= 0.0) return "El precio debe ser mayor que 0"

        return null
    }

    private fun validateCategoriaId(id: String): String? {
        if (id.isBlank()) return "Debe seleccionar una categoría"
        return null
    }

    fun validateAll(): Boolean {
        val s = _uiState.value
        val nameErr = validateName(s.name)
        val imageErr = validateImagePath(s.imagePath)
        val priceErr = validatePrice(s.price)
        val categoriaErr = validateCategoriaId(s.categoriaId)
        val newState = s.copy(
            nameError = nameErr,
            imagePathError = imageErr,
            priceError = priceErr,
            categoriaIdError = categoriaErr,
            submitted = true
        )
        _uiState.value = newState

        return listOf(nameErr, imageErr, priceErr, categoriaErr).all { it == null }
    }


    fun submit(
        onSuccess: ( ProductoFormState) -> Unit,
        onFailure: ((ProductoFormState) -> Unit)? = null
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