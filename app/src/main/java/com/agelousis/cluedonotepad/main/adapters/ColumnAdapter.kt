package com.agelousis.cluedonotepad.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agelousis.cluedonotepad.application.MainApplication
import com.agelousis.cluedonotepad.databinding.NotepadRowColumnLayoutBinding
import com.agelousis.cluedonotepad.main.enums.ColumnState
import com.agelousis.cluedonotepad.main.models.ColumnDataModel
import com.agelousis.cluedonotepad.main.presenters.ColumnPresenter
import com.agelousis.cluedonotepad.main.viewHolders.ColumnViewHolder

class ColumnAdapter(private val columnDataModelList: List<ColumnDataModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>(), ColumnPresenter {

    override fun onIconSet(columnState: ColumnState, adapterPosition: Int) {
        columnDataModelList.getOrNull(index = adapterPosition)?.columnState = columnState
        if (adapterPosition == 1 || adapterPosition ==  2) {
            if (columnState != ColumnState.EMPTY)
                MainApplication.currentSelectedCards.add(
                    columnDataModelList.firstOrNull()?.title ?: return
                )
            else
                MainApplication.currentSelectedCards.remove(
                    columnDataModelList.firstOrNull()?.title ?: return
                )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ColumnViewHolder(binding = NotepadRowColumnLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = columnDataModelList.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ColumnViewHolder)?.bind(
            columnDataModel = columnDataModelList.getOrNull(index = position) ?: return,
            columnPresenter = this
        )
    }

}