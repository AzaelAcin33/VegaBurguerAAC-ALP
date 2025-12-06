package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.PedidosViewModel

@Composable
fun PedidoForm(
    pedidoViewModel: PedidosViewModel,
    onClose: () -> Unit,
    onConfirm: (datos: PedidoFormState) -> Unit = {},
    pedidoFormularioViewModel: PedidoFormViewModel = viewModel {
        PedidoFormViewModel(
            pedidoViewModel.selected.value, onConfirm
        )
    }
) {

}