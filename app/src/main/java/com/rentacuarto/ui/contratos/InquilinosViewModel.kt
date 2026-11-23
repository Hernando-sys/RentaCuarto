package com.rentacuarto.ui.contratos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rentacuarto.data.local.entity.InquilinoEntity
import com.rentacuarto.data.repository.InquilinosRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InquilinosViewModel(private val repository: InquilinosRepository) : ViewModel() {
    val inquilinos = repository.listar()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun listarPorId(id: Int): Flow<List<InquilinoEntity>> = repository.listarPorId(id)

    fun guardar(id: Int, nombres: String, dni: String, telefono: String,
                onResultado: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                repository.guardar(id, nombres, dni, telefono)
                onResultado(null)
            } catch (error: CancellationException) { throw error }
            catch (error: Exception) {
                Log.e("INQUILINOS", "Error al guardar", error)
                onResultado(error.message ?: "No se pudo guardar el inquilino.")
            }
        }
    }

    fun eliminar(inquilino: InquilinoEntity, onResultado: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                repository.eliminar(inquilino)
                onResultado(null)
            } catch (error: CancellationException) { throw error }
            catch (error: Exception) {
                Log.e("INQUILINOS", "Error al eliminar", error)
                onResultado(error.message ?: "No se pudo eliminar el inquilino.")
            }
        }
    }
}
