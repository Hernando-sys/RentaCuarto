package com.rentacuarto.ui.propiedades

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rentacuarto.data.local.entity.HabitacionEntity
import com.rentacuarto.data.repository.PropiedadesRepository
import com.rentacuarto.util.FechaUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitacionesViewModel(private val repository: PropiedadesRepository) : ViewModel() {
    val habitaciones = repository.listarHabitaciones()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val idsOcupadas = repository.habitacionesOcupadas(FechaUtils.hoy())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun listarPorPropiedad(propiedadId: Int): Flow<List<HabitacionEntity>> =
        repository.listarHabitacionesPorPropiedad(propiedadId)

    fun listarPorId(id: Int): Flow<List<HabitacionEntity>> = repository.listarHabitacionPorId(id)

    fun guardar(id: Int, propiedadId: Int, codigo: String, descripcion: String,
                onResultado: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                repository.guardarHabitacion(id, propiedadId, codigo, descripcion)
                onResultado(null)
            } catch (error: CancellationException) { throw error }
            catch (error: Exception) {
                Log.e("HABITACIONES", "Error al guardar", error)
                onResultado(error.message ?: "No se pudo guardar la habitación.")
            }
        }
    }

    fun eliminar(habitacion: HabitacionEntity, onResultado: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                repository.eliminarHabitacion(habitacion)
                onResultado(null)
            } catch (error: CancellationException) { throw error }
            catch (error: Exception) {
                Log.e("HABITACIONES", "Error al eliminar", error)
                onResultado(error.message ?: "No se pudo eliminar la habitación.")
            }
        }
    }
}
