package com.rentacuarto.ui.contratos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rentacuarto.data.local.entity.ContratoEntity
import com.rentacuarto.data.repository.ContratosRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContratosViewModel(private val repository: ContratosRepository) : ViewModel() {
    val contratos = repository.listar()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun listarPorId(id: Int): Flow<List<ContratoEntity>> = repository.listarPorId(id)

    fun registrar(
        habitacionId: Int, inquilinoId: Int, fechaInicio: String,
        primerVencimiento: String, finPactada: String, renta: String,
        numeroGarantias: String, numeroAdelantos: String,
        onResultado: (String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.registrar(habitacionId, inquilinoId, fechaInicio, primerVencimiento,
                    finPactada, renta, numeroGarantias, numeroAdelantos)
                onResultado(null)
            } catch (error: CancellationException) { throw error }
            catch (error: Exception) {
                Log.e("CONTRATOS", "Error al registrar", error)
                onResultado(error.message ?: "No se pudo registrar el contrato.")
            }
        }
    }

    fun finalizar(id: Int, fechaFin: String, onResultado: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                repository.finalizar(id, fechaFin)
                onResultado(null)
            } catch (error: CancellationException) { throw error }
            catch (error: Exception) { onResultado(error.message ?: "No se pudo finalizar el contrato.") }
        }
    }
}
