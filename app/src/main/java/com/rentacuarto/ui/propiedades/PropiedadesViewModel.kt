package com.rentacuarto.ui.propiedades

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rentacuarto.data.local.entity.PropiedadEntity
import com.rentacuarto.data.repository.PropiedadesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PropiedadesViewModel(private val repository: PropiedadesRepository) : ViewModel() {
    val propiedades = repository.listarPropiedades()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun listarPorId(id: Int): Flow<List<PropiedadEntity>> = repository.listarPropiedadPorId(id)

    /** El callback entrega null si se guardó o el texto del error si falló. */
    fun guardar(id: Int, nombre: String, direccion: String, tipo: String, notas: String,
                onResultado: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                repository.guardarPropiedad(id, nombre, direccion, tipo, notas)
                onResultado(null)
            } catch (error: CancellationException) { throw error }
            catch (error: Exception) {
                Log.e("PROPIEDADES", "Error al guardar", error)
                onResultado(error.message ?: "No se pudo guardar la propiedad.")
            }
        }
    }

    fun eliminar(propiedad: PropiedadEntity, onResultado: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                repository.eliminarPropiedad(propiedad)
                onResultado(null)
            } catch (error: CancellationException) { throw error }
            catch (error: Exception) {
                Log.e("PROPIEDADES", "Error al eliminar", error)
                onResultado(error.message ?: "No se pudo eliminar la propiedad.")
            }
        }
    }
}
