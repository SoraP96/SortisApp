package com.example.banca.domain.usecases

import com.example.banca.data.repository.PlayRepository
import com.example.banca.data.repository.ListRepository

class CloseListUseCase(
    private val playRepository: PlayRepository,
    private val listRepository: ListRepository,
    private val calculateTotals: CalculateListTotalsUseCase
) {

    suspend fun execute(listId: Long) {

        // 1 obtener jugadas
        val plays = playRepository.getPlaysByList(listId)

        // 2 calcular totales
        val (total, listero, bank) = calculateTotals.execute(plays)

        // 3 guardar totales
        listRepository.updateTotals(listId, total, listero, bank)

        // 4 cerrar lista
        listRepository.closeList(listId)
    }
}